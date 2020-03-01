//Harrison Chiu - ICS4U1 09 - February 15, 2019
//Mexico Dice Game


import java.util.Scanner;
import java.util.Random;


public class CH_MexicoDiceGame {

	static Scanner sc;


	public static String roll(int numberOfDice){  // rolls x amount of dice and returns string of dice

		Random rand = new Random(); // seed taken from system clock

		String rolls = "";

		rolls += Integer.toString(rand.nextInt(6)+1); // print first die separate, without a space before it
		for (int x = 1; x < numberOfDice; x++){  // iterate through num of dice
			rolls += " " + Integer.toString(rand.nextInt(6)+1);  // generate random num from 1 to 6 for die, append to string of rolls
		}
		return rolls;  // return string of rolls 
	}

	public static String checkSwap(String roll){  // check if dice have to be swapped and do so(e.g. rolling 1 then 5 requires a swap)

		String newRoll = roll;
		if (Integer.valueOf(roll.charAt(0)) < Integer.valueOf(roll.charAt(2))){  // compares number values of dice roll string with two rolls (at indexes 0 and 2)

			newRoll = roll.charAt(2) + " " + roll.charAt(0);   // if the first number is bigger than second, swap the two values
		}
		return newRoll;  // return appropriately modified string
	}

	public static int getIndex(String roll, String[] stringArray){  // returns at which index the string appears in the array(to be used with listOfRolls)
		roll = checkSwap(roll); // check if dice have to be swapped and do so (e.g. rolling 1 then 5 requires a swap)
		for (int possibleRoll = 0; possibleRoll < 21; possibleRoll ++){

			if (roll.equals(stringArray[possibleRoll])){
				return possibleRoll;
			}
		}
		return -1;
	}

	// this function is for the non-lead rollers
	public static int rollForPlayer(int maxRolls, int player, String[] playerArray, String[] allRollsArray){
		int rollNumber = 0;
		String choice = "", currentRoll;

		do{
			rollNumber ++;

			// roll for current player, swap if needed
			currentRoll = checkSwap(roll(2));
			System.out.println("\n"+playerArray[player] + "'s roll #" + rollNumber + " is: " + currentRoll + "." );

			// prompt player whether to roll again if only if they haven't rolled past the maxRolls limit (dictated by lead already)
			if (rollNumber < maxRolls){
				// prompt current player whether they want to roll again
				System.out.print("Roll again? (Enter 'y' for yes, 'n' for no) ");
				choice = sc.nextLine();
				while (choice.charAt(0) != 'y' && choice.charAt(0) != 'n'){
					System.out.println("Invalid response.");
					System.out.print("Roll again? (Enter 'y' for yes, 'n' for no) ");
					choice = sc.nextLine();
				}
			}
			// iterate so long as number of rolls is less than 3, and response is 'y'
		} while (rollNumber < maxRolls && choice.charAt(0) != 'n');
		return getIndex(currentRoll, allRollsArray);
	}



	public static void main(String[] args) {

		sc = new Scanner(System.in);  // initialize and declare scanner for prompts
		Random rand = new Random(); // seed taken from system clock

		// array of possible (sorted) rolls from lowest value to highest
		String[] listOfRolls = {"3 1", "3 2", "4 1", "4 2", "4 3", "5 1", "5 2", "5 3", "5 4", "6 1", "6 2", "6 3", "6 4", "6 5", "1 1", "2 2", "3 3", "4 4", "5 5", "6 6", "2 1"};      
		// the stored value of a roll will be equal to the index of the string in the array above; since the 
		// array is sorted from lowest to highest scores, higher scoring rolls have a higher index and vice versa
		// e.g. a "3 1" will have a stored value/index of 0, lower than the "5 1" with stored value of 5

		// set each player's number of lives to 6
		int p1 = 6, p2 = 6, p3 = 6, playersAlive = 3;

		//  declare arrays of players, their lives, and scores 
		String[] players = new String[]{"Player 1", "Player 2", "Player 3"};
		int[] playerLives = new int[]{p1,p2,p3};
		int p1score = 0;
		int p2score = 0;
		int p3score = 0;
		int[] playerScores = new int[]{p1score, p2score, p3score};

		for (int x = 1; x < 4; x++){
			
			System.out.format("Enter player %d's name: ", x);
			players[x-1] = sc.nextLine();
			
		}
		// prompt for number of rounds
		int rounds;  
		System.out.print("Enter number of rounds: ");
		rounds = sc.nextInt();

		// "roll die" (generate number 0, 1, or 2) to see who is lead for the first round
		int lead = Integer.valueOf(rand.nextInt(3)); 

		// print who rolls as lead for round 1
		System.out.println("\nDie has been rolled. The first lead will be " + players[lead] + "." );
		System.out.print("Press ENTER to start.");
		sc.nextLine();
		sc.nextLine();

		int currentRound = 1;
		String currentRoll;
		while (playersAlive > 1 && currentRound <= rounds){  // continue rounds as long as round max is not exceeded (incremented each time) and more than one player is 'alive'

			// this value holds the lowest held score, default high. At the end of the round, all players' scores will be
			// compared to this value. If their score matches this value then they will be deducted a life. 
			// this way, two or all players may lose a life in the same round

			int lowestScore = 99;  

			// iterate through player list and print how many lives each player has at beginning of round (only if alive)
			System.out.println("\n-----Round " + currentRound + "-----");
			for (int player = 0; player < 3; player ++){
				if (playerLives[player] > 0){
					System.out.println(players[player] + " has " + playerLives[player] + " lives left.");
				}
			}

			// if the lead lands on the person who does not have lives, keep moving on the lead position
			while (playerLives[lead] <= 0){
				lead ++;
				lead = lead % 3;
			}	
			
			// print who is lead
			System.out.println("\nLead player of this round is " + players[lead] + "." );


			int maxDiceRolls = 0;
			String choice = "";
			do{
				maxDiceRolls ++;

				// roll for LEAD player, swap if needed
				currentRoll = checkSwap(roll(2));
				System.out.println("\n" + players[lead] + "'s roll #" + maxDiceRolls + " is: " + currentRoll + "." );
				if (currentRoll == "2 1"){
					System.out.println("MEXICO!");
					break;
				}
				// prompt player whether to roll again if only if they haven't rolled 3 times already
				if (maxDiceRolls < 3){
					// prompt LEAD player whether they want to roll again
					System.out.print("Roll again? (Enter 'y' for yes, 'n' for no) ");
					choice = sc.nextLine();
					while (choice.charAt(0) != 'y' && choice.charAt(0) != 'n'){
						System.out.println("Invalid response.");
						System.out.print ("Roll again? (Enter 'y' for yes, 'n' for no) ");
						choice = sc.nextLine();
					}
				}
				// iterate so long as number of rolls is less than 3, and response is 'y'
			} while (maxDiceRolls < 3 && choice.charAt(0) != 'n');

			// print max number of dice rolls
			System.out.println("\nMaximum number of dice rolls for this round is " + maxDiceRolls + ".\n");

			// set index-score as lead's score, set that score as default lowest for now
			playerScores[lead % playersAlive] = getIndex(currentRoll, listOfRolls);
			lowestScore = playerScores[lead % playersAlive];

			//end of lead's turn, do next players' turns


			// iterate for next player(s)
			for (int x = 1; x <= 2; x ++){
				
				// offset player position by x
				int playerPosition = (lead + x) % playersAlive;  

				// skip player if they have no more lives
				if (playerLives[playerPosition] == 0)
					continue;

				//print and prompt to start turn
				System.out.println("-----\nStarting " + players[playerPosition] + "'s turn.");
				System.out.print("Press ENTER to continue.");
				sc.nextLine();

				// call method which is slightly altered from lead (not keeping track of max dice rolls for example)
				int currentIndex = rollForPlayer(maxDiceRolls, playerPosition, players, listOfRolls);
				playerScores[playerPosition] = currentIndex;

				if (currentIndex < lowestScore)
					lowestScore = currentIndex;
			}
			// signal end of round
			System.out.format("\n-----End of Round %d-----\n", currentRound);
			
			// print lowest dice roll
			System.out.println("The lowest dice roll was: " + listOfRolls[lowestScore]);

			// iterate through players to see who rolled the lowest, deduct a life if so, and adjust playersAlive if needed
			for (int p = 0; p < 3; p ++){
				if (playerLives[p] == 0)
					continue;
				if (playerScores[p] == lowestScore){
					System.out.println(players[p] + " has lost a life.");
					playerLives[p] --;
					if (playerLives[p] == 0)
						playersAlive --;

				}

			}
			
			
			System.out.print("Press ENTER to continue.");
			sc.nextLine();

			// update round number and next lead for next iteration
			currentRound ++;
			lead ++;

			// make sure lead is never more than 2
			lead = lead % 3;


		}
		// signal game is over
		System.out.format("\n-----Game over after Round %d-----\n", currentRound - 1);

		// if only one is alive, print them as winner
		if (playersAlive == 1){
			for (int p = 0; p < 3; p ++){
				if (playerLives[p] > 0){
					System.out.println(players[p] + " has won the game with " + playerLives[p] +" lives.");
					break;
				}
			}
		}
		
		// if no one is alive, print there is no winner
		else if (playersAlive == 0)
			System.out.println("There is no winner.");

		// if the round is exceeded, print so and the lives of each player
		if (currentRound > rounds){
			System.out.println("Number of maximum rounds exceeded.");
			for (int player = 0; player < 3; player ++){
				if (playerLives[player] > 0){
					System.out.println(players[player] + " has " + playerLives[player] + " lives left.");
				}
			}
			
		}
	}

}
