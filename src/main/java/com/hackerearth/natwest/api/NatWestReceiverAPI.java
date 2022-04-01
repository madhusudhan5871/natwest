package com.hackerearth.natwest.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hackerearth.natwest.entity.TransactionEntity;
import com.hackerearth.natwest.model.Transaction;
import com.hackerearth.natwest.service.NatWestReceiverService;

@CrossOrigin
@RestController
@RequestMapping("receive")
public class NatWestReceiverAPI {
	
	@Autowired
	private NatWestReceiverService receiverService;
	
	@PostMapping("")
	public ResponseEntity<Integer> receivePayload(@RequestBody String base64encoded) {
		try {			
			Transaction transaction = receiverService.decrypt(base64encoded);
			
			TransactionEntity transactionEntity = receiverService.insert(transaction);
								    
			return new ResponseEntity<>(transactionEntity.getTransactionId(), HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

}
