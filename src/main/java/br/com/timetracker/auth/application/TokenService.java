package br.com.timetracker.auth.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final BCryptPasswordEncoder cryptService;
    private final UserAsyncService userAsyncService;

    // Vamos usar esses caracteres secretos para gerar o token do usuário
    private static final String SECRET_CHARS = "RXYZabghijyz289!@#(=+";

    // Método para gerar um token criptografado baseado no e-mail
    public String generateByEmail(String email) {
        // Dar uma olhada no e-mail para ver se está tudo certo
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        // Juntamos o e-mail com caracteres secretos para criar o token
        String textToEncrypt = cryptService.encode(email + SECRET_CHARS);

        // Persistir o usuário de forma assíncrona e retornar o resultado futuro
        Future<Void> asyncResult = userAsyncService.persistUserAsync(email, textToEncrypt);

        // Usar a criptografia e temos o nosso token seguro
        return textToEncrypt;
    }

    // Esse método verifica se o e-mail é válido
    private boolean isValidEmail(String email) {
        // Regex verifica o formato do e-mail
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        // Checando se o e-mail está correto!
        return email != null && email.matches(emailRegex);
    }
}
