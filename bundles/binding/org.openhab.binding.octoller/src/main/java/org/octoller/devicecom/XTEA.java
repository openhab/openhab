/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.octoller.devicecom;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Implementation of the XTEA algorithm for data encryption to octoller device
 * 
 * @author JPlenert
 * @since 1.5.1
 * openHAB binding for octoller (www.octoller.com)
 * Only for preperation for use without octoller-Gateway
 */
class XTEA
{
	
	static int delta = 0x9E3779B9;
	
	static void encipher(int num_cycles, int[] v, int[] k) throws Exception
	{
		if (v.length != 2 || k.length != 4)
			throw new Exception("Invalid length for parameter v or k");

		int v0 = v[0], v1 = v[1], sum = 0;

		for (int i=0; i < num_cycles; i++) 
		{
			v0 += (((v1 << 4) ^ (v1 >> 5)) + v1) ^ (sum + k[sum & 3]);
			sum += delta;
			v1 += (((v0 << 4) ^ (v0 >> 5)) + v0) ^ (sum + k[(sum>>11) & 3]);
		}
		v[0] = v0; v[1] = v1;
	}
	
	static void decipher (int num_cycles, int[] v, int[] k) throws Exception
	{
		if (v.length != 2 || k.length != 4)
			throw new Exception("Invalid length for parameter v or k");

		int v0 = v[0], v1 = v[1], sum = delta * num_cycles;
		for (int i=0; i < num_cycles; i++) 
		{
			v1 -= (((v0 << 4) ^ (v0 >> 5)) + v0) ^ (sum + k[(sum>>11) & 3]);
			sum -= delta;
			v0 -= (((v1 << 4) ^ (v1 >> 5)) + v1) ^ (sum + k[sum & 3]);
		}
		v[0] = v0; v[1] = v1;
	}
	
	/**
	 * Converts bytes into an integer (e.g. for the key creation)
	 * @param b
	 * @param offset
	 * @return
	 */
	public static int byteArrayToInt(byte[] b, int offset) 
	{
	    final ByteBuffer bb = ByteBuffer.wrap(b, offset, 4);
	    bb.order(ByteOrder.LITTLE_ENDIAN);
	    return bb.getInt();
	}

	
	/**
	 * Converts an int to a byte array
	 * @param i
	 * @return
	 */
	public static byte[] intToByteArray(int i) 
	{
	    final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
	    bb.order(ByteOrder.LITTLE_ENDIAN);
	    bb.putInt(i);
	    return bb.array();
	}
	
	/**
	 * Crypts data using XTEA algorithm
	 * @param key 
	 * 				key for crypt (int[4]) 
	 * @param data
	 * 				data to crypt
	 * @param offset
	 * 				offset in data
	 * @param dataLen
	 * 				length of data to crypt
	 * @throws Exception
	 */
	public static void XTEACrypt(int[] key, byte[] data, int offset, int dataLen) throws Exception
	{
		int pos;
		int[] values = new int[2];
		int[] keyUse;

		// Copy original key, because it will be modified
		keyUse = Arrays.copyOf(key, key.length);

		// encrypt data as long as aligned to 64 bits
		for (pos = offset; pos<offset+dataLen; pos+=8)
		{
			values[0] = byteArrayToInt(data, pos);
			values[1] = byteArrayToInt(data, pos+4);
			encipher(32, values, keyUse);
			System.arraycopy(intToByteArray(values[0]), 0, data, pos, 4);
			System.arraycopy(intToByteArray(values[1]), 0, data, pos+4, 4);

			// Modify key
			keyUse[1] ^= values[0];
			keyUse[2] ^= values[1];
		}
	}
	
	/**
	 * Decrypts data using XTEA alogrithm
	 * @param key 
	 * 				key for decrypt (int[4]) 
	 * @param data
	 * 				data to decrypt
	 * @param offset
	 * 				offset in data
	 * @param dataLen
	 * 				length of data to decrypt
	 * @throws Exception
	 */
	public static void XTEADecrypt(int[] key, byte[] data, int offset, int dataLen) throws Exception
	{
		int pos;
		int[] values = new int[2];
		int[] cryptValues = new int[2];
		int[] keyUse;

		// Copy original key, because it will be modified
		keyUse = Arrays.copyOf(key, key.length);

		// encrypt data as long as aligned to 64 bits
		for (pos = offset; pos < offset + dataLen; pos += 8)
		{
			cryptValues[0] = values[0] = byteArrayToInt(data, pos);
			cryptValues[1] = values[1] = byteArrayToInt(data, pos+4);
			decipher(32, values, keyUse);
			System.arraycopy(intToByteArray(values[0]), 0, data, pos, 4);
			System.arraycopy(intToByteArray(values[1]), 0, data, pos+4, 4);

			// Modify key
			keyUse[1] ^= cryptValues[0];
			keyUse[2] ^= cryptValues[1];
		}
	}
	
}
