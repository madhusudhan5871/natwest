package com.hackerearth.natwest.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hackerearth.natwest.model.Transaction;
import com.hackerearth.natwest.service.NatWestSenderService;

@CrossOrigin
@RestController
@RequestMapping("send")
@Validated
public class NatWestSenderAPI {

	private static final String RECEIVED_SUCCESSFULLY = "Your payload is received successfully and is queued for further processing";

	@Autowired
	private NatWestSenderService senderService;

	@PostMapping("")
	public ResponseEntity<String> sendPayload(@Valid @RequestBody Transaction transaction) {
		try {
			senderService.encryptAndQueue(transaction);

			return new ResponseEntity<>(RECEIVED_SUCCESSFULLY, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

}
