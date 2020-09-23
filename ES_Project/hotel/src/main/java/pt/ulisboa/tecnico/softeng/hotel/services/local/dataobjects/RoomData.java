package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class RoomData {
	public static enum CopyDepth {
		SHALLOW, BOOKINGS
	};
	
	private String hotelName;
	private String hotelCode;
	private String number;
	private Type type;
	private List<RoomBookingData> bookings = new ArrayList<>();

	public RoomData() {
	}

	public RoomData(Room room, CopyDepth depth) {
		this.hotelName = room.getHotel().getName();
		this.hotelCode = room.getHotel().getCode();
		this.number = room.getNumber();
		this.type = room.getType();
		
		switch (depth) {
		case BOOKINGS:
			for (Booking booking : room.getBookingSet()) {
				this.bookings.add(new RoomBookingData(room, booking));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}

	public String getHotelName() {
		return this.hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelCode() {
		return this.hotelCode;
	}

	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Room.Type getType() {
		return this.type;
	}

	public void setType(Room.Type type) {
		this.type = type;
	}
	
	public List<RoomBookingData> getBookings() {  //  *BRUNO* tem de ser alterado para RoomData
		return this.bookings;
	}

	public void setBookings(List<RoomBookingData> bookings) { // *Bruno* tem de ser alterado para RoomData
		this.bookings = bookings;
	}
}
