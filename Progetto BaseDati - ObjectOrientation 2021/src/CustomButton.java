import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class CustomButton extends JButton{
	
	private String s; //String to display in the middle of the button
	
	private Color backgroundColor; //Color of the button
	
	private Color hoveringColor; //Color of the rectangle being drawn on the button when the mouse is hovering
	private int hoveringAlpha; //Alpha of the color of the rectangle being drawn on the button when the mouse is hovering
	
	private Color fontColor; //Color of the font
	private int fontSize; //Size of the font
	private int finalFontSize; //Size of the font after the hovering animation is completed
	
	private boolean border = false; //If the button should have a border
	private Color borderColor; //Color of the border
	private int borderThickness; //Thickness of the border
	
	//Animation
	private hoveringAnimationStatus animationStatus = hoveringAnimationStatus.unselected; //Animation status
	private int animationHeight = 0; //Height of the rectangle being drawn on the button when the mouse is hovering
	private int animationAlpha = 0; //Alpha of the rectangle being drawn on the button when the mouse is hovering
	private float animationFontSize = 0; //Amount to add to the font size to reach the finalFontSize
	
	/**
	 * Create a button with a custom UI
	 * @param s String to display in the middle of the button
	 * @param backgroundColor Color of the button
	 * @param hoveringColor Color of the rectangle being drawn on the button when the mouse is hovering
	 * @param fontColor Color of the font
	 * @param fontSize Size of the font
	 * @param border If the button should have a border
	 * @param borderColor Color of the border
	 * @param borderThickness Thickness of the border
	 */
	public CustomButton(String s, Color backgroundColor, Color hoveringColor, Color fontColor, int fontSize, boolean border, Color borderColor, int borderThickness) {
		
		this.s = s;
		
		this.backgroundColor = backgroundColor;
		
		this.hoveringColor = hoveringColor;
		hoveringAlpha =  hoveringColor.getAlpha(); //Get hovering color's alpha
		
		this.fontColor = fontColor;
		this.fontSize = fontSize;
		this.finalFontSize = (int)(fontSize * 1.2); //Final font size is 120% of the font size
		
		this.border = border;
		this.borderColor = borderColor;
		this.borderThickness = borderThickness;
		
		setFocusable(false); //Non focusable
		setContentAreaFilled(false); //Don't draw the content area
		setBorder(null); //Set border to null
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Hand cursor when mouse passes on the button
		
	}
	
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		//Background
		if(backgroundColor != null) {
			g2d.setColor(backgroundColor);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
		
		//String
		g2d.setColor(fontColor);
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, fontSize + (int)animationFontSize));
		int sLenght = g2d.getFontMetrics().stringWidth(s);
		g2d.drawString(s, (getWidth()/2) - (sLenght/2), (getHeight()/2) + ((fontSize + animationFontSize)/2) - 4);
		
		//Draw hovering animation rectangle
		animationAlpha = (animationAlpha > 255)? 255 : animationAlpha; //Clamp max (alpha can't be > 255)
		animationAlpha = (animationAlpha < 0)? 0 : animationAlpha; //Clamp minimum (alpha can't be < 0)
		g2d.setColor(new Color(hoveringColor.getRed(), hoveringColor.getGreen(), hoveringColor.getBlue(), animationAlpha));
		g2d.fillRect(0, (getHeight()/2) - (animationHeight/2), getWidth(), animationHeight);
		
		//Border
		if(border) {
			
			g2d.setColor(borderColor);
			g2d.setStroke(new BasicStroke(borderThickness));
			g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
			
		}
		
	}
	
	/**
	 * Select animation when the mouse hovers on the button
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
					
					//Animate alpha
					int alphaAmount = hoveringAlpha/frames; //How much to increase alpha each pass
					if(alphaAmount < 1) {
						alphaAmount = 1;
					}
					animationAlpha = (animationAlpha > 255)? 255 : animationAlpha; //Clamp to 255 max
					animationAlpha += alphaAmount;
					
					//Animate font size
					float fontSizeAmount = (float)(finalFontSize - fontSize)/frames; //How much to increase font size each pass
					if(fontSizeAmount < .1f) {
						fontSizeAmount = .1f;
					}
					animationFontSize += fontSizeAmount; //Increase animation font size
					
					repaint();
					
					//If animation is interrupted (it's status is no longer selecting)
					if(animationStatus != hoveringAnimationStatus.selecting) {
						
						this.cancel(); //Stop animation
						
					}else if(animationHeight >= getHeight()) { //If the animation height gets to the button height
						
						animationHeight = getHeight(); //Set the animation height to the button height
						animationFontSize = (finalFontSize - fontSize); //Set animation font size to it's final value
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
	 * Unselect animation when the mouse hovers on the button
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
					
					//Animate font size
					float fontSizeAmount = (float)(finalFontSize - fontSize)/frames; //How much to decrease font size each pass
					if(fontSizeAmount < .1f) {
						fontSizeAmount = .1f;
					}
					animationFontSize -= fontSizeAmount; //Decrease animation font size
					
					repaint();
					
					//If animation is interrupted (it's status is no longer selecting)
					if(animationStatus != hoveringAnimationStatus.unselecting) {
						
						this.cancel(); //Stop animation
						
					}else if(animationHeight <= 0) { //If the animation height gets to 0
						
						animationHeight = 0; //Set the animation height to 0
						animationFontSize = 0; //Set animation font size to 0
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

	/**
	 * Set the color of the background
	 * @param c Color of the background for the button
	 */
	public void setButtonBackgroundColor(Color c) {
		this.backgroundColor = c;
	}

}
