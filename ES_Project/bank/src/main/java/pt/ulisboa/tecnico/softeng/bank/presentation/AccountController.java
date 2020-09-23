package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/banks/{bankCode}/clients/{clientID}/accounts")
public class AccountController {
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showAccounts(Model model, @PathVariable String bankCode, @PathVariable String clientID) {
		logger.info("showAccounts BankCode:{} ID:{}", bankCode, clientID);

		ClientData clientData = BankInterface.getClientDataByID(clientID, bankCode, ClientData.CopyDepth.ACCOUNTS);

		if (clientData == null) {
			model.addAttribute("error", "Error: it does not exist a client with the ID " + clientID);
			model.addAttribute("client", new ClientData());
			model.addAttribute("clients", BankInterface.getClients(bankCode));
			return "banks";
		} else {
			model.addAttribute("account", new AccountData());
			model.addAttribute("client", clientData);
			return "accounts";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitAccount(Model model, @PathVariable String bankCode,
			@PathVariable String clientID, @ModelAttribute AccountData accountData) {
		logger.info("accountSubmit bankCode:{}, clientID:{}, IBAN:{}", bankCode, clientID, accountData.getIBAN());

		try {
			BankInterface.createAccount(bankCode, clientID);
		} catch (BankException be) {
			model.addAttribute("error", "Error: it was not possible to create the account");
			model.addAttribute("account", accountData);
			model.addAttribute("client", BankInterface.getClientDataByID(clientID, bankCode, ClientData.CopyDepth.ACCOUNTS));
			return "accounts";
		}

		return "redirect:/banks/" + bankCode +"/clients/"+ clientID + "/accounts";
	}

}
