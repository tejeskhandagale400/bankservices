package com.capgemini.transaction.transaction.service;

public interface TransactionService {

	  Double deposit(int accountNumber , String transactionDetails,double currentBalance, double amount);
	  Double withdraw(int accountNumber , String transactionDetails,double currentBalance, double amount);

}
