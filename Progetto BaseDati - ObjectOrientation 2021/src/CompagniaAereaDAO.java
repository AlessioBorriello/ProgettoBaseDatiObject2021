import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CompagniaAereaDAO {
	
	/**
	 * Get all the companies contained in the database
	 * @return List of all the companies
	 */
	public ArrayList<CompagniaAerea> getAllCompagniaAerea(){
		
		try {
			
			String q = "Select * from compagniaaerea order by nomeCompagnia ASC" ; //Initialize query
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			ArrayList<CompagniaAerea> list = new ArrayList<CompagniaAerea>(); //Initialize a list of companies
			
			//Get results and store them in the list
			while(rs.next()) {
				String nomeCompagnia = rs.getString("nomeCompagnia"); //Company name
				int numeroVoli = rs.getInt("numeroVoli"); //Get flights number
				
				//Create company
				CompagniaAerea c = new CompagniaAerea();
				c.setNome(nomeCompagnia);
				c.setNumeroVoli(numeroVoli);
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
	
	/**
	 * Get a specific company from the database by it's name
	 * @param nome Name of the company to get
	 * @return If the company was found
	 */
	public CompagniaAerea getCompagniaAereaByNome(String name) {
		
		try {
			
			String q = "Select * from compagniaaerea where nomeCompagnia = '" + name + "'" ; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			if(rs.next()) { //Company found
				
				//Create company instance
				CompagniaAerea c = new CompagniaAerea();
				c.setNome(name);
				c.setNumeroVoli(rs.getInt("numeroVoli"));
				
				return c;
				
			}else { //Company not found
				
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
	 * Increase the flight count in a company
	 * @param mainFrame Link to the mainFrame
	 * @param name Name of the company to update
	 * @return If the update operation was successful
	 */
	public boolean increaseCompagniaAereaFlightCount(MainFrame mainFrame, String name) {
		
		int numeroVoli = (getCompagniaAereaByNome(name).getNumeroVoli()); //Get current flight count
		
		try {
			
			String q = "UPDATE compagniaaerea SET numeroVoli = '" + (numeroVoli + 1) + "' WHERE nomeCompagnia = '" + name + "'"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			mainFrame.createNotificationFrame("Qualcosa è andato storto!");
			return false; //Operation failed
		}
		
	}
	
	/**
	 * decrease the flight count in a company
	 * @param mainFrame Link to the mainFrame
	 * @param name Name of the company to update
	 * @return If the update operation was successful
	 */
	public boolean decreaseCompagniaAereaFlightCount(MainFrame mainFrame, String name) {
		
		int numeroVoli = (getCompagniaAereaByNome(name).getNumeroVoli()); //Get current flight count
		
		try {
			
			String q = "UPDATE compagniaaerea SET numeroVoli = '" + (numeroVoli - 1) + "' WHERE nomeCompagnia = '" + name + "'"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			mainFrame.createNotificationFrame("Qualcosa è andato storto!");
			return false; //Operation failed
		}
		
	}

}
