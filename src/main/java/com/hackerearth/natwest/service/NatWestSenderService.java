package com.hackerearth.natwest.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.hackerearth.natwest.model.Transaction;

public interface NatWestSenderService {
	
	public void encryptAndQueue(Transaction transaction) throws NoSuchAlgorithmException, NoSuchPaddingException,
	InvalidKeySpecException, InvalidKeyException, IOException, IllegalBlockSizeException, InterruptedException, ClassNotFoundException;
	
}
