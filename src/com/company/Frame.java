package com.company;

import java.util.*;

/**
 * Frame class is used in conjunction with the BowlingGame class
 *
 * It helps keep track of the standard 10 frames in a bowling game (additionally an 11th/12th frames may be used depending on outcome of 10th frame in game)
 */
public class Frame {

	/* Constants used in the Frame class */
	private static final int MAX_ROLL_VALUE = 10;
	private static final int MAX_ROLL_COUNT = 2;

	private List<Integer> rolls;
	private int frameNumber;


	/**
	 * The constructor sets up the frame number and initializes the rolls array
	 * @param frameNumber - int Represents the frame number in the bowling game (should be a value between 1-12)
	 */
	public Frame(int frameNumber) {
		this.frameNumber = frameNumber;
		this.rolls = new ArrayList<Integer>();
	}


	/**
	 * Returns the Frame number, typically in a bowling game there are 10 frames (with ability to achieve 11th/12th if ending with strikes)
	 * @return - int
	 */
	public int getFrameNumber() {
		return frameNumber;
	}

	/**
	 * Returns the score of the first "Roll" in the frame, if a "Roll" hasn't been made then it returns 0.
	 * @return int
	 */
	public int getFirstRollScore() {
		if (rolls.isEmpty()) {
			return 0;
		} else {
			return rolls.get(0);
		}
	}

	/**
	 * Returns the current score of the frame
	 * @return int
	 */
	public int getFrameScore() {
		int score = 0;
		for (int roll : rolls) {
			score += roll;
		}
		return score;
	}

	/**
	 * Returns the number of rolls that have been made in this frame (should be 0, 1, or 2)
	 * @return int
	 */
	public int getRollCount() {
		return rolls.size();
	}

	/**
	 * Imitates the rolling of a ball in a frame
	 * @param pinsDowned - int (represents the number of pins knocked down in the roll, should be in range [0, 10])
	 * @throws InvalidCommand - Throwable Object, used if an invalid int is passed into the method.
	 */
	public void addRoll(int pinsDowned) throws InvalidCommand {
		if (pinsDowned < 0 || getFrameScore() + pinsDowned > 10) {
			throw new InvalidCommand("Total Score for a frame cannot exceed 10.");
		}
		rolls.add(pinsDowned);
	}

	/**
	 * Returns bool indicating whether a strike has been achieved in this frame or not
	 * @return - bool
	 */
	public boolean isStrike() {
		return !rolls.isEmpty() && rolls.get(0) == MAX_ROLL_VALUE;
	}

	/**
	 * Returns bool indicating whether a spare has been achieved in this frame or not
	 * @return - bool
	 */
	public boolean isSpare() {
		return getFrameScore() == MAX_ROLL_VALUE && getRollCount() == MAX_ROLL_COUNT;
	}

	/**
	 * Returns a bool indicating whether there are any more rolls available in this frame
	 * @return - bool
	 */
	public boolean isFinished() {
		return isStrike() || rolls.size() == MAX_ROLL_COUNT;
	}
}