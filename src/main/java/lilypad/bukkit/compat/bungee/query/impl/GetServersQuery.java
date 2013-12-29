package lilypad.bukkit.compat.bungee.query.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import lilypad.bukkit.compat.bungee.connect.NetServerCache;
import lilypad.bukkit.compat.bungee.query.Query;
import lilypad.bukkit.compat.bungee.util.Constants;
import lilypad.bukkit.compat.bungee.util.StringUtils;

public class GetServersQuery implements Query {

	private NetServerCache serverCache;
	private Server server;
	
	public GetServersQuery(NetServerCache serverCache, Server server) {
		this.serverCache = serverCache;
		this.server = server;
	}
	
	public void execute(Player player, String id, DataInput input) throws Exception {
		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
		DataOutput output = new DataOutputStream(byteArrayOutput);
		output.writeUTF("GetServers");
		output.writeUTF(StringUtils.concat(this.serverCache.getServers(), ", "));
		this.server.getMessenger().dispatchIncomingMessage(player, Constants.channel, byteArrayOutput.toByteArray());
	}

	public String getId() {
		return "GetServers";
	}

}
