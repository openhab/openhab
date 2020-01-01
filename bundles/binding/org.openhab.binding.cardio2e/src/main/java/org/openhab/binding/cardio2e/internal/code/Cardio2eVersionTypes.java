/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.cardio2e.internal.code;

/**
 * Enumerates the various VersionTypes for Cardio2e binding Also provides
 * Cardio2e string communication protocol symbol conversion
 * 
 * Note: We unknown what each type of version means. The complete Cardio2e
 * version is a string as C-M-P-S. Example: C=2.40S.512, M=2.2, P=1.00, S=1111
 * -> Cardio2e COMPLETE VERSION=2.40S.512-2.2-1.00-1111
 * 
 * @author Manuel Alberto Guerrero Díaz
 * @Since 1.11.0
 */

public enum Cardio2eVersionTypes {

	C {
		{
			symbol = 'C';
		}
	},

	M {
		{
			symbol = 'M';
		}
	},

	P {
		{
			symbol = 'P';
		}
	},

	S {
		{
			symbol = 'S';
		}
	};

	public char symbol;

	public static Cardio2eVersionTypes fromString(String versionType) {

		if ("".equals(versionType)) {
			return null;
		} else {
			versionType = versionType.toUpperCase();
		}

		for (Cardio2eVersionTypes type : Cardio2eVersionTypes.values()) {
			if (type.name().equals(versionType)) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid versionType '"
				+ versionType + "'");
	}

	public static Cardio2eVersionTypes fromSymbol(char versionType) {
		for (Cardio2eVersionTypes type : Cardio2eVersionTypes.values()) {
			if (type.symbol == versionType) {
				return type;
			}
		}

		throw new IllegalArgumentException("invalid versionType '"
				+ versionType + "'");
	}

}
