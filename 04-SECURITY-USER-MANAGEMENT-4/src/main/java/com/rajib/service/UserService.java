package com.rajib.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rajib.dto.Response;
import com.rajib.entity.OurUser;
import com.rajib.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public Response register(Response registrationRequest) {
		Response response = new Response();
		
		try {
			OurUser ourUser = new OurUser();
			ourUser.setEmail(registrationRequest.getEmail());
			ourUser.setCity(registrationRequest.getCity());
			ourUser.setRole(registrationRequest.getRole());
			ourUser.setName(registrationRequest.getName());
			ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
			OurUser savedUser = userRepo.save(ourUser);
			
			if(savedUser.getId() > 0) {
				response.setOurUsers(ourUser);
				response.setMessage("User saved Successfuly");
				response.setStatusCode(200);
			}
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setError(e.getMessage());
		}
		return response;
	}
	
	public Response login(Response loginRequest) {
		Response response = new Response();
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
			var user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
			var jwt = jwtUtils.generateToken(user);
			var refreshtoken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
			response.setStatusCode(200);
			response.setToken(jwt);
			response.setRole(user.getRole());
			response.setRefreshToken(refreshtoken);
			response.setExpirationTime("24Hrs");
			response.setMessage("Successfully Logged In");
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage(e.getMessage());
		}
		return response;
	}
	
	 public Response refreshToken(Response refreshTokenReqiest){
		 Response response = new Response();
	        try{
	            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
	            OurUser users = userRepo.findByEmail(ourEmail).orElseThrow();
	            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
	                var jwt = jwtUtils.generateToken(users);
	                response.setStatusCode(200);
	                response.setToken(jwt);
	                response.setRefreshToken(refreshTokenReqiest.getToken());
	                response.setExpirationTime("24Hr");
	                response.setMessage("Successfully Refreshed Token");
	            }
	            response.setStatusCode(200);
	            return response;

	        }catch (Exception e){
	            response.setStatusCode(500);
	            response.setMessage(e.getMessage());
	            return response;
	        }
	    }


	    public Response getAllUsers() {
	    	Response response = new Response();

	        try {
	            List<OurUser> result = userRepo.findAll();
	            if (!result.isEmpty()) {
	            	response.setOurUsersList(result);
	            	response.setStatusCode(200);
	            	response.setMessage("Successful");
	            } else {
	            	response.setStatusCode(404);
	            	response.setMessage("No users found");
	            }
	            return response;
	        } catch (Exception e) {
	        	response.setStatusCode(500);
	        	response.setMessage("Error occurred: " + e.getMessage());
	            return response;
	        }
	    }


	    public Response getUsersById(Integer id) {
	        Response response = new Response();
	        try {
	            OurUser usersById = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
	            response.setOurUsers(usersById);
	            response.setStatusCode(200);
	            response.setMessage("Users with id '" + id + "' found successfully");
	        } catch (Exception e) {
	        	response.setStatusCode(500);
	        	response.setMessage("Error occurred: " + e.getMessage());
	        }
	        return response;
	    }


	    public Response deleteUser(Integer userId) {
	        Response reqRes = new Response();
	        try {
	            Optional<OurUser> userOptional = userRepo.findById(userId);
	            if (userOptional.isPresent()) {
	                userRepo.deleteById(userId);
	                reqRes.setStatusCode(200);
	                reqRes.setMessage("User deleted successfully");
	            } else {
	                reqRes.setStatusCode(404);
	                reqRes.setMessage("User not found for deletion");
	            }
	        } catch (Exception e) {
	            reqRes.setStatusCode(500);
	            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
	        }
	        return reqRes;
	    }

	    public Response updateUser(Integer userId, OurUser updatedUser) {
	    	Response reqRes = new Response();
	        try {
	            Optional<OurUser> userOptional = userRepo.findById(userId);
	            if (userOptional.isPresent()) {
	                OurUser existingUser = userOptional.get();
	                existingUser.setEmail(updatedUser.getEmail());
	                existingUser.setName(updatedUser.getName());
	                existingUser.setCity(updatedUser.getCity());
	                existingUser.setRole(updatedUser.getRole());

	                // Check if password is present in the request
	                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
	                    // Encode the password and update it
	                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
	                }

	                OurUser savedUser = userRepo.save(existingUser);
	                reqRes.setOurUsers(savedUser);
	                reqRes.setStatusCode(200);
	                reqRes.setMessage("User updated successfully");
	            } else {
	                reqRes.setStatusCode(404);
	                reqRes.setMessage("User not found for update");
	            }
	        } catch (Exception e) {
	            reqRes.setStatusCode(500);
	            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
	        }
	        return reqRes;
	    }


	    public Response getMyInfo(String email){
	    	Response reqRes = new Response();
	        try {
	            Optional<OurUser> userOptional = userRepo.findByEmail(email);
	            if (userOptional.isPresent()) {
	                reqRes.setOurUsers(userOptional.get());
	                reqRes.setStatusCode(200);
	                reqRes.setMessage("successful");
	            } else {
	                reqRes.setStatusCode(404);
	                reqRes.setMessage("User not found for update");
	            }

	        }catch (Exception e){
	            reqRes.setStatusCode(500);
	            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
	        }
	        return reqRes;

	    }

}
