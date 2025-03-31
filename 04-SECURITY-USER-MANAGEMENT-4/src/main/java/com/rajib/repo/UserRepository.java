package com.rajib.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajib.entity.OurUser;

public interface UserRepository extends JpaRepository<OurUser, Integer> {

	Optional<OurUser> findByEmail(String email);
}
