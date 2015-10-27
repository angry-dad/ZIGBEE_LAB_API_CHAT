package wice.wsn.zigbee.api.chat;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.exceptions.TimeoutException;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.models.XBee64BitAddress;

public class MainWindow {

	private JFrame frmXbeeZigbeeApi;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JTextField txtPort;
	private JTextField txtBaud;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private JButton btnClear;
	private JLabel lblDiscoveryTimeouts;
	private JTextField txtTimeout;
	private JCheckBox cbxBroadcast;
	private JLabel lblMessage;
	private JTextField txtMessage;
	private JButton btnSend;
	private JLabel lblDestination;
	private DefaultListModel<String> lstModel;
	private JList<String> lstNetwork;
	private JToggleButton btnConnect;
	private XBeeNetwork myXBeeNetwork;
	private XBeeDevice myDevice;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmXbeeZigbeeApi.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmXbeeZigbeeApi = new JFrame();
		frmXbeeZigbeeApi.setTitle("XBee: ZigBee API Chat");
		frmXbeeZigbeeApi.setBounds(100, 100, 470, 403);
		frmXbeeZigbeeApi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 84, 218, 152, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		frmXbeeZigbeeApi.getContentPane().setLayout(gridBagLayout);

		lblNewLabel = new JLabel("Serial Port:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		frmXbeeZigbeeApi.getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		txtPort = new JTextField();
		txtPort.setText("/dev/tty.usbserial-A902UUM4");
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPort.insets = new Insets(0, 0, 5, 5);
		gbc_txtPort.gridx = 1;
		gbc_txtPort.gridy = 0;
		frmXbeeZigbeeApi.getContentPane().add(txtPort, gbc_txtPort);
		txtPort.setColumns(10);

		lblNewLabel_1 = new JLabel("Baud Rate:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		frmXbeeZigbeeApi.getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);

		txtBaud = new JTextField();
		txtBaud.setText("115200");
		GridBagConstraints gbc_txtBaud = new GridBagConstraints();
		gbc_txtBaud.insets = new Insets(0, 0, 5, 5);
		gbc_txtBaud.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtBaud.gridx = 1;
		gbc_txtBaud.gridy = 1;
		frmXbeeZigbeeApi.getContentPane().add(txtBaud, gbc_txtBaud);
		txtBaud.setColumns(10);

		btnConnect = new JToggleButton("Connect");
		btnConnect.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent itemEvent) {
				int state = itemEvent.getStateChange();
				;
				if (state == ItemEvent.SELECTED) {
					btnConnect.setText("Disconnect");
					btnConnect.setEnabled(false);
					lstModel.clear();
					myDevice = new XBeeDevice(txtPort.getText(), Integer.parseInt(txtBaud.getText()));
					DiscoveryWorker discover = new DiscoveryWorker(textArea, btnConnect, btnSend, lstModel,
							txtPort.getText(), Integer.parseInt(txtBaud.getText()),
							Integer.parseInt(txtTimeout.getText()), myDevice, myXBeeNetwork);
					discover.execute();

				} else {
					btnConnect.setText("Connect");
					lstModel.clear();
					btnSend.setEnabled(false);
				}
			}
		});

		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.anchor = GridBagConstraints.NORTH;
		gbc_btnConnect.insets = new Insets(0, 0, 5, 0);
		gbc_btnConnect.gridx = 2;
		gbc_btnConnect.gridy = 1;
		frmXbeeZigbeeApi.getContentPane().add(btnConnect, gbc_btnConnect);

		lblDiscoveryTimeouts = new JLabel("Timeout (s):");
		GridBagConstraints gbc_lblDiscoveryTimeouts = new GridBagConstraints();
		gbc_lblDiscoveryTimeouts.anchor = GridBagConstraints.EAST;
		gbc_lblDiscoveryTimeouts.insets = new Insets(0, 0, 5, 5);
		gbc_lblDiscoveryTimeouts.gridx = 0;
		gbc_lblDiscoveryTimeouts.gridy = 2;
		frmXbeeZigbeeApi.getContentPane().add(lblDiscoveryTimeouts, gbc_lblDiscoveryTimeouts);

		btnClear = new JButton("Clear Output");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});

		txtTimeout = new JTextField();
		txtTimeout.setText("5");
		txtTimeout.setColumns(10);
		GridBagConstraints gbc_txtTimeout = new GridBagConstraints();
		gbc_txtTimeout.insets = new Insets(0, 0, 5, 5);
		gbc_txtTimeout.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTimeout.gridx = 1;
		gbc_txtTimeout.gridy = 2;
		frmXbeeZigbeeApi.getContentPane().add(txtTimeout, gbc_txtTimeout);
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnClear.gridx = 2;
		gbc_btnClear.gridy = 2;
		frmXbeeZigbeeApi.getContentPane().add(btnClear, gbc_btnClear);
		;

		lblDestination = new JLabel("Destination:");
		GridBagConstraints gbc_lblDestination = new GridBagConstraints();
		gbc_lblDestination.insets = new Insets(0, 0, 5, 5);
		gbc_lblDestination.gridx = 0;
		gbc_lblDestination.gridy = 4;
		frmXbeeZigbeeApi.getContentPane().add(lblDestination, gbc_lblDestination);

		cbxBroadcast = new JCheckBox("Broadcast?");
		cbxBroadcast.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (cbxBroadcast.isSelected()) {
					lstNetwork.setVisible(false);
				} else {
					lstNetwork.setVisible(true);
				}
			}
		});
		lstModel = new DefaultListModel<String>();
		lstNetwork = new JList<String>(lstModel);
		lstNetwork.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GridBagConstraints gbc_lstNetwork = new GridBagConstraints();
		gbc_lstNetwork.insets = new Insets(0, 0, 5, 5);
		gbc_lstNetwork.fill = GridBagConstraints.BOTH;
		gbc_lstNetwork.gridx = 1;
		gbc_lstNetwork.gridy = 4;
		frmXbeeZigbeeApi.getContentPane().add(lstNetwork, gbc_lstNetwork);
		GridBagConstraints gbc_cbxBroadcast = new GridBagConstraints();
		gbc_cbxBroadcast.anchor = GridBagConstraints.WEST;
		gbc_cbxBroadcast.insets = new Insets(0, 0, 5, 0);
		gbc_cbxBroadcast.gridx = 2;
		gbc_cbxBroadcast.gridy = 4;
		frmXbeeZigbeeApi.getContentPane().add(cbxBroadcast, gbc_cbxBroadcast);

		lblMessage = new JLabel("Message:");
		GridBagConstraints gbc_lblMessage = new GridBagConstraints();
		gbc_lblMessage.anchor = GridBagConstraints.EAST;
		gbc_lblMessage.insets = new Insets(0, 0, 5, 5);
		gbc_lblMessage.gridx = 0;
		gbc_lblMessage.gridy = 5;
		frmXbeeZigbeeApi.getContentPane().add(lblMessage, gbc_lblMessage);

		txtMessage = new JTextField();
		txtMessage.setText("Hello World!");
		txtMessage.setColumns(10);
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 5, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 1;
		gbc_txtMessage.gridy = 5;
		frmXbeeZigbeeApi.getContentPane().add(txtMessage, gbc_txtMessage);

		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lstNetwork.getSelectedValue() == null) {
					lstNetwork.setSelectedIndex(0);
				}
				XBee64BitAddress remoteAddress = new XBee64BitAddress(lstNetwork.getSelectedValue());
				RemoteXBeeDevice remoteDevice = new RemoteXBeeDevice(myDevice, remoteAddress);
				try {
					if (cbxBroadcast.isSelected()) {
						myDevice.sendBroadcastData(txtMessage.getText().getBytes());
						textArea.append("Broadcast successfully sent!\n");
					} else {
						myDevice.sendData(remoteDevice, txtMessage.getText().getBytes());
						textArea.append("Message successfully sent!\n");
					}
				} catch (TimeoutException e1) {
					textArea.append(e1.getMessage() + "\n");
				} catch (XBeeException e1) {
					textArea.append(e1.getMessage() + "\n");
				}
			}
		});
		btnSend.setEnabled(false);
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 5, 0);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 5;
		frmXbeeZigbeeApi.getContentPane().add(btnSend, gbc_btnSend);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 6;
		frmXbeeZigbeeApi.getContentPane().add(scrollPane, gbc_scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		scrollPane.setViewportView(textArea);
	}
}
