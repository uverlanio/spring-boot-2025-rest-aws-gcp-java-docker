package br.com.sbrwgjd;

import br.com.sbrwgjd.security.jwt.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;

import java.util.*;

@SpringBootApplication
public class StartUp {

	public static void main(String[] args) {
		SpringApplication.run(StartUp.class, args);
		generateHashPassword();
	}

	private static void generateHashPassword() {

		PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
				"",
				8,
				185000,
				Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
		);

		Map<String, PasswordEncoder> encoders = new HashMap<>();

		encoders.put("pbkdf2", pbkdf2Encoder);
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(encoders.get("pbkdf2"));
		var pass1 = passwordEncoder.encode("admin123");
		var pass2 = passwordEncoder.encode("admin234");

		System.out.println(pass1);
		System.out.println(pass2);
	}
}
