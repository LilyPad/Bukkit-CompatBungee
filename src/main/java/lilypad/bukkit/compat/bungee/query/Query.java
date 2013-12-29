package lilypad.bukkit.compat.bungee.query;

import java.io.DataInput;

import org.bukkit.entity.Player;

public interface Query {

	public void execute(Player player, String id, DataInput input) throws Exception;
	
	public String getId();
	
}
