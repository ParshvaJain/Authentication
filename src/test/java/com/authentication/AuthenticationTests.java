package com.authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.authentication.model.User;
import com.authentication.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
public class AuthenticationTests {
	
	@Mock
	private UserRepository repository;
	
	@Test
	public void check_if_user_created() {
		User user = new User();
		user.setUsername("test");
		user.setEmail("test@email.com");
		
		when(repository.save(any(User.class))).thenReturn(user);
		User savedUser = repository.findByusername(user.getUsername());
		MatcherAssert.assertThat(savedUser.getUsername(), true);
	}
	
	
}
