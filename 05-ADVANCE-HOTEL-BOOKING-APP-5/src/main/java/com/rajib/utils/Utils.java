package com.rajib.utils;



import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

import com.rajib.dto.BookingDTO;
import com.rajib.dto.RoomDTO;
import com.rajib.dto.UserDTO;
import com.rajib.entity.Booking;
import com.rajib.entity.Room;
import com.rajib.entity.User;

// Utility class to provide helper methods for various operations
public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Alphanumeric characters for code generation
    private static final SecureRandom secureRandom = new SecureRandom(); // SecureRandom instance for randomness

    // Method to generate a random alphanumeric confirmation code
    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    // Method to map User entity to UserDTO
    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId()); // Map user ID
        userDTO.setName(user.getName()); // Map user name
        userDTO.setEmail(user.getEmail()); // Map user email
        userDTO.setPhoneNumber(user.getPhoneNumber()); // Map user phone number
        userDTO.setRole(user.getRole()); // Map user role
        return userDTO;
    }

    // Method to map Room entity to RoomDTO
    public static RoomDTO mapRoomEntityToRoomDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId()); // Map room ID
        roomDTO.setRoomType(room.getRoomType()); // Map room type
        roomDTO.setRoomPrice(room.getRoomPrice()); // Map room price
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl()); // Map room photo URL
        roomDTO.setRoomDescription(room.getRoomDescription()); // Map room description
        return roomDTO;
    }

    // Method to map Booking entity to BookingDTO
    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId()); // Map booking ID
        bookingDTO.setCheckInDate(booking.getCheckInDate()); // Map check-in date
        bookingDTO.setCheckOutDate(booking.getCheckOutDate()); // Map check-out date
        bookingDTO.setNumOfAdults(booking.getNumOfAdults()); // Map number of adults
        bookingDTO.setNumOfChildren(booking.getNumOfChildren()); // Map number of children
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest()); // Map total number of guests
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode()); // Map booking confirmation code
        return bookingDTO;
    }

    // Method to map Room entity to RoomDTO including associated bookings
    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room) {
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());

        // Map associated bookings if available
        if (room.getBookings() != null) {
            roomDTO.setBookings(room.getBookings().stream()
                    .map(Utils::mapBookingEntityToBookingDTO)
                    .collect(Collectors.toList()));
        }
        return roomDTO;
    }

    // Method to map Booking entity to BookingDTO including user and room details
    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking, boolean mapUser) {
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());

        if (mapUser) {
            bookingDTO.setUser(Utils.mapUserEntityToUserDTO(booking.getUser())); // Map associated user
        }
        if (booking.getRoom() != null) {
            RoomDTO roomDTO = new RoomDTO();

            roomDTO.setId(booking.getRoom().getId());
            roomDTO.setRoomType(booking.getRoom().getRoomType());
            roomDTO.setRoomPrice(booking.getRoom().getRoomPrice());
            roomDTO.setRoomPhotoUrl(booking.getRoom().getRoomPhotoUrl());
            roomDTO.setRoomDescription(booking.getRoom().getRoomDescription());
            bookingDTO.setRoom(roomDTO); // Map associated room
        }
        return bookingDTO;
    }

    // Method to map User entity to UserDTO including associated bookings and their rooms
    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        // Map associated bookings if available
        if (!user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream()
                    .map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking, false))
                    .collect(Collectors.toList()));
        }
        return userDTO;
    }

    // Method to map a list of User entities to UserDTOs
    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream()
                .map(Utils::mapUserEntityToUserDTO)
                .collect(Collectors.toList());
    }

    // Method to map a list of Room entities to RoomDTOs
    public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList) {
        return roomList.stream()
                .map(Utils::mapRoomEntityToRoomDTO)
                .collect(Collectors.toList());
    }

    // Method to map a list of Booking entities to BookingDTOs
    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList) {
        return bookingList.stream()
                .map(Utils::mapBookingEntityToBookingDTO)
                .collect(Collectors.toList());
    }
}

