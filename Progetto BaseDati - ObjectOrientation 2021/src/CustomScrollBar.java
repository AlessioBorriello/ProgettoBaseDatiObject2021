import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollBar extends JScrollBar {
	
	//Bar variables
	private int barWidth;
	private int buttonHeight;
	
	private Color backgroundColor;
	
	private Color thumbC;
	private boolean roundedThumb;
	
	private boolean thumbBorder;
	private Color thumbBorderColor;
	private int thumbBorderThickness;
	
	private boolean paintTrackHighlights;
	private Color trackHiglightsColor;
	
	//Buttons variables
	private Color buttonBackgroundColor;
	private Color buttonHoveringColor;
	private boolean buttonBorder;
	private Color buttonBorderColor;
	private int buttonBorderThickness;

	public CustomScrollBar(int barWidth, int buttonHeight, 
							Color backgroundColor, 
							Color thumbColor, 
							boolean roundedThumb, 
							boolean thumbBorder, Color thumbBorderColor, int thumbBorderThickness,
							boolean paintTrackHighlights, Color trackHiglightsColor,
							Color buttonBackgroundColor, Color buttonHoveringColor, boolean buttonBorder, Color buttonBorderColor, int buttonBorderThickness) {
		
		this.barWidth = barWidth;
		this.buttonHeight = buttonHeight;
		
		this.backgroundColor = backgroundColor;
		
		this.thumbC = thumbColor;
		this.roundedThumb = roundedThumb;
		
		this.thumbBorder = thumbBorder;
		this.thumbBorderColor = thumbBorderColor;
		this.thumbBorderThickness = thumbBorderThickness;
		
		this.paintTrackHighlights = paintTrackHighlights;
		this.trackHiglightsColor = trackHiglightsColor;
		
		this.buttonBackgroundColor = buttonBackgroundColor;
		this.buttonHoveringColor = buttonHoveringColor;
		this.buttonBorder = buttonBorder;
		this.buttonBorderColor = buttonBorderColor;
		this.buttonBorderThickness = buttonBorderThickness;
		
		setUI(new CustomScrollBarUI());
		
	}
	
	private class CustomScrollBarUI extends BasicScrollBarUI {
		
		@Override
		protected Dimension getMinimumThumbSize() {
			if(super.getMinimumThumbSize() == null) {
				return new Dimension(10, 10);
			}else {
				return super.getMinimumThumbSize();
			}
			
		}

		protected void installDefaults() {
			
			super.installDefaults(); //Call method normally first
			
			//Override bar width
	        scrollBarWidth = barWidth;
	        
	        //Get default's minimum and max thumb sizes
	        minimumThumbSize = (Dimension)UIManager.get("ScrollBar.minimumThumbSize");
	        maximumThumbSize = (Dimension)UIManager.get("ScrollBar.maximumThumbSize");
	        
	    }
		
		protected void installComponents() {
			
			//Create increase and decrease button
            incrButton = createIncreaseButton();
            decrButton = createDecreaseButton();
            
            //Add buttons
            scrollbar.add(incrButton);
	        scrollbar.add(decrButton);
	        
	        //Force the children's enabled state to be updated.
	        scrollbar.setEnabled(scrollbar.isEnabled());
	    }
		
		protected JButton createDecreaseButton() {
			
			CustomButton decreaseButton = (new CustomButton("", buttonBackgroundColor, buttonHoveringColor, null, 0, buttonBorder, buttonBorderColor, buttonBorderThickness) {
				
				public void paint(Graphics g) {
            		
            		super.paint(g); //Paint button normally first
            		
            		Graphics2D g2d = (Graphics2D)g;
            		
            		//AA
            	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            	    //Text AA
            	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            	    
            	    //Draw arrows
            	    g2d.setStroke(new BasicStroke(buttonBorderThickness));
            	    g2d.setColor(buttonBorderColor);
            	    
            	    //Up arrow
            	    int[] xPoints = {3, getWidth()/2, getWidth() - 3};
            	    int[] yPoints = {getHeight() - 3, 4, getHeight() - 3};
            	    g2d.drawPolyline(xPoints, yPoints, 3);
            		
            	}
				
			});
			decreaseButton.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					decreaseButton.selectAnimation(8);
				}
				public void mouseExited(MouseEvent e) {
					decreaseButton.unselectAnimation(8);
				}
			});
			return decreaseButton;
			
		}
		
		protected JButton createIncreaseButton() {
			
			CustomButton increaseButton = (new CustomButton("", buttonBackgroundColor, buttonHoveringColor, null, 0, buttonBorder, buttonBorderColor, buttonBorderThickness) {
				
				public void paint(Graphics g) {
            		
            		super.paint(g); //Paint button normally first
            		
            		Graphics2D g2d = (Graphics2D)g;
            		
            		//AA
            	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            	    //Text AA
            	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            	    
            	    //Draw arrows
            	    g2d.setStroke(new BasicStroke(buttonBorderThickness));
            	    g2d.setColor(buttonBorderColor);
            	    
            	    //Down arrow
            	    int[] xPoints = {3, getWidth()/2, getWidth() - 3};
            	    int[] yPoints = {3, getHeight() - 4, 3};
            	    g2d.drawPolyline(xPoints, yPoints, 3);
            		
            	}
				
			});
			increaseButton.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
						increaseButton.selectAnimation(8);
				}
				public void mouseExited(MouseEvent e) {
					increaseButton.unselectAnimation(8);
				}
			});
			return increaseButton;
			
		}
		
		protected void paintDecreaseHighlight(Graphics g) {
	        
			Insets insets = scrollbar.getInsets();
	        Rectangle thumbR = getThumbBounds();
	        g.setColor(trackHiglightsColor);

            //paint the distance between the start of the track and top of the thumb
            int x = insets.left;
            int y = trackRect.y;
            int w = scrollbar.getWidth() - (insets.left + insets.right);
            int h = thumbR.y - y;
            g.fillRect(x, y, w, h);
	    
		}
		
		protected void paintIncreaseHighlight(Graphics g) {
			
	        Insets insets = scrollbar.getInsets();
	        Rectangle thumbR = getThumbBounds();
	        g.setColor(trackHiglightsColor);

            //fill the area between the bottom of the thumb and the end of the track.
            int x = insets.left;
            int y = thumbR.y + thumbR.height;
            int w = scrollbar.getWidth() - (insets.left + insets.right);
            int h = trackRect.y + trackRect.height - y;
            g.fillRect(x, y, w, h);
	    
		}
		
		protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
	        
			//Draw scroll bar background
		 	g.setColor(backgroundColor);
	        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
	        
	        if(paintTrackHighlights) {
		        if(trackHighlight == DECREASE_HIGHLIGHT) {
		            paintDecreaseHighlight(g);
		        }
		        else if(trackHighlight == INCREASE_HIGHLIGHT) {
		            paintIncreaseHighlight(g);
		        }
	        }
	    
		}
		
		protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
			
			Graphics2D g2d = (Graphics2D)g;
			
			//AA
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			//Text AA
		    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
	        if(thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
	            return;
	        }
	        
	        //Get thumb's bounds
	        int w = thumbBounds.width;
	        int h = thumbBounds.height;

	        g2d.translate(thumbBounds.x, thumbBounds.y);
	        g2d.setColor(thumbC);
	        
	        //Draw thumb
	        if(roundedThumb) {
	        	g2d.fillRoundRect(0, 0, w - 1, h - 1, barWidth, barWidth);
	        }else {
	        	g2d.fillRect(0, 0, w - 1, h - 1);
	        }
	        
	        //Draw border
	        if(thumbBorder) {
	        	g2d.setColor(thumbBorderColor);
	        	g2d.setStroke(new BasicStroke(thumbBorderThickness));
	        	if(roundedThumb) {
		        	g2d.drawRoundRect(0, 0, w - 1, h - 1, barWidth, barWidth);
		        }else {
		        	g2d.drawRect(0, 0, w - 1, h - 1);
		        }
	        }
	    
	    }
		
		protected void layoutVScrollbar(JScrollBar sb) {
	        
			super.layoutVScrollbar(sb); //Call method normally first
			
			//Override buttons bounds
			incrButton.setBounds(0, scrollbar.getHeight() - 15, decrButton.getBounds().width, buttonHeight);
			decrButton.setBounds(0, 0, decrButton.getBounds().width, buttonHeight);
			
			//Set the gaps that limit the thumb movement
			decrGap = buttonHeight;
			incrGap = buttonHeight;
			
		}
	
	
	}

}
