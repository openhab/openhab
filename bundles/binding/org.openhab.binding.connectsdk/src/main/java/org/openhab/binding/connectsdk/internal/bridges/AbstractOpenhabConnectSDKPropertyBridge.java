package org.openhab.binding.connectsdk.internal.bridges;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.command.URLServiceSubscription;

public abstract class AbstractOpenhabConnectSDKPropertyBridge<T> implements OpenhabConnectSDKPropertyBridge {
	private static final Logger logger = LoggerFactory.getLogger(AbstractOpenhabConnectSDKPropertyBridge.class);

	private Map<String, ServiceSubscription<T>> subscriptions;

	private synchronized Map<String, ServiceSubscription<T>> getSubscriptions() {
		if (subscriptions == null) {
			subscriptions = new ConcurrentHashMap<String, ServiceSubscription<T>>();
		}
		return subscriptions;
	}

	@Override
	public final void addSubscription(final ConnectableDevice device,
			final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher) { 
		ServiceSubscription<T> listener = getSubscription(device, providers, eventPublisher);
		if (listener != null) {
			logger.debug("Subscribed {}:{} listener on IP: {}", getItemClass(), getItemProperty(),
					device.getIpAddress());
			
			// not sure if this is required, trying to find performance leak 
			if(subscriptions != null && subscriptions.containsKey(device.getIpAddress())){
				throw new IllegalStateException("A listener for device "+device.getIpAddress()+" already exists for this Bridge: "+ this.getClass().getName()+ " Call removeAnySubscription first.");
			}
			
			getSubscriptions().put(device.getIpAddress(), listener);
		}
	}

	/**
	 * Creates a subscription instance for this device. This may return <code>null</code> if no subscription is possible or required.
	 * 
	 * @param device
	 * @param providers
	 * @param eventPublisher
	 * @return instance or <code>null</code> if no subscription is possible or required
	 */
	protected abstract ServiceSubscription<T> getSubscription(final ConnectableDevice device,
			final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher);

	@Override
	public final synchronized void removeAnySubscription(final ConnectableDevice device) { // here
		if (subscriptions != null) { // only if subscriptions was initialized (lazy loading)
			ServiceSubscription<T> l = subscriptions.remove(device.getIpAddress());
			if (l != null) {
				l.unsubscribe();
				
				// not sure if this is required, trying to find performance leak 
				if(l instanceof URLServiceSubscription) {
					((URLServiceSubscription<?>)l).removeListeners();
				}
				
				logger.debug("Unsubscribed {}:{} listener on IP: {}", getItemClass(), getItemProperty(),
						device.getIpAddress());
			}
		}
	}

	/**
	 * 
	 * @return the third binding configuration parameter in the items file
	 */
	protected abstract String getItemProperty();

	/**
	 * 
	 * @return the second binding configuration parameter in the items file
	 */
	protected abstract String getItemClass();

	protected boolean matchClassAndProperty(String clazz, String property) {
		return getItemClass().equals(clazz) && getItemProperty().equals(property);
	}

	protected <O> ResponseListener<O> createDefaultResponseListener() {
		return new ResponseListener<O>() {

			@Override
			public void onError(ServiceCommandError error) {
				logger.error("Error setting {}:{}: {}.", getItemClass(), getItemProperty(), error.getMessage());

			}

			@Override
			public void onSuccess(O object) {
				logger.debug("Successfully set {}:{}: {}.", getItemClass(), getItemProperty(), object);

			}
		};
	}

}