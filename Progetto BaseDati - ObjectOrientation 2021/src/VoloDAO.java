import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VoloDAO {
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm"); //Date format
	
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
		String id = v.getID();
		String destinazione = v.getDestinazione();
		int numeroGate = v.getGate().getNumeroGate();
		int numeroPrenotazioni = v.getNumeroPrenotazioni();
		
		//Convert date format to a usable format in the database
		String dataString = dateTimeFormat.format(orarioDecollo);
		
		try {
			
			String q = "INSERT INTO volo(idvolo, nomeCompagnia, dataPartenza, destinazione, numeroGate, numeroPrenotazioni)\r\n" + 
					"VALUES ('" + id + "','" + compagnia + "','"+ dataString + "', '" + destinazione + "','" + numeroGate + "', '" + numeroPrenotazioni + "');"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Insert slot
			SlotDAO daoSlot = new SlotDAO();
			daoSlot.insertSlot(mainFrame, v.getSlot(), id);
			
			/*
			if(!daoSlot.insertSlot(mainFrame, v.getSlot(), id)) {
				//Something went wrong when inserting the slot, therefore remove the flight just inserted (if found)
				if(getFlightByID(mainFrame, id) != null) {
					removeFlight(mainFrame, v);
				}
				return false;
			}
			*/
			
			//Update company and gate flight amounts in application
			v.getCompagnia().setNumeroVoli(v.getCompagnia().getNumeroVoli() + 1);
			v.getGate().setNumeroVoli(v.getGate().getNumeroVoli() + 1);
			
			mainFrame.createNotificationFrame("Volo inserito!");
			
			return true; //Operation successful
		
		}catch(SQLException e) { //Error catching
			System.out.println(e);
			if(e.getSQLState().toString().equals("42069")) {
				mainFrame.createNotificationFrame("Un gate puo' avere solo un volo contemporaneamente!");
				return false; //Operation failed
			}
			mainFrame.createNotificationFrame("Qualcosa e' andato storto!: " + e + "");
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Update a flight into the database
	 * @param mainFrame Link to the mainFrame
	 * @param newFlight Instance of the updated flight
	 * @param oldFlight Instance of the flight to update
	 * @return If the update operation was successful
	 */
	public boolean updateFlight(MainFrame mainFrame, Volo newFlight, Volo oldFlight) {
		
		String compagnia = newFlight.getCompagnia().getNome();
		Date orarioDecollo = newFlight.getOrarioDecollo();
		String id = newFlight.getID();
		String destinazione = newFlight.getDestinazione();
		int numeroGate = newFlight.getGate().getNumeroGate();
		
		//Convert date format to a usable format in the database
		String dataString = dateTimeFormat.format(orarioDecollo);
		
		try {
			
			String q = "UPDATE volo SET nomeCompagnia = '" + compagnia + "', dataPartenza = '" + dataString + "', destinazione = '" + destinazione + "', numeroGate = '" + numeroGate + "' WHERE idvolo = '" + id + "'"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Update slot
			SlotDAO daoSlot = new SlotDAO();
			daoSlot.updateSlot(mainFrame, newFlight.getSlot(), id);
			
			/*
			if(!daoSlot.updateSlot(mainFrame, newFlight.getSlot(), id)) {
				//Something went wrong when inserting the slot, therefore remove the flight just inserted (if found)
				if(getFlightByID(mainFrame, id) != null) {
					revertFlightValues(mainFrame, oldFlight);
				}
				return false;
			}
			*/
			
			//Update company and gate flight amounts in application
			if(!newFlight.getCompagnia().getNome().equals(oldFlight.getCompagnia().getNome())) { //If the company changed
				newFlight.getCompagnia().setNumeroVoli(newFlight.getCompagnia().getNumeroVoli() + 1); //Increase new company count
				oldFlight.getCompagnia().setNumeroVoli(oldFlight.getCompagnia().getNumeroVoli() - 1); //decrease old company count
			}
			if(newFlight.getGate().getNumeroGate() != oldFlight.getGate().getNumeroGate()) { //If the gate changed
				newFlight.getGate().setNumeroVoli(newFlight.getGate().getNumeroVoli() + 1); //Increase new gate count
				oldFlight.getGate().setNumeroVoli(oldFlight.getGate().getNumeroVoli() - 1); //decrease old gate count
			}
			
			mainFrame.createNotificationFrame("Volo modificato!");
			
			return true; //Operation successful
		
		}catch(SQLException e) { //Error catching
			System.out.println(e);
			if(e.getSQLState().toString().equals("42069")) {
				mainFrame.createNotificationFrame("Un gate puo' avere solo un volo contemporaneamente!");
				return false; //Operation failed
			}
			mainFrame.createNotificationFrame("Qualcosa e' andato storto!: " + e + "");
			return false; //Operation failed
		}
		
	}
	
	/**
	 * Revert flight values
	 * @param mainFrame Link to mainFrame
	 * @param backupFlight The flight to revert the values from
	 * @return If the Update was successful
	 */
	public boolean revertFlightValues(MainFrame mainFrame, Volo backupFlight) {
		
		String compagnia = backupFlight.getCompagnia().getNome();
		Date orarioDecollo = backupFlight.getOrarioDecollo();
		String id = backupFlight.getID();
		String destinazione = backupFlight.getDestinazione();
		int numeroGate = backupFlight.getGate().getNumeroGate();
		
		//Convert date format to a usable format in the database
		String dataString = dateTimeFormat.format(orarioDecollo);
		
		try {
			
			String q = "UPDATE volo SET nomeCompagnia = '" + compagnia + "', dataPartenza = '" + dataString + "', destinazione = '" + destinazione + "', numeroGate = '" + numeroGate + "' WHERE idvolo = '" + id + "'"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Update slot
			SlotDAO daoSlot = new SlotDAO();
			daoSlot.updateSlot(mainFrame, backupFlight.getSlot(), id);
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			mainFrame.createNotificationFrame("Qualcosa e' andato storto!: " + e + "");
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
			
			String q = "DELETE FROM volo WHERE idvolo = '" + v.getID() + "'"; //Initialize query
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
	 * @param mainFrame Link to the mainFrame
	 * @param ID ID of the flight to get
	 * @return The flight with the given ID
	 */
	public Volo getFlightByID(MainFrame mainFrame, String ID) {
		
		try {
			
			String q = "Select * from volo where idvolo = '" + ID + "' ORDER BY dataPartenza ASC" ; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			if(rs.next()) { //Flight found
				
				Volo v = new Volo();

				//Get company class from it's name
				CompagniaAerea compagnia = null;
				for(CompagniaAerea c : mainFrame.getListaCompagnie()) {
					if(c.getNome().equals(rs.getString("nomeCompagnia"))) {
						compagnia = c;
						break;
					}
				}
				v.setCompagnia(compagnia); //Set company
				
				//Get gate from it's number
				Gate gate = null;
				for(Gate g : mainFrame.getListaGate()) {
					if(g.getNumeroGate() == rs.getInt("numeroGate")) {
						gate = g;
						break;
					}
				}
				
				v.setGate(gate);
				v.setID(ID);
				v.setDestinazione(rs.getString("destinazione"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = rs.getBoolean("partito"); //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = rs.getBoolean("cancellato"); //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(new SlotDAO().getSlotByID(ID)); //Get the slot by the ID
				v.setNumeroPrenotazioni(rs.getInt("numeroPrenotazioni"));
				
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
	 * Set a given flight's 'partito' as 1 inside the database
	 * @param mainFrame Link to the mainFrame
	 * @param v Flight to set as 'partito' = 1 inside the database
	 * @return If the update operation was successful
	 */
	public boolean setFlightAsTakenOff(MainFrame mainFrame, Volo v) {
		
		try {
			
			String q = "UPDATE volo SET partito = '1' where idvolo = '" + v.getID() + "'"; //Initialize query
			
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
	 * Set a given flight's 'cancellato' as 1 inside the database
	 * @param mainFrame Link to the mainFrame
	 * @param v Flight to set as 'cancellato' = 1 inside the database
	 * @return If the update operation was successful
	 */
	public boolean setFlightAsCancelled(MainFrame mainFrame, Volo v) {
		
		try {
			
			String q = "UPDATE volo SET cancellato = '1' where idvolo = '" + v.getID() + "'"; //Initialize query
			
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
	 * Gets a list of Flights from the database that pass the given query
	 * @param query Query to make to the database
	 * @param mainFrame Link to the mainFrame
	 * @return List of Flights that passed the query
	 */
	public ArrayList<Volo> searchFlight(MainFrame mainFrame, String query){
		
		try {
			
			String q = query; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Volo> list = new ArrayList<Volo>(); //Initialize a list of flights
			long startTime = System.currentTimeMillis();
			
			//Get all the flights
			while(rs.next()) {
				
				Volo v = new Volo();

				//Get company class from it's name
				CompagniaAerea compagnia = null;
				for(CompagniaAerea c : mainFrame.getListaCompagnie()) {
					if(c.getNome().equals(rs.getString("nomeCompagnia"))) {
						compagnia = c;
						break;
					}
				}
				
				//Get gate from it's number
				Gate gate = null;
				for(Gate g : mainFrame.getListaGate()) {
					if(g.getNumeroGate() == rs.getInt("numeroGate")) {
						gate = g;
						break;
					}
				}

				//Create slot
				Slot s = new Slot();
				s.setInizioTempoStimato(rs.getTimestamp("inizioTempoStimato"));
				s.setFineTempoStimato(rs.getTimestamp("fineTempoStimato"));
				s.setInizioTempoEffettivo(rs.getTimestamp("inizioTempoEffettivo"));
				s.setFineTempoEffettivo(rs.getTimestamp("fineTempoEffettivo"));
				
				v.setCompagnia(compagnia); //Set company
				v.setGate(gate); //Set gate
				v.setID(rs.getString("idvolo"));
				v.setDestinazione(rs.getString("destinazione"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = rs.getBoolean("partito"); //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = rs.getBoolean("cancellato"); //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(s); //Set slot
				v.setNumeroPrenotazioni(rs.getInt("numeroPrenotazioni"));
				
				list.add(v);
				
			}
			
			long stopTime = System.currentTimeMillis();
			System.out.println("Search time: " + (stopTime - startTime) + "ms");
			
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
	 * @param mainFrame Link to the mainFrame
	 * @param gateNumber Gate number of the flight to get
	 * @return The flight with the given gate number
	 */
	public ArrayList<Volo> getFlightsByGate(MainFrame mainFrame, int gateNumber){
		
		try {
			
			String q = "SELECT * FROM volo where numeroGate = " + gateNumber + " ORDER BY dataPartenza ASC"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Volo> list = new ArrayList<Volo>(); //Initialize a list of flights
			
			//Get all the flights
			while(rs.next()) {
				
				Volo v = new Volo();

				//Get company class from it's name
				CompagniaAerea compagnia = null;
				for(CompagniaAerea c : mainFrame.getListaCompagnie()) {
					if(c.getNome().equals(rs.getString("nomeCompagnia"))) {
						compagnia = c;
						break;
					}
				}
				
				//Get gate from it's number
				Gate gate = null;
				for(Gate g : mainFrame.getListaGate()) {
					if(g.getNumeroGate() == rs.getInt("numeroGate")) {
						gate = g;
						break;
					}
				}
				
				v.setCompagnia(compagnia); //Set company
				v.setGate(gate); //Set gate
				v.setID(rs.getString("idvolo"));
				v.setDestinazione(rs.getString("destinazione"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = rs.getBoolean("partito"); //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = rs.getBoolean("cancellato"); //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(new SlotDAO().getSlotByID(rs.getString("idvolo"))); //Get the slot by the ID
				v.setNumeroPrenotazioni(rs.getInt("numeroPrenotazioni"));
				
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
	 * Get a list of the flight's ID with a specified gate number in it's linked gate
	 * @param gateNumber The number of the gate
	 * @return List of ID where a flight's linked gate has the gateNumber equal to the passed gateNumber argument
	 */
	public ArrayList<String> getFlightIdByGateNumber(int gateNumber){
		
		try {
			
			String q = "Select idVolo from volo where numeroGate = " + gateNumber + " AND partito = '0' and cancellato = '0'"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<String> list = new ArrayList<String>(); //Initialize a list of id s
			
			//Get all the id s
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
	 * Get a list of ID's of all the flights whose take off time happen before a given date
	 * @param date The date that a flight needs to take off before of to be added in the return list
	 * @return List of ID's of all the flights whose take off time happen before a given date
	 */
	public ArrayList<String> getIDListOfFlightsTakeOffTimePassed(Date date) {
		
		//Convert date format to a usable format in the database
		String dateString = dateTimeFormat.format(date);
		
		try {
			
			String q = "Select idvolo from volo where partito = '0' AND cancellato = '0' AND dataPartenza <= '" + dateString + "' ORDER BY dataPartenza ASC" ; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<String> idList = new ArrayList<String>();
			
			while(rs.next()) {
				
				idList.add(rs.getString("idvolo"));
				
			}
			
			return idList;
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}

}