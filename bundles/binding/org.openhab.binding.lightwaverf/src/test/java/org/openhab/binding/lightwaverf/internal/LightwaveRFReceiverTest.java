package org.openhab.binding.lightwaverf.internal;

import static org.junit.Assert.*;

import org.junit.Test;

public class LightwaveRFReceiverTest {

	@Test
	public void test() throws Exception {
		LightwaveRFReceiver receiver = new LightwaveRFReceiver(new LightwaverfConvertor(), 9761);
		receiver.start();
		Thread.sleep(5 * 1000 * 60);	}

}
