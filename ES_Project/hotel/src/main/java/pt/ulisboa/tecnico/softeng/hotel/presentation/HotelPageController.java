package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData.CopyDepth;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;

@Controller
@RequestMapping(value = "/hotels/hotel/{hotelCode}/rooms")
public class HotelPageController {
	private static Logger logger = LoggerFactory.getLogger(HotelPageController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showHotel(Model model, @PathVariable String hotelCode) {
		logger.info("showHotel code:{}", hotelCode);

		HotelData hotelData = HotelInterface.getHotelDataByCode(hotelCode, CopyDepth.ROOMS);

		if (hotelData == null) {
			model.addAttribute("error", "Error: it does not exist a hotel with the code " + hotelCode);
			model.addAttribute("hotel", new HotelData());
			model.addAttribute("hotels", HotelInterface.getHotels());
			return "hotels";
		} else {
			model.addAttribute("room", new RoomData());
			model.addAttribute("hotel", hotelData);
			return "rooms";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitRoom(Model model, @PathVariable String hotelCode, @ModelAttribute RoomData roomData) {
		logger.info("submitRoom hotelCode:{}, number:{}, type:{}", hotelCode, roomData.getNumber(), roomData.getType());

		try {
			HotelInterface.createRoom(hotelCode, roomData);
		} catch (HotelException be) {
			model.addAttribute("error", "Error: it was not possible to create the room");
			model.addAttribute("room", roomData);
			model.addAttribute("hotels", HotelInterface.getHotelDataByCode(hotelCode, CopyDepth.ROOMS));
			return "rooms";
		}

		return "redirect:/hotels/hotel/" + hotelCode + "/rooms";
	}

}
