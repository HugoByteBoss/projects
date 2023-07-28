package br.com.timetracker.auth;

import br.com.cvc.exceptionhandlerpackage.controller.ExceptionController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableFeignClients
@Import(ExceptionController.class)
public class TimeTrackerAuthApp {

	public static void main(String[] args) {
		SpringApplication.run(TimeTrackerAuthApp.class, args);
	}

}
