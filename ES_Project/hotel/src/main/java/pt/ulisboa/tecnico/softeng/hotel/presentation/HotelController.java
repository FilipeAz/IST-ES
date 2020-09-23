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

@Controller
@RequestMapping(value = "/hotels")
public class HotelController {
	private static Logger logger = LoggerFactory.getLogger(HotelController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String hotelForm(Model model) {
		logger.info("hotelForm");
		model.addAttribute("hotel", new HotelData());
		model.addAttribute("hotels", HotelInterface.getHotels());
		return "hotels"; 
	}

	@RequestMapping(method = RequestMethod.POST)
	public String hotelSubmit(Model model, @ModelAttribute HotelData hotelData) {
		logger.info("hotelSubmit name:{}, code:{}", hotelData.getName(), hotelData.getCode());

		try {
			HotelInterface.createHotel(hotelData);
		} catch (HotelException be) {
			model.addAttribute("error", "Error: it was not possible to create the hotel");
			model.addAttribute("hotel", hotelData);
			model.addAttribute("hotels", HotelInterface.getHotels());
			return "hotels";
		}

		return "redirect:/hotels";
	}

	/*
	@RequestMapping(value = "/hotel/{code}/rooms", method = RequestMethod.GET)
	public String showHotel(Model model, @PathVariable String code) {
		logger.info("showHotel code:{}", code);
		

		Hotel hotel = HotelInterface.getHotelByCode(code);

		new Room(hotel, "ID01",null); //PERIGO BRUNO inves de null colocar os respetivos types
		new Room(hotel, "ID02",null); // PERIGO BRUNO

		model.addAttribute("hotel", hotel);
		return "hotel";
	}
	*/
}