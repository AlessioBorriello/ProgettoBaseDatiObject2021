import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SlotDAO {
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //Date format
	
	/**
	 * Insert a slot in the database
	 * @param mainFrame Link to the mainFrame
	 * @param s Slot to add in the database
	 * @param id ID of the flight linked to this slot
	 * @return If the insert operation was successful
	 */
	public boolean insertSlot(MainFrame mainFrame, Slot s, String id) {
		
		//Convert dates to a string format
		String inizioTempoStimato = dateTimeFormat.format(s.getInizioTempoStimato());
		String fineTempoStimato = dateTimeFormat.format(s.getFineTempoStimato());
		
		
		try {
			
			String q = "INSERT INTO slot(IDVolo, inizioTempoStimato, fineTempoStimato)\r\n" + 
					"VALUES ('" + id + "','" + inizioTempoStimato + "','" + fineTempoStimato + "');"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Update a slot in the database
	 * @param mainFrame Link to the mainFrame
	 * @param newSlot Instance of the updated slot
	 * @param id ID of the flight to link the slot to
	 * @return If the update operation was successful
	 */
	public boolean updateSlot(MainFrame mainFrame, Slot newSlot, String id) {
		
		//Convert dates to a string format
		String inizioTempoStimato = dateTimeFormat.format(newSlot.getInizioTempoStimato());
		String fineTempoStimato = dateTimeFormat.format(newSlot.getFineTempoStimato());
		
		try {
			
			String q = "UPDATE slot SET inizioTempoStimato = '" + inizioTempoStimato + "', fineTempoStimato = '" + fineTempoStimato + "' WHERE IDVolo = '" + id + "'"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Get a slot instance by it's flight's id
	 * @param ID ID of the flight the slot is linked to
	 * @return The slot linked to the given id
	 */
	public Slot getSlotByID(String ID) {
		
		try {
			
			String q = "Select * from slot where IDVolo = '" + ID + "'"; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			if(rs.next()) { //Slot found
				
				Slot s = new Slot();
				s.setInizioTempoStimato(rs.getTimestamp("inizioTempoStimato"));
				s.setFineTempoStimato(rs.getTimestamp("fineTempoStimato"));
				s.setInizioTempoEffettivo(rs.getTimestamp("inizioTempoEffettivo"));
				s.setFineTempoEffettivo(rs.getTimestamp("fineTempoEffettivo"));
				
				return s;
				
			}else { //Slot not found
				
				con.close(); //Close connection
				st.close(); //Close statement
				
				return null; //Return null
			}
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}
	
	/**
	 * Update the effective time in a given slot
	 * @param mainFrame Link to the mainFrame
	 * @param s Slot with the updated effective time
	 * @param id ID of the flight to link the slot to
	 * @return If the update operation was successful
	 */
	public boolean updateTempoEffettivo(MainFrame mainFrame, Slot s, String id) {
		
		//Convert dates to a string format
		String inizioTempoEffettivo = dateTimeFormat.format(s.getInizioTempoEffettivo());
		String fineTempoEffettivo = dateTimeFormat.format(s.getFineTempoEffettivo());
		
		try {
			
			String q = "UPDATE slot SET inizioTempoEffettivo = '" + inizioTempoEffettivo + "', fineTempoEffettivo = '" + fineTempoEffettivo + "' WHERE IDVolo = '" + id + "'"; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			return true; //Operation successful
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return false; //Operation failed
		}
		
	}

}
