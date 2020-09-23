package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;



import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;

public class OperationData {
	private Type type;
	private Account account;
	private int value;
	private String reference;

	public OperationData() {
	}

	public OperationData(Operation operation) {
		this.type = operation.getType();
		this.account = operation.getAccount();
		this.value = operation.getValue();
		this.setReference(operation.getReference());
		

	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}
