import java.util.Scanner;

//Harrison Chiu - ICS4U1-09 - May 1256, 2019
//"Two-Dimensional Arrays Set"

public class TwoDArrays {
	static Scanner sc;

	// computes sum of 2D array
	public static int sum(int[][] array1) {
		int total = 0;

		// go through each row
		for (int[] row:array1){
			// go through each value in the current row
			for (int value:row) {
				total += value;
			}
		}
		return total;						// return final array
	}

	// computes sums of each row in 2D array (returns array)
	public static int[] rowSums(int[][] array1) {
		int[] totals = new int[array1.length];

		// go through each row
		for (int r = 0; r < array1.length; r ++){
			// go through each value in the current row
			for (int value:array1[r]) {
				totals[r] += value;			// add the current value to the current row (using the index)
			}
		}
		return totals;						// return final array
	}
	public static int[] columnSums(int[][] array1) {

		// setting default value of 0, find longest row in 2D array and set length as variable
		// iterate through all rows and check if current row length is larger than stored longestRow length
		// update longestRow value if yes to above
		int longestRow = 0;
		for (int[] row:array1)
			if (row.length > longestRow) longestRow = row.length;

		// return list of col. sums (only needs one value from 1 until longestRow)
		int colValues[] = new int[longestRow];

		// now increment the index (shift from beginning of rows to right by 1) 
		// only goes up to the longest row found's length as to not try to add when no row has an available index
		for (int colID = 0; colID < longestRow; colID ++) {
			// go through the given rows again
			for (int[] row:array1) {
				// if true, there is another "column" value to be added from this row
				if (row.length-1 >= colID)
					colValues[colID] += row[colID];
			}
		}
		return colValues;			// return array of col. sums

	}
	
	// return maximum and minimum values of 2D array
	public static int[] maxMin(int[][] array1) {
		
		// set the current maximum as a default low (anything will be higher than this)
		// set the current minimum as a default high (anything will be lower than this)
		int curMax = Integer.MIN_VALUE;
		int curMin = Integer.MAX_VALUE;

		// iterate through each row
		for (int[] row: array1) {
			// iterate through the current row's values
			for (int value: row) {
				// if the current value is more than the current maximum, set that to curMax
				if (value > curMax)
					curMax = value;
				// if the current value is less than the current minimum, set that to curMin 
				if (value < curMin) 
					curMin = value;
				
				// both must be evaluated as the first value is both the max and the min
			}
		}
		
		// return an array with the min and max after going through all values
		return new int[]{curMin, curMax};
	}



	////////////////////////////////////////
	////////////main program////////////////
	////////////////////////////////////////

	public static void main (String[] args){

		sc = new Scanner (System.in); 

		// declare main rectangular array to be used
		int[][] mainArray = {
				{3, 2, 5, 2, 1, 5},
				{1, 2, 5, 4, 2},
				{9, 1, 0, 2, 5},
				{0, 2, 6, 3, -1} 
		};

		char choice; 
		do{
			// print menu/program items with number
			System.out.println ("\n\n\nTwo-dimensional Arrays Set, Harrison Chiu");   
			System.out.println ("--------------");
			System.out.println ("1. Total Sum");
			System.out.println ("2. Sum of Each Row");
			System.out.println ("3. Sum of Each Column");
			System.out.println ("4. Max & Min");

			System.out.print ("\nSelect an option ('0' to quit): ");

			while (true){
				try{
					choice = sc.nextLine().charAt(0);  // prompt for choice
					break;
				}
				// if line does not have length, will catch error and prompt for retry (indefinitely)
				catch(StringIndexOutOfBoundsException e){
					System.out.print("Select an option ('0' to quit): ");
				}
			}

			System.out.println();

			//look at given choice, print program name and execute appropriate program (with driver method)
			if (choice == '1'){
				System.out.println("TOTAL SUM");  
				System.out.println("------------------------\n");
				System.out.println("The sum of the values of each element is " + sum(mainArray));
			}
			else if (choice == '2'){
				System.out.println("SUM OF EACH ROW");  
				System.out.println("------------------------\n");

				// let method calculate sum of values in each row
				int[] rowSums = rowSums(mainArray);

				// iterate through each index of resulting array and print sum
				for (int x = 0; x < mainArray.length; x ++)
					System.out.println("The sum of values in row " + (x+1) + " is " + rowSums[x]);
			}
			else if (choice == '3'){
				System.out.println("SUM OF EACH COLUMN");  
				System.out.println("------------------------\n");
				
				// let method calculate sum of values in each column
				int[] colSums = columnSums(mainArray);

				// iterate through each index of resulting array and print sum
				for (int x = 0; x < colSums.length; x ++)
					System.out.println("The sum of values in column "+ (x+1) + " is " + colSums[x]);
			}
			else if (choice == '4'){
				System.out.println("MAX & MIN");  
				System.out.println("------------------------\n");
				
				// let method find the maximum and minimum values (in format {min, max})
				int[] maxMin = maxMin(mainArray);
				
				// print results
				System.out.println("The minimum value in the array is " + maxMin[0]);
				System.out.println("The maximum value in the array is " + maxMin[1]);
			}

			if (choice != '0'){  // if choice is not 0, wait for enter before continuing
				System.out.println("\n\n------------------------");
				System.out.println("Going back to menu. Press ENTER to continue.");
				sc.nextLine(); 
				sc.nextLine(); 

			}   
		}
		while (choice != '0'); // exit when 0    
		System.out.println("Choice is 0, class has ended.");        
		System.out.println("-----------------------------");

	}
}
