package lilypad.bukkit.compat.bungee.connect;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import lilypad.bukkit.compat.bungee.util.Constants;
import lilypad.bukkit.compat.bungee.util.StringUtils;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;

public class NetMessageListener {

	private NetServerCache serverCache;
	private Connect connect;
	private Server server;
	
	public NetMessageListener(NetServerCache serverCache, Connect connect, Server server) {
		this.serverCache = serverCache;
		this.connect = connect;
		this.server = server;
	}
	
	@EventListener
	public void onMessage(MessageEvent event) {
		if(event.getChannel().equals("lpCbA")) {
			this.serverCache.flagUpdated(event.getSender());
		} else if(event.getChannel().equals("lpCbF")) {
			if(event.getSender().equals(this.connect.getSettings().getUsername())) {
				return;
			}
			Player player = null;
			if(this.server.getOnlinePlayers().length != 0) {
				player = this.server.getOnlinePlayers()[0];
			}
			this.server.getMessenger().dispatchIncomingMessage(player, Constants.channel, event.getMessage());
		} else if(event.getChannel().equals("lpCbMsg")) {
			String message;
			try {
				message = event.getMessageAsString();
			} catch(UnsupportedEncodingException exception) {
				return;
			}
			String player = message.substring(0, message.indexOf(' '));
			message = message.substring(message.indexOf(' ') + 1);
			Player bukkit = this.server.getPlayer(player);
			if(bukkit == null) {
				return;
			}
			bukkit.sendMessage(message);
		} else if(event.getChannel().equals("lpCbPCR")) {
			String message;
			try {
				message = event.getMessageAsString();
			} catch(UnsupportedEncodingException exception) {
				return;
			}
			ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
			DataOutput output = new DataOutputStream(byteArrayOutput);
			try {
				output.writeUTF("PlayerCount");
				output.writeUTF(event.getSender());
				output.writeInt(Integer.parseInt(message));
			} catch(IOException exception) {
				// ignore
			}
			Player player = null;
			if(this.server.getOnlinePlayers().length != 0) {
				player = this.server.getOnlinePlayers()[0];
			}
			this.server.getMessenger().dispatchIncomingMessage(player, Constants.channel, byteArrayOutput.toByteArray());
		} else if(event.getChannel().equals("lpCbPC")) {
			try {
				this.connect.request(new MessageRequest(event.getSender(), "lpCbPCR", Integer.toString(this.server.getOnlinePlayers().length)));
			} catch(UnsupportedEncodingException exception) {
				// ignore
			} catch(RequestException exception) {
				// ignore
			}
		} else if(event.getChannel().equals("lpCbPLR")) {
			String message;
			try {
				message = event.getMessageAsString();
			} catch(UnsupportedEncodingException exception) {
				return;
			}
			ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
			DataOutput output = new DataOutputStream(byteArrayOutput);
			try {
				output.writeUTF("PlayerList");
				output.writeUTF(event.getSender());
				output.writeUTF(message);
			} catch(IOException exception) {
				// ignore
			}
			Player player = null;
			if(this.server.getOnlinePlayers().length != 0) {
				player = this.server.getOnlinePlayers()[0];
			}
			this.server.getMessenger().dispatchIncomingMessage(player, Constants.channel, byteArrayOutput.toByteArray());
		} else if(event.getChannel().equals("lpCbPL")) {
			Player[] players = this.server.getOnlinePlayers();
			String[] playerNames = new String[players.length];
			for(int i = 0; i < players.length; i++) {
				playerNames[i] = players[i].getName();
			}
			try {
				this.connect.request(new MessageRequest(event.getSender(), "lpCbPLR", StringUtils.concat(playerNames, ", ")));
			} catch(UnsupportedEncodingException exception) {
				// ignore
			} catch(RequestException exception) {
				// ignore
			}
		}
	}
	
}