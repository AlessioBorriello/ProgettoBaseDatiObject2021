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
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class AddQueueFrame extends JDialog {
	
	private MainFrame mainFrame; //Link to the mainFrame
	private String choice; //String containing the returned choice of the user

	/**
	 * Frame where the user is prompted to choose a queue to add or undo the action
	 * @param mf Link to the MainFrame
	 * @param queueList List of the currently added queues to the flight being edited/created
	 */
	public AddQueueFrame(MainFrame mf, ArrayList<String> queueList) {
		
		mainFrame = mf; //Link this frame to the mainFrame
		
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
			    
			    //Draw the string
			    g2d.setFont(new Font(MainController.fontOne.getFontName(), Font.PLAIN, 22));
			    g2d.setColor(MainController.foregroundColorThree);
			    int length = g2d.getFontMetrics(g2d.getFont()).stringWidth("Seleziona la coda");
			    g2d.drawString("Seleziona la coda", (getWidth()/2) - (length/2), 50);
			    length = g2d.getFontMetrics(g2d.getFont()).stringWidth("da aggiungere");
			    g2d.drawString("da aggiungere", (getWidth()/2) - (length/2), 80);
			    
			}
			
		}); //Create contentPanel
		add(contentPanel, BorderLayout.CENTER); //Add contentPanel to the frame
		contentPanel.setLayout(null); //Set contentPanel's layout to absolute
			
		CustomComboBox cBoxAddQueue = mainFrame.createCustomComboBox(); //Create combo box
		//Populate combo box
		comboBoxAddItemIfNotPresent(cBoxAddQueue, "Famiglia", queueList);
		comboBoxAddItemIfNotPresent(cBoxAddQueue, "Priority", queueList);
		comboBoxAddItemIfNotPresent(cBoxAddQueue, "Diversamente Abili", queueList);
		comboBoxAddItemIfNotPresent(cBoxAddQueue, "Business Class", queueList);
		comboBoxAddItemIfNotPresent(cBoxAddQueue, "Standard Class", queueList);
		comboBoxAddItemIfNotPresent(cBoxAddQueue, "Economy Class", queueList);
		
		cBoxAddQueue.setBounds((getWidth()/2) - 100, 108, 200, 22); //Set combo box bounds
		contentPanel.add(cBoxAddQueue); //Add combo box to the content panel
		
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
		
		CustomButton addButton = new CustomButton("AGGIUNGI", null, 
				new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 64), 
				MainController.foregroundColorThree, 18, true, MainController.foregroundColorThree, 1); //Create addButton
		addButton.setFocusable(false); //Set as non focusable
		addButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				addButton.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				addButton.unselectAnimation(8);
			}
		});
		//Button's action listeners
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				choice = cBoxAddQueue.getSelectedItem().toString(); //Set choice string to the selected item in the combo box
				dispose(); //Dispose frame
			}
		});
		
		CustomButton undoButton = new CustomButton("ANNULLA", null, 
				new Color(MainController.foregroundColorThree.getRed(), MainController.foregroundColorThree.getGreen(), MainController.foregroundColorThree.getBlue(), 64), 
				MainController.foregroundColorThree, 18, true, MainController.foregroundColorThree, 1); //Create undoButton
		undoButton.setFocusable(false); //Set as non focusable
		undoButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				undoButton.selectAnimation(8);
			}

			public void mouseExited(MouseEvent e) {
				undoButton.unselectAnimation(8);
			}
		});
		//Button's action listeners
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //Set frame as invisible
				choice = "undo"; //Set choice string as "undo"
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
					.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
					.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
					.addGap(52))
		);
		//GroupLayout vertical properties
		gl_buttonPane.setVerticalGroup(
			gl_buttonPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addContainerGap(19, Short.MAX_VALUE)
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
						.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		buttonPanel.setLayout(gl_buttonPane); //Set buttonPanel's layout to the GroupLayout just created
		
	}
	
	/**
	 * Get the user's choice to add a queue or undo
	 * @return The user's choice
	 */
	public String getChoice() {
		return choice;
	}

	/**
	 * Add a queue type to the given combo box if said queue is not already in the queue list
	 * @param c ComboBox to add the queues to
	 * @param queue Queue type to add
	 * @param queueList List of the queues already added to the flight being edited/created
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void comboBoxAddItemIfNotPresent(JComboBox c, String queue, ArrayList<String> queueList) {
		
		for(String q : queueList) {
			if(queue.equals(q)) { //The queue to add is already contained in the queue list
				return;
			}
		}
		
		//The queue to add is not already contained in the queue list
		c.addItem(queue); //Add it
		
	}
	
}
