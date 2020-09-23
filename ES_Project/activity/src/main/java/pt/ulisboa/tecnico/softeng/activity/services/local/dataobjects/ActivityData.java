package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;


import java.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;


public class ActivityData {
	
	
	
	ActivityProviderData activityProvider;
	int minAge;
	int maxAge;
	int capacity;
	String name;
	public ActivityData(){
		
	}
	
	public ActivityData(ActivityProviderData provider, String name, int minAge, int maxAge, int capacity) {
		setActivityProvider(provider);
		setName(name);
		setMinAge(minAge);
		setMaxAge(maxAge);
		setCapacity(capacity);
		
	}		
	
	public void setActivityProvider(ActivityProviderData provider) { this.activityProvider = provider; } 
	public void setCapacity(int capacity){ this.capacity = capacity; }
	public void setMinAge(int minAge){ this.minAge = minAge; }
	public void setMaxAge(int maxAge){ this.maxAge = maxAge; }
	public void setName(String name){ this.name = name; }
	public ActivityProviderData getActivityProvider() { return this.activityProvider; }
	public int getMinAge() { return this.minAge; }
	public int getMaxAge() { return this.maxAge; }
	public int getCapacity() { return this.capacity; }
	public String getName() { return this.name; }
}