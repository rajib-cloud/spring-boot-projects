package com.rajib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rajib.dto.Response;
import com.rajib.entity.OurUser;
import com.rajib.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/auth/register")
	public ResponseEntity<Response> register(@RequestBody Response reg){
		return ResponseEntity.ok(userService.login(reg));
	}
	
	@PostMapping("/auth/login")
	public ResponseEntity<Response> login(@RequestBody Response req){
		return ResponseEntity.ok(userService.login(req));
	}
	
	@PostMapping("/auth/refresh")
	public ResponseEntity<Response> refreshToken(@RequestBody Response req){
		return ResponseEntity.ok(userService.refreshToken(req));
	}
	
	@GetMapping("/admin/get-all-users")
	public ResponseEntity<Response> getAllUsers(){
		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<Response> getUSerByID(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.getUsersById(userId));

    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<Response> updateUser(@PathVariable Integer userId, @RequestBody OurUser Response){
        return ResponseEntity.ok(userService.updateUser(userId, Response));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<Response> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Response response = userService.getMyInfo(email);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<Response> deleteUSer(@PathVariable Integer userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

}
