package com.company;

public class InvalidCommand extends Throwable {
	public InvalidCommand(String message) {
		super(message);
	}
}
