import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainController {
	
	static String URL = "jdbc:mysql://localhost:3306/aereoporto?autoReconnect=true&useSSL=false"; //Database URL
	static String PASSWORD = "password"; //Password
	static String USER = "root"; //User name
	
	//Fonts
	static Font fontOne;
	static Font fontTwo;
	
	//Dark palette
	static Color darkBackgroundColorOne = new Color(29, 35, 39);
	static Color darkBackgroundColorTwo = new Color(33, 51, 64);
	static Color darkBackgroundColorThree = new Color(32, 32, 54);
	
	static Color darkForegroundColorOne = new Color(15, 76, 117);
	static Color darkForegroundColorTwo = new Color(50, 130, 184);
	static Color darkForegroundColorThree = new Color(207, 245, 255);
	
	static Color darkHighlightColorOne = new Color(242, 163, 101);
	static Color darkHighlightColorTwo = new Color(214, 90, 49);
	static Color darkHighlightColorThree = new Color(255, 77, 0);

	//Defined palette (numbers are from darker to brighter)
	static Color backgroundColorOne;
	static Color backgroundColorTwo;
	static Color backgroundColorThree;
	
	static Color foregroundColorOne;
	static Color foregroundColorTwo;
	static Color foregroundColorThree;
	
	static Color highlightColorOne;
	static Color highlightColorTwo;
	static Color highlightColorThree;
	
	//Companies color
	static Color airfranceColor = Color.red;
	static Color alitaliaColor = Color.green;
	static Color easyjetColor = Color.orange;
	static Color ryanairColor = Color.blue;
	
	//Flight statuses color
	static Color flightProgrammedColor = new Color(180, 180, 180, 150);
	static Color flightTakenOffColor = new Color(0, 180, 0, 150);
	static Color flightTakenOffLateColor = new Color(180, 180, 0, 150);
	static Color flightCancelledColor = new Color(180, 0, 0, 150);
	
	public static void main(String[] args) {
		
		//Import fonts
		try {
			
			fontOne = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/hgrsmp_0.TTF"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/hgrsmp_0.TTF")));
			
		}catch(IOException | FontFormatException e) {
			System.out.println(e);
		}
		
		//Set color palette
		MainController.setPaletteToDarkPalette();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Create MainController
					MainController controller = new MainController();
					
					//Create MainFrame and show it
					MainFrame mainFrame = new MainFrame(controller);
					mainFrame.setVisible(true); //Set it visible
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	/**
	 * Generate a random alphanumeric string of a specified length
	 * @param lenght Length of the string
	 * @return Generated string
	 */
	public String generateIDString(int length) {
		
	    int leftLimit = 48; //Numeral '0'
	    int rightLimit = 122; //Letter 'z'
	    int targetStringLength = length; //Length of the generated string 
	    
	    Random random = new Random(); //Create random instance

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString(); //Create string

	    return generatedString;
	    
	}

	/**
	 * Get a component by it's name
	 * @param container Outer container that contains the component
	 * @param name Name of the component to find
	 * @return The component if it is found, null otherwise
	 */
	public Component getComponentByName(Container container, String name) {
		
		List<Component> list = getAllComponents(container); //Take all components from the container
		
		for(Component c: list) {
			if(c.getName() != null && c.getName().contentEquals(name)) { //Component found
				return c;
			}
		}
		
		return null;
		
	}
	
	/**
	 * Get a list of the all the components contained in a specified container
	 * @param container Container  that the  user wants the components of
	 * @return List of the components contained in the passed container
	 */
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
	
	/**
	 * Checks if a gate is free in a given slot of time or if it's already in use at that moment
	 * @param s The new slot to check if its available
	 * @param gateNumber The gate number where the slot takes place
	 * @param volo If the check is being done updating a flight (set to null otherwise) gets it's ID and removes it from the list of IDs so the updated flight can have the same slot without returning true
	 * @return If the given gate is taken in the given slot of time
	 */
	public boolean checkIfSlotIsTaken(Slot s, int gateNumber, Volo volo) {
		
		ArrayList<String> idList = (new GateDAO().getFlightIdByGateNumber(gateNumber));
		System.out.println(idList);
		
		//If an id has been passed then we are updating a flight, therefore remove it from the array of IDs (If the gate of the new flight (gateNumber) is the same as the old one (volo.getGate().getNumeroGate()))
		if(volo != null && volo.getGate().getNumeroGate() == gateNumber) {
			int index = idList.indexOf(volo.getID()); //Get the index of the ID in the list
			if(index != -1) { //If the ID has been found (it always should be)
				idList.remove(index); //Remove it from the list
			}
		}
		
		for(String idString : idList) { //For all of the flights id where the gate number is the gate selected in the flight creation
			Slot slotToCheck = (new SlotDAO().getSlotByID(idString)); //Get the slot with that id
			
			/*
			 *  	|-----| slot to insert (passed as argument)
			 *   |-----|	  slot to check
			 */
			boolean condition1 = (slotToCheck.getInizioTempoStimato().before(s.getInizioTempoStimato()) && slotToCheck.getFineTempoStimato().after(s.getInizioTempoStimato()));
			
			/*
			 *  	|-----| 		  slot to insert (passed as argument)
			 *   		|-----|	  slot to check
			 */
			boolean condition2 = (slotToCheck.getInizioTempoStimato().before(s.getFineTempoStimato()) && slotToCheck.getFineTempoStimato().after(s.getFineTempoStimato()));
			
			/*
			 *  	|-----|   slot to insert (passed as argument)
			 *   	|-----|	  slot to check
			 */
			boolean condition3 = (slotToCheck.getInizioTempoStimato().compareTo(s.getInizioTempoStimato()) == 0 && slotToCheck.getFineTempoStimato().compareTo(s.getFineTempoStimato()) == 0); //Dates are the same

			
			if(condition1 || condition2 || condition3) { //If any of the above condition is true
				//The slot s is taken
				return true;
			}
		}
		
		return false; //The slot is not taken
		
	}
	
	static public void setPaletteToDarkPalette() {
		
		backgroundColorOne = darkBackgroundColorOne;
		backgroundColorTwo = darkBackgroundColorTwo;
		backgroundColorThree = darkBackgroundColorThree;
		
		foregroundColorOne = darkForegroundColorOne;
		foregroundColorTwo = darkForegroundColorTwo;
		foregroundColorThree = darkForegroundColorThree;
		
		highlightColorOne = darkHighlightColorOne;
		highlightColorTwo = darkHighlightColorTwo;
		highlightColorThree = darkHighlightColorThree;
		
	}
	
	
}
