package lilypad.bukkit.compat.bungee.query.impl;

import java.io.DataInput;

import org.bukkit.entity.Player;

import lilypad.bukkit.compat.bungee.query.Query;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.RedirectRequest;

public class ConnectQuery implements Query {

	private Connect connect;
	
	public ConnectQuery(Connect connect) {
		this.connect = connect;
	}
	
	public void execute(Player player, String id, DataInput input) throws Exception {
		String server = input.readUTF();
		try {
			this.connect.request(new RedirectRequest(server, player.getName()));
		} catch(RequestException exception) {
			// ignore
		}
	}

	public String getId() {
		return "Connect";
	}

}
