import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class VoloDAO {
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public boolean insertVolo(MainFrame mainFrame, Volo v) {
		
		String compagnia = v.getCompagnia().getNome();
		Date orarioDecollo = v.getOrarioDecollo();
		int numeroPrenotazioni = v.getNumeroPrenotazioni();
		int numeroGate = v.getGate().getNumeroGate();
		
		//Convert date format to a usable format in the database
		String dataString = dateTimeFormat.format(orarioDecollo);
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			String q = "INSERT INTO volo(nomeCompagnia, dataPartenza, numeroPrenotazioni, partito, numeroGate)\r\n" + 
					"VALUES ('"+ compagnia +"','"+ dataString +"', '"+ numeroPrenotazioni + "','" +  0 + "','" + numeroGate + "');"; //Initialize query
			String connectionURL = MainController.URL; //Connection URL
	
	        Connection con = DriverManager.getConnection(connectionURL, MainController.USER, MainController.PASSWORD);  //Create connection
			Statement st = con.createStatement(); //Create statement
			st.executeUpdate(q); //Execute query
			
			con.close(); //Close connection
			st.close(); //Close statement
			
			mainFrame.createNotificationFrame("Volo inserito!");
			
			return true; //Operation successful
		
		}catch(Exception e) { //Error catching
			System.out.println(e);
			mainFrame.createNotificationFrame("Qualcosa è andato storto!");
			return false; //Operation failed
		}
		
	}
	
}
