package pt.ulisboa.tecnico.softeng.hotel.services.local;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData.CopyDepth;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomBookingData;


public class HotelInterface {
	
	
	@Atomic(mode = TxMode.READ)
	public static List<HotelData> getHotels() {
		List<HotelData> hotels = new ArrayList<HotelData>();
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotels.add(new HotelData(hotel, HotelData.CopyDepth.SHALLOW));
		}
		return hotels;
	}
	

	
	@Atomic(mode = TxMode.WRITE)
	public static void createHotel(HotelData hotelData) {
		new Hotel(hotelData.getCode(), hotelData.getName());
	}	
	
	
	@Atomic(mode = TxMode.WRITE)
	public static Hotel getHotelByCode(String code) {
			for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
				if (hotel.getCode().equals(code)) {
					return hotel;
				}
			}
			return null;
	}
	
	
	
	
	

	
	@Atomic(mode = TxMode.READ)
	public static HotelData getHotelDataByCode(String hotelCode, CopyDepth depth) {
		Hotel hotel = getHotelByCode(hotelCode);

		if (hotel != null) {
			return new HotelData(hotel, depth);
		} else {
			return null;
		}
	}
	

	
	@Atomic(mode = TxMode.WRITE)
	public static void createRoom(String hotelCode, RoomData roomData) {
		new Room(getHotelByCode(hotelCode), roomData.getNumber(), roomData.getType());

	}
	

	@Atomic(mode = TxMode.WRITE)
	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelBooking(String reference) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			Booking booking = hotel.getBooking(reference);
			if (booking != null) {
				return booking.cancel();
			}
		}
		throw new HotelException();
	}

	@Atomic(mode = TxMode.READ)
	public static RoomBookingData getRoomBookingData(String reference) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			for (Room room : hotel.getRoomSet()) {
				Booking booking = room.getBooking(reference);
				if (booking != null) {
					return new RoomBookingData(room, booking);
				}
			}
		}
		throw new HotelException();
	}



	static List<Room> getAvailableRooms(int number, LocalDate arrival, LocalDate departure) {
		List<Room> availableRooms = new ArrayList<>();
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			availableRooms.addAll(hotel.getAvailableRooms(arrival, departure));
			if (availableRooms.size() >= number) {
				return availableRooms;
			}
		}
		return availableRooms;
	}


/*	@Atomic(mode = TxMode.WRITE)
	public static void createBooking(String roomNumber, RoomBookingData bookingData) {
		new Booking(getRoomByCode(roomNumber), bookingData.getArrival(), bookingData.getDeparture());

	}*/
}
