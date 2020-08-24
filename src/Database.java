import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Database class that handles connecting and disconnecting to the database along with
 * the methods for all queries that can be run on the database.
 * 
 * Last Modified: 7/28/2020
 * @author Chris Hoffman
 *
 */
public class Database {

	private String url = "jdbc:sqlite:C:\\Users\\Chris\\Documents\\Cs364\\Project\\Zoo.db";
	private Connection connection;
	private static final Database INSTANCE = new Database();
	
	/**
	 * Private constructor for the database class
	 */
	private Database() {
		
	}
	
	/**
	 * Method returns the static INSTANCE variable ensuring only one connection to the 
	 * database at a time. 
	 * 
	 * @return INSTANCE
	 */
	public static Database getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Connects the user to the database
	 * 
	 * @throws SQLException
	 */
	public void connect() {
		try {
			connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println("There was an issue connecting...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Disconnects the user from the database
	 * 
	 * @throws SQLException
	 */
	public void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	/*
	 * The following methods will be for the user interacting with the database. 
	 * They will consist of querying, updating, inserting, and deleting data. 
	 */
	
	/**
	 * Method will add a new zoo keeper to the database
	 * 
	 * @param zookeeperID
	 * 				The ID number for the new zoo keeper beginning with a 'Z' followed by a 
	 * 				6 digit number
	 * @param firstName
	 * 				The first name of the zoo keeper
	 * @param middleName
	 * 				The middle name of the zoo keeper
	 * @param lastName
	 * 				The last name of the zoo keeper
	 * @param salary
	 * 				The salary for the zoo keeper
	 */
	public void addZookeeper(String zookeeperID, String firstName, String middleName, String lastName, double salary ) {
		String sql = "INSERT INTO Zookeeper(ZookeeperID, FirstName, MiddleName, Lastname, Salary) VALUES(?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, zookeeperID);
			statement.setString(2, firstName);
			statement.setString(3, middleName);
			statement.setString(4, lastName);
			statement.setDouble(5, salary);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assignZookeeperManager(zookeeperID);
	}
	
	/**
	 * Method will add a new zoo keeper to the database
	 * 
	 * @param zookeeperID
	 * 				The ID number for the new zoo keeper beginning with a 'Z' followed by a 
	 * 				6 digit number
	 * @param firstName
	 * 				The first name of the zoo keeper
	 * @param lastName
	 * 				The last name of the zoo keeper
	 * @param salary
	 * 				The salary for the zoo keeper
	 */
	public void addZookeeper(String zookeeperID, String firstName, String lastName, double salary) {
		String sql = "INSERT INTO Zookeeper(ZookeeperID, FirstName, Lastname, Salary) VALUES(?, ?, ?, ?)";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, zookeeperID);
			statement.setString(2, firstName);
			statement.setString(3, lastName);
			statement.setDouble(4, salary);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assignZookeeperManager(zookeeperID);
	}
	
	/**
	 * Helper method for assigning a newly added zoo keeper to a manager. 
	 * Zoo keeper will be assigned to the manager with the least amount of zoo keepers.
	 * 
	 * @param zookeeperID
	 * 				The zoo keeper ID for the zoo keeper that was added
	 */
	private void assignZookeeperManager(String zookeeperID) {
		String sql = "INSERT INTO Reports_To(ZookeeperID, ManagerID) VALUES(?, ?)";
		String manager = findManagerWithLeastZookeepers();
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, zookeeperID);
			statement.setString(2,  manager);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Finds the manager with the least number of zoo keepers under them and 
	 * returns their Manager ID
	 * 
	 * @return the Manager ID of the manager with the least number of zoo keepers under them
	 * 
	 */
	private String findManagerWithLeastZookeepers() {
		String toReturn = "";
		String sql = "SELECT Manager.ManagerID FROM MANAGER JOIN Reports_To JOIN Zookeeper ON Manager.ManagerID = Reports_To.ManagerID AND Reports_To.ZookeeperID = Zookeeper.ZookeeperID GROUP BY Manager.ManagerID ORDER BY count(*) ASC LIMIT 1";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet results = statement.executeQuery();
			toReturn = results.getString("ManagerID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return toReturn;
	}
	
	/**
	 * Method will delete a zoo keeper from the database
	 * 
	 * @param zookeeperID
	 * 				The ID number for the zoo keeper beginning with a 'Z' followed by a 
	 * 				6 digit number
	 */
	public void deleteZookeeper(String zookeeperID) {
		String sql = "DELETE FROM Zookeeper WHERE ZookeeperID = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, zookeeperID);
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		unassignZookeeperManager(zookeeperID);
	}
	
	/**
	 * Helper method for unassigning a zoo keeper that has been deleted from their manager.
	 * 
	 * @param zookeeperID
	 * 				The zoo keeper ID for the zoo keeper that was deleted
	 */
	private void unassignZookeeperManager(String zookeeperID) {
		String sql = "DELETE FROM Reports_To WHERE ZookeeperID = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, zookeeperID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method will update a zoo keeper's salary
	 * 
	 * @param zookeeperID
	 * 				The ID number for the zoo keeper beginning with a 'Z' followed by a 
	 * 				6 digit number
	 * @param salary
	 * 				The new salary for the zookeeper
	 */
	public void updateZookeeperSalary(String zookeeperID, double salary) {
		String sql = "UPDATE Zookeeper SET Salary = ? WHERE ZookeeperID = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, salary);
			statement.setString(2, zookeeperID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method will update a managers salary
	 * 
	 * @param managerID
	 * 				The ID number for the manager beginning with an 'M' followed by a 5 digit number
	 * 
	 * @param salary
	 * 				The new salary for the manager
	 */
	public void updateManagerSalary(String managerID, double salary) {
		String sql = "UPDATE Manager SET Salary = ? WHERE ManagerID = ?";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, salary);
			statement.setString(2, managerID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method finds a zoo keeper and returns their name, salary, feeding times,
	 * and the animals they feed
	 * 
	 * @param zookeeperID
	 * 				The ID number for the zoo keeper beginning with a 'Z' followed by a 
	 * 				6 digit number
	 * 
	 * @return the zoo keeper's info
	 */
	public String searchZookeeper(String zookeeperID) {
		String sql = "SELECT Zookeeper.FirstName, Zookeeper.MiddleName, Zookeeper.LastName, Zookeeper.Salary, Manager.LastName AS Manager FROM Zookeeper JOIN Reports_To JOIN Manager ON Zookeeper.ZookeeperID = Reports_To.ZookeeperID AND Reports_To.ManagerID = Manager.ManagerID WHERE Zookeeper.ZookeeperID = ?";
		String toReturn = "";
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, zookeeperID);
			ResultSet results = statement.executeQuery();
			
			String firstName = results.getString("FirstName");
			String middleName = results.getString("MiddleName");
			String lastName = results.getString("LastName");
			double salary = results.getDouble("Salary");
			String manager = results.getString("Manager");
			
			toReturn = "First Name: " + firstName + "| Middle Name: " + middleName + "| Last Name: " + lastName + "| Salary: " + salary + "| Manager: " + manager;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return toReturn;
	}
	
	/**
	 * Method returns a list of all animals ordered alphabetically by their species and their names
	 * 
	 * @return an ArrayList of each animals info
	 */
	public ArrayList<String> listAnimals() {
		String sql = "SELECT Animal.Species, Animal.AnimalName, Habitat.HabitatID FROM Animal JOIN Lives_In JOIN Habitat ON Animal.AnimalID = Lives_In.AnimalID AND Lives_In.HabitatID = Habitat.HabitatID ORDER BY Species ASC, AnimalName ASC";
		ArrayList<String> animals = new ArrayList<>();
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet results = statement.executeQuery();
			
			while(results.next()) {
				String species = results.getString("Species");
				String animalName = results.getString("AnimalName");
				String habitatID = results.getString("HabitatID");
				
				animals.add(" Species: " + species + "| Name: " + animalName + "| Habitat: " + habitatID);
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return animals;
	}
	
	/**
	 * Method returns a list of all zoo keepers ordered alphabetically by their first and last names
	 * 
	 */
	public ArrayList<String> listZookeepers() {
		String sql = "SELECT ZookeeperID, FirstName, MiddleName, LastName, Salary FROM Zookeeper ORDER BY ZookeeperID";
		ArrayList<String> zookeepers = new ArrayList<>();
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet results = statement.executeQuery();
			
			while(results.next()) {
				String zookeeperID = results.getString("ZookeeperID");
				String firstName = results.getString("FirstName");
				String middleName = results.getString("MiddleName");
				String lastName = results.getString("LastName");
				double salary = results.getDouble("Salary");
				
				zookeepers.add("ZookeeperID: " + zookeeperID + "| First Name: " + firstName + "| Middle Name: " + middleName + "| Last Name: " + lastName + " | Salary: " + salary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return zookeepers;
	}
	
	/**
	 * Method displays a list of all animal species at the zoo, the number of animals at the zoo
	 * of that species, and the name of the exhibit that species is part of. 
	 * Will be ordered alphabetically by species.
	 * 
	 */
	public ArrayList<String> listAnimalSpecies() {
		String sql = "SELECT Species, count(*) AS NumAnimals, Exhibit.ExhibitName FROM Animal JOIN Lives_In JOIN Habitat JOIN Part_Of JOIN Exhibit ON Animal.AnimalID = Lives_In.AnimalID AND Lives_In.HabitatId = Habitat.HabitatID AND Habitat.HabitatID = Part_Of.HabitatID AND Part_Of.ExhibitID = Exhibit.ExhibitID GROUP BY Species ORDER BY Species";
		ArrayList<String> speciesInfo = new ArrayList<>();
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet results = statement.executeQuery();
			
			while(results.next()) {
				String species = results.getString("Species");
				int numAnimals = results.getInt("NumAnimals");
				String exhibitName = results.getString("ExhibitName");
				
				speciesInfo.add("Species: " + species + "| Number Of Animals: " + numAnimals + "| Exhibit: " + exhibitName);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return speciesInfo;
	}
	
	/**
	 * Method displays a feeding schedule for the animals. This includes returning the list of all animals, 
	 * their names, their feeding time, the zoo keeper that feeds them, and the time they are fed. The list will 
	 * be ordered by the feeding time.
	 * 
	 */
	public ArrayList<String> feedingSchedule() {
		String sql = "SELECT FeedingTime, Animal.AnimalName, Habitat.HabitatID, Zookeeper.ZookeeperID FROM Zookeeper JOIN Feeds JOIN Animal JOIN Lives_In JOIN Habitat ON Zookeeper.ZookeeperID = Feeds.ZookeeperID AND Feeds.AnimalID = Animal.AnimalID AND Animal.AnimalID = Lives_In.AnimalID AND Lives_In.HabitatID = Habitat.HabitatID ORDER BY FeedingTime ASC";
		ArrayList<String> feedingInfo = new ArrayList<String>();
		
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet results = statement.executeQuery();
			
			while(results.next()) {
				String feedingTime = results.getString("FeedingTime");
				String animalName = results.getString("AnimalName");
				String habitatID = results.getString("HabitatID");
				String zookeeperID = results.getString("ZookeeperID");
				
				feedingInfo.add("Feeding Time: " + feedingTime + "| Animal Name: " + animalName + "| Habitat ID: " + habitatID + "| Zoo Keeper ID: " + zookeeperID);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return feedingInfo;
		
	}
	
	/**
	 * Method displays a list of all managers, their names, their salary, 
	 * the number of zoo keepers they manage, the average salary of the zoo keepers they manage, 
	 * and the name of the zoo keeper with the highest salary of all the zoo keepers each manager
	 * manages. 
	 * 
	 */
	public String headManager() {
		String sql = "SELECT Managers.LastName, Managers.Salary, NumZookeepers, AvgZookeeperSalary, HeadZookeeper FROM (SELECT Manager.ManagerID, Manager.LastName, Manager.Salary, Zookeeper.LastName AS HeadZookeeper, count(*) AS NumZookeepers, avg(Zookeeper.Salary) AS AvgZookeeperSalary FROM Zookeeper JOIN Reports_To JOIN Manager ON Zookeeper.ZookeeperID = Reports_To.ZookeeperID AND Reports_To.ManagerID = Manager.ManagerID GROUP BY Manager.ManagerID ) AS Managers ORDER BY Salary DESC LIMIT 1";
		String toReturn = "";
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet results = statement.executeQuery();
			
			while(results.next()) {
				String manager = results.getString("LastName");
				double managerSalary = results.getDouble("Salary");
				int numZookeepers = results.getInt("NumZookeepers");
				double avgZookeeperSalary = results.getDouble("AvgZookeeperSalary");
				String headZookeeper = results.getString("HeadZookeeper");
				
				toReturn = "Manager: " + manager + "| Manager Salary: " + managerSalary + "| Number Of Zoo Keepers: " + numZookeepers + "| Average Zoo Keeper Salary: " + avgZookeeperSalary + "| Head Zoo Keeper: " + headZookeeper;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return toReturn;
	}
	
	
	
}
