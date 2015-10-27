
package wice.wsn.zigbee.api.chat;

import com.digi.xbee.api.listeners.IDataReceiveListener;
import com.digi.xbee.api.models.XBeeMessage;

public class ReceiveListener implements IDataReceiveListener {

	private ReceiveWorker rxThread;

	public ReceiveListener(ReceiveWorker rxThread) {
		this.rxThread = rxThread;
	}

	@Override
	public void dataReceived(XBeeMessage xbeeMessage) {
		rxThread.publishData(
				xbeeMessage.getDevice().get64BitAddress().toString() + ": " + xbeeMessage.getDataString() + "\n");

	}
}