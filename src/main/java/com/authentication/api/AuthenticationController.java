package com.authentication.api;

import com.authentication.repository.TokenRepository;
import com.authentication.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.model.ConfirmationToken;
import com.authentication.model.User;
import com.authentication.payloads.request.LoginRequest;
import com.authentication.payloads.request.SignUpRequest;
import com.authentication.payloads.response.JwtResponse;
import com.authentication.payloads.response.MessageResponse;
import com.authentication.security.jwt.JwtUtils;
import com.authentication.security.services.UserDetailsImpl;
import com.authentication.service.EmailService;


@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	TokenRepository tokenRepository;
	
	@Autowired
	EmailService emailService;
	
	@Value("{$spring.mail.username}")
	private String fromEmail;
	
	private String confirmAccountURL = "https://authentication--service.herokuapp.com/authentication/confirm-account?token=";
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		
		User tempUser = userRepository.findByusername(loginRequest.getUsername());
		if(tempUser == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("UserName does not exist !"));
		}
		else {
			if(tempUser.isConfirmed()) {
				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = jwtUtils.generateJwtToken(authentication);
				
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
				
				return ResponseEntity.ok(new JwtResponse(jwt, 
														 userDetails.getId(), 
														 userDetails.getUsername(), 
														 userDetails.getEmail() 
														 ));
			} else {
				return ResponseEntity.status(401).body(new MessageResponse("Please confirm the email before signin"));
			}
		}	
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}


		// Create new user's account
		User user = new User(ObjectId.get().toString(),
							 signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()),
							 signUpRequest.getPhoneNumber()
							 );
		
		if(user.getUsername().contains("admin")) {
			user.setUserType("admin");
		}
		userRepository.save(user);
		
		//Send confirmation mail
		ConfirmationToken token = new ConfirmationToken(user);
		tokenRepository.save(token);
		System.out.println("hi");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Confirm the account");
		mailMessage.setFrom(fromEmail);
		mailMessage.setText("In order to Confirm your account, click on the below given link : " + 
		confirmAccountURL + token.getConfirmationToken());
		System.out.println(mailMessage);
		try {
			emailService.sendEmail(mailMessage);
		} catch(Exception e) {
			System.out.println(e);
		}
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	@RequestMapping(value = "/confirm-account", method = RequestMethod.GET)
	public ResponseEntity<?> confirmAccount(@RequestParam("token") String confToken) {
		
		ConfirmationToken Token = tokenRepository.findByconfirmationToken(confToken);
		
		if(Token == null) {
			return ResponseEntity.badRequest().body(new MessageResponse("Link is invalid or broken"));
		}
		
		User user = userRepository.findByemail(Token.getUser().getEmail());
		user.setConfirmed(true);
		userRepository.save(user);
		return ResponseEntity.ok().body(new MessageResponse("Account is verified"));
	}
	
	
}