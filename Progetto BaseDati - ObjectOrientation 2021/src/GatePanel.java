import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GatePanel extends JPanel{
	
	private int gateNumber;
	private MainFrame mainFrame;
	
	private int flightCount; //Number of flights in this gate
	
	private Color hoveringColor = new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 42); //Color of the rectangle being drawn on the panel when the mouse is hovering
	private int hoveringAlpha = hoveringColor.getAlpha(); //Alpha of the color of the rectangle being drawn on the panel when the mouse is hovering
	
	private Color borderColor; //Border color based on if the gate hosts any flight
	
	//Animation
	private hoveringAnimationStatus animationStatus = hoveringAnimationStatus.unselected; //Animation status
	private int animationHeight = 0; //Height of the rectangle being drawn on the panel when the mouse is hovering
	private int animationAlpha = 0; //Alpha of the rectangle being drawn on the panel when the mouse is hovering
	
	/**
	 * Panel showing the number of flights that take place in a given gate
	 * @param mainFrame Link to the MainController
	 * @param gateNumber Number that identifies the gate
	 */
	public GatePanel(MainFrame mainFrame, int gateNumber) {
		
		this.mainFrame = mainFrame;
		this.gateNumber = gateNumber;
		
		flightCount = mainFrame.getGateFromList(gateNumber).getNumeroVoli(); //Get number of flights in this gate
		
		//There are no flight with this gate
		if(flightCount == 0) {
			borderColor = MainController.flightCancelledColor;
		}else {
			borderColor = MainController.foregroundColorThree;
		}

		addMouseListener(new MouseAdapter() {
			//When mouse clicked
			public void mouseClicked(MouseEvent e) {
				if(flightCount > 0) {
					Thread queryThread = new Thread() {
					      public void run() {
					    	  mainFrame.changeContentPanel(new CheckGatePanel(new Rectangle(72, 2, 1124, 666), mainFrame, gateNumber), false, false);
					    	  unselectAnimation(8);
					      }
					};
				    queryThread.start();
				}else {
					mainFrame.createNotificationFrame("Non ci sono voli che utilizzano questo gate!");
				}
			}
		});
		
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
	
	public void paintComponent(Graphics g) {
		
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
	    
	    //Draw border
	    g2d.setColor(borderColor);
	    g2d.setStroke(new BasicStroke(3));
	    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, roundCornerAmount, roundCornerAmount);
		
		//Draw gate number
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 32));
	    String s = "G" + String.valueOf(gateNumber);
	    g2d.drawString(s, (getWidth()/2) - (g2d.getFontMetrics().stringWidth(s)/2), (getHeight()/2)); //Draw string in the middle of the panel
	    
	    //Draw flights amount
	    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, 16));
	    s = "Voli: " + flightCount;
	    g2d.drawString(s, (getWidth()/2) - (g2d.getFontMetrics().stringWidth(s)/2), (getHeight()/2) + 30);
	    
	    //Hovering animation rectangle
	    animationAlpha = (animationAlpha > 255)? 255 : animationAlpha; //Clamp max
		animationAlpha = (animationAlpha < 0)? 0 : animationAlpha; //Clamp minimum
		g2d.setColor(new Color(hoveringColor.getRed(), hoveringColor.getGreen(), hoveringColor.getBlue(), animationAlpha));
		g2d.fillRoundRect(0, (getHeight()/2) - (animationHeight/2), getWidth(), animationHeight, roundCornerAmount, roundCornerAmount);
		
	}
	
}
