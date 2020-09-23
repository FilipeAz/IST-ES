package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;



import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData.CopyDepth;

public class AccountData {
	
	public static enum CopyDepth {
		SHALLOW, OPERATIONS
	};
	
	private Client client;
	private Bank bank;
	private String IBAN;
	private List<OperationData> operations = new ArrayList<>();
	

	public AccountData() {
	}

	public AccountData(Account account, CopyDepth depth) {
		this.client = account.getClient();
		this.bank = account.getBank();
		this.IBAN=account.getIBAN();
		switch (depth) {
		case OPERATIONS:
			for (Operation operation : account.getOperationSet()) {
				this.operations.add(new OperationData(operation));
			}
		case SHALLOW:
			break;
		default:
			break;
		}
		
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}
	
	public void setOperations(List<OperationData> operations) {
		this.operations = operations;
	}
	
	public List<OperationData> getOperations() {
		return this.operations;
	}

}