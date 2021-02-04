import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;

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
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class SetEffectiveTimeFrame extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton setButton;
	private JButton undoButton;
	
	Date time;
	private JLabel setEffectiveTimeLabel;
	private JSpinner spinnerTakeOffDate;

	public SetEffectiveTimeFrame(MainFrame mf, Date data) {
		
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
			setEffectiveTimeLabel = new JLabel("Imposta il tempo effettivo dello slot");
			setEffectiveTimeLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
			setEffectiveTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
			setEffectiveTimeLabel.setHorizontalTextPosition(SwingConstants.LEADING);
			setEffectiveTimeLabel.setBounds(10, 11, 390, 61);
			contentPanel.add(setEffectiveTimeLabel);
		}
		{
			spinnerTakeOffDate = new JSpinner();
			spinnerTakeOffDate.setName("spinnerTakeOffDate");
			spinnerTakeOffDate.setModel(new SpinnerDateModel(new Date(1612134000000L), null, null, Calendar.DAY_OF_YEAR));
			spinnerTakeOffDate.setValue(data);
			spinnerTakeOffDate.setFont(new Font("Tahoma", Font.BOLD, 11));
			spinnerTakeOffDate.setBounds(142, 83, 148, 20);
			contentPanel.add(spinnerTakeOffDate);
		}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			buttonPane.setBackground(new Color(70, 130, 180));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				setButton = new JButton("FATTO");
				setButton.setFocusable(false);
				setButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				setButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						setVisible(false);
						time = (Date)spinnerTakeOffDate.getValue();
						dispose();
					}
				});
				setButton.setActionCommand("AGGIUNGI");
				getRootPane().setDefaultButton(setButton);
			}
			
			undoButton = new JButton("ANNULLA");
			undoButton.setFocusable(false);
			undoButton.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					setVisible(false);
					time = null;
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
						.addComponent(setButton, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
						.addGap(52))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addContainerGap(19, Short.MAX_VALUE)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.LEADING)
							.addComponent(undoButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addComponent(setButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
						.addContainerGap())
			);
			buttonPane.setLayout(gl_buttonPane);
		}
		
	}
	
	public Date getDate() {
		return time;
	}

}
