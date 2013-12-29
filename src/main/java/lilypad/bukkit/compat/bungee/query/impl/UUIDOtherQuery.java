package lilypad.bukkit.compat.bungee.query.impl;

import java.io.DataInput;
import java.util.Collections;

import org.bukkit.entity.Player;

import lilypad.bukkit.compat.bungee.query.Query;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;

public class UUIDOtherQuery implements Query {

	private Connect connect;
	
	public UUIDOtherQuery(Connect connect) {
		this.connect = connect;
	}

	@SuppressWarnings("unchecked")
	public void execute(Player _, String id, DataInput input) throws Exception {
		String player = input.readUTF();
		try {
			this.connect.request(new MessageRequest(Collections.EMPTY_LIST, "lpCbU", player));
		} catch(RequestException exception) {
			// ignore
		}
	}

	public String getId() {
		return "UUIDOther";
	}

}
