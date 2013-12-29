package lilypad.bukkit.compat.bungee.query.impl;

import java.io.DataInput;
import java.io.UnsupportedEncodingException;
import java.util.Collections;

import org.bukkit.entity.Player;

import lilypad.bukkit.compat.bungee.query.Query;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;

public class MessageQuery implements Query {

	private Connect connect;
	
	public MessageQuery(Connect connect) {
		this.connect = connect;
	}
	
	@SuppressWarnings("unchecked")
	public void execute(Player _, String id, DataInput input) throws Exception {
		String player = input.readUTF();
		String message = input.readUTF();
		try {
			this.connect.request(new MessageRequest(Collections.EMPTY_LIST, "lpCbMsg", player + " " + message));
		} catch(UnsupportedEncodingException exception) {
			// ignore
		} catch(RequestException exception) {
			// ignore
		}
	}

	public String getId() {
		return "Message";
	}

}
