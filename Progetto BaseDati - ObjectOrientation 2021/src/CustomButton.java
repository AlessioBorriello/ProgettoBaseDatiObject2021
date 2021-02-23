import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;

public class CustomButton extends JButton{
	
	private String s;
	
	private Color backgroundColor;
	
	private Color hoveringColor;
	private int hoveringAlpha;
	
	private Color fontColor;
	private int fontSize;
	private int finalFontSize;
	
	private boolean border = false;
	private Color borderColor;
	private int borderThickness;
	
	//Animation
	private buttonAnimationStatus animationStatus = buttonAnimationStatus.unselected;
	private int animationHeight = 0;
	private int animationAlpha = 0;
	private float animationFontSize = 0;
	
	public CustomButton(String s, Color backgroundColor, Color hoveringColor, Color fontColor, int fontSize, boolean border, Color borderColor, int borderThickness) {
		
		this.s = s;
		
		this.backgroundColor = backgroundColor;
		
		this.hoveringColor = hoveringColor;
		hoveringAlpha =  hoveringColor.getAlpha();
		
		this.fontColor = fontColor;
		this.fontSize = fontSize;
		this.finalFontSize = (int)(fontSize * 1.2);
		
		this.border = border;
		this.borderColor = borderColor;
		this.borderThickness = borderThickness;
		
		setFocusable(false);
		setContentAreaFilled(false);
		setBorder(null);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
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
		
		//Hovering animation rectangle
		animationAlpha = (animationAlpha > 255)? 255 : animationAlpha; //Clamp max
		animationAlpha = (animationAlpha < 0)? 0 : animationAlpha; //Clamp minimum
		g2d.setColor(new Color(hoveringColor.getRed(), hoveringColor.getGreen(), hoveringColor.getBlue(), animationAlpha));
		g2d.fillRect(0, (getHeight()/2) - (animationHeight/2), getWidth(), animationHeight);
		
		//Border
		if(border) {
			
			g2d.setColor(borderColor);
			g2d.setStroke(new BasicStroke(borderThickness));
			g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
			
		}
		
	}
	
	public void selectAnimation(int frames) {

		//If not already selected
		if(animationStatus != buttonAnimationStatus.selected) {
			
			animationStatus = buttonAnimationStatus.selecting;
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
					if(animationStatus != buttonAnimationStatus.selecting) {
						
						this.cancel(); //Stop animation
						
					}else if(animationHeight >= getHeight()) { //If the animation height gets to the button height
						
						animationHeight = getHeight(); //Set the animation height to the button height
						animationFontSize = (finalFontSize - fontSize); //Set animation font size to it's final value
						animationStatus = buttonAnimationStatus.selected; //Set status to extended status (animation complete)
						this.cancel(); //Stop animation
						
					}
					
				}
				
			}
		
			animationSelecting animation = new animationSelecting(); //Create animation instance
			Timer t = new Timer(); //Create timer
			t.schedule(animation, 10, 10); //Schedule the animation task in the timer with a delay of 10ms and a repeat of 10ms
			
		}
		
	}
	
	public void unselectAnimation(int frames) {
		
		//If not already selected
		if(animationStatus != buttonAnimationStatus.unselected) {
			
			animationStatus = buttonAnimationStatus.unselecting;
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
					if(animationStatus != buttonAnimationStatus.unselecting) {
						
						this.cancel(); //Stop animation
						
					}else if(animationHeight <= 0) { //If the animation height gets to 0
						
						animationHeight = 0; //Set the animation height to 0
						animationFontSize = 0; //Set animation font size to 0
						animationStatus = buttonAnimationStatus.unselected; //Set status to extended status (animation complete)
						this.cancel(); //Stop animation
						
					}
					
				}
				
			}
		
			animationUnselecting animation = new animationUnselecting(); //Create animation instance
			Timer t = new Timer(); //Create timer
			t.schedule(animation, 10, 10); //Schedule the animation task in the timer with a delay of 10ms and a repeat of 10ms
			
		}
		
	}

	public void setButtonBackgroundColor(Color c) {
		this.backgroundColor = c;
	}

}


enum buttonAnimationStatus {
	
	selected, //Mouse hovering on button, animation finished
	selecting, //Mouse hovering on button, animation in progress
	unselected, //Mouse not hovering on button, animation finished
	unselecting //Mouse not hovering on button, animation in progress
	
}
