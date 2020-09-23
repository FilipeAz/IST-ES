package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;



import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;

public class HotelData {
	public static enum CopyDepth {
		SHALLOW, ROOMS
	};

	private String name;
	private String code;
	private List<RoomData> rooms = new ArrayList<>();

	public HotelData() {
	}

	public HotelData(Hotel hotel, CopyDepth depth) {
		this.name = hotel.getName();
		this.code = hotel.getCode();
		
		
	
		switch (depth) {
		case ROOMS:
			for (Room room : hotel.getRoomSet()) {
				this.rooms.add(new RoomData(room, RoomData.CopyDepth.SHALLOW));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<RoomData> getRooms() {  //  *BRUNO* tem de ser alterado para RoomData
		return this.rooms;
	}

	public void setRooms(List<RoomData> rooms) { // *Bruno* tem de ser alterado para RoomData
		this.rooms = rooms;
	}

}