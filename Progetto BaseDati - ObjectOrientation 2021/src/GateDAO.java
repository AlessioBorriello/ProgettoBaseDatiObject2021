import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GateDAO {
	
	/**
	 * Insert a gate into the database
	 * @param mainFrame Link to the mainFrame
	 * @param g Gate instance to add
	 * @param id ID of the flight to link the gate to
	 * @return If the insert operation was successful
	 */
	public boolean insertGate(MainFrame mainFrame, Gate g, String id) {
		
		int numeroGate = g.getNumeroGate();
		
		try {
			
			String q = "INSERT INTO gate(IDVolo, numeroGate)\r\n" + 
					"VALUES ('" + id + "','" + numeroGate + "');"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Insert queues
			CodaDAO dao = new CodaDAO();
			for(Coda c : g.getListaCode()) {
				if(!dao.insertCoda(mainFrame, c, id)) {
					//One of the queues has not been inserted, abort insertion
					return false;
				}
			}
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Update a gate into the database
	 * @param mainFrame Link to the mainFrame
	 * @param newGate Instance of the updated gate
	 * @param id ID of the flight to link the gate to
	 * @return If the update operation was successful
	 */
	public boolean updateGate(MainFrame mainFrame, Gate newGate, String id) {
		
		int numeroGate = newGate.getNumeroGate();
		
		try {
			
			String q = "UPDATE gate SET numeroGate = '" + numeroGate + "' WHERE IDVolo = '" + id + "'"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Remove all old gate's queues
			CodaDAO removeDAO = new CodaDAO();
			removeDAO.removeCode(mainFrame, id);
			
			//Insert new queues of the updated gate
			CodaDAO insertDAO = new CodaDAO();
			for(Coda c : newGate.getListaCode()) {
				insertDAO.insertCoda(mainFrame, c, id);
			}
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Get a gate instance by it's flight's id
	 * @param con Connection to the database
	 * @param ID ID of the flight the gate is linked to
	 * @return The gate linked to the given id
	 */
	public Gate getGateByID(Connection con, String ID) {
		
		try {
			
			String q = "Select * from gate where IDVolo = '" + ID + "'"; //Initialize query

			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			if(rs.next()) { //Gate found
				
				Gate g = new Gate();
				g.setNumeroGate(rs.getInt("numeroGate"));
				g.setListaCode(new CodaDAO().getQueueListByID(con, st, ID));
				
				return g;
				
			}else { //Gate not found
				
				st.close(); //Close statement
				return null; //Return null
			}
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}

	/**
	 * Get a list of all the gates with a specified gate number
	 * @param gateNumber The number of the gate
	 * @return List of gates where gateNumber is equal to the passed gateNumber argument
	 */
	public ArrayList<Gate> getGateListByGateNumber(int gateNumber){
		
		try {
			
			String q = "Select * from gate where numeroGate = " + gateNumber + ""; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

			Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Gate> list = new ArrayList<Gate>(); //Initialize a list of gates
			
			//Get all the gates
			while(rs.next()) {
				
				Gate g = new Gate();
				g.setNumeroGate(gateNumber);
				g.setListaCode(new CodaDAO().getQueueListByID(con, st, rs.getString("IDVolo")));
				list.add(g);
				
			}
			
			con.close(); //Close connection
			st.close(); //Close statement
			return list; //Return list
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}
	
	/**
	 * Get a list of the flight's ID with a specified gate number in it's linked gate
	 * @param gateNumber The number of the gate
	 * @return List of ID where a flight's linked gate has the gateNumber equal to the passed gateNumber argument
	 */
	public ArrayList<String> getFlightIdByGateNumber(int gateNumber){
		
		try {
			
			String q = "Select IDVolo, partito, cancellato from gate inner join volo on gate.IDVolo = volo.id where numeroGate = " + gateNumber + " AND partito = 0 and cancellato = 0"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<String> list = new ArrayList<String>(); //Initialize a list of gates
			
			//Get all the gates
			while(rs.next()) {
				
				list.add(rs.getString("IDVolo"));
				
			}
			
			con.close(); //Close connection
			st.close(); //Close statement
			return list; //Return list
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}

	/**
	 * Get the amount of flights that take place on a specified gate
	 * @param gateNumber The number of the gate we want the number of flights of
	 * @return The number of flights that take place on the specified gate
	 */
	public int getFlightAmountByGateNumber(int gateNumber) {
		
		try {
			
			String q = "SELECT COUNT(IDVolo) AS amount FROM gate INNER JOIN volo ON volo.id = gate.IDVolo WHERE cancellato = 0 AND numeroGate = " + gateNumber; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			rs.next();
			
			int count = rs.getInt("amount");
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			return count; //Return list
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return 0; //Return null
		}
		
	}

}