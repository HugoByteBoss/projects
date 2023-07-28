package br.com.timetracker.auth.repositories;

import br.com.timetracker.auth.application.UserAsyncService;
import br.com.timetracker.auth.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserAsyncService userAsyncService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void persist_shouldReturn_newUser() {
        // E-mail e senha válidos
        String email = "john.doe@example.com";
        String password = "validPassword";

        // Simular que não existe usuário com o mesmo e-mail
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Chamando o método a ser testado
        userAsyncService.persistUserAsync(email, password);

        // Verificar se o método save do userRepository foi chamado corretamente com o novo usuário
        verify(userRepository).save(argThat(user -> user.getEmail().equals(email) && user.getToken().equals(password)));
    }

    @Test
    public void persist_shouldReturn_existingUser() {
        // E-mail e senha válidos
        String email = "john.doe@example.com";
        String password = "validPassword";

        // Simular que já existe um usuário com o mesmo e-mail
        User existingUser = User.builder()
                .email(email)
                .token("existingToken")
                .createDate(LocalDateTime.now().minusDays(1))
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Chama o método a ser testado
        userAsyncService.persistUserAsync(email, password);

        // Verificar se o método save do userRepository foi chamado corretamente com o usuário atualizado
        verify(userRepository).save(argThat(user -> user.getEmail().equals(email)
                && user.getToken().equals("existingToken")
                && user.getCreateDate().equals(existingUser.getCreateDate())
                && user.getUpdateDate().isAfter(existingUser.getCreateDate())));
    }

    @Test
    public void persistWithInvalidPassword_shouldReturn_Exception() {

        String email = "john.doe@example.com";
        String invalidPassword = "";

        // Asegurar que a exceção é lançada ao tentar persistir com senha inválida
        assertThrows(IllegalArgumentException.class, () -> userAsyncService.persistUserAsync(email, invalidPassword));

        // Verificar se o userRepository não foi chamado
        verifyNoInteractions(userRepository);
    }
}
