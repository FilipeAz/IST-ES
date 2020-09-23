package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;


public class BookingData {
	
	
	ActivityOffer offer;
	String reference;
	String cancel;
	org.joda.time.LocalDate cancellationDate;
	
	public BookingData(){
		
	}
	
	public BookingData(ActivityOffer offer) {
			
		setActivityOffer(offer);
		
	}		
	
	public void setActivityOffer(ActivityOffer offer) { this.offer = offer; } 
	public ActivityOffer getActivityOffer() { return this.offer; }
	public String getReference(){ return this.reference; }
	public void setReference(String reference){ this.reference = reference; }
	public String getCancel(){ return this.cancel; }
	public void setCancel(String string){ this.cancel = string; }
	public org.joda.time.LocalDate getCancellationDate(){ return this.cancellationDate; }
	public void setCancellationDate(org.joda.time.LocalDate localDate){ this.cancellationDate = localDate; }

}