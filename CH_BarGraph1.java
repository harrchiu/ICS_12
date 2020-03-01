//Harrison Chiu - ICS4U1-09 - May 16, 2019
//"Bar Graph GUI"

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import java.awt.geom.AffineTransform;


class CH_BarGraph1 extends JFrame implements ActionListener {

	// make dictionary of colours for retrieval later
	Map<String, Color> colors = new HashMap<String, Color>();

	// textfields for axes title inputs
	private JTextField xTF = new JTextField (10);
	private JTextField yTF = new JTextField (10);
	private JTextField titleTF = new JTextField (10); // Multi-line text field

	// textfields for data input (category name and corresponding quantity)
	private JTextField cat1TF = new JTextField (7); 	
	private JTextField quantity1TF = new JTextField (5); 
	private JTextField cat2TF = new JTextField (7);
	private JTextField quantity2TF = new JTextField (5); 
	private JTextField cat3TF = new JTextField (7);
	private JTextField quantity3TF = new JTextField (5); 
	private JTextField cat4TF = new JTextField (7); 
	private JTextField quantity4TF = new JTextField (5); 
	private JTextField cat5TF = new JTextField (7); 
	private JTextField quantity5TF = new JTextField (5); 
	private JTextField cat6TF = new JTextField (7); 
	private JTextField quantity6TF = new JTextField (5);

	JTextField[] categories = {cat1TF, cat2TF, cat3TF, cat4TF, cat5TF, cat6TF} ;   // store these in arrays for future use
	JTextField[] quantities = {quantity1TF, quantity2TF, quantity3TF, quantity4TF, quantity5TF, quantity6TF};

	static JButton graphBtn = new JButton ("Graph!");	// graph button 
	static int largestQuant = 0;
	static int[] graphableCategories; // hold which categories are actually graphable (only stores indices)
	static int barsToGraph = 0; // store what # of categories are actually graphable

	// set the default/initial color for each of the six bars (index of the color array)
	static int[] barColor = {0,1,2,3,4,5};
	// set the order which the bars cycle through the possible color options
	static String[] colorCycle = {"Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet", "White", "Black"};
	static JButton barCol1 = new JButton ("Red");	// bar color button 1-6 
	static JButton barCol2 = new JButton ("Orange");	
	static JButton barCol3 = new JButton ("Yellow");	
	static JButton barCol4 = new JButton ("Green");
	static JButton barCol5 = new JButton ("Blue");	
	static JButton barCol6 = new JButton ("Indigo");	
	static JButton[] barColorBtns = {barCol1, barCol2, barCol3, barCol4, barCol5, barCol6};		// array of each of the color buttons

	public CH_BarGraph1() { 

		// initialize colors
		colors.put("Red", Color.RED);
		colors.put("Orange", Color.ORANGE);
		colors.put("Yellow", new Color (245,245,0));
		colors.put("Green", new Color (0,210,0));		//  easier shades for the eyes
		colors.put("Blue", Color.BLUE);
		colors.put("Indigo", new Color(75,0,130));
		colors.put("Violet", new Color(128,0,128));
		colors.put("White", Color.WHITE);
		colors.put("Black", Color.BLACK);

		//###############################################################
		//###### 1... Create/initialize components (buttons) ############
		//###############################################################
		graphBtn.addActionListener (this);	 // Connect graph button to listener
		for (JButton btn:barColorBtns) {
			btn.addActionListener(this); 	 // Connect each bar color button to listener
		}
		graphBtn.setForeground(new Color(0,170,0));	// change text color to green

		//###############################################################
		//###### 2... Create content pane, set layout ###################
		//###############################################################

		JPanel leftPanel = new JPanel ();        // Create a content pane for left side
		leftPanel.setLayout (new BoxLayout (leftPanel,BoxLayout.Y_AXIS)); // Use boxlayout for this panel for vertical downwards stacking

		JPanel rightPanel = new JPanel ();        // Create a content pane for right side (actual graph)
		//rightPanel.setLayout (new BoxLayout (leftPanel,BoxLayout.PAGE_AXIS)); // Use flow

		JPanel mainWindow = new JPanel ();        // Create a main pane
		mainWindow.setLayout (new FlowLayout());

		// first two panels for graph title and axis
		JPanel axisRow = new JPanel ();        
		axisRow.setLayout (new FlowLayout ()); // Use FlowLayout for input area
		JPanel titleRow = new JPanel ();        
		titleRow.setLayout (new FlowLayout ()); // Use FlowLayout for input area

		// panels for data entry fields
		JPanel dataRow1 = new JPanel ();        
		dataRow1.setLayout (new FlowLayout ()); // Use FlowLayout for input area
		JPanel dataRow2 = new JPanel ();        
		dataRow2.setLayout (new FlowLayout ()); // Use FlowLayout for input area
		JPanel dataRow3 = new JPanel ();        
		dataRow3.setLayout (new FlowLayout ()); // Use FlowLayout for input area
		JPanel dataRow4 = new JPanel ();        
		dataRow4.setLayout (new FlowLayout ()); // Use FlowLayout for input area
		JPanel dataRow5 = new JPanel ();        
		dataRow5.setLayout (new FlowLayout ()); // Use FlowLayout for input area
		JPanel dataRow6 = new JPanel ();        
		dataRow6.setLayout (new FlowLayout ()); // Use FlowLayout for input area
		JPanel finalRow = new JPanel ();        
		finalRow.setLayout (new FlowLayout ()); // Use FlowLayout for graph button and info area

		// "graph" button can be added separately on main frame

		//###############################################################
		//### 3... Add the components to the input area #################
		//###############################################################

		// adding row for main graph title entry
		titleRow.add(new JLabel ("Graph title"));   // Create, add label
		titleRow.add(titleTF);						// Add input field

		// adding row for axis title entries
		axisRow.add (new JLabel ("X-axis title")); 
		axisRow.add (xTF);           				 
		axisRow.add (new JLabel (" Y-axis title")); 
		axisRow.add (yTF);         				

		// addings rows for DATA entries (each row contains: label for category # & TF and label for quantity & TF)
		JPanel[] dataRows = {dataRow1, dataRow2, dataRow3, dataRow4, dataRow5, dataRow6};
		for (int row = 1; row < 7; row ++) {
			dataRows[row-1].add(new JLabel("Category " + row));
			dataRows[row-1].add(categories[row-1]);
			dataRows[row-1].add(new JLabel ("Quantity"));
			dataRows[row-1].add(quantities[row-1]);
			dataRows[row-1].add(barColorBtns[row-1]);
		}
		//"Bar color will match button"
		finalRow.add(new JLabel ("(Each bar's colour can be chosen. \"Graph!\" will update colours.)"));		
		finalRow.add(graphBtn); 		// add button to generate graph upon user request (and activate drawGraph function)

		// adding each side panel to main panel
		leftPanel.add(titleRow); 		// add title and axis rows
		leftPanel.add(axisRow);
		for (JPanel row:dataRows)  		// add each of the six created rows
			leftPanel.add(row, "East");
		leftPanel.add(finalRow);
		

		DrawArea graph = new DrawArea(600,520);		// draw the graph for the first time
		rightPanel.add(graph, "West");

		//###############################################################
		//### 4... Set the actual window's attributes ###################
		//###############################################################

		mainWindow.add (leftPanel, "West");
		mainWindow.add (rightPanel, "East");

		setContentPane (mainWindow);
		setTitle ("CH Bar Graph GUI");
		setSize(1200,560);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo (null);           // Center window.
	}

	public void setLargestQuant() {
		largestQuant = 0;
		for (JTextField quant:quantities) {
			try {
				if (Integer.parseInt(quant.getText()) > largestQuant)
					largestQuant = (int) Integer.parseInt(quant.getText());   // replace largestquant if current is bigger
			}
			catch (Exception e) {		// catch string-to-int conversion errors or others
				continue; // skip any non-convertable entries
			}
		}
	}

	// updates static array holding which categories have graphable quantities (i.e. double/int, not blank)
	// updates # of categories actually graphable
	public void setCategoriesToGraph() {
		barsToGraph = 0;

		for (JTextField quant:quantities) {
			try {
				if (Integer.parseInt(quant.getText()) != 999999999)	// try operation requiring conversion to double
					barsToGraph += 1;
			}
			catch (Exception e) {		// catch string-to-int conversion errors or others
				continue; // skip this entry since it's not convertable
			}
		}
		graphableCategories = new int[barsToGraph];			// set to-be-filled array with graphable bars length
		int cur = 0;
		for (int id = 0; id < 6; id += 1) {
			try {
				if (Integer.parseInt(quantities[id].getText()) != 999999999) {	// try operation requiring conversion to double
					graphableCategories[cur] = id;	// if no fail, append THIS index into graphable categories for later
					cur += 1;
				}
			}
			catch (Exception e) {		// catch string-to-int conversion errors or others
				continue; // skip this entry since it's not convertable
			}
		}
	}


	// method to generate graph
	public void drawGraph(Graphics g) {
		Font sideFont = new Font("American Typewriter", Font.PLAIN, 15);  

		setLargestQuant(); // update the largest quantity entered across categories
		setCategoriesToGraph();	// update array of graphable categories and number of bars needed

		// set regular interval distance (called "gap")
		// bar has width of two gaps, spaces between bars are the width of one gap
		int gap = 500/(barsToGraph*2+1+barsToGraph);

		// set largest quant to nearest ten
		int ceiling = largestQuant;
		while (ceiling % 10 != 0 || ceiling == 0)
			ceiling += 1;

		int tick;
		if (largestQuant == 0)
			tick = 1;
		else
			tick = ceiling/10;

		// draw x- and y-axis
		g.setColor(Color.black);
		g.drawLine(100, 380, 600, 380);   // x  (500x330 graph)
		g.drawLine(100, 380, 100, 50);  // y

		//title
		g.setFont( new Font("American Typewriter", Font.PLAIN, 30));
		g.drawString(titleTF.getText(), 353 - (titleTF.getText().length())*8, 30);

		// y-axis title (rotate)
		Font yFont = new Font("American Typewriter", Font.PLAIN, 20);  
		AffineTransform yTilt = new AffineTransform();
		yTilt.rotate(Math.toRadians(-90), 0, 0);
		yFont = yFont.deriveFont(yTilt);
		g.setFont(yFont);
		g.drawString(yTF.getText(),65 - Integer.toString(largestQuant).length()*7, 215 + yTF.getText().length()*5);

		// x-axis category titles (tilt slightly)
		Font xFont = new Font("American Typewriter", Font.PLAIN, 15);  
		AffineTransform xTilt = new AffineTransform();
		xTilt.rotate(Math.toRadians(-50), 0, 0);
		xFont = xFont.deriveFont(xTilt);
		g.setFont(xFont);
		int longestCategoryLen = 0;		// track longest name
		for (int x = 0; x < barsToGraph; x += 1) {
			int id = graphableCategories[x];
			String curCategory = categories[id].getText();
			if (curCategory.length() > longestCategoryLen)
				longestCategoryLen = curCategory.length();		// drawing for each category's label (tilted) below x-axis
			g.drawString(curCategory, 110 + (int)((2+3*x)*gap-curCategory.length()*4.5), 398 + (int)(curCategory.length()*5.2));
			System.out.println(curCategory);
		}

		g.setFont(new Font("American Typewriter", Font.PLAIN, 20));			// draw main title for categories below x-axis
		g.drawString(xTF.getText(), 359 - xTF.getText().length()*6, 410+longestCategoryLen*6);

		// notches for axes and draw cat. titles
		int counter = 0;
		for(int i = 1; i < barsToGraph*2+1+barsToGraph; i += 3){
			g.drawLine(100 + gap*i, 380, 100 + gap*i, 375);
			g.drawLine(100 + gap*(i+2), 380, 100 + gap*(i+2), 375);
			String curTitle = categories[graphableCategories[counter]].getText();
			counter += 1;
		}
		g.setFont(sideFont);
		for(int i = 0; i < 11; i += 1){
			g.drawLine(97,380 - 33*i, 101, 380 - 33*i);			// draw ticks for y-axis and numbers for user
			g.drawString(Integer.toString(tick*i), 85-Integer.toString(tick*i).length()*8, 386 - 33*i);
		}
		for (int bar = 0; bar < barsToGraph; bar ++) {
			//calculate the current pixel height of the bar
			int curBarHeight = 330 * Integer.valueOf(quantities[graphableCategories[bar]].getText()) / ceiling;
			System.out.println((1+3*bar)*gap);
			g.setColor( colors.get(barColorBtns[graphableCategories[bar]].getText()) );
			g.fillRect(100 + (1+3*bar)*gap, 380-curBarHeight, 2*gap, curBarHeight);
		}
	}

	//subclass of JPanel with paintComponent method to display drawn items
	class DrawArea extends JPanel{

		public DrawArea (int w, int h){
			this.setPreferredSize(new Dimension(w, h)); //set preferred size
		}
		public void paintComponent (Graphics g){
			super.paintComponent(g);
			drawGraph(g);   // call draw graph method
		}
	}

	// returns the index of a value in a given array (used for color shifting bar upon button press)
	public int getIndex(String array[], String value) {
		for (int x = 0; x < array.length; x ++) 
			if (array[x].equals(value)) return x;
		return -1;
	}

	// handle button events
	public void actionPerformed(ActionEvent e) {
		String choice = e.getActionCommand();
		if(choice.equals("Graph!"))
		{
			DrawArea area = new DrawArea(600,400);	
			area = new DrawArea(600,400);	
			add(area, BorderLayout.EAST);			// add new graph on right
			validate();
			repaint();
		}

		else {		// not the graph button, but a color bar button
			JButton pressed = (JButton) e.getSource();		// store which button was pressed
			// shift the button text to the next color in the color array
			int curColID = getIndex(colorCycle, pressed.getText());
			pressed.setText(colorCycle[ (curColID+1)%colorCycle.length]);
		}
	}
	//////////////////////////////////////main method////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args){
		CH_BarGraph1 frame = new CH_BarGraph1() ;    
		frame.setVisible( true ); 
	}
}