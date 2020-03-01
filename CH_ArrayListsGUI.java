//Harrison Chiu - ICS4U1-09 - May 16, 2019
//"ArrayLists GUI"

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

class Names{
	private ArrayList<String> namesList;						// declare namesList outside of constructors first

	// constructors
	public Names() {										// no parameter creates empty list
		namesList = new ArrayList<String> ();				// initialize empty list
	}

	public Names(String file) {								// string will take names from file
		namesList = new ArrayList<String> ();				// initialize list

		// declare file and scanner (file name alone works in terminal)
		File text = new File(file);
		Scanner scnr;

		// scan from file, if file not found return null string and print erorr
		try {
			scnr = new Scanner(text);

			while (scnr.hasNextLine()) {

				// get key from first line
				String curLine = scnr.nextLine();
				curLine = curLine.trim();					// trim off end spaces
				namesList.add(curLine);						// add to end of list
			}
		} catch (FileNotFoundException e) {
			// print error message if file is not found
			System.out.println("Error: no file found");
		}
	}

	// convert to multi-line string
	public String toString() {
		String result = "";									// create new string
		for (int x = 0; x < namesList.size(); x++) {		// iterate through values of list
			result += namesList.get(x) + "\n";				// add name and \n for a new line
		}
		return result;
	}

	// adds new name to end of the names list
	public void add(String newName) {
		namesList.add(newName);
	}

	// takes in a position to remove and removes value at that position
	// will do nothing if position is not valid
	public String remove(int position) {
		try {
			String name = namesList.get(position);
			namesList.remove(position); 				// store name at given position, remove it, then return it if possible
			return name; 											
		}
		catch (Exception IndexOutOfBoundsException){
			return "not found"; 						// do nothing if index not valid, return error string
		}
	}

	// sort the list alphabetically (by surname)
	public void sort() {
		ArrayList<String> replacement = new ArrayList<String>(namesList.size());		// create filling list as we go
		int startingLen = namesList.size();
		for (int iteration = 0; iteration < startingLen; iteration ++) {				// go through the list once for every value in list
			String earliest = namesList.get(0);											// set 'earliest' (in alphabet, as in 'a') as the first value in list by default
			for (int comparison = 1; comparison < namesList.size(); comparison ++) {	// compare with each other element in list excluding self

				String comparing = namesList.get(comparison);							// current word to compare with
				int earliestCurChar = 0;
				int comparingCurChar = 0;

				int shorterLen = earliest.length();										// find the length of the shorter string
				if (comparing.length() < shorterLen)
					shorterLen = comparing.length();

				for (int ID = 0; ID < shorterLen; ID ++) {								// iterate through indexes
					if (earliest.charAt(ID) == ' ' || earliest.charAt(ID) == ',')		// between earliest word and current word
						earliestCurChar = 0;
					else
						earliestCurChar = (int) Character.toLowerCase(earliest.charAt(ID));		

					if (comparing.charAt(ID)  == ' ' || comparing.charAt(ID)  == ',')
						comparingCurChar = 0;
					else
						comparingCurChar = (int) Character.toLowerCase( comparing.charAt(ID) );	

					if (comparingCurChar != earliestCurChar) {
						if (comparingCurChar < earliestCurChar)			// switch them if current is smaller
							earliest = comparing;
						break;
					}
					// if values are equal, it will go to next cycle and look at next index
				}

				if (comparingCurChar < earliestCurChar)					// if the current word has the lower ID, 
					earliest = comparing;								// set the earliest word to the currently compared word\

			}

			// at this point the earliest word is retrieved
			replacement.add(earliest);

			// add the earliest word into the new list
			namesList.remove(namesList.indexOf(earliest));				// remove the earliest word from the old list
		}
		// after all iterations, put the new lists' contents in the old one
		for (String name: replacement) {
			namesList.add(name);
		}
	}

	// search using a character or string
	public Names search(char letter) {
		Names returnList = new Names();														// declare new names object (constructor will generate empty list)
		for (String name: namesList) {
			if (Character.toLowerCase(name.charAt(0)) == Character.toLowerCase(letter))		// if current name starts with 'letter'
				returnList.add(name);														// append the current name to return list
		}																					// use Character.toLowerCase() method to ensure a 'c' would match with 'C' for ex.
		return returnList;
	}

	public Names search(String surname) {
		System.out.println("searching prop");
		Names returnList = new Names();												// declare new names object (constructor will generate empty list)
		
		for (String name: namesList) {												// iterate through names in names list
			if (name.substring(0, name.indexOf(',')).equalsIgnoreCase(surname))		// if the surname (ends with the comma) equals given surname
				returnList.add(name);												// append the current name to return list
		}
		return returnList;
	}

	// return size of list
	public int returnLen() {
		return namesList.size();
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// GUI CLASS //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class CH_ArrayListsGUI extends JFrame implements ActionListener {

	// area for user to type text
	static JTextArea textArea = new JTextArea();

	// buttons for actions/method activation
	static JButton toStringBtn = new JButton ("Show list as string"); 	// search button
	static JButton addBtn = new JButton ("Add name"); 						// add button
	static JButton removeBtn = new JButton ("Remove"); 					// remove button
	static JButton searchBtn = new JButton ("Search"); 					// search button
	static JButton sortBtn = new JButton ("Sort List"); 				// sort button

	// text fields for entering data
	static JTextField searchTF = new JTextField(25);
	static JTextField addTF = new JTextField(25);
	static JPanel row4 = new JPanel();

	// declare list
	static Names array1;
	static int length;

	// combo box for removing list positions
	static JComboBox positionBox;

	// constructor
	public CH_ArrayListsGUI() {

		array1 = new Names("namesFile.txt");			// 	set list values
		length = array1.returnLen();

		//###############################################################
		//###### 1... Create/initialize components (buttons) ############
		//###############################################################

		toStringBtn.addActionListener (this);	 // Connect each button to listener
		addBtn.addActionListener (this);	 
		removeBtn.addActionListener (this);	
		searchBtn.addActionListener (this);	
		sortBtn.addActionListener (this);	 

		//###############################################################
		//###### 2... Create content pane, set layout ###################
		//###############################################################

		// Create a main pane
		JPanel mainWindow = new JPanel ();        
		mainWindow.setLayout (new BoxLayout (mainWindow,BoxLayout.Y_AXIS)); // Use boxlayout for this panel for vertical downwards stacking

		// create four rows and set them all with flow layout
		JPanel row1 = new JPanel();
		row1.setLayout (new FlowLayout ());
		JPanel row2 = new JPanel();
		row2.setLayout (new FlowLayout ());
		JPanel row3 = new JPanel();
		row3.setLayout (new FlowLayout ());
		row4.setLayout (new FlowLayout ());
		JPanel row5 = new JPanel();
		row5.setLayout (new FlowLayout ());

		//###############################################################
		//### 3... Add the components to the input area #################
		//###############################################################

		// add correct components to each row
		row1.add(toStringBtn);
		row1.add(sortBtn);

		row2.add(searchTF);
		row2.add(searchBtn);

		row3.add(addTF);
		row3.add(addBtn);

		String[] filledIDs = new String[length];
		for (int x = 0; x < length; x ++)
			filledIDs[x] = Integer.toString(x);
		System.out.println("length at gui " + length);
		positionBox = new JComboBox<String>(filledIDs);
		if (length > 0)
			positionBox.setSelectedIndex(0);

		row4.add(positionBox);			// used to indicate which position should be removed
		row4.add(removeBtn);

		row5.add(textArea);

		mainWindow.add(row1);
		mainWindow.add(row2);
		mainWindow.add(row3);
		mainWindow.add(row4);
		mainWindow.add(row5);

		textArea.setText("Names will be updated here");

		//###############################################################
		//### 4... Set the actual window's attributes ###################
		//###############################################################

		setContentPane (mainWindow);
		setTitle ("CH ArrayLists");		// title, size, and exit command
		setSize(450,400);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo (null);           // Center window.
	}

	// handle button events
	public void actionPerformed(ActionEvent e) {
		String choice = e.getActionCommand();

		if(choice.equals("Show list as string"))			// create appropriate response according to which button was pressed by user
		{
			textArea.setText("Names:\n" + array1.toString());
		}
		else if(choice.equals("Add name")) {
			if (addTF.getText().indexOf(',') == -1) {													// do not add name if there is no comma
				textArea.setText("Please make sure the added name\nhas a comma with a space after it.");
				return;
			}
			textArea.setText("Name added.");
			array1.add(addTF.getText());		// add name otherwise
			addTF.setText("");
			length ++;

		}
		else if(choice.equals("Remove")) {		// use JComboBox to remove value at certain index

			if (length == 0) {															// when names run out
				textArea.setText("No names in list!");
				return;				
			}

			int index = Integer.valueOf((String) positionBox.getSelectedItem());		// get selected index

			array1.remove(index);														// remove value at right index
			length -= 1;																// decrease the number of values in list
			textArea.setText("Name removed.");
		}
		else if(choice.equals("Search")) {
			Names foundNames;														// make new names object

			if (searchTF.getText().length() == 1)									// input char if only length 1
				foundNames = array1.search(searchTF.getText().charAt(0));
			else { 																	// otherwise put in string into search method
				foundNames = array1.search(searchTF.getText());
			}

			if (foundNames.returnLen() == 0) {
				textArea.setText("No names match search criteria.");		// show if no names found
				return;
			}

			textArea.setText("Names found:\n" + foundNames.toString());		// show names found
		}		

		else if(choice.equals("Sort List")) {				
			array1.sort();						
			textArea.setText("List has been sorted.");			// just sort the list and say so
		}
		
		row4.remove(positionBox);

		String[] filledIDs = new String[length];									 // redo combobox 
		for (int x = 0; x < length; x ++) 
			filledIDs[x] = Integer.toString(x);
		positionBox = new JComboBox<String>(filledIDs);

		System.out.println("length here " + length);
		for (String x:filledIDs)
			System.out.print(x);

		row4.add(positionBox, 0);
		validate();		// update GUI
		repaint();
	}

	//subclass of JPanel with paintComponent method to display drawn items
	class DrawArea extends JPanel{
		public DrawArea (int w, int h){
			this.setPreferredSize(new Dimension(w, h)); 	//set preferred size
		}
		public void paintComponent (Graphics g){
			super.paintComponent(g);
		}
	}

	//////////////////////////////////////main method////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main (String[] args) {
		Names array1 = new Names("namesFile.txt");					// fill Names object with values from file
		CH_ArrayListsGUI frame = new CH_ArrayListsGUI() ;    
		frame.setVisible( true ); 		
	}

}