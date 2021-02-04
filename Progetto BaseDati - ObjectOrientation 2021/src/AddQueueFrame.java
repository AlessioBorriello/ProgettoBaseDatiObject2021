import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EtchedBorder;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;

public class AddQueueFrame extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton addButton;
	private JButton undoButton;
	
	String choice;
	private JLabel lblAddQueue;
	private JComboBox cBoxAddQueue;

	public AddQueueFrame(MainFrame mf) {
		
		MainFrame mainFrame = mf;
		
		setType(Type.POPUP);
		setAlwaysOnTop(true);
		setUndecorated(true);
		setModal(true);
		setSize(410, 227);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(mainFrame.getLocation().x + (mainFrame.getWidth()/2) - (this.getWidth()/2), mainFrame.getLocation().y + (mainFrame.getHeight()/2) - (this.getHeight()/2), 410, 227);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lblAddQueue = new JLabel("Seleziona una coda da aggiungere");
			lblAddQueue.setFont(new Font("Tahoma", Font.BOLD, 16));
			lblAddQueue.setHorizontalAlignment(SwingConstants.CENTER);
			lblAddQueue.setHorizontalTextPosition(SwingConstants.LEADING);
			lblAddQueue.setBounds(10, 11, 390, 61);
			contentPanel.add(lblAddQueue);
		}
		{
			cBoxAddQueue = new JComboBox();
			//Debug population
			for(int i = 1; i < 6; i++) {
				cBoxAddQueue.addItem("Coda " + i);
			}
			cBoxAddQueue.setBounds(110, 83, 190, 22);
			contentPanel.add(cBoxAddQueue);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			buttonPane.setBackground(new Color(70, 130, 180));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				addButton = new JButton("AGGIUNGI");
				addButton.setFocusable(false);
				addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				addButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						setVisible(false);
						choice = cBoxAddQueue.getSelectedItem().toString();
						dispose();
					}
				});
				addButton.setActionCommand("AGGIUNGI");
				getRootPane().setDefaultButton(addButton);
			}
			
			undoButton = new JButton("ANNULLA");
			undoButton.setFocusable(false);
			undoButton.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					setVisible(false);
					choice = "undo";
					dispose();
				}
			});
			undoButton.setActionCommand("CANCELLA");
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(52)
						.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
						.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
						.addGap(52))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addContainerGap(19, Short.MAX_VALUE)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
							.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addComponent(addButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
						.addContainerGap())
			);
			buttonPane.setLayout(gl_buttonPane);
		}
		
	}
	
	public String getChoice() {
		return choice;
	}

}
