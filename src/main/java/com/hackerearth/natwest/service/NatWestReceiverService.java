package com.hackerearth.natwest.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.hackerearth.natwest.entity.TransactionEntity;
import com.hackerearth.natwest.model.Transaction;

public interface NatWestReceiverService {

	public Transaction decrypt(String base64Encoded)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
			IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException;
	
	public TransactionEntity insert(Transaction ransaction);
}
