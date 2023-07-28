package br.com.timetracker.auth.application;

import br.com.timetracker.auth.domain.User;
import br.com.timetracker.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAsyncService {

    private final UserRepository userRepository;

    @Async
    public CompletableFuture<Void> persistUserAsync(String email, String password) {

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Token inválido.");
        }

        CompletableFuture<Optional<User>> existingUserFuture = CompletableFuture.supplyAsync(() -> userRepository.findByEmail(email));
        CompletableFuture<User> userResult = existingUserFuture.thenCompose(existingUserOptional -> {
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                existingUser.setUpdateDate(LocalDateTime.now());
                return CompletableFuture.supplyAsync(() -> userRepository.save(existingUser));
            } else {
                User newUser = User.createNew(email, password);
                return CompletableFuture.supplyAsync(() -> userRepository.save(newUser))
                        .thenApply(user -> newUser);
            }
        });

        return userResult.thenAccept(this::logUserAction)
                .thenApply(ignored -> null);
    }

    private void logUserAction(User user) {
        if (user.getUpdateDate() != null) {
            log.info("Usuário atualizado: {}", user.getEmail());
        } else {
            log.info("Novo usuário criado: {}", user.getEmail());
        }
    }

    // Esse método verifica se o password é válido
    private boolean isValidPassword(String password) {
        return password != null && !password.isEmpty();
    }
}
