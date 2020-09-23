package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;

@Controller
@RequestMapping(value = "/activityProviders")
public class ActivityProviderController {
	private static Logger logger = LoggerFactory.getLogger(ActivityProviderController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String activityProviderForm(Model model) {
		logger.info("activityProviderForm");
		model.addAttribute("activityProvider", new ActivityProviderData());
		model.addAttribute("providers", ActivityInterface.getActivityProviders());
		logger.info("activityProviders: " + ActivityInterface.getActivityProviders().size());
		return "activityProviders";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String activityProviderSubmit(Model model, @ModelAttribute ActivityProviderData activityProvider) {
		logger.info("activityProviderSubmit name:{}, code:{}", activityProvider.getName(), activityProvider.getCode());

		try {
			ActivityInterface.createActivityProvider(activityProvider);
		} catch (ActivityException be) {
			logger.info(be.getMessage());
			model.addAttribute("error", "Error: it was not possible to create the activity provider");
			model.addAttribute("activityProvider", activityProvider);
			model.addAttribute("providers", ActivityInterface.getActivityProviders());
			return "activityProviders";
		}

		return "redirect:/activityProviders";
	}
}