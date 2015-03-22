# Tenpin
Simple Java Bowling Calculator

## This program calculates the score of a bowling game.
In the main and static methods have been created separate from the class itself.
You may run the main method and it will facilitate the interaction between the user and the BowlingGame class.
However, it isn't necessary to use the main and static methods. You may write your own code that uses BowlingGame and all calculations should remain accurate.

## Using main method inside BowlingGame:
This will start a commandline interface for the user to submit commands to the BowlingGame and check on the current status of their game.
The commands provided on the commandline are:
- new
- roll
- status
- score
- quit

## Interacting with the BowlingGame class
After creating an instance there 4 public methods you'll commonly use
### Roll
The "roll" method inside of BowlingGame throws an InvalidCommand exception.
This exception is thrown whenever the input is invalid (i.e., you try to score more than 10 points in a single frame or try to "roll" after the game has ended).

### getScore
Returns an int indicating the current score of the game

### isFinished
Simply returns a boolean indicating whether the current game has ended or not.

### getStatus
Returns a string indicating the game is finished/in progress, the current frame, and the current score.