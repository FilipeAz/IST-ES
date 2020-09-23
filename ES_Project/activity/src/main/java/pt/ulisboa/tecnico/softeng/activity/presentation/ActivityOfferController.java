package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;



@Controller
@RequestMapping(value = "/activityProviders")
public class ActivityOfferController {
	
	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);

	@RequestMapping(value="/{activityProviderCode}/activities/{activityName}/activityOffers", method = RequestMethod.GET)
	public String activityProviderForm(Model model, @PathVariable String activityProviderCode, @PathVariable String activityName) {
		logger.info("activityOffersForm");
		ActivityOfferData aod = new ActivityOfferData();
		aod.setActivity(ActivityInterface.getActivityByName(activityProviderCode, activityName));
		model.addAttribute("activityOffer", aod);
		model.addAttribute("activityOffers", ActivityInterface.getActivityOfferDataByName(activityProviderCode, activityName));
		logger.info("activityOffers: " + ActivityInterface.getActivityOfferDataByName(activityProviderCode, activityName).size());
		return "activityOffers";
	}

	@RequestMapping(value="/{activityProviderCode}/activities/{activityName}/activityOffers", method = RequestMethod.POST)
	public String activityOfferSubmit(Model model, @ModelAttribute ActivityOfferData activityOffer, @PathVariable String activityProviderCode, 
			@PathVariable String activityName ) {
		logger.info("activityOfferSubmit activityName:{}, begin:{}, end:{}", activityName, activityOffer.getBegin(), activityOffer.getEnd());
		activityOffer.setActivity(ActivityInterface.getActivityByName(activityProviderCode, activityName));
		try {
			ActivityInterface.createActivityOffer(activityOffer);
		} catch (ActivityException be) {
			model.addAttribute("error", "Error: it was not possible to create the activity provider");
			model.addAttribute("activityOffer", activityOffer);
			model.addAttribute("activityOffers", ActivityInterface.getActivityOfferDataByName(activityProviderCode, activityName));
			return "activityOffers";
		}


		return "redirect:/activityProviders/{activityProviderCode}/activities/{activityName}/activityOffers";


	}
	
	

}
