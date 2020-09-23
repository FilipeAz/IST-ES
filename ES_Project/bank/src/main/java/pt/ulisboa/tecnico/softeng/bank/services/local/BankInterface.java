package pt.ulisboa.tecnico.softeng.bank.services.local;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData.CopyDepth;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.OperationData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankOperationData;

public class BankInterface {
	
	//-----  Ja feito -------
	
	@Atomic(mode = TxMode.WRITE)
	public static String processPayment(String IBAN, int amount) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			if (bank.getAccount(IBAN) != null) {
				return bank.getAccount(IBAN).withdraw(amount);
			}
		}
		throw new BankException();
	}

	@Atomic(mode = TxMode.WRITE)
	public static String cancelPayment(String paymentConfirmation) {
		Operation operation = getOperationByReference(paymentConfirmation);
		if (operation != null) {
			return operation.revert();
		}
		throw new BankException();
	}

	@Atomic(mode = TxMode.READ)
	public static BankOperationData getOperationData(String reference) {
		Operation operation = getOperationByReference(reference);
		if (operation != null) {
			return new BankOperationData(operation);
		}
		throw new BankException();
	}

	private static Operation getOperationByReference(String reference) {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			Operation operation = bank.getOperation(reference);
			if (operation != null) {
				return operation;
			}
		}
		return null;
	}
	
	
	//-------------
	
	
	// -------- Goncalo 
	
	
	@Atomic(mode = TxMode.READ)
	public static List<BankData> getBanks() {
		List<BankData> banks = new ArrayList<BankData>();
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			banks.add(new BankData(bank, BankData.CopyDepth.SHALLOW));
		}
		return banks;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createBank(BankData bankData) {
		new Bank(bankData.getName(), bankData.getCode());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static Bank getBankByCode(String code) {
			for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
				if (bank.getCode().equals(code)) {
					return bank;
				}
			}
			return null;
	}

	// ---- New
	
	@Atomic(mode = TxMode.READ)
	public static BankData getBankDataByCode(String bankCode, CopyDepth depth) {
		Bank bank = getBankByCode(bankCode);

		if (bank != null) {
			return new BankData(bank, depth);
		} else {
			return null;
		}
	}
	
	
	
	@Atomic(mode = TxMode.READ)
	public static List<ClientData> getClients(String bankCode) {
		List<ClientData> clients = new ArrayList<ClientData>();
		Bank bank=getBankByCode(bankCode);
		for (Client client : bank.getClientSet()) {
			clients.add(new ClientData(client, ClientData.CopyDepth.SHALLOW));
		}
		return clients;
	}
	
	@Atomic(mode = TxMode.READ)
	public static List<AccountData> getAccounts(String bankCode, String clientID) {
		List<AccountData> accounts = new ArrayList<AccountData>();
		Client client=getClientByID(clientID, bankCode);
		for (Account account : client.getAccountSet()) {
			accounts.add(new AccountData(account, AccountData.CopyDepth.SHALLOW));
		}
		return accounts;
	}
	
	@Atomic(mode = TxMode.READ)
	public static ClientData getClientDataByID(String clientID, String bankCode, ClientData.CopyDepth depth) {
		Client client = getClientByID(clientID, bankCode);

		if (client != null) {
			return new ClientData(client, depth);
		} else {
			return null;
		}
	}
	
	@Atomic(mode = TxMode.READ)
	public static AccountData getAccountDataByIBAN(String IBAN, String client, String bank, AccountData.CopyDepth depth) {
		Account account= getAccountByIBAN(IBAN, client, bank);
		if (client != null) {
			return new AccountData(account, depth);
		} else {
			return null;
		}
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createClient(String bankCode, ClientData clientData) {
		new Client(getBankByCode(bankCode), clientData.getName());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createAccount(String bankCode, String clientID) {
		new Account(getBankByCode(bankCode), getClientByID(clientID, bankCode));
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createOperation(String bankCode, String clientID, String AccountIBAN, OperationData operationData) {
		new Operation(operationData.getType(), getAccountByIBAN(AccountIBAN, clientID, bankCode),operationData.getValue());
	}
	
	private static Client getClientByID(String id, String bankCode) {
		Bank bank= getBankByCode(bankCode);
		for (Client client : bank.getClientSet()) {
			if (client.getID().equals(id)) {
				return client;
			}
		}
		return null;
	}

	private static Account getAccountByIBAN(String IBAN, String clientID, String bankCode) {
		Client client = getClientByID(clientID, bankCode);
		for (Account account: client.getAccountSet()) {
			if (account.getIBAN().equals(IBAN)) {
				return account;
			}
		}
		return null;
	}

}
