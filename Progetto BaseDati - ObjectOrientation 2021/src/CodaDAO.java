import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CodaDAO {
	
	/**
	 * Insert a queue in the database
	 * @param mainFrame Link to the MainFrame
	 * @param c Queue to add in the database
	 * @param id ID of the flights to link with this queue
	 * @return If the insert operation was successful
	 */
	public boolean insertCoda(MainFrame mainFrame, Coda c, String id) {
		
		String tipo = c.getTipo(); //Get queue's type
		int lunghezza = c.getPersoneInCoda(); //Get queue's amount of people in the queue
		
		try {
			
			String q = "INSERT INTO coda(tipo, lunghezza, IDVolo)\r\n" + 
					"VALUES ('" + tipo + "','" + lunghezza + "','" + id + "');"; //Initialize query
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
	 * Remove queues of a flight in the database
	 * @param mainFrame Link to the MainFrame
	 * @param id ID of the flight that the user wants to remove the queue of
	 * @return If the delete operation was successful
	 */
	public boolean removeCode(MainFrame mainFrame, String id) {
		
		try {
			
			String q = "DELETE FROM coda where IDVolo = '" + id + "'"; //Initialize query
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
	 * Get a list of the queues of a specified Flight
	 * @param con Connection to the database
	 * @param st Statement of the database
	 * @param ID ID of the flight the user wants the queue of
	 * @return List of queues of the specified Flight
	 */
	public ArrayList<Coda> getQueueListByID(Connection con, Statement st, String ID){
		
		try {
			
			String q = "Select * from coda where IDVolo = '" + ID + "'"; //Initialize query

			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Coda> list = new ArrayList<Coda>(); //Initialize a list of queues
			
			//Get results and store them in the list
			while(rs.next()) {
				String tipo = rs.getString("tipo"); //Company name
				int lunghezza = rs.getInt("lunghezza"); //Get flights number
				
				//Create queue
				Coda c = new Coda();
				c.setTipo(tipo);
				c.setPersoneInCoda(lunghezza);
				list.add(c);
			}
			
			return list; //Return list
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}

}