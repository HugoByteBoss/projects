package br.com.timetracker.auth.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @MockBean
    private BCryptPasswordEncoder cryptService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String SECRET_CHARS = "RXYZabghijyz289!@#(=+";

    @Test
    public void generate_shouldReturn_token() {
        // Dado um e-mail válido
        String email = "john.doe@example.com";

        // Simule o comportamento do BCryptPasswordEncoder para retornar uma string fixa
        when(cryptService.encode(any())).thenReturn("dummy_encoded_token");

        // Chame o método a ser testado
        String token = tokenService.generateByEmail(email);

        // Verifique se o BCryptPasswordEncoder foi chamado corretamente
        verify(cryptService).encode(eq(email + SECRET_CHARS));

        // Verifique se o token retornado não está vazio e não é nulo
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("dummy_encoded_token", token);
    }

    @Test
    public void generateTokenWithInvalidEmail_shouldReturn_exception() {
        // Dado um e-mail inválido
        String invalidEmail = "invalid-email";

        // Verifique se uma exceção é lançada ao gerar o token com o e-mail inválido
        assertThrows(IllegalArgumentException.class, () -> tokenService.generateByEmail(invalidEmail));

        // Verifique se o BCryptPasswordEncoder não foi chamado
        verifyNoInteractions(cryptService);
    }
}

