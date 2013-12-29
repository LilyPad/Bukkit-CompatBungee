package lilypad.bukkit.compat.bungee.util;

import java.lang.reflect.Field;

public class ReflectionUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getPrivateField(Object object, Class<T> fieldClass, String fieldName) throws Exception {
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return (T) field.get(object);
	}
	
}
