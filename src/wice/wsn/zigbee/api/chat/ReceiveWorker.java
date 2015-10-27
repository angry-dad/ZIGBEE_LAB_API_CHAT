package wice.wsn.zigbee.api.chat;

import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class ReceiveWorker extends SwingWorker<String, String> {

	private JTextArea textArea;

	public ReceiveWorker(JTextArea textArea) {
		this.textArea = textArea;
	}

	@Override
	protected String doInBackground() throws Exception {

		publish("Connected and waiting for data...\n");
		return null;
	}

	@Override
	protected void process(List<String> chunks) {
		for (final String string : chunks) {
			textArea.append(string);

		}

	}

	public void publishData(String data) {
		publish(data);
	}

}
