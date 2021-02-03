import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompagniaAereaDAO {
	
	public ArrayList<CompagniaAerea> getAllCompagniaAerea(){
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
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
	
	public CompagniaAerea getCompagniaAereaByNome(String nome) {
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "Select * from compagniaaerea where nomeCompagnia = '" + nome + "'" ; //Initialize query
			
			String connectionURL = MainController.URL; //Connection URL

	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD); //Create connection
			Statement st = con.createStatement(); //Create statement
			ResultSet rs = st.executeQuery(q); //Execute query
			
			if(rs.next()) { //Company found
				
				//Create company instance
				CompagniaAerea c = new CompagniaAerea();
				c.setNome(nome);
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
	
}
