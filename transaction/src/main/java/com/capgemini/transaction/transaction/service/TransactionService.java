package com.capgemini.transaction.transaction.service;

public interface TransactionService {

	  Double deposit(int accountNumber , String transactionDetails,double currentBalance, double amount);

}
