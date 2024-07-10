package com.scm.exceptions;

public class ResourceNotFoundException extends RuntimeException{

	public ResourceNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ResourceNotFoundException() {
		super("User doesn't exist");
		// TODO Auto-generated constructor stub
	}
}
