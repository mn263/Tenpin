package com.company;

import java.util.*;

public class Frame {

	private static final int MAX_ROLL_VALUE = 10;
	private static final int MAX_ROLL_COUNT = 2;
	private List<Integer> rolls;
	private int frameNumber;


	public Frame(int frameNumber) {
		this.frameNumber = frameNumber;
		this.rolls = new ArrayList<Integer>();
	}


	public int getFrameNumber() {
		return frameNumber;
	}

	public int getFirstRollScore() {
		if (rolls.isEmpty()) {
			return 0;
		} else {
			return rolls.get(0);
		}
	}

	public int getFrameScore() {
		int score = 0;
		for (int roll : rolls) {
			score += roll;
		}
		return score;
	}

	public int getRollCount() {
		return rolls.size();
	}

	public void addRoll(int pinsDowned) throws InvalidCommand {
		if (getFrameScore() + pinsDowned > 10) {
			throw new InvalidCommand("Total Score for a frame cannot exceed 10.");
		}
		rolls.add(pinsDowned);
	}

	public boolean isStrike() {
		return !rolls.isEmpty() && rolls.get(0) == MAX_ROLL_VALUE;
	}

	public boolean isSpare() {
		return getFrameScore() == MAX_ROLL_VALUE && getRollCount() == MAX_ROLL_COUNT;
	}

	public boolean isFinished() {
		return isStrike() || rolls.size() == MAX_ROLL_COUNT;
	}
}