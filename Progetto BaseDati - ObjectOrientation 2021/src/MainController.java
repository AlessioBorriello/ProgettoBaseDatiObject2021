import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

	public Component getComponentByName(Container container, String nome) {
		
		List<Component> list = getAllComponents(container); //Take all components from the container
		
		for(Component c: list) {
			if(c.getName() != null && c.getName().contentEquals(nome)) { //Component found
				return c;
			}
		}
		
		return null;
		
	}
	
	public List<Component> getAllComponents(Container container) {
		
	    Component[] components = container.getComponents(); //Take all the components from the outer container
	    List<Component> listaComponents = new ArrayList<Component>();
	    for (Component c: components) {
	    	listaComponents.add(c); //Add component to the list
	        if (c instanceof Container) //If the component is a container in itself, repeat
	        	listaComponents.addAll(getAllComponents((Container) c)); //Recursive call
	    }
	    return listaComponents; //Return list
	    
	}
	
	public boolean checkIfSlotIsTaken(Slot s, int gateNumber) {
		
		ArrayList<String> idList = (new GateDAO().getFlightIdByGateNumber(gateNumber));
		for(String idString : idList) { //For all of the flights id where the gate number is the gate selected in the flight creation
			Slot slot = (new SlotDAO().getSlotByID(idString)); //Get the slot with that id
			if(s.getInizioTempoStimato().before(slot.getFineTempoStimato())) { //If the slot passed as argument starts before the end of the slot we are currently checking in the for loop
				//The slot s is taken
				return true;
			}
		}
		
		return false; //The slot is not taken
		
	}
	
}
