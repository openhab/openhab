/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki;

import org.openhab.binding.hideki.HidekiBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Alexander Falkenstern
 * @since 1.5.0
 */
public interface HidekiBindingProvider extends BindingProvider {		
  public HidekiBindingConfig getBindingConfig(String itemName);
	
  /**
   * Returns the item identified by {@code itemName}
   * 
   * @param itemName the name of the item to find
   * @return item identified by {@code itemName}
   */
  public Item getItem(String itemName);
}
