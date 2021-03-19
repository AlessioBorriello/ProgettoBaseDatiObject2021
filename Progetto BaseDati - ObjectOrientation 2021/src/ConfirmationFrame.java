import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

@SuppressWarnings("serial")
public class ConfirmationFrame extends JDialog {
	
	private MainFrame mainFrame; //Link to the mainFrame
	private boolean answer = false; //Boolean containing the choice of the user (False = undo, True = confirm)
	
	private ArrayList<String> notificationParts; //Array list of substrings of the notification

	/**
	 * Frame showing a confirmation choice to the user to confirm or undo
	 * @param notification Notification text to show the user
	 * @param mf Link to the MainFrame
	 */
	public ConfirmationFrame(String notification, MainFrame mf) {
		
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
			    notificationParts = mainFrame.subdivideString(g2d, notification, getWidth() - ((int)(getWidth()*.3))); //Subdivide the notification in a set of string that are narrower than the 70% of the width of the panel
			    int verticalSubdivision = (notificationParts.size() > 1)? notificationParts.size() : 2; //Vertical division of height (Must be at least 2)
			    int yOffset = 30; //Vertical offset for each notification part
			    int yStart = getHeight()/verticalSubdivision; //Start of the upper most notification part
			    
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
		add(contentPanel, BorderLayout.CENTER); //Add contentPanel that shows the notification
		contentPanel.setLayout(null); //Set contentPanel's layout to absolute
			
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
				answer = true; //Set answer to true (confirm)
				dispose(); //Dispose frame
			}
		});
		
		CustomButton cancelButton = new CustomButton("ANNULLA", null, 
				new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 64), 
				MainController.foregroundColorThree, 18, true, MainController.foregroundColorThree, 1); //Create cancellaButton
		cancelButton.setFocusable(false); //Set as non focusable
		cancelButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				cancelButton.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				cancelButton.unselectAnimation(8);
			}
		});
		//Button's action listeners
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				answer = false; //Set answer to true (undo)
				dispose(); //Dispose frame
			}
		});

		
		//Layout creation
		GroupLayout gl_buttonPane = new GroupLayout(buttonPanel); //Create new GroupLayout for the buttonPanel
		//GroupLayout horizontal properties
		gl_buttonPane.setHorizontalGroup(
			gl_buttonPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addGap(52)
					.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
					.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addGap(52))
		);
		//GroupLayout vertical properties
		gl_buttonPane.setVerticalGroup(
			gl_buttonPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addContainerGap(19, Short.MAX_VALUE)
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
						.addComponent(cancelButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(okButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		buttonPanel.setLayout(gl_buttonPane); //Set buttonPanel's layout to the GroupLayout just created
		
	}
	
	/**
	 * Get the user's answer to the confirmation prompt
	 * @return The user's answer to the confirmation prompt
	 */
	public boolean getAnswer() {
		return answer;
	}

}
