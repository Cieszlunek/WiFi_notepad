/*package com.example.trzeci;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
	
	private ClientActivity activity;
	private WifiP2pManager mManager;
	private Channel mChannel;
	private ClientActivity mActivity;
	PeerListListener myPeerListListener;
	
	public WifiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
	            ClientActivity activity) {
		super();
	    this.mManager = manager;
	    this.mChannel = channel;
		this.mActivity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				activity.setIsWifiP2pEnabled(true);
			}
			else {
				activity.setIsWifiP2pEnabled(false);
			}
		}
		else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			//peers list changed - reaction here
			if (mManager != null) {
				mManager.requestPeers(mChannel, myPeerListListener);
			}
		}
		else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			//connection state changed - reaction here
		}
		else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager().findFragmentById(R.id.search_button);
			fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
		}
	}
}
*/
