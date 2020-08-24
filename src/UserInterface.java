import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class UserInterface implements ActionListener{

	private JFrame homeScreen;
	private JButton addZookeeper, updateZookeeperSalary, updateManagerSalary, deleteZookeeper, searchZookeeper, animalList, zookeeperList, speciesList, 
				feedingSchedule, leadManager, enter;
	private Database db;
	private JLabel results, labelID, labelSalary, labelFirstName, labelMiddleName, labelLastName;
	private JTextField inputID, inputSalary, inputFirstName, inputMiddleName, inputLastName;
	private String enterKey = "";
	private boolean queryRun = false;
	
	/* 
	 * Method creates the home screen for the GUI
	 * 
	 */
	public void createHomeScreen() {
		
		int screenWidth = 725;
		int screenHeight = 650;
		
		int buttonWidth = 150;
		int buttonHeight = 40;
		
		int resultsWidth = 700;
		int resultsHeight = 300;
		
		db = Database.getInstance();
		db.connect();
		System.out.println("connected");
		
		homeScreen = new JFrame( "Zoo Database" );
		homeScreen.setSize(screenWidth, screenHeight);
		homeScreen.setVisible(true);
		homeScreen.setLayout(new FlowLayout(FlowLayout.LEFT));
		homeScreen.setResizable(true);
		homeScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		homeScreen.getContentPane().setBackground(Color.lightGray);
		
		//database will disconnect when the window is closed
		homeScreen.addWindowListener(new WindowAdapter()
		{
		    @Override
		    public void windowClosing(WindowEvent e)
		    {
		        super.windowClosing(e);
		        db.disconnect();
		        System.out.println("disonnected");
		    }
		});
		
		//create the buttons for each query
		addZookeeper = new JButton("Add Zookeeper");
		addZookeeper.setSize(buttonWidth, buttonHeight);
		addZookeeper.setVisible(true);
		addZookeeper.addActionListener(this);
		
		updateZookeeperSalary = new JButton("Update Zoo Keeper Salary");
		updateZookeeperSalary.setSize(buttonWidth, buttonHeight);
		updateZookeeperSalary.setVisible(true);
		updateZookeeperSalary.addActionListener(this);
		
		updateManagerSalary = new JButton("Update Manager Salary");
		updateManagerSalary.setSize(buttonWidth, buttonHeight);
		updateManagerSalary.setVisible(true);
		updateManagerSalary.addActionListener(this);
		
		deleteZookeeper = new JButton("Delete Zookeeper");
		deleteZookeeper.setSize(buttonWidth, buttonHeight);
		deleteZookeeper.setVisible(true);
		deleteZookeeper.addActionListener(this);
		
		searchZookeeper = new JButton("Search Zookeeper");
		searchZookeeper.setSize(buttonWidth, buttonHeight);
		searchZookeeper.setVisible(true);
		searchZookeeper.addActionListener(this);
		
		animalList = new JButton("List Animals");
		animalList.setSize(buttonWidth, buttonHeight);
		animalList.setVisible(true);
		animalList.addActionListener(this);
		
		zookeeperList = new JButton("List Zookeepers");
		zookeeperList.setSize(buttonWidth, buttonHeight);
		zookeeperList.setVisible(true);
		zookeeperList.addActionListener(this);
		
		speciesList = new JButton("List Species");
		speciesList.setSize(buttonWidth, buttonHeight);
		speciesList.setVisible(true);
		speciesList.addActionListener(this);
		
		feedingSchedule = new JButton("Feeding Schedule");
		feedingSchedule.setSize(buttonWidth, buttonHeight);
		feedingSchedule.setVisible(true);
		feedingSchedule.addActionListener(this);
		
		leadManager = new JButton("Lead Manager");
		leadManager.setSize(buttonWidth, buttonHeight);
		leadManager.setVisible(true);
		leadManager.addActionListener(this);
		
		enter = new JButton("Enter");
		enter.setSize(buttonWidth, buttonHeight);
		enter.setVisible(false);
		enter.addActionListener(this);
		
		//create the display window with a scroll bar
		results = new JLabel();
		results.setPreferredSize(new Dimension(resultsWidth + 100, resultsHeight + 1000));
		JScrollPane display = new JScrollPane(results);
		display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		display.setPreferredSize(new Dimension(resultsWidth, resultsHeight));
		results.setVerticalAlignment(JLabel.TOP);
		
		//create input text fields
		labelID = new JLabel("Input ID: ");
		labelID.setVisible(false);
		inputID = new JTextField();
		inputID.setVisible(false);
		inputID.setColumns(10);
		
		labelFirstName = new JLabel("Input First Name: ");
		labelFirstName.setVisible(false);
		inputFirstName = new JTextField();
		inputFirstName.setVisible(false);
		inputFirstName.setColumns(10);
		
		labelMiddleName = new JLabel("Input Middle Name: ");
		labelMiddleName.setVisible(false);
		inputMiddleName = new JTextField();
		inputMiddleName.setVisible(false);
		inputMiddleName.setColumns(10);
		
		labelLastName = new JLabel("Input Last Name: ");
		labelLastName.setVisible(false);
		inputLastName = new JTextField();
		inputLastName.setVisible(false);
		inputLastName.setColumns(10);
		
		labelSalary = new JLabel("Input Salary: ");
		labelSalary.setVisible(false);
		inputSalary = new JTextField();
		inputSalary.setVisible(false);
		inputSalary.setColumns(10);
	
		homeScreen.add(addZookeeper);
		homeScreen.add(updateZookeeperSalary);
		homeScreen.add(updateManagerSalary);
		homeScreen.add(deleteZookeeper);
		homeScreen.add(searchZookeeper);
		homeScreen.add(animalList);
		homeScreen.add(zookeeperList);
		homeScreen.add(speciesList);
		homeScreen.add(feedingSchedule);
		homeScreen.add(leadManager);
		homeScreen.add(display);
		
		homeScreen.add(labelID);
		homeScreen.add(inputID);
		homeScreen.add(labelFirstName);
		homeScreen.add(inputFirstName);
		homeScreen.add(labelMiddleName);
		homeScreen.add(inputMiddleName);
		homeScreen.add(labelLastName);
		homeScreen.add(inputLastName);
		homeScreen.add(labelSalary);
		homeScreen.add(inputSalary);
		homeScreen.add(enter);
	
		homeScreen.setVisible(true);
		homeScreen.repaint();
			
	}
	
	/**
	 * Displays the searched for zoo keeper in the display window
	 */
	private void displaySearchedZookeeper() {
		String zookeeperID = inputID.getText();
		String output = db.searchZookeeper(zookeeperID);
		results.setText(output);
		
	}
	
	/**
	 * Displays all of the listed animals in the display window
	 */
	private void displayListedAnimals() {
		String displayInfo = "<html>";
		ArrayList<String> animals = db.listAnimals();
		
		for( String animal: animals ) {
			displayInfo += animal + "<br/>";
		}
		
		displayInfo += "</html>";
		results.setText(displayInfo);

	}
	
	/**
	 * Displays all listed zoo keepers in the display window
	 */
	private void displayListedZookeepers() {
		String displayInfo = "<html>";
		ArrayList<String> zookeepers = db.listZookeepers();
		
		for(String zookeeper: zookeepers) {
			displayInfo += zookeeper + "<br/>";
		}
		
		displayInfo += "</html>";
		results.setText(displayInfo);
		
	}
	
	/**
	 * Adds the new zookeeper to the database and displays a confirmation message to 
	 * the display window
	 */
	private void displayAddedZookeeper() {
		String zookeeperID = inputID.getText();
		String firstName = inputFirstName.getText();
		String middleName = inputMiddleName.getText();
		String lastName = inputLastName.getText();
		double salary = Double.parseDouble(inputSalary.getText());
		
		if(middleName.equals("")) {
			db.addZookeeper(zookeeperID, firstName, lastName, salary);
			
		}
		
		db.addZookeeper(zookeeperID, firstName, middleName, lastName, salary);
		results.setText(firstName + " " + middleName + " " + lastName + " has been added");
		
	}
	
	/**
	 * Deletes the zoo keeper from the database and displays a confirmation message to
	 * the display window
	 */
	private void displayDeletedZookeeper() {
		String zookeeperID = inputID.getText();
		db.deleteZookeeper(zookeeperID);
		results.setText("The zoo keeper has been deleted.");
	}
	
	/**
	 * Updates the salary of the zoo keeper and displays a confirmation message to the 
	 * display window
	 */
	private void displayUpdatedZookeeperSalary() {
		String zookeeperID = inputID.getText();
		double salary = Double.parseDouble(inputSalary.getText());
		
		db.updateZookeeperSalary(zookeeperID, salary);
		results.setText("The zoo keeper's salary has been updated to $" + salary);
	}
	
	private void displayUpdatedManagerSalary() {
		String managerID = inputID.getText();
		double salary = Double.parseDouble(inputSalary.getText());
		
		db.updateManagerSalary(managerID, salary);
		results.setText("The manager's salary has been updated to $" + salary);
	}
	
	/**
	 * Displays all listed animal species to the display window
	 */
	private void displayListedSpecies() {
		String displayInfo = "<html>";
		ArrayList<String> animalSpecies = db.listAnimalSpecies();
		
		for(String species: animalSpecies) {
			displayInfo += species + "<br/>";
		}
		
		displayInfo += "</html>";
		results.setText(displayInfo);
	}
	
	/**
	 * Displays the feeding schedule of the zoo to the display window
	 */
	private void displayFeedingSchedule() {
		String displayInfo = "<html>";
		ArrayList<String> feedingSchedule = db.feedingSchedule();
		
		for(String animalFeedingTime: feedingSchedule) {
			displayInfo += animalFeedingTime + "<br/>";
		}
		
		displayInfo += "</html>";
		results.setText(displayInfo);
	}
	
	/**
	 * Displays the head manager to the display window
	 */
	private void displayHeadManager() {
		String output = db.headManager();
		results.setText(output);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == addZookeeper) {

			labelID.setVisible(true);
			inputID.setVisible(true);
			labelFirstName.setVisible(true);
			inputFirstName.setVisible(true);
			labelMiddleName.setVisible(true);
			inputMiddleName.setVisible(true);
			labelLastName.setVisible(true);
			inputLastName.setVisible(true);
			labelSalary.setVisible(true);
			inputSalary.setVisible(true);
			enter.setVisible(true);
			if( queryRun ) {
				results.setText("");
			}
			
			enterKey = "Add Zookeeper";
			queryRun = true;
			
		} else if(e.getSource() == updateZookeeperSalary) {
			
			labelID.setVisible(true);
			inputID.setVisible(true);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(true);
			inputSalary.setVisible(true);
			enter.setVisible(true);
			if( queryRun ) {
				results.setText("");
			}
			
			enterKey = "Update Zookeeper Salary";
			queryRun = true;
			
		} else if(e.getSource() == updateManagerSalary) {
			
			labelID.setVisible(true);
			inputID.setVisible(true);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(true);
			inputSalary.setVisible(true);
			enter.setVisible(true);
			if( queryRun ) {
				results.setText("");
			}
			
			enterKey = "Update Manager Salary";
			queryRun = true;
			
		} else if(e.getSource() == deleteZookeeper) {
		
			labelID.setVisible(true);
			inputID.setVisible(true);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(false);
			inputSalary.setVisible(false);
			enter.setVisible(true);
			if( queryRun ) {
				results.setText("");
			}
			
			enterKey = "Delete Zookeeper";
			queryRun = true;
			
		} else if(e.getSource() == searchZookeeper) {
			
			labelID.setVisible(true);
			inputID.setVisible(true);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(false);
			inputSalary.setVisible(false);
			enter.setVisible(true);
			if( queryRun ) {
				results.setText("");
			}
			
			enterKey = "Search Zookeeper";
			queryRun = true;
			
		} else if(e.getSource() == animalList) {
			displayListedAnimals();
			labelID.setVisible(false);
			inputID.setVisible(false);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(false);
			inputSalary.setVisible(false);

			enter.setVisible(false);
			queryRun = true;
			
		} else if(e.getSource() == zookeeperList) {
			displayListedZookeepers();
			labelID.setVisible(false);
			inputID.setVisible(false);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(false);
			inputSalary.setVisible(false);

			enter.setVisible(false);
			queryRun = true;

			
		} else if(e.getSource() == speciesList) {
			displayListedSpecies();
			labelID.setVisible(false);
			inputID.setVisible(false);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(false);
			inputSalary.setVisible(false);

			enter.setVisible(false);
			queryRun = true;
			
		} else if(e.getSource() == feedingSchedule) {
			displayFeedingSchedule();
			labelID.setVisible(false);
			inputID.setVisible(false);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(false);
			inputSalary.setVisible(false);

			enter.setVisible(false);
			queryRun = true;
			
		} else if(e.getSource() == leadManager) {
			displayHeadManager();
			labelID.setVisible(false);
			inputID.setVisible(false);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(false);
			inputSalary.setVisible(false);
			
			enter.setVisible(false);
			queryRun = true;
			
		} else if(e.getSource() == enter) {
			
			if(enterKey.equals("Add Zookeeper")) {
				displayAddedZookeeper();
			} else if(enterKey.equals("Update Zookeeper Salary")) {
				displayUpdatedZookeeperSalary();
			} else if(enterKey.equals("Update Manager Salary")) {
				displayUpdatedManagerSalary();
			} else if(enterKey.equals("Delete Zookeeper")) {
				displayDeletedZookeeper();
			} else if(enterKey.equals("Search Zookeeper")) {
				displaySearchedZookeeper();
			}
			
			labelID.setVisible(false);
			inputID.setVisible(false);
			labelFirstName.setVisible(false);
			inputFirstName.setVisible(false);
			labelMiddleName.setVisible(false);
			inputMiddleName.setVisible(false);
			labelLastName.setVisible(false);
			inputLastName.setVisible(false);
			labelSalary.setVisible(false);
			inputSalary.setVisible(false);
			enter.setVisible(false);
			
		}
		
	}

}
