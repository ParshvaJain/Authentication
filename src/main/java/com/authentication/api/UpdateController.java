package com.authentication.api;


import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.authentication.message.ResponseMessage;
import com.authentication.model.User;
import com.authentication.repository.UserRepository;

@CrossOrigin(origins ="*")
@RestController
@RequestMapping("/authentication/update")
public class UpdateController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> updateProfileUsingId(@PathVariable("id") String id,@RequestBody User user) {
		Optional<User> tempOptional = userRepository.findById(id);
		
		if(tempOptional.isPresent()) {
			User tempUser;
			tempUser = tempOptional.get();	
			user.setId(tempUser.getId().toString());
			user.setPassword(encoder.encode(user.getPassword()));
			user.setEmail(tempUser.getEmail());
			userRepository.save(user);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("User profile updated successfully !"));
		} else {
			return ResponseEntity.badRequest().body(new ResponseMessage("User is not present"));
		}
	}
	
	@RequestMapping(value = "/getDetails/{id}", method = RequestMethod.GET)
	public ResponseEntity<Optional<User>> getUserDetailsById(@PathVariable("id") String id) {
		Optional<User> tempUser = userRepository.findById(id);
		if(tempUser != null) {
			return new ResponseEntity<>(tempUser,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}