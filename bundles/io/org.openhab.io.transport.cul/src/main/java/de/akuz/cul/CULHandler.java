package de.akuz.cul;

/**
 * An interface representing a culfw based device. Can only be obtained via
 * CULManager and has to be closed via CULManager. Classes implementing this
 * interface need to have a constructor with a String as device address and the
 * CULMode as parameters.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public interface CULHandler {

	/**
	 * Register a listener to receive data and get notified about exceptions.
	 * 
	 * @param listener
	 */
	public void registerListener(CULListener listener);

	/**
	 * Unregister a previous registered listener. If you don't need the
	 * CULHanlder anymore and want to close it, unregister your listener first.
	 * 
	 * @param listener
	 */
	public void unregisterListener(CULListener listener);

	/**
	 * Send a String representing a culfw command to the CULHandler. Note that
	 * Strings changing the RF mode will be discarded silently.
	 * 
	 * @param command
	 * @throws CULCommunicationException
	 */
	public void send(String command) throws CULCommunicationException;

	/**
	 * Get the RF mode the device is currently in.
	 * 
	 * @return
	 */
	public CULMode getCULMode();
}
