import java.awt.EventQueue;
import java.util.Random;


public class MainController {
	
	static String URL = "jdbc:mysql://localhost:3306/aereoporto?autoReconnect=true&useSSL=false"; //Database URL
	static String PASSWORD = "password";
	static String USER = "root";

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Crea Main frame
					MainController controller = new MainController();
					MainFrame mainFrame = new MainFrame(controller);
					mainFrame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	public String generateIDString() {
		
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 8;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	    
	}

}
