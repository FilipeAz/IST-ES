package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;



import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;

public class ClientData {
	public static enum CopyDepth {
		SHALLOW, ACCOUNTS
	};
	
	private Bank bank;
	private String name;
	private String ID;
	private List<AccountData> accounts = new ArrayList<>();

	public ClientData() {
	}

	public ClientData(Client client, CopyDepth depth) {
		this.bank = client.getBank();
		this.setName(client.getName()); 
		switch (depth) {
		case ACCOUNTS:
			for (Account account : client.getAccountSet()) {
				this.accounts.add(new AccountData(account, AccountData.CopyDepth.SHALLOW));
			}
		case SHALLOW:
			break;
		default:
			break;
		}
	}

	public Bank getBank() {
		return this.bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<AccountData> getAccounts() {
		return this.accounts;
	}

	public void setAccounts(List<AccountData> accounts) {
		this.accounts = accounts;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}


}