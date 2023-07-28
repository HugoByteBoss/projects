package br.com.timetracker.auth.application;


import co.elastic.apm.api.CaptureTransaction;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "Generate Token by e-mail")
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    
    @CaptureTransaction
    @PutMapping("/{email}")
    public String giveToken(@PathVariable String email) {
    	log.info("Starting genarete token by : {}", email);
    	return tokenService.generateByEmail(email);
    }
}
