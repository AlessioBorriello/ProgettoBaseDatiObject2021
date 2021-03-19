import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
		
		//Convert date format to a usable format in the database
		String dataString = dateTimeFormat.format(orarioDecollo);
		
		try {
			
			String q = "INSERT INTO volo(id, nomeCompagnia, dataPartenza, destinazione, partito, cancellato)\r\n" + 
					"VALUES ('" + id + "','" + compagnia + "','"+ dataString + "', '" + destinazione + "','" +  0 + "','" + 0 + "');"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			//Insert gate
			GateDAO daoGate = new GateDAO();
			if(!daoGate.insertGate(mainFrame, v.getGate(), id)) {
				//Something went wrong when inserting the gate, therefore remove the flight just inserted (if found)
				if(getFlightByID(mainFrame, id) != null) {
					removeFlight(mainFrame, v);
				}
				mainFrame.createNotificationFrame("Qualcosa è andato storto!");
				return false;
			}
			
			//Insert slot
			SlotDAO daoSlot = new SlotDAO();
			if(!daoSlot.insertSlot(mainFrame, v.getSlot(), id)) {
				//Something went wrong when inserting the slot, therefore remove the flight just inserted (if found)
				if(getFlightByID(mainFrame, id) != null) {
					removeFlight(mainFrame, v);
				}
				mainFrame.createNotificationFrame("Qualcosa è andato storto!");
				return false;
			}
			
			mainFrame.createNotificationFrame("Volo inserito!");
			
			//Update flight count
			new CompagniaAereaDAO().increaseCompagniaAereaFlightCount(mainFrame, v.getCompagnia().getNome());
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			mainFrame.createNotificationFrame("Qualcosa è andato storto!");
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
		String destinazione = newFlight.getDestinazione();
		
		//Convert date format to a usable format in the database
		String dataString = dateTimeFormat.format(orarioDecollo);
		
		try {
			
			String q = "UPDATE volo SET nomeCompagnia = '" + compagnia + "', dataPartenza = '" + dataString + "', destinazione = '" + destinazione + "' WHERE id = '" + id + "'"; //Initialize query
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
			mainFrame.createNotificationFrame("Qualcosa è andato storto!");
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
	 * @param mainFrame Link to the mainFrame
	 * @param ID ID of the flight to get
	 * @return The flight with the given ID
	 */
	public Volo getFlightByID(MainFrame mainFrame, String ID) {
		
		try {
			
			String q = "Select * from volo where id = '" + ID + "' ORDER BY dataPartenza ASC" ; //Initialize query
			
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
				
				v.setGate(new GateDAO().getGateByID(con, ID)); //Get the gate by the ID
				v.setID(ID);
				v.setDestinazione(rs.getString("destinazione"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = (rs.getInt("partito") != 0)? true : false; //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = (rs.getInt("cancellato") != 0)? true : false; //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(new SlotDAO().getSlotByID(ID)); //Get the slot by the ID
				
				//Calculate number of bookings
				int sum = 0;
				for(Coda c : v.getGate().getListaCode()) {
					sum += c.getPersoneInCoda();
				}
				v.setNumeroPrenotazioni(sum);
				
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
			
			String q = "UPDATE volo SET partito = '1' where id = '" + v.getID() + "'"; //Initialize query
			
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
	 * @param mainFrame Link to the mainFrame
	 * @return List of Flights that passed the query
	 */
	public ArrayList<Volo> searchFlight(MainFrame mainFrame, String query){
		
		try {
			
			String q = query; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
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
				
				//Create gate
				Gate g = new Gate();
				g.setNumeroGate(rs.getInt("numeroGate"));
				
				//Create queue list
				String currentID = rs.getString("id");
				ArrayList<Coda> queues = new ArrayList<Coda>();
				
				//Go through all the results where the id is the same
				while(rs.next() && rs.getString("id").equals(currentID)) {
					
					Coda c = new Coda();
					c.setTipo(rs.getString("tipo"));
					c.setPersoneInCoda(rs.getInt("lunghezza"));
					queues.add(c);
				}
				
				rs.previous(); //Go back once on the result set, to go back to the last result where the id is the same
				
				//Set the queue list
				g.setListaCode(queues);
				
				//Create slot
				Slot s = new Slot();
				s.setInizioTempoStimato(rs.getTimestamp("inizioTempoStimato"));
				s.setFineTempoStimato(rs.getTimestamp("fineTempoStimato"));
				s.setInizioTempoEffettivo(rs.getTimestamp("inizioTempoEffettivo"));
				s.setFineTempoEffettivo(rs.getTimestamp("fineTempoEffettivo"));
				
				v.setCompagnia(compagnia); //Set company
				v.setGate(g); //Set gate
				v.setID(currentID);
				v.setDestinazione(rs.getString("destinazione"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = (rs.getInt("partito") != 0)? true : false; //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = (rs.getInt("cancellato") != 0)? true : false; //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(s); //Set slot
				
				//Calculate number of bookings
				int sum = 0;
				for(Coda c : v.getGate().getListaCode()) {
					sum += c.getPersoneInCoda();
				}
				v.setNumeroPrenotazioni(sum);
				
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
			
			String q = "SELECT * FROM volo INNER JOIN gate ON volo.id = gate.IDVolo WHERE numeroGate = " + gateNumber + " ORDER BY dataPartenza ASC"; //Initialize query
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
				
				v.setCompagnia(compagnia); //Set company
				
				v.setGate(new GateDAO().getGateByID(con, rs.getString("id"))); //Get the gate by the ID
				v.setID(rs.getString("id"));
				v.setDestinazione(rs.getString("destinazione"));
				v.setOrarioDecollo(rs.getTimestamp("dataPartenza"));
				boolean partito = (rs.getInt("partito") != 0)? true : false; //Set partito to true if the database has a different value than 0, otherwise set it to false
				v.setPartito(partito);
				boolean cancellato = (rs.getInt("cancellato") != 0)? true : false; //Set cancellato to true if the database has a different value than 0, otherwise set it to false
				v.setCancellato(cancellato);
				v.setSlot(new SlotDAO().getSlotByID(rs.getString("id"))); //Get the slot by the ID
				
				//Calculate number of bookings
				int sum = 0;
				for(Coda c : v.getGate().getListaCode()) {
					sum += c.getPersoneInCoda();
				}
				v.setNumeroPrenotazioni(sum);
				
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
	 * Get a list of ID's of all the flights whose take off time happen before a given date
	 * @param date The date that a flight needs to take off before of to be added in the return list
	 * @return List of ID's of all the flights whose take off time happen before a given date
	 */
	public ArrayList<String> getIDListOfFlightsTakeOffTimePassed(Date date) {
		
		//Convert date format to a usable format in the database
		String dateString = dateTimeFormat.format(date);
		
		try {
			
			String q = "Select id from volo where partito = false AND cancellato = false AND dataPartenza <= '" + dateString + "' ORDER BY dataPartenza ASC" ; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<String> idList = new ArrayList<String>();
			
			while(rs.next()) {
				
				idList.add(rs.getString("id"));
				
			}
			
			return idList;
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}

	/**
	 * Get the minimum and maximum dates present in the database, non considering cancelled or flights that have taken off
	 * @return Array of size 2 containing the minimum and maximum dates present in the database, non considering cancelled or flights that have taken off
	 */
	public Date[] getMinAndMaxTakeOffTime() {
		
		try {
			
			String q = "SELECT MIN(dataPartenza) AS minimo, MAX(dataPartenza) as massimo FROM volo" ; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			Date[] dates = new Date[2];
			
			if(rs.next()) {
				
				//Minimum
				dates[0] = rs.getTimestamp("minimo");
				//Max
				dates[1] = rs.getTimestamp("massimo");
				
			}
			
			return dates;
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}

}
