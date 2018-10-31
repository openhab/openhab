/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.bridge;

import org.openhab.binding.velux.bridge.comm.SetSilentMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link VeluxBridgeSceneMode} represents a complete set of transactions
 * for modifying the silent-mode of a scene defined on the <B>Velux</B> bridge.
 * <P>
 * It therefore provides a method
 * <UL>
 * <LI>{@link VeluxBridgeSceneMode#setSilentMode} for execution of a scene.
 * </UL>
 * Any parameters are controlled by {@link org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration}.
 *
 * @author Guenther Schreiner - Initial contribution
 */
public class VeluxBridgeSceneMode {
	private final Logger logger = LoggerFactory.getLogger(VeluxBridgeSceneMode.class);

	/**
	 * Login into bridge, executes a scene and logout from bridge based
	 * on a well-prepared environment of a {@link VeluxBridgeProvider}.
	 *
	 * @param bridge     Initialized Velux bridge handler.
	 * @param sceneNo    Number of scene to be executed.
	 * @param silentMode Mode of this mentioned scene.
	 * @return <b>success</b>
	 *         of type boolean describing the overall result of this interaction.
	 */
	public boolean setSilentMode(VeluxBridge bridge, int sceneNo, boolean silentMode) {
		logger.trace("setSilentMode({},{}) called.", sceneNo, silentMode);

		SetSilentMode bcp = bridge.bridgeAPI().setSilentMode();
		bcp.setMode(sceneNo, silentMode);
		if ((bridge.bridgeCommunicate(bcp)) && (bcp.isCommunicationSuccessful())) {
			logger.info("setSilentMode() finished successfully.");
			return true;
		}
		logger.trace("setSilentMode() finished with failure.");
		return false;
	}
}
/**
 * end-of-bridge/VeluxBridgeSceneMode.java
 */
