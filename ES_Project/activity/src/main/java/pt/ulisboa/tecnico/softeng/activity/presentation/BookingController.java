package pt.ulisboa.tecnico.softeng.activity.presentation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.BookingData;

@Controller
@RequestMapping(value = "/activityProviders")
public class BookingController {
	
	private static Logger logger = LoggerFactory.getLogger(BookingController.class);

	@RequestMapping(value = "/{activityProviderCode}/activities/{activityName}/activityOffers/{activityOfferBegin}/Bookings", method = RequestMethod.GET)
	public String activityProviderForm(Model model, @PathVariable String activityProviderCode, @PathVariable String activityName, 
			@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate activityOfferBegin ) {
		logger.info("bookingForm providerCode:{}, activityName:{}, activityOffer:{}", activityProviderCode, activityName, activityOfferBegin);
		BookingData bd = new BookingData();
		model.addAttribute("booking", bd);
		Set<BookingData> bs = ActivityInterface.getBookingByDate(activityProviderCode, activityName, activityOfferBegin);
		for (BookingData b : bs ) System.out.println(b.getReference());
		model.addAttribute("currBookings", bs);
		logger.info("bookings: " + bs.size());
		return "bookings";
	}

	@InitBinder
    public void bindDatePost(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
	@RequestMapping(value = "/{activityProviderCode}/activities/{activityName}/activityOffers/{activityOfferBegin}/Bookings", method = RequestMethod.POST)
	public String bookingSubmit(Model model, @ModelAttribute BookingData booking, @PathVariable String activityProviderCode, @PathVariable String activityName, 
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate activityOfferBegin) {
		logger.info("bookingSubmit activityOfferBegin:{}", activityOfferBegin);
		booking.setActivityOffer(ActivityInterface.getActivityOffer(activityProviderCode, activityName, activityOfferBegin));
		try {
			ActivityInterface.createBooking(booking);
		} catch (ActivityException be) {
			logger.info(be.getMessage());
			model.addAttribute("error", "Error: it was not possible to create the booking");
			model.addAttribute("booking", booking);
			model.addAttribute("currBookings", ActivityInterface.getBookingByDate(activityProviderCode, activityName, activityOfferBegin));
			return "bookings";
		}

		return "redirect:/activityProviders/{activityProviderCode}/activities/{activityName}/activityOffers/{activityOfferBegin}/Bookings";
	}

}