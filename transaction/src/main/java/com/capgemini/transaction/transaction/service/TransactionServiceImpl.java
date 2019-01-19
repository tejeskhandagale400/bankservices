package com.capgemini.transaction.transaction.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.transaction.transaction.entity.Transaction;
import com.capgemini.transaction.transaction.entity.TransactionType;
import com.capgemini.transaction.transaction.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;
	
	public Double deposit(int accountNumber , String transactionDetails,double currentBalance, double amount){
		Transaction transaction =  new Transaction(accountNumber, amount, TransactionType.DEPOSIT, transactionDetails);
		currentBalance += amount; 
		transaction.setCurrentBalance(currentBalance);
		transaction.setTransactionDate(LocalDateTime.now());
		transactionRepository.save(transaction);
		return currentBalance;
		
	}
	
	public Double withdraw(int accountNumber , String transactionDetails,double currentBalance, double amount){
		Transaction transaction =  new Transaction(accountNumber, amount, TransactionType.WITHDRAW, transactionDetails);
		if(currentBalance>amount)
		{
		currentBalance -= amount; 
		}
		transaction.setCurrentBalance(currentBalance);
		transaction.setTransactionDate(LocalDateTime.now());
		transactionRepository.save(transaction);
		return currentBalance;
		
	}
}
