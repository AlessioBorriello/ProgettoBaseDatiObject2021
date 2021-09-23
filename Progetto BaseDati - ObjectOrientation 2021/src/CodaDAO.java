import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CodaDAO {
	
	/**
	 * Get a list of the queues that are being used by a gate
	 * @param gateNumber The gate to get the queues of
	 * @return The queues that are being used by the given gate
	 */
	public ArrayList<Coda> getQueueListFromGate(int gateNumber){
		
		try {

			String q = "Select * from coda where numeroGate = " + gateNumber; // Initialize query
			String connectionURL = MainController.URL; // Connection URL

			Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); // Create
																														// connection
			Statement st = con.createStatement(); // Create statement
			ResultSet rs = st.executeQuery(q); // Execute query

			ArrayList<Coda> list = new ArrayList<Coda>(); // Initialize a list of queues

			// Get results and store them in the list
			while (rs.next()) {
				
				String tipo = rs.getString("tipo"); //Queue type
				int numeroGate = rs.getInt("numeroGate"); // Get the queue's gate
				int lunghezzaMax = rs.getInt("lunghezzaMax"); //Get the queue's max lenght

				// Create queue
				Coda c = new Coda();
				c.setGateNumber(numeroGate);
				c.setTipo(tipo);
				c.setLunghezzaMax(lunghezzaMax);
				
				list.add(c);
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