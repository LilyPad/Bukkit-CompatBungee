package lilypad.bukkit.compat.bungee.util;

import java.util.Collection;

public class StringUtils {

	public static String concat(Collection<String> strings, String separator) {
		return concat(strings.toArray(new String[strings.size()]), separator);
	}
	
	public static String concat(String[] strings, String separator) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < strings.length; i++) {
			if (i != 0) {
				builder.append(separator);
			}
			builder.append(strings[i]);
		}
		return builder.toString();
	}
	
}
