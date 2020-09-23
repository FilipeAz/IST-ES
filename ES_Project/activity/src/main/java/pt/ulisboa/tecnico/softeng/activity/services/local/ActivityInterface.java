package pt.ulisboa.tecnico.softeng.activity.services.local;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.BookingData;

public class ActivityInterface {

	@Atomic(mode = TxMode.READ)
	public static List<ActivityProviderData> getActivityProviders() {
		List<ActivityProviderData> providers = new ArrayList<ActivityProviderData>();
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			providers.add(new ActivityProviderData(provider.getCode(), provider.getName()));
		}
		return providers;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createActivityProvider(ActivityProviderData activityProvider) {
		new ActivityProvider(activityProvider.getCode(), activityProvider.getName());
	}	
	
	@Atomic(mode = TxMode.WRITE)
	public static void createActivityOffer(ActivityOfferData activityOffer) {
		new ActivityOffer(activityOffer.getActivity(), activityOffer.getBegin(), activityOffer.getEnd());
	}	
	
	@Atomic(mode = TxMode.WRITE)
	public static void createActivity(ActivityData activity) {
		new Activity(getProviderByCode(activity.getActivityProvider().getCode()), activity.getName(), activity.getMinAge(), activity.getMaxAge(), activity.getCapacity());
	}	
	
	@Atomic(mode = TxMode.WRITE)

	public static void createBooking(BookingData booking) {
		new Booking(booking.getActivityOffer());
	}	

	@Atomic(mode = TxMode.WRITE)
	public static String reserveActivity(LocalDate begin, LocalDate end, int age) {
		List<ActivityOffer> offers;
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			offers = provider.findOffer(begin, end, age);
			if (!offers.isEmpty()) {
				return new Booking(offers.get(0)).getReference();
			}
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelReservation(String reference) {
		Booking booking = getBookingByReference(reference);
		if (booking != null) {
			return booking.cancel();
		}
		throw new ActivityException();
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityReservationData getActivityReservationData(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			for (Activity activity : provider.getActivitySet()) {
				for (ActivityOffer offer : activity.getActivityOfferSet()) {
					Booking booking = offer.getBooking(reference);
					if (booking != null) {
						return new ActivityReservationData(provider, offer, booking);
					}
				}
			}
		}
		throw new ActivityException();
	}
	
	@Atomic(mode = TxMode.READ)
	public static HashSet<ActivityData> getActivityByProviderCode(String code) {
		HashSet<ActivityData> set = new HashSet<ActivityData>();
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (provider.getCode().equals(code)) {	
				for(Activity a : provider.getActivitySet()){
					ActivityData ad = new ActivityData();
					ad.setCapacity(a.getCapacity());
					ad.setMaxAge(a.getMaxAge());
					ad.setMinAge(a.getMinAge());
					ad.setName(a.getName());
					set.add(ad);
				}
			}
		}
		
		return set;				
	}
	
	@Atomic(mode = TxMode.READ)
	public static Set<ActivityOfferData> getActivityOfferDataByName(String code, String name) {
		Set<ActivityOfferData> aos = new HashSet<ActivityOfferData>();
		
		ActivityProvider provider = getProviderByCode(code);
		
		if (provider != null){
			for (Activity activity : provider.getActivitySet()) {
				if (activity.getName().equals(name)) {
					for(ActivityOffer ao : activity.getActivityOfferSet()){
						ActivityOfferData aod = new ActivityOfferData();
						aod.setActivity(activity);
						aod.setBegin(ao.getBegin());
						aod.setEnd(ao.getEnd());
						aos.add(aod);
					}
				}
			}
		}
		
		return aos;
	}
	
	@Atomic(mode = TxMode.READ)
	public static Set<BookingData> getBookingByDate(String code, String name, LocalDate begin) {
		Set<BookingData> bookings = new HashSet<BookingData>();
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (provider.getCode().equals(code)) {
				for (Activity activity : provider.getActivitySet()) {
					if (activity.getName().equals(name)) {
						for (ActivityOffer offer : activity.getActivityOfferSet()) {
							if (offer.getBegin().isEqual(begin)) {
								for(Booking b : offer.getBookingSet()){
									BookingData bd = new BookingData();
									bd.setReference(b.getReference());
									bd.setCancel(b.getCancel());
									bd.setCancellationDate(b.getCancellationDate());
									bookings.add(bd);
								}
							}
						}
					}
				}
			}
			
		}
			 
		return bookings;
	}
	
	
	@Atomic(mode = TxMode.READ)
	private static Booking getBookingByReference(String reference) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			Booking booking = provider.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityProviderData getProviderDataByCode(String code){
		ActivityProvider ap = getProviderByCode(code);
		
		if (ap != null){
			return new ActivityProviderData(ap.getCode(), ap.getName());
		}
		
		return null;
	}
	
	@Atomic(mode = TxMode.READ)
	private static ActivityProvider getProviderByCode(String code) {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (provider.getCode().equals(code)) {
				return provider;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.READ)
	public static Activity getActivityByName(String activityProviderCode, String activityName) {
		ActivityProvider ap = getProviderByCode(activityProviderCode);
		if (ap != null){
			for (Activity a : ap.getActivitySet()){
				if (a.getName().equals(activityName)) return a;
			}
		}
		return null;
	}

	@Atomic(mode = TxMode.READ)
	public static ActivityOffer getActivityOffer(String activityProviderCode, String activityName, LocalDate activityOfferBegin){
		Activity a = getActivityByName(activityProviderCode, activityName);
		for(ActivityOffer ao : a.getActivityOfferSet()){
			if(ao.getBegin().equals(activityOfferBegin)) return ao;
		}
		return null;
	}
}