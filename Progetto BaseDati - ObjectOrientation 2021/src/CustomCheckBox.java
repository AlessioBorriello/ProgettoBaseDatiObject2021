import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class CustomCheckBox extends JButton{
	
	private String s; //String of the check box
	private Color backgroundColor; //Background color
	
	private int fontSize; //Font size
	private Color fontColor; //Font color
	
	private boolean border = false; //If the check box should have a border
	private Color borderColor; //Color of the border
	private int borderThickness; //Thickness of the border
	
	private boolean selected = false; //If the check box is selected
	
	/**
	 * Create a check box with a custom UI
	 * @param s String of the check box
	 * @param backgroundColor Background color
	 * @param fontSize Font size
	 * @param fontColor Font color
	 * @param border If the check box should have a border
	 * @param borderColor Color of the border
	 * @param borderThickness Thickness of the border
	 */
	public CustomCheckBox(String s, Color backgroundColor, int fontSize, Color fontColor, boolean border, Color borderColor, int borderThickness) {
		
		this.s = s;
		this.fontColor = fontColor;
		this.fontSize = fontSize;
		
		this.backgroundColor = backgroundColor;
		
		this.border = border;
		this.borderColor = borderColor;
		this.borderThickness = borderThickness;
		
		setFocusable(false); //Non focusable
		setContentAreaFilled(false); //Don't draw the content area
		setBorder(null); //Set border to null
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); //Hand cursor when mouse passes on the check box
		//Mouse click on check box
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = !selected; //toggle selected
			}
		});
		
	}
	
	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D)g;
		
		//Background
		if(backgroundColor != null) {
			g2d.setColor(backgroundColor);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}
		
		int circleSize = 10; //Diameter of the select circle
		
		//Draw string
		g2d.setColor(fontColor);
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, fontSize));
		g2d.drawString(s, circleSize + 10, (getHeight()/2) + ((fontSize)/2) - 2);
		
		//Draw select circle's outline
		g2d.setStroke(new BasicStroke(2));
		g2d.drawOval(5, (getHeight()/2) - (circleSize/2), circleSize, circleSize);
		
		//If the check box is selected
		if(selected) {
			//Fill the previously draw circle's outline
			g2d.fillOval(5, (getHeight()/2) - (circleSize/2), circleSize, circleSize);
		}
		
		//Draw border
		if(border) {
			g2d.setColor(borderColor);
			g2d.setStroke(new BasicStroke(borderThickness));
			g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
		}
		
	}
	
	//Set and get selected boolean attribute
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean s) {
		selected = s;
	}

}