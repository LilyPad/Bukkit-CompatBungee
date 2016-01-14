package lilypad.bukkit.compat.bungee.query;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FuzzyReflection;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.MethodAccessor;
import com.comphenix.protocol.utility.MinecraftReflection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
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
		PacketContainer packet = event.getPacket();

		Class<?> dataSerializerClass = MinecraftReflection.getPacketDataSerializerClass();
		StructureModifier<?> dataSerializers = packet.getSpecificModifier(dataSerializerClass);
		Object dataSerializer = dataSerializers.read(0);

		FuzzyReflection fuzzy = FuzzyReflection.fromClass(dataSerializerClass);
		MethodAccessor readableBytes = Accessors.getMethodAccessor(fuzzy.getMethodByParameters("readableBytes", int.class, new Class<?>[0]));
		MethodAccessor readBytes = Accessors.getMethodAccessor(fuzzy.getMethodByParameters("readBytes", ByteBuf.class, new Class<?>[] { int.class }));

		int i = (int) readableBytes.invoke(dataSerializer);
		ByteBuf bytes = (ByteBuf) readBytes.invoke(dataSerializer, i);

		String channel = event.getPacket().getStrings().read(0);
		if(!channel.equals(Constants.channel)) {
			return;
		}
		event.setCancelled(true);
		DataInput input = new DataInputStream(new ByteBufInputStream(bytes));
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
