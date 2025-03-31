package com.rajib.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajib.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking,Long>{

	List<Booking> findByRoomId(Long roomId);
	List<Booking> findByUserId(Long userId);
	
	Optional<Booking> findByBookingConfirmationCode(String confirmationCode);
}
