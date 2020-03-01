import java.util.Scanner;

class CH_mazesRecursion{
	
	static Scanner sc;


	static char [][] grid = {
			{'o','o','X','o','o','o','X','o','o','o'},
			{'X','o','o','o','X','o','X','o','X','o'},
			{'X','X','X','o','X','o','X','o','X','o'},
			{'o','o','o','o','X','o','X','o','X','o'},
			{'X','o','X','o','X','X','X','o','X','o'},
			{'X','o','X','o','o','o','o','o','X','o'},
			{'X','o','X','X','X','X','X','X','X','o'},
			{'o','o','X','o','o','o','o','o','o','o'},
			{'X','o','X','X','X','o','X','X','X','X'},
			{'X','o','o','o','X','o','o','o','o','o'}
	};

	
	public static void display(){

		//iterate through grid columns and rows and print each tile/point
		for (int row = 0; row < grid.length; row ++){
			for (int col = 0; col < grid[0].length; col ++)
				System.out.print(grid[row][col]);
			System.out.println();
		}

	}

	// returns whether point in maze is valid
	public static boolean valid(int row, int col){

		// if the point's coordinates are within the grid dimensions
		if (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length )
		{
			// if the point is 'o' (not visited, available)
			if (grid[row][col] == 'o')
				return true;  // return true if these two conditions are met
		}
		
		// if either or both of above are not met, return false
		return false;
	}

	public static boolean findPath(int row, int col){

		boolean done = false;

		// if current point is valid
		if (valid(row,col)){
			// mark current point as visited, so it is not available in the future
			grid[row][col] = 'V';

			// if the current point is the bottom right of the grid, return true
			if (row == grid.length-1 && col == grid[0].length-1){
				done = true;
				// exits method
			}	
			// otherwise, check if adjacent points are the end point
			// continue to check adjacent points as long as done is false
			
			else{
				done = findPath(row+1,col);  // check the point below first
				if (done == false)  
					done = findPath(row-1,col);  // check the point above
				if (done == false)
					done = findPath(row,col+1);  // check the point on the right
				if (done == false)
					done = findPath(row,col-1);  // check the point on the left
			}
			// if this point ends with done == true, mark it as traveled
			if (done)
				grid[row][col] = '.';
		}
		return done;
	}


	public static void main (String[] args){
		
		sc = new Scanner (System.in); 

		display();
		System.out.println("Press enter to solve.");
		sc.nextLine();

		// starting point of 0,0
		if ( findPath(0,0) ){
			System.out.println("Maze is solvable.");
			display();
		}
		else
			System.out.println("Maze is NOT solvable.");

		
	}
}