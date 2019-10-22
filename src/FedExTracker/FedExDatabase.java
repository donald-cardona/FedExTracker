package FedExTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class FedExDatabase {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Random rand;
	private Calendar startDate;
	private Calendar endDate;
	private long randomEpochDay;
	private FedExDistCenters distCenters;
	
	final private String host = "localhost";
	final private String user = "Donald";
	final private String passwd = "dcardon3";
	
	public FedExDatabase() {
		rand = new Random();
		startDate = Calendar.getInstance();
		startDate.set(2019, 1, 1, 0, 0);
		endDate = Calendar.getInstance();
		endDate.set(2019, 10, 1, 23, 59);
		randomEpochDay = 0;
		distCenters = new FedExDistCenters();
	}
	
	public void addOrder() throws Exception {
		try {
			Stack<Node> stack = new Stack<Node>();
			Node node = distCenters.getNode(rand.nextInt(25));
			int count = 0;
			int numDates = 2;
			String[] dates = new String[17];
			String[] locations = new String[6];
			DateFormat df = new SimpleDateFormat("EEEE MM/dd/yyyy hh:mm a");
			Calendar cal = Calendar.getInstance();
			randomEpochDay = ThreadLocalRandom.current().nextLong(startDate.getTimeInMillis(), endDate.getTimeInMillis());
			cal.setTimeInMillis(randomEpochDay);
			int trackNum = rand.nextInt(90000000) +  10000000;
			
			while(node != null) {
				stack.push(node);
				node = node.getPrevNode();
				count++;
				numDates += 2;
			}
			for(int i = 0; i < locations.length; i++) {
				if(i < count)
					locations[i] = stack.pop().getLocation();
				else
					locations[i] = "";
			}
			for(int j = 0; j < dates.length; j++) {
				if(j < numDates) {
					dates[j] = df.format(cal.getTime());
					cal.add(Calendar.HOUR, rand.nextInt(12));
					cal.add(Calendar.MINUTE, rand.nextInt(60));
				}
				else if(j >= 14) {
					dates[j] = df.format(cal.getTime());
					cal.add(Calendar.HOUR, rand.nextInt(12));
					cal.add(Calendar.MINUTE, rand.nextInt(60));
				}
				else
					dates[j] = "";
			}
			
			// This will load the MySQL driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Setup the connection with the DB
			connect = DriverManager
				.getConnection("jdbc:mysql://" + host + "/feedback?"
					+ "user=" + user + "&password=" + passwd);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.
				prepareStatement("insert into FedEx.Orders values (?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			// (Track Number, Weight, #Pieces, Dimensions, Datum1A, Datum1B, Datum1C, Datum1D
			// Datum2A, Datum2B, Datum3A, Datum3B, Datum4A, Datum4B, Datum5A, Datum5B, Datum6A,
			// Datum6B, Datum6C, Datum6D, Datum6E, StartLoc, Loc2, Loc3, Loc4, Loc6, Loc6) 
			// Parameter starts with 1
			preparedStatement.setLong(1, trackNum);
			preparedStatement.setDouble(2, rand.nextInt(100) + 1);
			preparedStatement.setInt(3, rand.nextInt(10) + 1);
			preparedStatement.setString(4, (rand.nextInt(99) + 1) + "x" + (rand.nextInt(99) + 1) + "x" + 
				(rand.nextInt(99) + 1));
			// Inserting dates using for loop
			for(int k = 0; k < dates.length; k++) {
				preparedStatement.setString(k + 5, dates[k]);	// Parameters 5-21 hold dates
			}
			// Inserting locations using for loop
			for(int m = 0; m < locations.length; m++) {
				preparedStatement.setString(m + 22, locations[m]);	// Parameters 22-27 hold locations
			}
			preparedStatement.executeUpdate();
			
			System.out.println("Order has been added, your track number is: " + trackNum + "\n");
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			close();
		}
	}
	
	public void displayOrder(long trackNum) throws Exception {
		try {
			boolean orderExist = false;
			
			// This will load the MySQL driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Setup the connection with the DB
			connect = DriverManager
				.getConnection("jdbc:mysql://" + host + "/feedback?"
					+ "user=" + user + "&password=" + passwd);	
			
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			
			// ResultSet gets the result of the SQL query
			resultSet = statement.executeQuery("Select tracknumber from fedex.orders ;");
			while(resultSet.next()) {
				if(trackNum == resultSet.getLong("tracknumber"))
					orderExist = true;
			}
			if(orderExist == false) {
				System.out.println("Order does not exist, returning to menu.\n");
				close();
				return;
			}
			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.
				prepareStatement("select * from FedEx.orders where tracknumber = ? ;");
			preparedStatement.setLong(1, trackNum);
			
			// ResultSet gets the result of the SQL query
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);

		}
		catch(Exception e) {
			throw e;
		}
		finally {
			close();
		}
	}
	
	public void loadTrackNumbers() throws Exception {
		try {
			// This will load the MySQL driver
			Class.forName("com.mysql.cj.jdbc.Driver");
						
			// Setup the connection with the DB
			connect = DriverManager
				.getConnection("jdbc:mysql://" + host + "/feedback?"
					+ "user=" + user + "&password=" + passwd);	
						
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			
			// ResultSet gets the result of the SQL query
			resultSet = statement.executeQuery("SELECT TRACKNUMBER from fedex.orders;");
			
			writeMetaData(resultSet);
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			close();
		}
	}
	
	public void writeResultSet(ResultSet resultSet) throws SQLException, Exception {
		String[] date = new String[17];
		String[] locations = new String[6];
		int endLocIndex = 0;
		
		while(resultSet.next()) {
			long trackNum = resultSet.getLong("tracknumber");
			double weight = resultSet.getInt("weight");
			int pieces = resultSet.getInt("pieces");
			String dimension = resultSet.getString("dimensions");
			date[0] = resultSet.getString("datum1a");
			date[1] = resultSet.getString("datum1b");
			date[2] = resultSet.getString("datum1c");
			date[3] = resultSet.getString("datum1d");
			date[4] = resultSet.getString("datum2a");
			date[5] = resultSet.getString("datum2b");
			date[6] = resultSet.getString("datum3a");
			date[7] = resultSet.getString("datum3b");
			date[8] = resultSet.getString("datum4a");
			date[9] = resultSet.getString("datum4b");
			date[10] = resultSet.getString("datum5a");
			date[11] = resultSet.getString("datum5b");
			date[12] = resultSet.getString("datum6a");
			date[13] = resultSet.getString("datum6b");
			date[14] = resultSet.getString("datum6c");
			date[15] = resultSet.getString("datum6d");
			date[16] = resultSet.getString("datum6e");
			locations[0] = resultSet.getString("startloc");
			locations[1] = resultSet.getString("loc2");
			locations[2] = resultSet.getString("loc3");
			locations[3] = resultSet.getString("loc4");
			locations[4] = resultSet.getString("loc5");
			locations[5] = resultSet.getString("loc6");
			
			

			System.out.println("\n"+ date[0] + " : In FedEx Possesion\t\t\t\t\t\t\t" + locations[0]);
			System.out.println("\n" + date[1] + " : Item(s) picked up and shipment information sent to FedEx\t\t\t" + locations[0]);
			for(int i = 0; i < locations.length; i++) {
				if(!locations[i].equalsIgnoreCase("")) {
					System.out.println("\n" + date[i+2]+ " : Arrived at FedEx location\t\t\t\t\t\t\t" + locations[i]);
					System.out.println("\n" + date[i+3] + " : Departed at FedEx location\t\t\t\t\t\t\t" + locations[i]);
					endLocIndex = i;
				}
			}
			System.out.println("\n" + date[14] + " : At local FedEx facilty\t\t\t\t\t\t\t" + locations[endLocIndex]);
			System.out.println("\n" + date[15] + " : On FedEx vehicle for delivery\t\t\t\t\t\t" + locations[endLocIndex]);
			System.out.println("\n" + date[16] + " : Delivered\t\t\t\t\t\t\t\t" + locations[endLocIndex] + "\n\n");
			
			
			System.out.println("\n\tShipment Facts\n");
			System.out.println("Tracking Number: " + trackNum);
			System.out.println("Weight: "+ weight + " lbs / "+ (weight * 0.454) + " kgs");
			System.out.println("Signature Services: Direct Signature Required");
			System.out.println("Packaging: Package");
			System.out.println("Service: FedEx Home Delivery");
			System.out.println("Dimensions: " + dimension + " in.");
			System.out.println("Total pieces: " + pieces);
			System.out.println("Special Handling Section: Direct Signature Required\n");
		}
	}
	
	public void writeMetaData(ResultSet resultSet) throws SQLException {
		System.out.println("\nTrack Numbers in Database Currently:");
		while(resultSet.next()) {
			System.out.println(resultSet.getString("tracknumber"));
		}
		System.out.println("\n");
	}
	
	public void close() {
		try {
			if (resultSet != null)
				resultSet.close();
			
			if (statement != null)
				statement.close();
			
			if (connect != null)
				connect.close();
		} 
		catch (Exception e) {
			
		}	
	}
}
