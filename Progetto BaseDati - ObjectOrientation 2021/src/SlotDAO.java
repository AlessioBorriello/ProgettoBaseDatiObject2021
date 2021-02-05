import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SlotDAO {
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public boolean insertSlot(MainFrame mainFrame, Slot s, String id) {
		
		String inizioTempoStimato = dateTimeFormat.format(s.getInizioTempoStimato());
		String fineTempoStimato = dateTimeFormat.format(s.getFineTempoStimato());
		
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
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
	
	public boolean updateSlot(MainFrame mainFrame, Slot newSlot, String id) {
		
		String inizioTempoStimato = dateTimeFormat.format(newSlot.getInizioTempoStimato());
		String fineTempoStimato = dateTimeFormat.format(newSlot.getFineTempoStimato());
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
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
	
	public Slot getSlotByID(String ID) {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
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
	
	public boolean updateTempoEffettivo(MainFrame mainFrame, Slot s, String id) {
		
		String inizioTempoEffettivo = dateTimeFormat.format(s.getInizioTempoEffettivo());
		String fineTempoEffettivo = dateTimeFormat.format(s.getFineTempoEffettivo());
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
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
