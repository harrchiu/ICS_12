//Harrison Chiu - ICS4U1-09 - May 25, 2019
//"Image Smoother GUI - Multi-dimensional Arrays Set"

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ImageSmootherGUI extends JFrame {

	// initialize the grid
	int[][] mainGrid = {
			{0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,5,5,5,5,5,5,5,5,0,0},
			{0,0,5,5,5,5,5,5,5,5,0,0},
			{0,0,5,5,5,5,5,5,5,5,0,0},
			{0,0,5,5,5,5,5,5,5,5,0,0},
			{0,0,5,5,5,5,5,5,5,5,0,0},
			{0,0,5,5,5,5,5,5,5,5,0,0},
			{0,0,5,5,5,5,5,5,5,5,0,0},
			{0,0,5,5,5,5,5,5,5,5,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0,0,0}
	};

	// constructor
	public ImageSmootherGUI() { 

		// set main window
		JPanel mainWindow = new JPanel();
		mainWindow.setLayout (new FlowLayout());

		//###############################################################
		//######## Set the actual window's attributes ###################
		//###############################################################
		DrawArea graph = new DrawArea(600,600);		// draw the graph for the first time
		mainWindow.add(graph);
		setContentPane (mainWindow);
		setTitle ("CH 2D Array Smoother");
		setSize(600,650);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo (null);           // Center window.
	}

	// smoothen array 
	public int[][] smoothGrid(int [][] grid) {
		// since grid is assumed to be a rectangular 2D array, let the dimensions be the number of rows 
		// where each row is the length of the first row
		int[][] smoothGrid = new int[grid.length][grid[0].length];

		// iterate through each row
		for (int row = 0; row < grid.length; row ++) {
			// withint each row, go left to right through each index (take the length of the first row as max since it's rectangular)
			for (int col = 0; col < grid[0].length; col++) {

				// attempt to add the value of all 8 neighbours and self, using try catch
				// then divide the total sum by # of successful attempts to reach average for that value

				int average = 0;
				int valuesAdded = 1;			// leave as one for the current value itself; always possible to add

				// add self
				average += grid[row][col];

				// attempting to add 8 neighbours
				// since -1 <= vertShift&horzShift <= +1 through the nested for loops,
				// all combinations of shifting by one square will be iterated through thus
				// trying to add the values of each neighbour
				for (int vertShift = -1; vertShift <= 1; vertShift ++) {
					for (int horzShift = -1; horzShift <= 1; horzShift ++) {
						try {
							average += grid[row+vertShift][col+horzShift];
						}
						catch (Exception IndexOutOfBoundsException){		// catch any out of bounds errors
							continue; 										// try to go to next neighbour
						}
						valuesAdded ++;			// will only increment if it passed the try catch and successfully added the neighbour value
					}
				}
				// all possible neighbours added, now assign avg value to new array
				smoothGrid[row][col] = average/valuesAdded;
			}
		}
		// now return smooth grid
		return smoothGrid;
	}

	// method to generate graph
	public void drawGrid(Graphics g) {
		mainGrid = smoothGrid(mainGrid);
		
		// go through each row
		for (int row = 0; row < mainGrid.length; row ++) {
			for (int col = 0; col < mainGrid[0].length; col ++) {
				// since white is 255, set the current square color 0-255 based on current value
				g.setColor(new Color( 51*mainGrid[row][col], 51*mainGrid[row][col], 51*mainGrid[row][col]) );
				g.fillRect(50*col, 50*row, 50, 50); 
				System.out.print(mainGrid[row][col] + " ");

			}
			System.out.println();

		}
		g.setColor(Color.GREEN);

	}
	//subclass of JPanel with paintComponent method to display drawn items
	class DrawArea extends JPanel{

		public DrawArea (int w, int h){
			this.setPreferredSize(new Dimension(w, h)); //set preferred size
		}
		public void paintComponent (Graphics g){
			super.paintComponent(g);
			drawGrid(g);   // call draw grid method
		}
	}

	// handle button events
	public void actionPerformed(ActionEvent e) {
		String choice = e.getActionCommand();
		if(choice.equals("Graph!"))
		{}
	}
	//////////////////////////////////////main method////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args){
		ImageSmootherGUI frame = new ImageSmootherGUI() ;    
		frame.setVisible( true ); 
	}

}
