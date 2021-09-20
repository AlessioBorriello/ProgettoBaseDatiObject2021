import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;


public class MainController {
	
	static String URL = "jdbc:postgresql://localhost:5432/aeroporto"; //Database URL
	static String PASSWORD = "password"; //Password
	static String USER = "postgres"; //User name
	
	//Number of gates in the airport
	static int gateAirportNumber = 12;
	
	//Fonts
	static Font fontOne;
	
	//Dark palette
	static Color darkBackgroundColorOne = new Color(29, 35, 39);
	static Color darkBackgroundColorTwo = new Color(33, 51, 64);
	static Color darkBackgroundColorThree = new Color(32, 32, 54);
	
	static Color darkForegroundColorOne = new Color(15, 76, 117);
	static Color darkForegroundColorTwo = new Color(50, 130, 184);
	static Color darkForegroundColorThree = new Color(207, 245, 255);
	
	//Light palette
	static Color lightBackgroundColorOne = new Color(15, 76, 117);
	static Color lightBackgroundColorTwo = new Color(50, 130, 184);
	static Color lightBackgroundColorThree = new Color(207, 245, 255);
	
	static Color lightForegroundColorOne = new Color(29, 35, 39);
	static Color lightForegroundColorTwo = new Color(33, 51, 64);
	static Color lightForegroundColorThree = new Color(32, 32, 54);

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
	static Color airfranceColor = new Color(180, 0, 0);
	static Color alitaliaColor = new Color(0, 180, 0);
	static Color easyjetColor = new Color(255, 140, 0);
	static Color ryanairColor = new Color(0, 0, 180);
	
	//Flight statuses color
	static Color flightProgrammedColor; //Set as foregroundColorThree in palette assignment
	static Color flightTakenOffColor = new Color(0, 180, 0, 150);
	static Color flightTakenOffLateColor = new Color(234, 234, 0, 150);
	static Color flightCancelledColor = new Color(180, 0, 0, 150);
	
	public static void main(String[] args) {
		
		//Import and register font
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
	 * Set the color palette to the dark palette
	 */
	static public void setPaletteToDarkPalette() {
		
		flightProgrammedColor = darkForegroundColorThree;
		
		backgroundColorOne = darkBackgroundColorOne;
		backgroundColorTwo = darkBackgroundColorTwo;
		backgroundColorThree = darkBackgroundColorThree;
		
		foregroundColorOne = darkForegroundColorOne;
		foregroundColorTwo = darkForegroundColorTwo;
		foregroundColorThree = darkForegroundColorThree;
		
	}
	
	/**
	 * Set the color palette to the light palette
	 */
	static public void setPaletteToLightPalette() {
		
		flightProgrammedColor = lightForegroundColorThree;
		
		backgroundColorOne = lightBackgroundColorThree;
		backgroundColorTwo = lightBackgroundColorTwo;
		backgroundColorThree = lightBackgroundColorOne;
		
		foregroundColorOne = lightForegroundColorThree;
		foregroundColorTwo = lightForegroundColorTwo;
		foregroundColorThree = lightForegroundColorOne;
		
	}
	
}
