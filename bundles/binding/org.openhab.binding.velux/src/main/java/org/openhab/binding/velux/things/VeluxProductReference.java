/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.things;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Velux</B> product reference representation.
 * <P>
 * Combined set of information with reference towards a single Velux product.
 *
 * @see VeluxProduct
 *
 * @author Guenther Schreiner - initial contribution.
 */
public class VeluxProductReference {
	private final Logger logger = LoggerFactory.getLogger(VeluxProductReference.class);

	// Class internal

	private VeluxProductName name;
	private VeluxProductType typeId;

	// Constructor

	public VeluxProductReference(VeluxProduct product) {
		this.name = product.getName();
		this.typeId = product.getTypeId();
	}

	public VeluxProductReference(VeluxProductName name, int type) {
		this.name = name;
		this.typeId = VeluxProductType.get(type);
		if (this.typeId == null) {
			logger.warn(
					"Please report this to maintainer: VeluxProductReference({}) has found an unregistered ProductTypeId.",
					type);
		}
	}
	// Class access methods

	public VeluxProductName getName() {
		return this.name;
	}

	public VeluxProductType getTypeId() {
		return this.typeId;
	}

	@Override
	public String toString() {
		return String.format("Prod.ref. \"%s\"/%s", this.name, this.typeId);
	}

	// Class helper methods

	public String getProductUniqueIndex() {
		return this.name.toString().concat("#").concat(this.typeId.toString());
	}

}

/**
 * end-of-VeluxProductReference.java
 */
