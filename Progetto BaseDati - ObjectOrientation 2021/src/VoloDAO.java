import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoloDAO {
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //Date format
	
	/**
	 * Insert a flight into the database
	 * @param mainFrame Link to the mainFrame
	 * @param v Flight instance to add
	 * @return If the insert operation was successful
	 */
	public boolean insertFlight(MainFrame mainFrame, Volo v) {
		
		//Gather info
		String compagnia = v.getCompagnia().getNome();
		Date orarioDecollo = v.getOrarioDecollo();
		int numeroPrenotazioni = v.getNumeroPrenotazioni();
		String id = v.getID();
		
		//Convert date format to a usable format in the database
		String dataString = dateTimeFormat.format(orarioDecollo);
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "INSERT INTO volo(id, nomeCompagnia, dataPartenza, numeroPrenotazioni, partito, cancellato)\r\n" + 
					"VALUES ('" + id + "','" + compagnia + "','"+ dataString + "', '" + numeroPrenotazioni + "','" +  0 + "','" + 0 + "');"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Insert gate
			GateDAO daoGate = new GateDAO();
			if(!daoGate.insertGate(mainFrame, v.getGate(), id)) {
				//Gate not inserted, remove the flight just inserted (if found)
				if(getFlightByID(id) != null) {
					removeFlight(mainFrame, v);
				}
				mainFrame.createNotificationFrame("Qualcosa � andato storto!");
				return false;
			}
			
			//Insert slot
			SlotDAO daoSlot = new SlotDAO();
			if(!daoSlot.insertSlot(mainFrame, v.getSlot(), id)) {
				//Slot not inserted, remove the flight just inserted (if found)
				if(getFlightByID(id) != null) {
					removeFlight(mainFrame, v);
				}
				mainFrame.createNotificationFrame("Qualcosa � andato storto!");
				return false;
			}
			
			mainFrame.createNotificationFrame("Volo inserito!");
			
			//Update flight count
			new CompagniaAereaDAO().increaseCompagniaAereaFlightCount(mainFrame, v.getCompagnia().getNome());
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			mainFrame.createNotificationFrame("Qualcosa � andato storto!");
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Update a flight into the database
	 * @param mainFrame Link to the mainFrame
	 * @param oldFlight Instance of the old flight
	 * @param newFlight Instance of the updated flight
	 * @return If the update operation was successful
	 */
	public boolean updateFlight(MainFrame mainFrame, Volo oldFlight, Volo newFlight) {
		
		String compagnia = newFlight.getCompagnia().getNome();
		Date orarioDecollo = newFlight.getOrarioDecollo();
		String id = oldFlight.getID();
		
		//Convert date format to a usable format in the database
		String dataString = dateTimeFormat.format(orarioDecollo);
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "UPDATE volo SET nomeCompagnia = '" + compagnia + "', dataPartenza = '" + dataString + "' WHERE id = '" + id + "'"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Update gate
			GateDAO daoGate = new GateDAO();
			daoGate.updateGate(mainFrame, newFlight.getGate(), id);
			
			//Update slot
			SlotDAO daoSlot = new SlotDAO();
			daoSlot.updateSlot(mainFrame, newFlight.getSlot(), id);
			
			mainFrame.createNotificationFrame("Volo modificato!");
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			mainFrame.createNotificationFrame("Qualcosa � andato storto!");
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Remove a flight from a database
	 * @param mainFrame Link to the mainFrame
	 * @param v Instance of the flight to remove from the database
	 * @return If the remove operation was successful
	 */
	public boolean removeFlight(MainFrame mainFrame, Volo v) {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "DELETE FROM volo WHERE id = " + v.getID(); //Initialize query
			
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
	
	/**
	 * Get a flight instance by it's ID
	 * @param ID ID of the flight to get
	 * @return The flight with the given ID
	 */
	public Volo getFlightByID(String ID) {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "Select * from volo where id = '" + ID + "'" ; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			if(rs.next()) { //Flight found
				
				Volo v = new Volo();
				v.setCompagnia(new CompagniaAereaDAO().getCompagniaAereaByNome(rs.getString("nomeCompagnia"))); //Get the company by its name
				v.setGate(new GateDAO().getGateByID(ID)); //Get the gate by the ID
				v.setID(ID);
				v.setNumeroPrenotazioni(rs.getInt("numeroPrenotazioni"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = (rs.getInt("partito") != 0)? true : false; //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = (rs.getInt("cancellato") != 0)? true : false; //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(new SlotDAO().getSlotByID(ID)); //Get the slot by the ID
				
				return v;
				
			}else { //Flight not found
				
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
	 * Get all the flights that are not archived (where partito and cancellato are false)
	 * @return
	 */
	public ArrayList<Volo> getNonArchivedFlights(){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "Select * from volo where partito = " + false + " and cancellato = " + false + ""; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Volo> list = new ArrayList<Volo>(); //Initialize a list of flights
			
			//Get all the flights
			while(rs.next()) {
				
				Volo v = new Volo();
				v.setCompagnia(new CompagniaAereaDAO().getCompagniaAereaByNome(rs.getString("nomeCompagnia"))); //Get the company by its name
				v.setGate(new GateDAO().getGateByID(rs.getString("id"))); //Get the gate by the ID
				v.setID(rs.getString("id"));
				v.setNumeroPrenotazioni(rs.getInt("numeroPrenotazioni"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = (rs.getInt("partito") != 0)? true : false; //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = (rs.getInt("cancellato") != 0)? true : false; //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(new SlotDAO().getSlotByID(rs.getString("id"))); //Get the slot by the ID
				list.add(v);
				
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
	 * Get all the flights that are archived (where partito or cancellato are true)
	 * @return
	 */
	public ArrayList<Volo> getArchivedFlights(){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "Select * from volo where partito = " + true + " or cancellato = " + true + ""; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Volo> list = new ArrayList<Volo>(); //Initialize a list of flights
			
			//Get all the flights
			while(rs.next()) {
				
				Volo v = new Volo();
				v.setCompagnia(new CompagniaAereaDAO().getCompagniaAereaByNome(rs.getString("nomeCompagnia"))); //Get the company by its name
				v.setGate(new GateDAO().getGateByID(rs.getString("id"))); //Get the gate by the ID
				v.setID(rs.getString("id"));
				v.setNumeroPrenotazioni(rs.getInt("numeroPrenotazioni"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = (rs.getInt("partito") != 0)? true : false; //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = (rs.getInt("cancellato") != 0)? true : false; //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(new SlotDAO().getSlotByID(rs.getString("id"))); //Get the slot by the ID
				//Check if the flight has taken off and if so check if it did so late
				if(partito) {
					v.setInRitardo(v.checkIfFlightTookOffLate());
				}
				list.add(v);
				
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
	 * Set a given flight's 'partito' as 1 inside the database
	 * @param mainFrame Link to the mainFrame
	 * @param v Flight to set as 'partito' = 1 inside the database
	 * @return If the update operation was successful
	 */
	public boolean setFlightAsTakenOff(MainFrame mainFrame, Volo v) {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "UPDATE volo SET partito = '1' where id = '" + v.getID() + "'"; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Update flight count
			new CompagniaAereaDAO().decreaseCompagniaAereaFlightCount(mainFrame, v.getCompagnia().getNome());
			
			return true; //Operation successful
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Set a given flight's 'cancellato' as 1 inside the database
	 * @param mainFrame Link to the mainFrame
	 * @param v Flight to set as 'cancellato' = 1 inside the database
	 * @return If the update operation was successful
	 */
	public boolean setFlightAsCancelled(MainFrame mainFrame, Volo v) {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "UPDATE volo SET cancellato = '1' where id = '" + v.getID() + "'"; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Update flight count
			new CompagniaAereaDAO().decreaseCompagniaAereaFlightCount(mainFrame, v.getCompagnia().getNome());
			
			return true; //Operation successful
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Gets a list of Flights from the database that pass the given query
	 * @param query Query to make to the database
	 * @return List of Flights that passed the query
	 */
	public ArrayList<Volo> searchFlight(String query){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = query; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Volo> list = new ArrayList<Volo>(); //Initialize a list of flights
			
			//Get all the flights
			while(rs.next()) {
				
				Volo v = new Volo();
				v.setCompagnia(new CompagniaAereaDAO().getCompagniaAereaByNome(rs.getString("nomeCompagnia"))); //Get the company by its name
				v.setGate(new GateDAO().getGateByID(rs.getString("id"))); //Get the gate by the ID
				v.setID(rs.getString("id"));
				v.setNumeroPrenotazioni(rs.getInt("numeroPrenotazioni"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = (rs.getInt("partito") != 0)? true : false; //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = (rs.getInt("cancellato") != 0)? true : false; //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(new SlotDAO().getSlotByID(rs.getString("id"))); //Get the slot by the ID
				list.add(v);
				
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
	 * Get a flight instance by it's gate number
	 * @param gateNumber Gate number of the flight to get
	 * @return The flight with the given gate number
	 */
	public ArrayList<Volo> getFlightsByGate(int gateNumber){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "SELECT * FROM volo INNER JOIN gate ON volo.id = gate.IDVolo WHERE numeroGate = " + gateNumber + " ORDER BY dataPartenza ASC"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Volo> list = new ArrayList<Volo>(); //Initialize a list of flights
			
			//Get all the flights
			while(rs.next()) {
				
				Volo v = new Volo();
				v.setCompagnia(new CompagniaAereaDAO().getCompagniaAereaByNome(rs.getString("nomeCompagnia"))); //Get the company by its name
				v.setGate(new GateDAO().getGateByID(rs.getString("id"))); //Get the gate by the ID
				v.setID(rs.getString("id"));
				v.setNumeroPrenotazioni(rs.getInt("numeroPrenotazioni"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = (rs.getInt("partito") != 0)? true : false; //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = (rs.getInt("cancellato") != 0)? true : false; //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(new SlotDAO().getSlotByID(rs.getString("id"))); //Get the slot by the ID
				list.add(v);
				
			}
			
			con.close(); //Close connection
			st.close(); //Close statement
			return list; //Return list
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}

}
