import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class GateDAO {
	
	public boolean insertGate(MainFrame mainFrame, Gate g, String id) {
		
		int numeroGate = g.getNumeroGate();
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
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
	
	public Gate getGateByID(String ID) {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "Select * from gate where IDVolo = '" + ID + "'"; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			if(rs.next()) { //Gate found
				
				Gate g = new Gate();
				g.setNumeroGate(rs.getInt("numeroGate"));
				g.setListaCode(new CodaDAO().getQueueListByID(ID));
				
				return g;
				
			}else { //Gate not found
				
				con.close(); //Close connection
				st.close(); //Close statement
				
				return null; //Return null
			}
			
		}catch(Exception e) { //Error catching
			System.out.println(e);
			return null; //Return null
		}
		
	}

	public ArrayList<Gate> getGateListByNumber(int gateNumber){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "Select * from gate where numeroGate = " + gateNumber + ""; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<Gate> list = new ArrayList<Gate>(); //Initialize a list of gates
			
			//Get all the gates
			while(rs.next()) {
				
				Gate g = new Gate();
				g.setNumeroGate(gateNumber);
				g.setListaCode(new CodaDAO().getQueueListByID(rs.getString("IDVolo")));
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
	
	public ArrayList<String> getFlightIdByGateNumber(int gateNumber){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "Select IDVolo from gate where numeroGate = " + gateNumber + ""; //Initialize query
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

}
