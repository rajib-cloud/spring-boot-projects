package com.rajib.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rajib.dto.LoginRequest;
import com.rajib.dto.Response;
import com.rajib.dto.UserDTO;
import com.rajib.entity.User;
import com.rajib.exception.OurException;
import com.rajib.repo.UserRepository;
import com.rajib.service.IUserService;
import com.rajib.utils.JWTUtils;
import com.rajib.utils.Utils;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public Response register(User user) {
		Response response = new Response();
		try {
			if (user.getRole() == null || user.getRole().isBlank()) {
				user.setRole("USER");
			}
			if (userRepository.existsByEmail(user.getEmail())) {
				throw new OurException(user.getEmail() + "Already Exist");
			}
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			User savedUser = userRepository.save(user);
			UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);

			response.setStatusCode(200);
			response.setUser(userDTO);

		} catch (OurException e) {
			response.setStatusCode(400);
			response.setMessage(e.getMessage());

		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error occured during user registration : "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response login(LoginRequest loginRequest) {
		Response response = new Response();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			 var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new OurException("User not found"));
			 var token = jwtUtils.generateToken(user);
			 
			 response.setStatusCode(200);
			 response.setToken(token);
			 response.setRole(user.getRole());
			 response.setExpirationtime("7 Days");
			 response.setMessage("Successful");
			 
			 
		}catch(OurException e) {
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
		}
		catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error while login: "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getAllUsers() {
		Response response = new Response();
		try {
			List<User> userList = userRepository.findAll();
			List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
			
			response.setStatusCode(200);
			response.setMessage("Successful");
			response.setUserList(userDTOList);
	
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error while getting all user: "+e.getMessage());
		}

		return response;
	}

	@Override
	public Response getUserBookingHistory(String userId) {

		Response response = new Response();
		try {
			User user = userRepository.findById(Long.valueOf(userId))
			.orElseThrow(()-> new OurException("user not found. "));
			
			UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setUser(userDTO);
		}catch(OurException e) {
			response.setStatusCode(400);
			response.setMessage("successful");
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error while fetching booking history. "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response deleteUser(String userId) {

		Response response = new Response();
		try {
			if(!userRepository.existsById(Long.valueOf(userId))) {
				throw new OurException("user not found");
			}
			userRepository.deleteById(Long.valueOf(userId));
			
			response.setStatusCode(200);
			response.setMessage("User deleted successfuly.");
		}catch(OurException e) {
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error while deleteing user: "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getUserById(String userId) {
		Response response = new Response();
		try {
			User user = userRepository.findById(Long.parseLong(userId))
			.orElseThrow(()-> new OurException("user not found."));
			
			UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
			
			response.setStatusCode(200);
			response.setMessage("user details retrieved successfully.");
			response.setUser(userDTO);
		}catch(OurException e) {
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error while fetching user details: "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getMyInfo(String email) {
		Response response = new Response();
		try {
			User user = userRepository.findByEmail(email).orElseThrow(()-> new OurException("user not found"));
			UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
			
			response.setStatusCode(200);
			response.setMessage("user details retrieved successfuly.");
			response.setUser(userDTO);
		}catch(OurException e) {
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error while fetching user info: "+e.getMessage());
		}
		return response;
	}

}
