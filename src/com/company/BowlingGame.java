package com.company;

import java.util.*;

public class BowlingGame {
	private static final String NEW = "new";
	private static final String ROLL = "roll";
	private static final String SCORE = "score";
	private static final String STATUS = "status";
	private static final String QUIT = "quit";
	private static final int MAX_FRAMES_NUMBER = 12;
	private static final int TENTH_FRAME_INDEX = 9;
	private static final int ELEVENTH_FRAME_INDEX = 10;

	private Frame currentFrame;
	private boolean isFinished = false;
	private List<Frame> frameList;


	public BowlingGame() {
		frameList = new ArrayList<Frame>();

		for (int frameIndex = 0; frameIndex < MAX_FRAMES_NUMBER; frameIndex++) {
			frameList.add(new Frame(frameIndex + 1));
		}
		currentFrame = frameList.get(0);
	}


	public int getScore() {
		int score = 0;
		for (Frame frame : frameList) {
			if (frame.getFrameNumber() <= 10) {
				score += frame.getFrameScore();
				// Add extra points for Strikes/Spares
				if (frame.isSpare()) {
					// We need to add the first roll of the next frame
					Frame nextFrame = getNextFrame(frame);
					score += nextFrame.getFirstRollScore();
				} else if (frame.isStrike()) {
					// Then we need to add the next two rolls
					Frame nextFrame = getNextFrame(frame);
					if (nextFrame.isStrike()) {
						// Only one roll was used and we need to check for the next frame to see if it has a roll yet
						Frame twoFramesAhead = getNextFrame(nextFrame);
						// Now add the two consecutive rolls that came after this strike
						score += nextFrame.getFirstRollScore() + twoFramesAhead.getFirstRollScore();
					} else {
						// Otherwise we just add up the total score from the following frame
						score += nextFrame.getFrameScore();
					}
				}
			}
		}
		return score;
	}

	public String getStatus() {
		if (isFinished) {
			return "Game finished. Score: " + getScore();
		} else {
			return "Game in progress. Frame: " + currentFrame.getFrameNumber() +
					". Current Score: " + getScore();
		}
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void roll(int pinsDowned) throws InvalidCommand {
		if (isFinished()) {
			throw new InvalidCommand("Unable to roll. Game has ended.");
		}

		if (currentFrame.isFinished()) {
			currentFrame = getNextFrame();
		}

		if (currentFrame.getFrameNumber() > 10) {
			rollInBonusFrames(pinsDowned);
		} else { // Bowl normal frame
			currentFrame.addRoll(pinsDowned);

			//If 10th frame is finished & not a strike/spare (less than 10) then game is over
			if (currentFrame.getFrameNumber() == 10 && currentFrame.isFinished() &&
					currentFrame.getFrameScore() < 10) {
				isFinished = true;
			}
		}
	}

	private Frame getNextFrame() {
		return getNextFrame(currentFrame);
	}

	private Frame getNextFrame(Frame frame) {
		if (frame.getFrameNumber() < MAX_FRAMES_NUMBER) {
			return frameList.get(frame.getFrameNumber());
		} else {
			return null;
		}
	}

	private void rollInBonusFrames(int pinsDowned) throws InvalidCommand {
		// If 10th frame was neither Strike nor Spare, game is over
		if (!(frameList.get(TENTH_FRAME_INDEX).isStrike() || frameList.get(TENTH_FRAME_INDEX).isSpare())) {
			isFinished = true;
		} else {

			// Check/Handle 11th frame
			if (currentFrame.getFrameNumber() == 11) {
				// Allow 1 roll if 10th frame was spare
				if (frameList.get(TENTH_FRAME_INDEX).isStrike()) {
					// We can play the whole 11th frame
					currentFrame.addRoll(pinsDowned);
					if (frameList.get(ELEVENTH_FRAME_INDEX).getRollCount() == 2) {
						isFinished = true;
					}

				} else {
					// We can only add 1 roll
					if (currentFrame.getRollCount() == 0) {
						currentFrame.addRoll(pinsDowned);
						// Now that the 1 roll is added, the game is over
						isFinished = true;
					}
				}
			}

			// Check/Handle 12th frame
			if (currentFrame.getFrameNumber() == 12) {
				if (frameList.get(TENTH_FRAME_INDEX).isStrike() &&
						frameList.get(ELEVENTH_FRAME_INDEX).isStrike()) {
					// If 10th and 11th frames were strikes the add last roll and set game as finished
					currentFrame.addRoll(pinsDowned);
				}
				isFinished = true;
			}
		}
	}



	public static void main(String[] args) {
		BowlingGame bowlingGame = null;
		Scanner reader = new Scanner(System.in);
		printUserInstructions();

		boolean quit = false;
		while (!quit) {
			String confirmationMsg;
			System.out.print("Enter Command: ");

			String input = reader.nextLine().trim();
			if (input.equalsIgnoreCase(NEW)) {
				bowlingGame = new BowlingGame();
				confirmationMsg = "New game has been created.";
			} else if (input.equalsIgnoreCase(QUIT)) {
				quit = true;
				confirmationMsg = "Closing program.";
			} else { // All other commands need a game to work
				if (bowlingGame == null) {
					confirmationMsg = "Warning. You must create a new game before using this command. " +
							"Hint: Type \"new\" to create a game.";
				} else if (input.equalsIgnoreCase(ROLL)) {
					confirmationMsg = handleRoll(bowlingGame, reader);
				} else if (input.equalsIgnoreCase(SCORE)) {
					confirmationMsg = String.valueOf(bowlingGame.getScore());
				} else if (input.equalsIgnoreCase(STATUS)) {
					confirmationMsg = bowlingGame.getStatus();
				} else {
					confirmationMsg = "Warning. Unknown command used.";
				}
			}
			System.out.println(confirmationMsg);
		}
		System.exit(0);
	}

	private static String handleRoll(BowlingGame bowlingGame, Scanner reader) {
		if (bowlingGame.isFinished()) {
			return "Game has already ended. Final score was " + bowlingGame.getScore() + ".";
		}

		String updateMessage = "";
		System.out.print("Enter number of pins knocked down in roll: ");
		String scoreInput = reader.nextLine().trim();
		try {
			// Parse input for space separated scores
			String[] scoresList = scoreInput.split(" ");
			int[] scores = new int[scoresList.length];
			for (int n = 0; n < scoresList.length; n++) {
				scores[n] = Integer.parseInt(scoresList[n].trim());
			}

			for (int score : scores) {
				// Validate that all scores are in range [0,10]
				if (score > 10 || score < 0) {
					throw new InvalidCommand("Number of pins knocked down must be in range [0,10].");
				}

				// Add scores to game
				bowlingGame.roll(score);
				updateMessage += "\n" + score + " points added to frame " + bowlingGame.currentFrame.getFrameNumber();
			}
			if (scores.length == 0) {
				updateMessage = "\nWarning. No roll values were found";
			}
		} catch (NumberFormatException e) {
			updateMessage += "\nWarning. Roll not added. Use space-delimited digits in range [0, 10].";
		} catch (InvalidCommand e) {
			updateMessage += "\nWarning. Roll not added. " + e.getMessage();
		} catch (Exception e) {
			// A catch-all Warning that we would want to log if we were using a logger (fail-silently)
			updateMessage += "\nWarning. Roll not added. Use space-delimited digits in range [0, 10].";
		}
		return updateMessage.substring(1); // Remove the newline that starts the substring
	}

	private static void printUserInstructions() {
		System.out.println("Continue by using the following commands:");
		System.out.println("- new : Starts a new game, discarding any current game");
		System.out.println("- roll : Allows user to enter bowling results");
		System.out.println("- score : Shows the game's current score");
		System.out.println("- status : Shows the current status of the game");
		System.out.println("- quit : Exits program");
	}
}
