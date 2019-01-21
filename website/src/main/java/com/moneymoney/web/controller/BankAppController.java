package com.moneymoney.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.moneymoney.web.entity.CurrentDataSet;
import com.moneymoney.web.entity.Transaction;

@Controller
public class BankAppController {

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping("/getAllStatments")
	public String getAllStatements(Model model) {
		Transaction transactionList =  restTemplate.getForObject("http://localhost:8989/transactions/getAllStatments",Transaction.class);
		model.addAttribute("transactionList",transactionList);
		return "statements";
	}
	@RequestMapping("/depositform")
	public String depositForm() {
		return "DepositForm";
	}

	@RequestMapping("/deposit")
	public String deposit(@ModelAttribute Transaction transaction, Model model) {
		restTemplate.postForEntity("http://localhost:8989/transactions", transaction, null);
		model.addAttribute("message", "Success!");
		return "DepositForm";
	}


	@RequestMapping("/withdrawform")
	public String withdrawForm() {
		return "WithdrawForm";
	}
	
	@RequestMapping("/withdraw")
	public String withdraw(@ModelAttribute Transaction transaction, Model model) {
		restTemplate.postForEntity("http://localhost:8989/transactions/withdraw", transaction, null);
		model.addAttribute("message", "Success!");
		return "WithdrawForm";
	}

	@RequestMapping("/fundtransferform")
	public String fundtransferForm() {
		return "fundtransferform";
	}
	
	@RequestMapping("/transfer")
	public String fundTransferForm(@RequestParam("senderAccountNumber") Integer senderAccountNumber,
			@RequestParam("receiverAccountNumber") Integer receiverAccountNumber ,
			@RequestParam("amount") Double amount ,	Model model) {
		Transaction transaction =new Transaction();
		transaction.setAccountNumber(senderAccountNumber);
		transaction.setTransactionDetails("Online");
		transaction.setAmount(amount);
		restTemplate.postForEntity("http://localhost:8989/transactions/transfer?receiverAccountNumber="+receiverAccountNumber,
				transaction, null);
		model.addAttribute("message", "Success!");
		return "fundtransferform";
	}
	
	@RequestMapping("/statement")
	public ModelAndView getStatement(@RequestParam("offset") int offset, @RequestParam("size") int size) {
		int currentSize = size==0?5:size;
		int currentOffset = offset==0?1:offset;
		Link previous = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BankAppController.class).getStatement(currentOffset-currentSize, currentSize)).withRel("previous");
		Link next = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BankAppController.class).getStatement(currentOffset+currentSize, currentSize)).withRel("next");
		CurrentDataSet currentDataSet = restTemplate.getForObject("http://localhost:8989/transactions", CurrentDataSet.class);
		List<Transaction> transactionList = currentDataSet.getTransactions();
		List<Transaction> transactions = new ArrayList<Transaction>();
		for(int value=currentOffset-1; value<currentOffset+currentSize-1; value++) {
			if((transactionList.size() <= value && value > 0) || currentOffset < 1)
				break;
			Transaction transaction = transactionList.get(value);
			transactions.add(transaction);		
		}
		currentDataSet.setPreviousLink(previous);
		currentDataSet.setNextLink(next);
		currentDataSet.setTransactions(transactions);
		return new ModelAndView("DepositForm", "currentDataSet", currentDataSet);
	}
}
