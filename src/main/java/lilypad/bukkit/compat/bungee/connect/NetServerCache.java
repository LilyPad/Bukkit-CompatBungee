package lilypad.bukkit.compat.bungee.connect;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NetServerCache {

	private Map<String, NetServer> servers = new ConcurrentHashMap<String, NetServer>();

	public void flagUpdated(String name) {
		if(this.servers.containsKey(name)) {
			this.servers.get(name).flagUpdated();
		}
		this.servers.put(name, new NetServer(name));
	}
	
	public Set<String> getServers() {
		Set<String> servers = new HashSet<String>();
		for(NetServer server : this.servers.values()) {
			if(server.isExpired()) {
				continue;
			}
			servers.add(server.getName());
		}
		return servers;
	}
	
}
