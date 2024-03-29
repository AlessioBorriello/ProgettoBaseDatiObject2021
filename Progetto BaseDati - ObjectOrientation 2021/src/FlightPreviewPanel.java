import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

@SuppressWarnings("serial")
public class FlightPreviewPanel extends JPanel {
	
	private MainFrame mainFrame; //Main panel

	//Flight instance to show its info
	private Volo volo;
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm"); //Date format
	
	//Dimensions of panel
	private int width = 255;
	private int height = 255;
	
	private Color hoveringColor = new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 42); //Color of the rectangle being drawn on the panel when the mouse is hovering
	private int hoveringAlpha = hoveringColor.getAlpha(); //Alpha of the color of the rectangle being drawn on the panel when the mouse is hovering
	
	//Flight status
	private Color borderColor; //Border color based on the flight status
	private String statusString; //String based on the flight status
	
	//Company image
	private Image companyLogoImage;
	
	//Animation
	private hoveringAnimationStatus animationStatus = hoveringAnimationStatus.unselected; //Animation status
	private int animationHeight = 0; //Height of the rectangle being drawn on the panel when the mouse is hovering
	private int animationAlpha = 0; //Alpha of the rectangle being drawn on the panel when the mouse is hovering

	/**
	 * Panel showing the preview info of a given flight
	 * @param mf Link to the MainFrame
	 * @param v Flight to show the info of
	 * @param companyLogoImage Image of the company of the flight
	 */
	public FlightPreviewPanel(MainFrame mf, Volo v, Image companyLogoImage) {
		
		mainFrame = mf; //Link main frame
		//mainController = c; //Link main controller
		volo = v; //Flight instance to show its info
		
		//Determine border color and flight status string
		borderColor = getBorderColor();
		statusString = getStatusString();
		
		//Get company image path
		this.companyLogoImage = companyLogoImage;
		
		
		setSize(width, height); //Set panel size
		setBackground(new Color(0, 0, 0, 0)); //Set background color (invisible background)
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Set cursor to hand when hovering
		setLayout(null);
		
		//Panel action listener
		addMouseListener(new MouseAdapter() {
			//Mouse clicked
			public void mouseClicked(MouseEvent e) {
				//Change panel to the ViewFlightPanel
				unselectAnimation(8);
				mainFrame.changeContentPanel(new ViewFlightInfoPanel(new Rectangle(72, 2, 1124, 666), mainFrame, volo), false, false);
			}
			//Mouse entered
			public void mouseEntered(MouseEvent e) {
				selectAnimation(12);
			}
			//Mouse exited
			public void mouseExited(MouseEvent e) {
				unselectAnimation(12);
			}
		});

	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g); //Paint the component normally first
		
		Graphics2D g2d = (Graphics2D)g;
		int roundCornerAmount = 45;
		
		//AA
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    //Text AA
	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    
	    //Draw background
	    GradientPaint gPaint = new GradientPaint(getWidth()/2, 0, MainController.backgroundColorTwo, getWidth()/2, getHeight() + 200, MainController.backgroundColorThree);
	    g2d.setPaint(gPaint);
	    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), roundCornerAmount, roundCornerAmount);
	    
	    //Draw border effect
	    g2d.setColor(borderColor);
	    g2d.setStroke(new BasicStroke(3));
	    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, roundCornerAmount, roundCornerAmount);
	    
	    //Draw border feather effect
	    boolean featherEffect = false;
	    if(featherEffect) {
		    g2d.setStroke(new BasicStroke(1));
		    int borderPasses = 6; //Feather pixels
		    int alphaAmount = borderColor.getAlpha()/borderPasses;
		    for(int i = 0; i < borderPasses; i++) {
		    	
		    	g2d.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), borderColor.getAlpha() - (alphaAmount * i)));
			    g2d.drawRoundRect(0 + i, 0 + i, getWidth() - 1 - (i*2), getHeight() - 1 - (i*2), roundCornerAmount, roundCornerAmount);
		    }
	    }
	    
	    //Draw company image
	    g2d.drawImage(companyLogoImage, 15, 15, 107, 107, 0, 0, companyLogoImage.getWidth(null), companyLogoImage.getHeight(null), this);
	    g2d.setStroke(new BasicStroke(3));
	    g2d.setColor(MainController.foregroundColorThree);
	    g2d.drawRoundRect(15, 15, 90, 90, 32, 32);
	    
	    //Draw flight info
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 14));
	    g2d.setColor(MainController.foregroundColorThree);
	    g2d.drawString("ID: " + volo.getID(), 116, 55);
	    //g2d.drawString("Volo per:", 116 + ((g2d.getFontMetrics(getFont()).stringWidth("Volo per:"))/2), 65);
	    g2d.drawString("Per: " + volo.getDestinazione(), 116, 85);
	    g2d.drawString("Compagnia: " + volo.getCompagnia().getNome(), 15, 130);
	    g2d.drawString("Partenza: " + dateTimeFormat.format(volo.getOrarioDecollo()), 15, 152);
	    g2d.drawString("Gate: " + volo.getGate().getNumeroGate(), 15, 174);
	    g2d.drawString("Prenotazioni: " + volo.getNumeroPrenotazioni(), 15, 196);
	    
	    //Draw flight status
	    g2d.drawString(statusString, (getWidth()/2) - (g2d.getFontMetrics().stringWidth(statusString)/2), getHeight() - 16);
	    
	    //Hovering animation rectangle
	    animationAlpha = (animationAlpha > 255)? 255 : animationAlpha; //Clamp max
		animationAlpha = (animationAlpha < 0)? 0 : animationAlpha; //Clamp minimum
		g2d.setColor(new Color(hoveringColor.getRed(), hoveringColor.getGreen(), hoveringColor.getBlue(), animationAlpha));
		g2d.fillRoundRect(0, (getHeight()/2) - (animationHeight/2), getWidth(), animationHeight, roundCornerAmount, roundCornerAmount);
	    
	}
	
	/**
	 * Calculate the color of the border of the panel based on the flight's status, programmed, taken off, taken off late and cancelled
	 * @return The color of the border of the panel representing the flight's status
	 */
	public Color getBorderColor() {
		
		if(volo.isPartito()) {
			if(volo.checkIfFlightTookOffLate()) {
				return MainController.flightTakenOffLateColor; //Taken off late
			}else {
				return MainController.flightTakenOffColor; //Taken off
			}
		}else {
			if(volo.isCancellato()) {
				return MainController.flightCancelledColor; //Cancelled
			}else {
				return MainController.flightProgrammedColor; //Programmed
			}
		}
		
	}
	
	/**
	 * Calculate the string showing the status of the flight, programmed, taken off, taken off late and cancelled
	 * @return The string containing the status of the flight
	 */
	public String getStatusString() {
		
		if(volo.isPartito()) { //If the flight has taken off
			if(volo.checkIfFlightTookOffLate()) { //Check if it did so late
				return "Volo partito in ritardo";
			}else { //Otherwise
				return "Volo partito";
			}
		}else { //The flight has not taken off
			if(volo.isCancellato()) { //Check if it has been cancelled
				return "Volo cancellato";
			}else { //Otherwise
				return "Volo non partito";
			}
		}
		
	}

	/**
	 * Select animation when the mouse hovers on the panel
	 * @param frames How many frames the animation should take
	 */
	public void selectAnimation(int frames) {

		//If not already selected
		if(animationStatus != hoveringAnimationStatus.selected) {
			
			animationStatus = hoveringAnimationStatus.selecting;
			class animationSelecting extends TimerTask { //Create animation class
				
				//Override run method
				public void run() {
					
					//Animate height
					int heightAmount = getHeight()/frames; //How much to increase height each pass
					if(heightAmount < 1) {
						heightAmount = 1;
					}
					animationHeight += heightAmount; //Increase animation height
					animationHeight = (animationHeight > getHeight())? getHeight() : animationHeight; //Clamp to height max
					
					//Animate alpha
					int alphaAmount = hoveringAlpha/frames; //How much to increase alpha each pass
					if(alphaAmount < 1) {
						alphaAmount = 1;
					}
					animationAlpha = (animationAlpha > 255)? 255 : animationAlpha; //Clamp to 255 max
					animationAlpha += alphaAmount;
					
					mainFrame.repaint();
					
					//If animation is interrupted (it's status is no longer selecting)
					if(animationStatus != hoveringAnimationStatus.selecting) {
						
						this.cancel(); //Stop animation
						
					}else if(animationHeight >= getHeight()) { //If the animation height gets to the button height
						
						animationHeight = getHeight(); //Set the animation height to the button height
						animationStatus = hoveringAnimationStatus.selected; //Set status to extended status (animation complete)
						this.cancel(); //Stop animation
						
					}
					
				}
				
			}
		
			animationSelecting animation = new animationSelecting(); //Create animation instance
			Timer t = new Timer(); //Create timer
			t.schedule(animation, 10, 10); //Schedule the animation task in the timer with a delay of 10ms and a repeat of 10ms
			
		}
		
	}
	
	/**
	 * Unselect animation when the mouse hovers on the panel
	 * @param frames How many frames the animation should take
	 */
	public void unselectAnimation(int frames) {
		
		//If not already selected
		if(animationStatus != hoveringAnimationStatus.unselected) {
			
			animationStatus = hoveringAnimationStatus.unselecting;
			class animationUnselecting extends TimerTask { //Create animation class
				
				//Override run method
				public void run() {
					
					//Animate height
					int heightAmount = getHeight()/frames; //How much to decrease height each pass
					if(heightAmount < 1) {
						heightAmount = 1;
					}
					animationHeight -= heightAmount; //Decrease animation height
					
					//Animate alpha
					int alphaAmount = hoveringAlpha/frames; //How much to decrease alpha each pass
					if(alphaAmount < 1) {
						alphaAmount = 1;
					}
					animationAlpha = (animationAlpha < 0)? 0 : animationAlpha; //Clamp to 0 minimum
					animationAlpha -= alphaAmount;
					
					mainFrame.repaint();
					
					//If animation is interrupted (it's status is no longer selecting)
					if(animationStatus != hoveringAnimationStatus.unselecting) {
						
						this.cancel(); //Stop animation
						
					}else if(animationHeight <= 0) { //If the animation height gets to 0
						
						animationHeight = 0; //Set the animation height to 0
						animationStatus = hoveringAnimationStatus.unselected; //Set status to extended status (animation complete)
						this.cancel(); //Stop animation
						
					}
					
				}
				
			}
		
			animationUnselecting animation = new animationUnselecting(); //Create animation instance
			Timer t = new Timer(); //Create timer
			t.schedule(animation, 10, 10); //Schedule the animation task in the timer with a delay of 10ms and a repeat of 10ms
			
		}
		
	}

}
