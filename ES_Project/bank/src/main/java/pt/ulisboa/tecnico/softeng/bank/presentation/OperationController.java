package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.domain.Operation;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData.CopyDepth;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.OperationData;

@Controller
@RequestMapping(value = "/banks/{bankCode}/clients/{clientID}/accounts/{accountIBAN}/operations")
public class OperationController {
	private static Logger logger = LoggerFactory.getLogger(OperationController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showOperations(Model model, @PathVariable String bankCode, @PathVariable String clientID, @PathVariable String clientName, @PathVariable String accountIBAN) {
		logger.info("showOperations Bankcode:{} ClientName:{} ClientID:{} IBAN:{}", bankCode, clientName, clientID, accountIBAN);

		AccountData accountData= BankInterface.getAccountDataByIBAN(accountIBAN, clientID, bankCode, AccountData.CopyDepth.OPERATIONS);

		if (accountData == null) {
			model.addAttribute("error", "Error: it does not exist a account with the IBAN " + accountIBAN);
			model.addAttribute("account", new AccountData());
			model.addAttribute("accounts", BankInterface.getAccounts(bankCode,clientID));
			return "accounts";
		} else {
			model.addAttribute("operation", new OperationData());
			model.addAttribute("account", accountData);
			return "operations";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submitOperation(Model model, @PathVariable String bankCode,
			@PathVariable String clientID, @PathVariable String accountIBAN, @ModelAttribute OperationData operationData) {
		logger.info("accountSubmit bankCode:{}, clientID:{}, accountIBAN:{}, operationRef:{}", bankCode, clientID, accountIBAN, operationData.getReference());

		try {
			BankInterface.createOperation(bankCode, clientID,accountIBAN, operationData);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create the operation");
			model.addAttribute("operation", operationData);
			model.addAttribute("account", BankInterface.getAccountDataByIBAN(accountIBAN, clientID, bankCode, AccountData.CopyDepth.OPERATIONS));
			return "operations";
		}

		return "redirect:/banks/" + bankCode +"/clients/"+ clientID + "/accounts" + accountIBAN + "/operations";
	}
}