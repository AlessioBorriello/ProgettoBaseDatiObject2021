import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import java.awt.Window.Type;
import java.awt.Cursor;

public class NotificationFrame extends JDialog {

	private MainFrame mainFrame;
	
	private ArrayList<String> notificationParts; //Subdivision of the notification

	/**
	 * Frame showing a notification to the user
	 * @param notification Notification text to show the user
	 * @param mf Link to the MainFrame
	 */
	public NotificationFrame(String notification, MainFrame mf) {
		
		mainFrame = mf; //Link the frame and the mainFrame
		
		setType(Type.POPUP); //Set frame type to pop-up
		setAlwaysOnTop(true); //Set to stay always on top
		setUndecorated(true); //Set undecorated
		setModal(true); //Blocks input to other frames
		setSize(410, 227); //Set size
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); //Set operation on close (dispose)
		setBounds(mainFrame.getLocation().x + (mainFrame.getWidth()/2) - (this.getWidth()/2), mainFrame.getLocation().y + (mainFrame.getHeight()/2) - (this.getHeight()/2), 410, 227); //Put frame to the center of the mainFrame
		setLayout(new BorderLayout()); //Set layout
		
		JPanel contentPanel = (new JPanel() {
			
			public void paintComponent(Graphics g) { //Access component paint method
				
				super.paintComponent(g); //Paint the component normally first
				
				Graphics2D g2d = (Graphics2D)g;
				
				//AA
			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			    //Text AA
			    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			    
			    //Draw background
			    GradientPaint gPaint = new GradientPaint(getWidth()/2, -200, MainController.backgroundColorTwo, getWidth()/2, getHeight(), MainController.backgroundColorOne);
			    g2d.setPaint(gPaint);
			    g2d.fillRect(0, 0, getWidth(), getHeight());
			    
			    //Draw border
			    g2d.setColor(MainController.foregroundColorThree);
			    g2d.setStroke(new BasicStroke(3));
			    g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			    
			    //Draw notification
			    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 22));
			    g2d.setColor(MainController.foregroundColorThree);
			    
			    //Draw the string
			    notificationParts = mainFrame.subdivideString(g2d, notification, getWidth() - ((int)(getWidth()*.3)));
			    int verticalSubdivision = (notificationParts.size() > 1)? notificationParts.size() : 2;
			    int yOffset = 30;
			    int yStart = getHeight()/verticalSubdivision;
			    
			    //If it's only a single row
			    if(notificationParts.size() == 1) {
			    	yStart += 15; //Lower the string a bit
			    }
			    
			    int row = 0;
			    for(String s : notificationParts) { //Draw notification's parts
			    	int sLength = g2d.getFontMetrics(g2d.getFont()).stringWidth(s);
			    	g2d.drawString(s, (getWidth()/2) - (sLength/2), yStart + (yOffset * row));
			    	row++;
			    }
			    
			}
			
		}); //Create contentPanel
		add(contentPanel, BorderLayout.CENTER); //Add contentPanel to the frame
		contentPanel.setLayout(null); //Set contentPanel's layout to absolute
		
		JLabel lblNotification = new JLabel("Notification"); //Create label
		lblNotification.setText(notification); //Set label's text to the passed notification
		lblNotification.setFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 22)); //Set label's text font
		lblNotification.setForeground(MainController.foregroundColorThree); //Set label's text color
		lblNotification.setHorizontalAlignment(SwingConstants.CENTER); //Set label's horizontal alignment
		lblNotification.setBounds(0, 0, 410, 162); //Set label bounds
		//contentPanel.add(lblNotification); //Add label to the contentPanel
		
		JPanel buttonPanel = (new JPanel() {
			
			public void paintComponent(Graphics g) { //Access component paint method
				
				super.paintComponent(g); //Paint the component normally first
				
				Graphics2D g2d = (Graphics2D)g;
				
				//AA
			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			    //Text AA
			    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			    
			    //Draw background
			    g2d.setColor(MainController.backgroundColorTwo);
			    g2d.fillRect(0, 0, getWidth(), getHeight());
			    
			    //Draw border
			    g2d.setColor(MainController.foregroundColorThree);
			    g2d.setStroke(new BasicStroke(3));
			    g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			    
			}
			
		}); //Create buttonPanel
		add(buttonPanel, BorderLayout.SOUTH); //Add buttonPanel to the frame
		
		CustomButton okButton = new CustomButton("OK", null, 
				new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 64), 
				MainController.foregroundColorThree, 18, true, MainController.foregroundColorThree, 1); //Create okButton
		okButton.setFocusable(false); //Set as non focusable
		okButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				okButton.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				okButton.unselectAnimation(8);
			}
		});
		//Button's action listeners
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				dispose(); //Dispose frame
			}
		});
		
		//Layout creation
		GroupLayout gl_buttonPane = new GroupLayout(buttonPanel); //Create new GroupLayout for the buttonPanel
		//GroupLayout horizontal properties
		gl_buttonPane.setHorizontalGroup(
			gl_buttonPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addGap(137)
					.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(137, Short.MAX_VALUE))
		);
		//GroupLayout vertical properties
		gl_buttonPane.setVerticalGroup(
			gl_buttonPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_buttonPane.createSequentialGroup()
					.addContainerGap(19, Short.MAX_VALUE)
					.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		buttonPanel.setLayout(gl_buttonPane); //Set buttonPanel's layout to the GroupLayout just created
		
	}

}
