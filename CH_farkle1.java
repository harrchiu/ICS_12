// Harrison Chiu - ICS4U1-09 - May 4, 2019
// "Farkle"

import java.util.Random;
import java.util.Scanner;


public class CH_farkle1 {

	static int p1pts = 0;		// make visible to options method and main
	static int p2pts = 0;
	static String p1, p2;
	
	static Scanner sc;
	
	// count occurence of value in array and return count 
	public static int count(int array[], int value) { 
		int counter = 0;
		for (int x = 0; x < array.length; x += 1) {  // iterate and count if current matches wanted value
			if (array[x] == value)
				counter += 1;
		}
		return counter;
	}
	
	// rolls x amount of dice and returns string of dice, sorted by value
	public static int[] roll(int numberOfDice){  

		// assigning random dice rolls to each array index
		int rolls[] = new int[numberOfDice];  // create new array to be filled, sorted, and returned

		for (int x = 0; x < rolls.length; x += 1) {
			rolls[x] = (int) (Math.random()*6+1); // assign a random number from 1-6 for each index in the array result
		}

		// return array where each index represents 1-6 and the value of each index
		// is the number of times that roll value has been rolled
		// for ex. [0,1,0,0,0,5] represents one 2 and five 6's
		return new int[]{count(rolls,1), count(rolls,2), count(rolls,3), count(rolls,4), count(rolls,5), count(rolls,6)};
	}

	// return an array of rolls in String format (one space in between each roll)
	public static String rollsToString(int[] rolls) {   

		// account for array with no rolls at all 
		if (rolls[0] == 0 && rolls[1] == 0 && rolls[2] == 0 && rolls[3] == 0 && rolls[4] == 0 && rolls[5] == 0) 
			return "No dice left (rolling again will roll 6 new dice)";

		String output = "";
		for (int value = 1; value < 7; value += 1) {    // go through each value of 1-6
			for (int x = 0; x < rolls[value-1]; x += 1)   // print number of times that it has been rolled
				output += value + " ";					
		}
		return output;
	}

	// return array of which combinations are possible given a set of rolls (in array)
	public static String[] combos(int[] rolls) {

		// array for storing valid combos
		String possibleCombos[] = new String[15];
		int id = 0; // for tracking where to add to possibleCombos array

		//// now see which combos are possible given the valueCountArray ////

		// if there are 1's or 5's in the roll at all
		if (rolls[0] != 0) {
			possibleCombos[id] = "1 spot";
			id += 1;
		}
		if (rolls[4] != 0) {
			possibleCombos[id] = "5 spot";
			id += 1;
		}

		// if there any 3 of a kinds (or more than 3 of one value) 
		for (int y = 1; y < 7; y += 1) {  // iterate through 1-6
			if (rolls[y-1] >= 3) {   // if there are 3 or more of the current 1-6 value 
				possibleCombos[id] = "Three " + y + " spot";   // add that to array
				id += 1;
			}
		}

		// if there are three pairs (in this case each value in valueCountArray will be a 2 or a 0, three of which will be 2)
		int pairs = 0;
		for (int i:rolls) {  // iterate and switch pairTrue if condition is not met at any index
			if (i == 2)
				pairs += 1;
			else if (i == 0)
				continue;
			else
				pairs = 999;  // if any number of occurences is not 0 or 2, instantly make pairs higher than 3 (will not qualify for later)
		}
		if (pairs == 3) {  // only add to possibleCombos if there are exaclty 3 pairs
			possibleCombos[id] = "Three pairs";
			id += 1;
		}

		boolean straightTrue = true;
		for (int b:rolls) {  // iterate and switch straightTrue if condition is not met at any index
			if (b != 1) 
				straightTrue = false;
		}
		if (straightTrue == true) {  // add appropriately to possibleCombos
			possibleCombos[id] = "Six-dice straight";
			id += 1;
		}

		return possibleCombos;
	}

	// handle the method of rolling and prompting a player to choose combinations
	// handle point counting via the combos method
	// print statements to integrate with main method
	public static int optionsProcess(int[] rolls, String player) {
		int totalPoints = 0;
		int adder = 0;								// used to displace option choices depending on whether it's the first choosing after a roll

		boolean firstComboPicking = true;			// used to track whether player is forced to pick something

		while (true) {
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
			System.out.println(p1 + "'s total points: " + p1pts); 	// display points for each player (total)
			System.out.println(p2 + "'s total points: " + p2pts);
			System.out.println("--------------------------");

			System.out.println("Current roll: " + rollsToString(rolls));  // print current value of rolls

			String curCombos[] = combos(rolls);


			System.out.println(player + "'s points in this turn: "+ totalPoints);

			// print combos available, while keeping track of how many are actually valid combos (i.e. not null)
			System.out.println();
			int validCombos = 0;
			for (int x = 1; x < curCombos.length; x += 1) {  
				if (curCombos[x-1] != null) {
					System.out.println(x + ") " + curCombos[x-1]);
					validCombos += 1;
				}
			} 

			if (firstComboPicking) {   // if it's the first choosing after a roll, farkles are not allowed
				// if there are no available combos, score is instantly 0, increment consecutive farkle count outside of method
				if (validCombos == 0) {
					System.out.println("That's a Farkle! All points are lost for this round.");					
					System.out.println("Press enter to start next player's turn.");
					
					return 0;
				}
			}
			else					// if it's not the first choosing after a roll, displace options by 2 to allow pass and reroll
				adder = 2;

			if (!firstComboPicking) {    // if it's not the first choosing after a roll, player has the option to pass or roll remaining dice
				System.out.println((validCombos + 1) + ") Pass");
				System.out.println((validCombos + 2) + ") Roll dice remaining (and risk a Farkle!)");
			}

			// prompt for option, initialize holding variable as 0 to induce while loop
			int input = 0;	
			System.out.print("Enter number for corresponding combination/action above: ");

			while ( input < 1 || input > validCombos + adder) { // if input is out of range of combos (1 to length of combos array)
				while (!sc.hasNextInt()) {   			// go to next input if not an integer
					sc.next();  						// go to next scannable object
					System.out.print("Invalid entry, enter again: ");
				}
				input = sc.nextInt();
				if  (input < 1 || input > validCombos + adder)  // print entry not in range if so
					System.out.print("Entry out of range, enter again: ");
			}

			// first check for passes and rerolls if this is not the first choosing
			if (firstComboPicking == false){
				// player wants to pass
				if (input == validCombos + 1) {
					return totalPoints;
				}  
				// player wants to roll again
				else if (input == validCombos + 2)   {
					// find number of dice to actually reroll by taking sum of values counted in rolls array
					int diceRemaining = rolls[0] + rolls[1] + rolls[2] + rolls[3] + rolls[4] + rolls[5];
					if (diceRemaining == 0)  // if no dice are left, start back again at 6 dice
						rolls = roll(6);
					else
						rolls = roll(diceRemaining);

					firstComboPicking = true;   // make sure that a farkle after rerolling WILL be accounted for (elimnate all points, goto next player)
					continue; 					// go back to top
				}
			}

			String chosen = curCombos[input-1]; // create variable for chosen option to refer to later

			// three of a kind of 1
			if (chosen.equals("Three 1 spot")) { 
				totalPoints += 1000;
				rolls[0] -= 3;
			}

			// three of a kind of 2-6
			// take what value has a three of kind and multiply by 100 to find points attributed
			else if (chosen.equals("Three 2 spot") || chosen.equals("Three 3 spot") || chosen.equals("Three 4 spot") || chosen.equals("Three 5 spot") || chosen.equals("Three 6 spot")) {      
				totalPoints += Character.getNumericValue(chosen.charAt(6)) * 100;
				rolls[ Character.getNumericValue(chosen.charAt(6)) - 1 ] -= 3;
			}

			// 6 dice straight
			else if (chosen.equals("Six-dice straight")) {
				// add points and set all values to 0
				totalPoints += 1000;
				for (int i = 0; i < 6; i += 1) 
					rolls[i] = 0;
			}

			// 3 pairs
			else if (chosen.equals("Three pairs")) {
				// add points and set all values to 0
				totalPoints += 500;
				for (int x = 0; x < 6; x += 1) 
					rolls[x] = 0;
			}

			else if (chosen.equals("1 spot") || chosen.equals("5 spot")) {
				int whichSpot = Character.getNumericValue(chosen.charAt(0));  // set which value/number to spot is actually being referred to
				int numberToRemove = 0; 

				if (rolls[whichSpot-1] > 1) {   // only ask how many to remove if there's more than 1
					System.out.print("How many " + whichSpot + "'s would you like to remove? (" + rolls[whichSpot-1] + " max) ");

					while ( numberToRemove < 1 || numberToRemove > rolls[whichSpot-1]) { // if requested number of 1/5's is outside range of 1/5's available (below 0 or exceeding number)
						while (!sc.hasNextInt()) {   			// go to next input if not an integer
							sc.next();  						// go to next scannable object
							System.out.print("Invalid entry, enter again: ");
						}
						numberToRemove = sc.nextInt();
						if  (numberToRemove < 1 || numberToRemove > rolls[whichSpot-1])  // print entry not in range if so
							System.out.print("Entry out of range, enter again: ");
					}
				}
				else  // otherwise just remove 1
					numberToRemove = 1;
					
				// remove desired amount of 1/5's from set of dice
				rolls[whichSpot-1] -= numberToRemove;

				// add approriate amount of points, depending on spot/value chosen and numberToRemove chosen
				if (whichSpot == 1)
					totalPoints += 100*numberToRemove;
				else if (whichSpot == 5)
					totalPoints += 50*numberToRemove;
			}

			firstComboPicking = false;
		}
	}

	// main program for initializing game (i.e. player name, points until final round)
	// handle prompting to start turns and calling the optionsProcess method
	// handle storing overall point totals and tracking them to determine when final round starts
	// print result after final round
	public static void main(String[] args) {

		sc = new Scanner(System.in);  // initialize and declare scanner for prompts
		
		int round = 0;
		int p1Farkle = 0;  // used for tracking if a player farkles 3 times in a row (deduct 1000)
		int p2Farkle = 0;

		System.out.print("Enter player 1's name: ");
		p1 = sc.nextLine();
		System.out.print("Enter player 2's name: ");
		p2 = sc.nextLine();
		System.out.print("Enter points needed for final round to start (usually 10000): ");

		while (!sc.hasNextInt()) {   			// go to next input if not an integer
			sc.next();  						// go to next scannable object
			System.out.print("Invalid entry, enter again: ");
		}
		int maxPts = sc.nextInt();

		while (p1pts < maxPts && p2pts < maxPts) {   // while both players have not exceeded 10000 points, let both play
			round += 1;
			
			// print total points, round, and prompt for turn
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n---Round " + round + "---");
			System.out.println(p1 + " has " + p1pts + " points.");
			System.out.println(p2 + " has " + p2pts + " points.");
			System.out.println("\nStarting " + p1 + "'s turn.");
			System.out.println("Press enter to continue.");
			sc.nextLine();
			sc.nextLine();
			
			// call main rolling/options method
			int curPts = optionsProcess(roll(6), p1);		
			if (curPts == 0) {  // if player gets a farkle or 0 points increment farkle counter
				p1Farkle += 1;
				if (p1Farkle == 3) {
					System.out.println("Watch out! " + p1 + " has farkled 3 turns in a row! Minus 1000 points!");
					p1pts -= 1000;   // deduct 1000 points if this is 3rd farkle in a row
					p1Farkle = 0;	// reset counter
				}
			}
			else {   			// if player did not farkle
				p1pts += curPts;  // add points
				p1Farkle = 0; 	// reset farkle counter
			}
			sc.nextLine();
			sc.nextLine();

			// print total points, round, and prompt for turn
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n---Round " + round + "---");
			System.out.println(p1 + " has " + p1pts + " points.");
			System.out.println(p2 + " has " + p2pts + " points.");
			System.out.println("\nStarting " + p2 + "'s turn.");
			System.out.println("Press enter to continue.");
			sc.nextLine();
			sc.nextLine();
			
			// call main rolling/options method
			curPts = optionsProcess(roll(6), p2);		
			if (curPts == 0) {  // if player gets a farkle or 0 points increment farkle counter
				p2Farkle += 1;
				if (p2Farkle == 3) {
					System.out.println(p2 + " has farkled 3 turn in a row! Minus 1000 points!");
					p2pts -= 1000;   // deduct 1000 points if this is 3rd farkle in a row
					p2Farkle = 0;	// reset counter
				}
			}
			else {   			// if player did not farkle
				p2pts += curPts;  // add points
				p2Farkle = 0; 	// reset farkle counter
			}
			sc.nextLine();
			sc.nextLine();
		}
		round += 1;
		System.out.println("\n---A player has reached " + maxPts + " points, press enter to start FINAL ROUND.");
		sc.nextLine();
		sc.nextLine();

		// print total points, round, and prompt for turn
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n---Round " + round +  " (final)---");
		System.out.println(p1 + " has " + p1pts + " points.");
		System.out.println(p2 + " has " + p2pts + " points.");
		System.out.println("\nStarting " + p1 + "'s last turn.");
		System.out.println("Press enter to continue.");
		sc.nextLine();
		sc.nextLine();
		
		// call main rolling/options method
		int curPts = optionsProcess(roll(6), p1);		
		if (curPts == 0) {  // if player gets a farkle or 0 points increment farkle counter
			p2Farkle += 1;
			if (p1Farkle == 3) {
				System.out.println(p1 + " has farkled 3 turn in a row! Minus 1000 points!");
				p1pts -= 1000;   // deduct 1000 points if this is 3rd farkle in a row
				p1Farkle = 0;	// reset counter
			}
		}
		else    			// if player did not farkle
			p2pts += curPts;  // add points
		sc.nextLine();
		sc.nextLine();
		
		// print total points, round, and prompt for turn
		System.out.println("\n\n\n\n\n\n\n\n\n\n\nRound " + round + " (final)");
		System.out.println(p1 + " has " + p1pts + " points.");
		System.out.println(p2 + " has " + p2pts + " points.");
		System.out.println("\nStarting " + p2 + "'s last turn.");
		System.out.println("Press enter to continue.");
		sc.nextLine();
		sc.nextLine();
		
		// call main rolling/options method
		curPts = optionsProcess(roll(6), p2);		
		if (curPts == 0) {  // if player gets a farkle or 0 points increment farkle counter
			p2Farkle += 1;
			if (p2Farkle == 3) {
				System.out.println(p2 + " has farkled 3 turn in a row! Minus 1000 points!");
				p2pts -= 1000;   // deduct 1000 points if this is 3rd farkle in a row
				p2Farkle = 0;	// reset counter
			}
		}
		else    			// if player did not farkle
			p2pts += curPts;  // add points
		sc.nextLine();
		sc.nextLine();
		
		// after final round, display round number, and print appropriate winner and points for each player, or declare draw
		
		if (p1pts == p2pts)
			System.out.format("\nAfter round %d, the game is a draw.", round);
		else if (p1pts > p2pts)
			System.out.format("\nAfter round %d, %s with %d points has beaten %s with %d points.", round, p1, p1pts, p2, p2pts);
		else 
			System.out.format("\nAfter round %d, %s with %d points has beaten %s with %d points.", round, p2, p2pts, p1, p1pts);
	}
}

