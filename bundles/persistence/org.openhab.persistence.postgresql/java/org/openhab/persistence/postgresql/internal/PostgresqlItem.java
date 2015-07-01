package org.openhab.persistence.postgresql.internal;

import java.text.DateFormat;
import java.util.Date;

import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.types.State;

/**
 * This is a Java bean used to return historic items from a SQL database.
 * 
 * PostreSQL:
 * @author Helmut Lehmeyer
 * 
 * MySQL:
 * @author Chris Jackson
 * @since 1.3.0
 *
 */
public class PostgresqlItem implements HistoricItem {

	final private String name;
	final private State state;
	final private Date timestamp;
	
	public PostgresqlItem(String name, State state, Date timestamp) {
		this.name = name;
		this.state = state;
		this.timestamp = timestamp;
	}
	
	public String getName() {
		return name;
	}
	
	public State getState() {
		return state;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return DateFormat.getDateTimeInstance().format(timestamp) + ": " + name + " -> "+ state.toString();
	}

}