import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSpinnerUI;

public class CustomSpinner extends JSpinner{
	
	private Color buttonBackgroundColor; //Color of the background of the button on the spinner
	private Color arrowColor; //Color of the arrow on the buttons
	private Color buttonHoveringColor; //Color of the rectangle being drawn on the button on spinner when the mouse hovers over it
	private int arrowThickness; //Thickness  of the arrow on the button
	private int buttonWidth = 15; //Width of the button
	
	private boolean border = false; //If the spinner should have a border
	private Color borderColor; //Color of said border
	
	/**
	 * Create a spinner with a custom UI
	 * @param buttonBackgroundColor Color of the background of the button on the spinner
	 * @param buttonHoveringColor Color of the rectangle being drawn on the button on spinner when the mouse hovers over it
	 * @param arrowColor Color of the arrow on the buttons
	 * @param arrowThickness Thickness  of the arrow on the button
	 * @param border If the spinner should have a border
	 * @param borderColor Color of the border
	 * @param borderThickness Thickness of the border
	 */
	public CustomSpinner(Color buttonBackgroundColor, Color buttonHoveringColor, Color arrowColor, int arrowThickness, boolean border, Color borderColor, int borderThickness) {
        
		
		this.buttonBackgroundColor = buttonBackgroundColor;
		this.arrowColor = arrowColor;
		this.buttonHoveringColor =  buttonHoveringColor;
		this.arrowThickness = arrowThickness;
		
		this.border = border;
		this.borderColor = borderColor;
		
		
		setUI(new CustomSpinnerUI()); //Implement custom UI
		setBorder(new LineBorder(borderColor, borderThickness)); //Set border
		
    }
	
	public void paint(Graphics g) {
		
		super.paint(g); //Paint normally first
		
		Graphics2D g2d = (Graphics2D)g;
		
		//AA
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	    //Text AA
	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    
	    //Draw a semi-transparent black rectangle if the spinner is not enabled
	    if(!isEnabled()) {
	    	g2d.setColor(new Color(0, 0, 0, 168));
	    	g2d.fillRect(0, 0, getWidth(), getHeight());
	    }
		
	}
	
	/**
	 * Set the color of the background of the editor of the spinner
	 * @param c Color of the background
	 */
	public void setEditorBackgroundColor(Color c) {
		getEditor().getComponent(0).setBackground(c); //Access editor's text field (getComponent(0)) and set it's background
	}
	
	/**
	 * Set the color of the foreground of the editor of the spinner
	 * @param c Color of the foreground
	 */
	public void setEditorForegroundColor(Color c) {
		getEditor().getComponent(0).setForeground(c); //Access editor's text field (getComponent(0)) and set it's foreground
	}
	
	/**
	 * Set the font of the editor of the spinner
	 * @param f Font of the editor
	 */
	public void setEditorFont(Font f) {
		getEditor().getComponent(0).setFont(f);; //Access editor's text field (getComponent(0)) and set it's font
	}
	
	//Create custom spinner UI being implemented by the custom spinner
	private class CustomSpinnerUI extends BasicSpinnerUI {

        protected Component createPreviousButton() {
        	
        	//Create button with the down arrow pointing down
        	Component component = createButton(spinnerButtonArrowDirection.down);
            
            if (component != null) { //If the button has been created
                installPreviousButtonListeners(component); //Install the button as the 'previous button' (the bottom one)
            }
            
            return component;
        	
        }

        protected Component createNextButton() {
        	
        	//Create button with the down arrow pointing up
        	Component component = createButton(spinnerButtonArrowDirection.up);
        	
            if (component != null) { //If the button has been created
                installNextButtonListeners(component); //Install the button as the 'next button' (the upper one)
            }
            
            return component;
        	
        }

        /**
         * Create a button with an arrow pointing up or down to be installed in the custom spinner UI
         * @param direction Direction the arrow should point at, up or down
         * @return The button
         */
        private Component createButton(spinnerButtonArrowDirection direction) {
        	
            CustomButton button = (new CustomButton("", buttonBackgroundColor, buttonHoveringColor, arrowColor, 2, border, borderColor, 2) {
            	
            	public void paint(Graphics g) {
            		
            		super.paint(g); //Paint button normally first
            		
            		Graphics2D g2d = (Graphics2D)g;
            		
            		//AA
            	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            	    //Text AA
            	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            	    
            	    //Draw arrows
            	    g2d.setStroke(new BasicStroke(arrowThickness));
            	    g2d.setColor(arrowColor);
            	    
            	    if(direction == spinnerButtonArrowDirection.up) {
	            	    //Up arrow
	            	    int[] xPoints = {3, getWidth()/2, getWidth() - 3};
	            	    int[] yPoints = {getHeight() - 3, 3, getHeight() - 3};
	            	    g2d.drawPolyline(xPoints, yPoints, 3);
            	    }else {
            	    	//Down arrow
	            	    int[] xPoints = {3, getWidth()/2, getWidth() - 3};
	            	    int[] yPoints = {3, getHeight() - 3, 3};
	            	    g2d.drawPolyline(xPoints, yPoints, 3);
            	    }
            		
            	}
            	
            });
            button.setPreferredSize(new Dimension(buttonWidth, 4)); //Set button size
            button.addMouseListener(new MouseAdapter() {
    			public void mouseEntered(MouseEvent e) {
    				if(isEnabled()) { //Animate button only if the spinner is enabled
    					button.selectAnimation(8);
    				}
    			}
    			public void mouseExited(MouseEvent e) {
    				if(isEnabled()) { //Animate button only if the spinner is enabled
    					button.unselectAnimation(8);
    				}
    			}
    		});
            return button;
            
        }

    }

}

//Direction of the arrow on the button
enum spinnerButtonArrowDirection {
	up,
	down
}
