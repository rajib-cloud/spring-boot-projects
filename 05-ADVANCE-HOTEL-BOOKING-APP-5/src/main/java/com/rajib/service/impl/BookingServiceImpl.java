package com.rajib.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rajib.dto.BookingDTO;
import com.rajib.dto.Response;
import com.rajib.entity.Booking;
import com.rajib.entity.Room;
import com.rajib.entity.User;
import com.rajib.exception.OurException;
import com.rajib.repo.BookingRepository;
import com.rajib.repo.RoomRepository;
import com.rajib.repo.UserRepository;
import com.rajib.service.IBookingService;
import com.rajib.utils.Utils;

@Service
public class BookingServiceImpl implements IBookingService{

	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Response saveBooking(Long roomId, Long userId, Booking bookingRequest) {
		Response response = new Response();
		try {
			if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
				throw new IllegalArgumentException("Check in date must come after check out date.");
			}
			Room room = roomRepository.findById(roomId).orElseThrow(()-> new OurException("room not found."));
			User user = userRepository.findById(userId).orElseThrow(()->new OurException("user not found."));
			
			List<Booking> existingBookings = room.getBookings();
			
			if(!roomIsAvailable(bookingRequest, existingBookings)) {
				throw new OurException("Room not available for selected date.");
			}
			bookingRequest.setRoom(room);
			bookingRequest.setUser(user);
			String bookingConfirmationCode = Utils.generateRandomConfirmationCode(10);
			bookingRequest.setBookingConfirmationCode(bookingConfirmationCode);
			bookingRepository.save(bookingRequest);
			
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setBookingConfirmationCode(bookingConfirmationCode);
			
		}catch(OurException e) {
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error while booking room: "+e.getMessage());
		}
		return response;
	}
	private boolean roomIsAvailable(Booking bookingRequest, List<Booking> existingBookings) {

        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }

	@Override
	public Response findBookingByConfirmationCode(String confirmationCode) {
		Response response = new Response();
		try {
			Booking booking = bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new OurException("booking not found."));
			BookingDTO bookingDTO = Utils.mapBookingEntityToBookingDTOPlusBookedRooms(booking, true);
			
			response.setStatusCode(200);
			response.setMessage("successful");
			response.setBooking(bookingDTO);
		}catch(OurException e) {
			response.setStatusCode(404);
			response.setMessage(e.getMessage());
			
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error while booking: "+e.getMessage());
		}
		return response;
	}

	@Override
	public Response getAllBookings() {
		Response response = new Response();
		try {
			List<Booking> bookingList = bookingRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
			List<BookingDTO> bookingDTOList = Utils.mapBookingListEntityToBookingListDTO(bookingList);
			response.setStatusCode(200);
            response.setMessage("successful");
            response.setBookingList(bookingDTOList);
		}catch(OurException e) {
			response.setStatusCode(404);
            response.setMessage(e.getMessage());
		}catch(Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error while fetching all records: "+e.getMessage());
		}
		return response;
	}

	@Override
    public Response cancelBooking(Long bookingId) {

        Response response = new Response();

        try {
            bookingRepository.findById(bookingId).orElseThrow(() -> new OurException("Booking Does Not Exist"));
            bookingRepository.deleteById(bookingId);
            response.setStatusCode(200);
            response.setMessage("successful");

        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error Cancelling a booking: " + e.getMessage());

        }
        return response;
    }

}
