package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;



import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityOfferData {
	
	
	
	Activity activity;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate begin;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate end;
	
	public ActivityOfferData(){
		
	}
	
	public ActivityOfferData(Activity activity, LocalDate begin, LocalDate end) {
		setActivity(activity);
		setBegin(begin);
		setEnd(end);
	}		
	
	public void setActivity(Activity activity) { this.activity = activity; } 
	public void setBegin(LocalDate begin){ this.begin = begin; }
	public void setEnd(LocalDate end){ this.end = end; }
	public Activity getActivity() { return this.activity; }
	public LocalDate getBegin() { return this.begin; }
	public LocalDate getEnd() { return this.end; }
	


}