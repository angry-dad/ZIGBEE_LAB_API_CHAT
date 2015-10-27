
package wice.wsn.zigbee.api.chat;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.listeners.IDiscoveryListener;

public class DiscoveryListener implements IDiscoveryListener {

	private DiscoveryWorker discovery;

	public DiscoveryListener(DiscoveryWorker discovery) {
		this.discovery = discovery;
	}

	@Override
	public void deviceDiscovered(RemoteXBeeDevice discoveredDevice) {
		discovery.publishData(">> Device discovered: " + discoveredDevice.get64BitAddress());
		discovery.publishData(discoveredDevice.get64BitAddress().toString() + "ADDRESS");
	}

	@Override
	public void discoveryError(String error) {
		discovery.publishData(">> There was an error discovering devices: " + error);
	}

	@Override
	public void discoveryFinished(String error) {
		if (error == null) {
			discovery.publishData(">> Discovery process finished successfully.");
		} else {
			discovery.publishData(">> Discovery process finished due to the following error: " + error);
		}
		discovery.cancel(true);
	}
}
