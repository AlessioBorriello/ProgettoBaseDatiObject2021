import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GateDAO {
	
	/**
	 * Get all the gates contained in the database
	 * 
	 * @return List of all the gates
	 */
	public ArrayList<Gate> getAllGates() {

		try {

			String q = "Select * from gate order by numerogate ASC"; // Initialize query
			String connectionURL = MainController.URL; // Connection URL

			Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); // Create
																														// connection
			Statement st = con.createStatement(); // Create statement
			ResultSet rs = st.executeQuery(q); // Execute query

			ArrayList<Gate> list = new ArrayList<Gate>(); // Initialize a list of gates

			// Get results and store them in the list
			while (rs.next()) {
				int numeroGate = rs.getInt("numeroGate"); // Gate number
				int numeroVoli = rs.getInt("numeroVoli"); // Get flights number

				// Create gate
				Gate g = new Gate();
				g.setNumeroGate(numeroGate);
				g.setNumeroVoli(numeroVoli);
				
				//Get gate's queues
				g.setListaCode(new CodaDAO().getQueueListFromGate(numeroGate));
				
				list.add(g);
			}

			con.close(); // Close connection
			st.close(); // Close statement
			return list; // Return list

		} catch (Exception e) { // Error catching
			System.out.println(e);
			return null; // Return null
		}

	}

}