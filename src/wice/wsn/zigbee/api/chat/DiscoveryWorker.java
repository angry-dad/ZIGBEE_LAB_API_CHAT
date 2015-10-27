package wice.wsn.zigbee.api.chat;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;

import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.utils.HexUtils;

public class DiscoveryWorker extends SwingWorker<String, String> {

	private JTextArea textArea;
	private String port;
	private int baud;
	private int timeout;
	private JToggleButton btnConnect;
	private JButton btnSend;
	private DefaultListModel<String> lstModel;
	private XBeeDevice myDevice;
	private XBeeNetwork myXBeeNetwork;

	public DiscoveryWorker(JTextArea textArea, JToggleButton btnConnect, JButton btnSend,
			DefaultListModel<String> lstModel, String port, int baud, int timeout, XBeeDevice myDevice,
			XBeeNetwork myXBeeNetwork) {
		this.textArea = textArea;
		this.port = port;
		this.baud = baud;
		this.timeout = timeout;
		this.btnConnect = btnConnect;
		this.lstModel = lstModel;
		this.btnSend = btnSend;
		this.myDevice = myDevice;
		this.myXBeeNetwork = myXBeeNetwork;
	}

	@Override
	protected String doInBackground() throws Exception {
		DiscoveryListener discoveryCallback = null;

		try {
			myDevice.open();
			myXBeeNetwork = myDevice.getNetwork();

			myXBeeNetwork.setDiscoveryTimeout(timeout * 1000); // GUI specifies
																// seconds,
																// method is
																// specified in
																// milliseconds

			myXBeeNetwork.addDiscoveryListener(discoveryCallback = new DiscoveryListener(this));
			myXBeeNetwork.startDiscoveryProcess();

			publish("Discovering remote XBee devices on PAN ID: " + HexUtils.prettyHexString((myDevice.getPANID())));
			while (!this.isCancelled()) {

			}

		} catch (Exception ex) {
			publish(ex.getMessage());

		} finally {
			ReceiveWorker rxThread = new ReceiveWorker(textArea);
			myDevice.addDataListener(new ReceiveListener(rxThread));
			rxThread.execute();
			if (myXBeeNetwork != null) {
				myXBeeNetwork.stopDiscoveryProcess();
				myXBeeNetwork.removeDiscoveryListener(discoveryCallback);
			}

			publish("done");
			this.cancel(true);
		}
		return null;
	}

	@Override
	protected void process(List<String> chunks) {
		for (final String string : chunks) {
			if (string.equals("done")) {
				btnConnect.setEnabled(true);
				if (!lstModel.isEmpty()) {
					btnSend.setEnabled(true);

				}
			} else if (string.endsWith("ADDRESS")) {
				lstModel.addElement(string.substring(0, 16));

			} else {
				textArea.append(string);
				textArea.append("\n");
			}
		}
	}

	public void publishData(String data) {
		publish(data);
	}

}
