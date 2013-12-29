package lilypad.bukkit.compat.bungee.query;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import lilypad.bukkit.compat.bungee.util.Constants;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class PacketToQueryIntercepter extends PacketAdapter {

	private QueryLookupService queryLookupService;

	public PacketToQueryIntercepter(Plugin plugin, QueryLookupService queryLookupService) {
		super(plugin, new PacketType[] { PacketType.Play.Server.CUSTOM_PAYLOAD, PacketType.Play.Client.CUSTOM_PAYLOAD });
		this.queryLookupService = queryLookupService;
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		String channel = event.getPacket().getStrings().read(0);
		if(!channel.equals(Constants.channel)) {
			return;
		}
		event.setCancelled(true);
		DataInput input = new DataInputStream(new ByteArrayInputStream(event.getPacket().getByteArrays().read(0)));
		String id;
		try {
			id = input.readUTF();
		} catch(IOException exception) {
			exception.printStackTrace();
			return;
		}
		try {
			this.queryLookupService.getById(id).execute(event.getPlayer(), id, input);
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void onPacketReceiving(PacketEvent event) {
		String channel = event.getPacket().getStrings().read(0);
		if(!channel.equals(Constants.channel)) {
			return;
		}
		event.setCancelled(true);
	}

}
