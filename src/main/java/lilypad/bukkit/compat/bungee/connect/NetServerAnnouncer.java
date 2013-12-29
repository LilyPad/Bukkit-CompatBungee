package lilypad.bukkit.compat.bungee.connect;

import java.util.Collections;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;

public class NetServerAnnouncer implements Runnable {

	private Connect connect;
	
	public NetServerAnnouncer(Connect connect) {
		this.connect = connect;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		try {
			this.connect.request(new MessageRequest(Collections.EMPTY_LIST, "lpCbA", new byte[0]));
		} catch(RequestException exception) {
			// ignore
		}
	}
	
}
