package com.hackerearth.natwest.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hackerearth.natwest.entity.TransactionEntity;
import com.hackerearth.natwest.model.Transaction;
import com.hackerearth.natwest.repository.TransactionRepository;

@Service
public class NatWestReceiverServiceImpl implements NatWestReceiverService {

	private static final String AES = "AES";

	private static final String PBKDF2 = "PBKDF2WithHmacSHA256";
	
	@Autowired
	private TransactionRepository transactionRepo;

	@Value("${receiver.api.url}")
	private String receiverApiUrl;

	@Value("${key.salt.value}")
	private String salt;

	@Value("${key.password.value}")
	private String password;

	@Value("${key.iteration.value}")
	private int iterationCount;

	@Value("${key.length.value}")
	private int keyLength;

	@Override
	public Transaction decrypt(String base64Encoded)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
			IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {

		SecretKey key = generateKey();

		Cipher cipher = initializeCipher(key);

		return getDecrypted(base64Encoded, cipher);
	}

	private Transaction getDecrypted(String base64Encoded, Cipher cipher)
			throws IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {
		ByteArrayInputStream istream = new ByteArrayInputStream(Base64.getDecoder().decode(base64Encoded));
		CipherInputStream cipherInputStream = new CipherInputStream(istream, cipher);
		ObjectInputStream inputStream = new ObjectInputStream(cipherInputStream);
		SealedObject sealedObject = (SealedObject) inputStream.readObject();
		cipherInputStream.close();
		return (Transaction) sealedObject.getObject(cipher);
	}

	private Cipher initializeCipher(SecretKey key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(AES);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher;
	}

	private SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount, keyLength);
		return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), AES);
	}

	@Override
	public TransactionEntity insert(Transaction transaction) {
		TransactionEntity transactionEntity = new TransactionEntity();
		transactionEntity.setAccountNumber(transaction.getAccountNumber());
		transactionEntity.setType(transaction.getType());
		transactionEntity.setAmount(transaction.getAmount());
		transactionEntity.setCurrency(transaction.getCurrency());
		transactionEntity.setAccountFrom(transaction.getAccountFrom());
		return transactionRepo.save(transactionEntity);
	}
}
