package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderData {
	static final int CODE_SIZE = 6;
	
	String code;
	String name;
	public ActivityProviderData(){
		
	}
	
	public ActivityProviderData(String code, String name) {
		setCode(code);
		setName(name);
	}
	
	public void setCode(String code){ this.code = code; }
	public void setName(String name){ this.name = name; }
	public String getCode() { return this.code; }
	public String getName() { return this.name; }

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().equals("") || name.trim().equals("")) {
			throw new ActivityException();
		}

		if (code.length() != ActivityProviderData.CODE_SIZE) {
			throw new ActivityException();
		}

		for (ActivityProvider activityProvider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			if (activityProvider.getCode().equals(code) || activityProvider.getName().equals(name)) {
				throw new ActivityException();
			}
		}
	}
}