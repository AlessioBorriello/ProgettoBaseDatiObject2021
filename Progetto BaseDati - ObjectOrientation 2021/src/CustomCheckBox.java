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
	
	private String s;
	private Color backgroundColor;
	
	private int fontSize;
	private Color fontColor;
	
	private boolean border = false;
	private Color borderColor;
	private int borderThickness;
	
	private boolean selected = false;
	
	public CustomCheckBox(String s, Color backgroundColor, int fontSize, Color fontColor, boolean border, Color borderColor, int borderThickness) {
		
		this.s = s;
		this.fontColor = fontColor;
		this.fontSize = fontSize;
		
		this.backgroundColor = backgroundColor;
		
		this.border = border;
		this.borderColor = borderColor;
		this.borderThickness = borderThickness;
		
		setFocusable(false);
		setContentAreaFilled(false);
		setBorder(null);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
		
		//String and box
		g2d.setColor(fontColor);
		g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.BOLD, fontSize));
		g2d.setStroke(new BasicStroke(2));
		
		int boxSize = 10;
		g2d.drawOval(5, (getHeight()/2) - (boxSize/2), boxSize, boxSize);
		g2d.drawString(s, boxSize + 10, (getHeight()/2) + ((fontSize)/2) - 2);
		
		if(selected) {
			g2d.fillOval(5, (getHeight()/2) - (boxSize/2), boxSize, boxSize);
		}
		
		//Border
		if(border) {
			g2d.setColor(borderColor);
			g2d.setStroke(new BasicStroke(borderThickness));
			g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
		}
		
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean s) {
		selected = s;
	}

}