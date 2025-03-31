package com.rajib.service;

import com.rajib.dto.Response;
import com.rajib.entity.Booking;

public interface IBookingService {

	Response saveBooking(Long roomId, Long userId, Booking bookingRequest);
	Response findBookingByConfirmationCode(String confirmationCode);
	Response getAllBookings();
	Response cancelBooking(Long booking);
}
