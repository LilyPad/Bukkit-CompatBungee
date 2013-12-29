package lilypad.bukkit.compat.bungee.query.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import lilypad.bukkit.compat.bungee.query.Query;
import lilypad.bukkit.compat.bungee.util.Constants;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.result.FutureResultListener;
import lilypad.client.connect.api.result.impl.GetPlayersResult;

public class PlayerCountQuery implements Query {

	private Connect connect;
	private Server server;

	public PlayerCountQuery(Connect connect, Server server) {
		this.connect = connect;
		this.server = server;
	}

	public void execute(final Player player, String id, DataInput input) throws Exception {
		String target = input.readUTF();
		if(target.equals("ALL")) {
			this.connect.request(new GetPlayersRequest(false)).registerListener(new FutureResultListener<GetPlayersResult>() {
				public void onResult(GetPlayersResult result) {
					ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
					DataOutput output = new DataOutputStream(byteArrayOutput);
					try {
						output.writeUTF("PlayerCount");
						output.writeUTF("ALL");
						output.writeInt(result.getCurrentPlayers());
					} catch(IOException exception) {
						// ignore
					}
					PlayerCountQuery.this.server.getMessenger().dispatchIncomingMessage(player, Constants.channel, byteArrayOutput.toByteArray());
				}
			});
		} else {
			try {
				this.connect.request(new MessageRequest(target, "lpCbPC", new byte[0]));
			} catch(RequestException exception) {
				// ignore
			}
		}
	}

	public String getId() {
		return "PlayerCount";
	}

}
