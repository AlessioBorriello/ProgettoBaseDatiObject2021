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
	
	private Color buttonBackgroundColor;
	private Color arrowColor;
	private Color buttonHoveringColor;
	private int arrowThickness;
	private int buttonWidth = 15;
	
	private boolean border = false;
	private Color borderColor;
	private int borderThickness;
	
	private int width;
	private int height;
	
	public CustomSpinner(Color buttonBackgroundColor, Color buttonHoveringColor, Color arrowColor, int arrowThickness, boolean border, Color borderColor, int borderThickness) {
        
		
		this.buttonBackgroundColor = buttonBackgroundColor;
		this.arrowColor = arrowColor;
		this.buttonHoveringColor =  buttonHoveringColor;
		this.arrowThickness = arrowThickness;
		
		this.border = border;
		this.borderColor = borderColor;
		this.borderThickness = borderThickness;
		
		this.width = getPreferredSize().width;
		this.height = getPreferredSize().height;
		
		setUI(new CustomSpinnerUI()); //Set the custom UI
		setBorder(new LineBorder(borderColor, borderThickness));
		
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
	
	public void setEditorBackgroundColor(Color c) {
		getEditor().getComponent(0).setBackground(c); //Access editor's text field (getComponent(0)) and set it's background
	}
	
	public void setEditorForegroundColor(Color c) {
		getEditor().getComponent(0).setForeground(c); //Access editor's text field (getComponent(0)) and set it's foreground
	}
	
	public void setEditorFont(Font f) {
		getEditor().getComponent(0).setFont(f);; //Access editor's text field (getComponent(0)) and set it's font
	}
	
	//Create custom UI
	private class CustomSpinnerUI extends BasicSpinnerUI {

        protected Component createPreviousButton() {
        	
        	Component component = createButton(spinnerButtonArrowDirection.down);
            super.createPreviousButton();
            if (component != null) {
                installPreviousButtonListeners(component);
            }
            return component;
        	
        }

        protected Component createNextButton() {
        	
        	Component component = createButton(spinnerButtonArrowDirection.up);
            if (component != null) {
                installNextButtonListeners(component);
            }
            return component;
        	
        }

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
            button.setPreferredSize(new Dimension(buttonWidth, 4));
            button.addMouseListener(new MouseAdapter() {
    			public void mouseEntered(MouseEvent e) {
    				if(isEnabled()) {
    					button.selectAnimation(8);
    				}
    			}

    			public void mouseExited(MouseEvent e) {
    				if(isEnabled()) {
    					button.unselectAnimation(8);
    				}
    			}
    		});
            return button;
            
        }

    }

}

enum spinnerButtonArrowDirection {
	up,
	down
}
