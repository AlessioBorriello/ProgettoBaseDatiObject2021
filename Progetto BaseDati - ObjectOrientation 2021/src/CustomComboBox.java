import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ComboBoxEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

public class CustomComboBox extends JComboBox<Object> {
	
	Color borderColor; //Color of the border
	int borderThickness; //Thickness of the border
	
	Color buttonBackgroundColor; //Background color of the button on the side of the combo box
	Color buttonHoveringColor; //Color of the rectangle being drawn on the button on the side of the combo box when the mouse hovers over it
	
	JScrollBar customScrollbar; //Scroll bar of the combo box
	
	Color unselectedItemBackgroundColor; //Color of the background of an unselected item
	Color unselectedItemForegroundColor; //Color of the foreground of an unselected item
	
	Font font; //Font used in the combo box
	
	/**
	 * Create a combo box with a custom UI
	 * @param borderColor Color of the border
	 * @param borderThickness Thickness of the border
	 * @param buttonBackgroundColor Color of the background of the button on the combo box
	 * @param buttonHoveringColor Color of the rectangle being drawn on the button on the combo box when the mouse is hovering
	 * @param customScrollbar Scroll bar of the combo box pop up
	 * @param unselectedItemBackgroundColor Color of the background of an unselected item (Mouse not hovering on it in the pop up)
	 * @param unselectedItemForegroundColor Color of the foreground of an unselected item (Mouse not hovering on it in the pop up)
	 * @param font Font of the combo box
	 */
	public CustomComboBox(Color borderColor, int borderThickness, Color buttonBackgroundColor, Color buttonHoveringColor, JScrollBar customScrollbar, Color unselectedItemBackgroundColor, Color unselectedItemForegroundColor, Font font) {
		
		this.borderColor = borderColor;
		this.borderThickness = borderThickness;
		
		this.buttonBackgroundColor = buttonBackgroundColor;
		this.buttonHoveringColor = buttonHoveringColor;
		
		this.customScrollbar = customScrollbar;
		
		this.unselectedItemBackgroundColor = unselectedItemBackgroundColor;
		this.unselectedItemForegroundColor = unselectedItemForegroundColor;
		
		this.font = font;
		
		setBorder(new LineBorder(borderColor, borderThickness)); //Draw border
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Hand cursor when mouse passes on the combo box
		setUI(new CustomComboBoxUI()); //Implement custom UI
		
	}
	
	//Custom combo box UI being implemented by the combo box
	private class CustomComboBoxUI extends BasicComboBoxUI {
		
		protected JButton createArrowButton() {
			
			//Create custom button
			CustomButton button = (new CustomButton("", buttonBackgroundColor,  buttonHoveringColor, null, 0, true, borderColor, borderThickness - 1) {
				
				public void paint(Graphics g) {
            		
            		super.paint(g); //Paint button normally first
            		
            		Graphics2D g2d = (Graphics2D)g;
            		
            		//AA
            	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            	    //Text AA
            	    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            	    
            	    //Draw arrow
            	    g2d.setStroke(new BasicStroke(borderThickness));
            	    g2d.setColor(borderColor);
            	    
            	    //Down arrow
            	    int[] xPoints = {3, getWidth()/2, getWidth() - 3};
            	    int[] yPoints = {3, getHeight() - 4, 3};
            	    g2d.drawPolyline(xPoints, yPoints, 3);
            		
            	}
				
			});
	        button.setName("ComboBox.arrowButton");
	        button.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					button.selectAnimation(8);
				}
				public void mouseExited(MouseEvent e) {
					button.unselectAnimation(8);
				}
			});
	        return button;
	    }
		
		protected ComboPopup createPopup() {
			
			//Create custom combo pop up
	        return new CustomComboPopup( comboBox );
	    
		}
		
		protected ListCellRenderer<Object> createRenderer() {
			
			//Create custom box renderer
	        return new CustomComboBoxRenderer();
	        
	    }
		
		public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
			
	        Component c; //Component to store the renderer
	        
	        //Get the renderer
	        if(hasFocus && !isPopupVisible(comboBox)) {
	            c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, true, false);
	        }else {
	            c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
	            c.setBackground(UIManager.getColor("ComboBox.background"));
	        }
	        
	        //Set font
	        c.setFont(font);
	        
	        //Set background and foreground of the unselected items renderer
            c.setForeground(unselectedItemForegroundColor);
            c.setBackground(unselectedItemBackgroundColor);

	        // Fix for 4238829: should lay out the JPanel.
	        boolean shouldValidate = false;
	        if (c instanceof JPanel) {
	            shouldValidate = true;
	        }

	        int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
	        if (padding != null) {
	            x = bounds.x + padding.left;
	            y = bounds.y + padding.top;
	            w = bounds.width - (padding.left + padding.right);
	            h = bounds.height - (padding.top + padding.bottom);
	        }

	        currentValuePane.paintComponent(g,c,comboBox,x,y,w,h,shouldValidate);
	        
	    }
	
	}
	
	//Custom combo pop up implemented by the custom combo box UI
	private class CustomComboPopup extends BasicComboPopup {

		public CustomComboPopup(JComboBox<Object> combo) {
			
			super(combo);//Call normal BasicComboPopup constructor
			setBorder(new LineBorder(borderColor, borderThickness)); //Set border
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Hand cursor when mouse passes on the combo box pop up
			
		}
		
		protected JScrollPane createScroller() {
			
			//Create scroll pane
	        JScrollPane sp = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); //Never utilize horizontal scroll bar
	        sp.setHorizontalScrollBar(null); //Set horizontal scroll bar as null
	        sp.setVerticalScrollBar(customScrollbar); //Set vertical scroll bar as custom scroll bar
	        return sp;
	        
	    }
		
	}
	
	//Custom combo box renderer implemented by the custom combo box UI
	private class CustomComboBoxRenderer extends BasicComboBoxRenderer {
		
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			
			//If the component is the one being selected (mouse hovering on it)
			if(isSelected) {
				//Invert colors for the renderer of the item being selected
				setBackground(unselectedItemForegroundColor);
				setForeground(unselectedItemBackgroundColor);
			}else {
				setBackground(unselectedItemBackgroundColor);
				setForeground(unselectedItemForegroundColor);
			}
			
			setFont(font); //Set font
			
			setHorizontalAlignment(JLabel.CENTER); //Set the text to the center
			setText((value == null) ? "" : value.toString()); //If value is not null
			
			return this; //Return this instance of Custom Combo Box Renderer
			
		}
		
	}

}
