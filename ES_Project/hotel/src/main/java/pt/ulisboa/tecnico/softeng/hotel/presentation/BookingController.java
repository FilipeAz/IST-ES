package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomBookingData;

@Controller
@RequestMapping(value = "/hotel/{hotelCode}/room/{roomCode}/booking")
public class BookingController {
	private static Logger logger = LoggerFactory.getLogger(BookingController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showBooking(Model model, @PathVariable String roomCode) {
		logger.info("showBookings code:{}", roomCode);

		RoomBookingData rbData = HotelInterface.getHotelByCode(HotelInterface.getRoomDatabyCode(roomCode).getHotelCode()).getRoomSet();

		
		if (rbData == null) {
			model.addAttribute("error", "Error: it does not exist a broker with the code " + roomCode);
			model.addAttribute("room", new RoomBookingData());
			model.addAttribute("rooms", HotelInterface.getRooms());
			return "rooms";
		} else {
			model.addAttribute("booking", new RoomBookingData());
			model.addAttribute("room", rbData);
			return "bookings";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitBooking(Model model, @PathVariable String roomCode, @ModelAttribute RoomBookingData bookingData) {
		logger.info("submitBulk roomCode:{}, arrival:{}, departure:{}", roomCode,
				bookingData.getArrival(), bookingData.getDeparture());

		try {
			HotelInterface.createBooking(roomCode, bookingData);
		} catch (HotelException be) {
			model.addAttribute("error", "Error: it was not possible to create the bulk room booking");
			model.addAttribute("booking", bookingData);
			model.addAttribute("room", HotelInterface.getRoomBookingData(roomCode));
			return "bookings";
		}

		return "redirect:/room/" + roomCode + "/bookings";
	}

}
