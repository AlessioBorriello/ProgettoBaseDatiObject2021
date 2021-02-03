import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CodaDAO {
	
	public boolean insertCoda(MainFrame mainFrame, Coda c, String id) {
		
		String tipo = c.getTipo();
		int lunghezza = c.getPersoneInCoda();
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
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
	
	public ArrayList<Coda> getQueueListByID(String ID){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "Select * from coda where IDVolo = '" + ID + "'"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
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
			
			con.close(); //Close connection
			st.close(); //Close statement
			return list; //Return list
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}

}
