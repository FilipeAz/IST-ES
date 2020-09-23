package pt.ulisboa.tecnico.softeng.activity.presentation;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;



@Controller
@RequestMapping(value = "/activityProviders")
public class ActivityController {
	
	private static Logger logger = LoggerFactory.getLogger(ActivityController.class);

	@RequestMapping(value="/{activityProviderCode}/activities", method = RequestMethod.GET)
	public String activityForm(Model model, @PathVariable String activityProviderCode) {
		System.out.println(activityProviderCode + "\n\n\n\n");
		logger.info("activityForm: {}", activityProviderCode);
		ActivityProviderData ap = ActivityInterface.getProviderDataByCode(activityProviderCode);
		ActivityData ad = new ActivityData();
		ad.setActivityProvider(ap);
		model.addAttribute("activity", ad);
		Set<ActivityData> acts = ActivityInterface.getActivityByProviderCode(activityProviderCode);
		model.addAttribute("activities", acts);
		model.addAttribute("providerCode", activityProviderCode);
		if (acts != null) logger.info("activities: " + acts.size());
		return "activities";
	}

	@RequestMapping(value="/{activityProviderCode}/activities", method = RequestMethod.POST)
	public String activitySubmit(Model model, @ModelAttribute ActivityData activity, @PathVariable String activityProviderCode) {
		ActivityProviderData ap = ActivityInterface.getProviderDataByCode(activityProviderCode);
		activity.setActivityProvider(ap);
		logger.info("activitySubmit providerCode:{}, name:{}, minAge:{}, maxAge:{}, capacity:{}",activity.getActivityProvider().getCode(), activity.getName(), activity.getMinAge(), 
				activity.getMaxAge(), activity.getCapacity());
		try {
			ActivityInterface.createActivity(activity);
		} catch (ActivityException be) {
			logger.info(be.getMessage());
			model.addAttribute("error", "Error: it was not possible to create the activity");
			model.addAttribute("activity", activity);
			model.addAttribute("activities", ActivityInterface.getActivityByProviderCode(activityProviderCode));
			return "activities";
		}

		return "redirect:/activityProviders/{activityProviderCode}/activities";

	}



}