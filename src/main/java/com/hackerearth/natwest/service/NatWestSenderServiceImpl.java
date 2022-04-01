package com.hackerearth.natwest.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hackerearth.natwest.model.Transaction;

@Service
public class NatWestSenderServiceImpl implements NatWestSenderService {

	private static final String AES = "AES";

	private static final String PBKDF2 = "PBKDF2WithHmacSHA256";

	@Autowired
	private RestTemplate restTemplate;

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

	@Async
	@Override
	public void encryptAndQueue(Transaction transaction)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException,
			IOException, IllegalBlockSizeException, InterruptedException, ClassNotFoundException {
		
		SecretKey key = generateKey();

		Cipher cipher = initializeCipher(key);

		SealedObject encrypted = new SealedObject((Serializable) transaction, cipher);

		callReceiverApi(encrypted, cipher);

	}

	private void callReceiverApi(SealedObject encrypted, Cipher cipher) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
		ObjectOutputStream oos = new ObjectOutputStream(cipherOutputStream);
		oos.writeObject(encrypted);
		cipherOutputStream.close();

		byte[] values = outputStream.toByteArray();

		String base64encoded = Base64.getEncoder().encodeToString(values);

		HttpEntity<String> request = new HttpEntity<>(base64encoded, new HttpHeaders());

		restTemplate.postForObject(receiverApiUrl, request, Integer.class);

	}

	private Cipher initializeCipher(SecretKey key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(key.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher;
	}

	private SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount, keyLength);
		SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), AES);
		return key;
	}

}
