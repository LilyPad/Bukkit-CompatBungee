package lilypad.bukkit.compat.bungee.query;

import java.util.HashMap;
import java.util.Map;

public class QueryLookupService {

	private Map<String, Query> queries = new HashMap<String, Query>();
	
	public void submit(Query query) {
		this.queries.put(query.getId(), query);
	}
	
	public Query getById(String id) {
		return this.queries.get(id);
	}
	
}
