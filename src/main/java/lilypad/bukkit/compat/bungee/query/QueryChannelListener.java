package lilypad.bukkit.compat.bungee.query;

import java.util.Set;

import lilypad.bukkit.compat.bungee.util.Constants;
import lilypad.bukkit.compat.bungee.util.ReflectionUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class QueryChannelListener implements Listener {

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		try {
			ReflectionUtils.getPrivateField(event.getPlayer(), Set.class, "channels").add(Constants.channel);
		} catch(Exception exception) {
			System.out.println("[BungeeCompat] Failed to add channel to player");
			exception.printStackTrace();
		}
	}
	
}
