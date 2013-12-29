package lilypad.bukkit.compat.bungee.connect;

public class NetServer {

	private String name;
	private long lastUpdate = System.currentTimeMillis();
	
	public NetServer(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void flagUpdated() {
		this.lastUpdate = System.currentTimeMillis();
	}
	
	public boolean isExpired() {
		return System.currentTimeMillis() - this.lastUpdate > 5000L;
	}
	
}
