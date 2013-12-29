package lilypad.bukkit.compat.bungee.query.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import lilypad.bukkit.compat.bungee.query.Query;
import lilypad.bukkit.compat.bungee.util.Constants;
import lilypad.bukkit.compat.bungee.util.StringUtils;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.result.FutureResultListener;
import lilypad.client.connect.api.result.impl.GetPlayersResult;

public class PlayerListQuery implements Query {

	private Connect connect;
	private Server server;

	public PlayerListQuery(Connect connect, Server server) {
		this.connect = connect;
		this.server = server;
	}

	public void execute(final Player player, String id, DataInput input) throws Exception {
		String target = input.readUTF();
		if(target.equals("ALL")) {
			this.connect.request(new GetPlayersRequest(true)).registerListener(new FutureResultListener<GetPlayersResult>() {
				public void onResult(GetPlayersResult result) {
					ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
					DataOutput output = new DataOutputStream(byteArrayOutput);
					Set<String> players = result.getPlayers();
					try {
						output.writeUTF("PlayerList");
						output.writeUTF("ALL");
						output.writeUTF(StringUtils.concat(players.toArray(new String[players.size()]), ", "));
					} catch(IOException exception) {
						// ignore
					}
					PlayerListQuery.this.server.getMessenger().dispatchIncomingMessage(player, Constants.channel, byteArrayOutput.toByteArray());
				}
			});
		} else {
			try {
				this.connect.request(new MessageRequest(target, "lpCbPL", new byte[0]));
			} catch(RequestException exception) {
				// ignore
			}
		}
	}

	public String getId() {
		return "PlayerList";
	}

}
