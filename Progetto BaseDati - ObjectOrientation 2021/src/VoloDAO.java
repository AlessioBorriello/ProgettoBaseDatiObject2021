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
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public boolean insertVolo(MainFrame mainFrame, Volo v) {
		
		String compagnia = v.getCompagnia().getNome();
		Date orarioDecollo = v.getOrarioDecollo();
		int numeroPrenotazioni = v.getNumeroPrenotazioni();
		int numeroGate = v.getGate().getNumeroGate();
		String id = v.getID();
		
		//Convert date format to a usable format in the database
		String dataString = dateTimeFormat.format(orarioDecollo);
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "INSERT INTO volo(id, nomeCompagnia, dataPartenza, numeroPrenotazioni, partito, numeroGate)\r\n" + 
					"VALUES ('" + id + "','" + compagnia + "','"+ dataString + "', '" + numeroPrenotazioni + "','" +  0 + "','" + numeroGate + "');"; //Initialize query
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
				mainFrame.createNotificationFrame("Qualcosa è andato storto!");
				return false;
			}
			
			//Insert slot
			SlotDAO daoSlot = new SlotDAO();
			if(!daoSlot.insertSlot(mainFrame, v.getSlot(), id)) {
				//Slot not inserted, remove the flight just inserted (if found)
				if(getFlightByID(id) != null) {
					removeFlight(mainFrame, v);
				}
				mainFrame.createNotificationFrame("Qualcosa è andato storto!");
				return false;
			}
			
			mainFrame.createNotificationFrame("Volo inserito!");
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			mainFrame.createNotificationFrame("Qualcosa è andato storto!");
			return false; //Operation failed
		}
		
	}
	
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
	 * Get a list with all of the flights where partito is true or false
	 * @param partito Wether to get the flights where partito is true or false
	 * @return List of the flights
	 */
	public ArrayList<Volo> getAllFlights(boolean partito){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "Select id from volo where partito = '" + partito + "'" ; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Volo> list = new ArrayList<Volo>(); //Initialize a list of flights
			
			//Get results and store them in the list
			while(rs.next()) {
				
				VoloDAO dao = new VoloDAO();
				Volo v = dao.getFlightByID(rs.getString("id"));
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
