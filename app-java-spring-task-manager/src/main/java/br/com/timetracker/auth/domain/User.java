package br.com.timetracker.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {
	private String id;
	private String email;
	private String token;
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalDateTime createDate;
	@DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
	private LocalDateTime updateDate;

	public static User createNew(String email, String token) {
		return User.builder()
				.email(email)
				.token(token)
				.createDate(LocalDateTime.now())
				.build();
	}
}
