package com.rajib.service;

import com.rajib.dto.LoginRequest;
import com.rajib.dto.Response;
import com.rajib.entity.User;

public interface IUserService {

	Response register(User user);
	Response login(LoginRequest loginRequest);
	
	Response getAllUsers();
	Response getUserBookingHistory(String userId);
	Response deleteUser(String userId);
	Response getUserById(String userId);
	Response getMyInfo(String email);
}
