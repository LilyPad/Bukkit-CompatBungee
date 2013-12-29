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
import lilypad.client.connect.api.request.impl.GetDetailsRequest;
import lilypad.client.connect.api.result.FutureResultListener;
import lilypad.client.connect.api.result.impl.GetDetailsResult;

public class IPQuery implements Query {

	private Connect connect;
	private Server server;
	
	public IPQuery(Connect connect, Server server) {
		this.connect = connect;
		this.server = server;
	}

	public void execute(final Player player, String id, DataInput input) throws Exception {
		this.connect.request(new GetDetailsRequest()).registerListener(new FutureResultListener<GetDetailsResult>() {
			public void onResult(GetDetailsResult result) {
				ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
				DataOutput output = new DataOutputStream(byteArrayOutput);
				try {
					output.writeUTF("IP");
					output.writeUTF(result.getIp());
					output.writeInt(result.getPort());
				} catch(IOException exception) {
					// ignore
				}
				IPQuery.this.server.getMessenger().dispatchIncomingMessage(player, Constants.channel, byteArrayOutput.toByteArray());
			}
		});
	}

	public String getId() {
		return "IP";
	}

}
