package lilypad.bukkit.compat.bungee.query.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.util.Collections;

import org.bukkit.entity.Player;

import lilypad.bukkit.compat.bungee.query.Query;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;

public class ForwardQuery implements Query {

	private Connect connect;

	public ForwardQuery(Connect connect) {
		this.connect = connect;
	}

	@SuppressWarnings("unchecked")
	public void execute(Player player, String id, DataInput input) throws Exception {
		/* read */
		String target = input.readUTF();
		String channel = input.readUTF();
		short length = input.readShort();
		byte[] payload = new byte[length];
		input.readFully(payload);
		/* write */
		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
		DataOutput output = new DataOutputStream(byteArrayOutput);
		output.writeUTF(channel);
		output.writeShort(length);
		output.write(payload);
		byte[] data = byteArrayOutput.toByteArray();
		/* forward */
		try {
			if(target.equals("ALL")) {
				this.connect.request(new MessageRequest(Collections.EMPTY_LIST, "lpCbF", data));
			} else {
				this.connect.request(new MessageRequest(target, "lpCbF", data));
			}
		} catch(RequestException exception) {
			// ignore
		}
	}

	public String getId() {
		return "Forward";
	}

}
