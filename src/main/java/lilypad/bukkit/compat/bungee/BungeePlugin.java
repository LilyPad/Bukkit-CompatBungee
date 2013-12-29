package lilypad.bukkit.compat.bungee;

import lilypad.bukkit.compat.bungee.connect.NetMessageListener;
import lilypad.bukkit.compat.bungee.connect.NetServerAnnouncer;
import lilypad.bukkit.compat.bungee.connect.NetServerCache;
import lilypad.bukkit.compat.bungee.query.PacketToQueryIntercepter;
import lilypad.bukkit.compat.bungee.query.QueryChannelListener;
import lilypad.bukkit.compat.bungee.query.QueryLookupService;
import lilypad.bukkit.compat.bungee.query.impl.ConnectOtherQuery;
import lilypad.bukkit.compat.bungee.query.impl.ConnectQuery;
import lilypad.bukkit.compat.bungee.query.impl.ForwardQuery;
import lilypad.bukkit.compat.bungee.query.impl.GetServerQuery;
import lilypad.bukkit.compat.bungee.query.impl.GetServersQuery;
import lilypad.bukkit.compat.bungee.query.impl.IPQuery;
import lilypad.bukkit.compat.bungee.query.impl.MessageQuery;
import lilypad.bukkit.compat.bungee.query.impl.PlayerCountQuery;
import lilypad.bukkit.compat.bungee.query.impl.PlayerListQuery;
import lilypad.bukkit.compat.bungee.query.impl.UUIDOtherQuery;
import lilypad.bukkit.compat.bungee.query.impl.UUIDQuery;
import lilypad.bukkit.compat.bungee.util.Constants;
import lilypad.client.connect.api.Connect;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

public class BungeePlugin extends JavaPlugin {

	private NetMessageListener messageListener;
	
	@Override
	public void onEnable() {
		Connect connect = this.getConnect();
		NetServerCache serverCache = new NetServerCache();
		connect.registerEvents(this.messageListener = new NetMessageListener(serverCache, connect, super.getServer()));
		super.getServer().getScheduler().runTaskTimer(this, new NetServerAnnouncer(connect), 20L, 20L);
		QueryLookupService queryLookupService = new QueryLookupService();
		queryLookupService.submit(new ConnectOtherQuery(connect));
		queryLookupService.submit(new ConnectQuery(connect));
		queryLookupService.submit(new ForwardQuery(connect));
		queryLookupService.submit(new GetServerQuery(connect, super.getServer()));
		queryLookupService.submit(new GetServersQuery(serverCache, super.getServer()));
		queryLookupService.submit(new IPQuery(super.getServer()));
		queryLookupService.submit(new MessageQuery(connect));
		queryLookupService.submit(new PlayerCountQuery(connect, super.getServer()));
		queryLookupService.submit(new PlayerListQuery(connect, super.getServer()));
		queryLookupService.submit(new UUIDQuery(super.getServer()));
		queryLookupService.submit(new UUIDOtherQuery(connect));
		super.getServer().getMessenger().registerOutgoingPluginChannel(this, Constants.channel);
		super.getServer().getPluginManager().registerEvents(new QueryChannelListener(), this);
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketToQueryIntercepter(this, queryLookupService));
	}
	
	@Override
	public void onDisable() {
		this.getConnect().unregisterEvents(this.messageListener);
	}
	
	public Connect getConnect() {
		return super.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
	}
	
}
