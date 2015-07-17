package org.openhab.binding.ulux.internal.handler.messages;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.types.DecimalType;

public class AudioVolumeMessageHandlerTest extends AbstractUluxMessageTest {

	@Test
	public void testNoConfig() throws Exception {
		handleMessage("06:91:00:00:4B:00");

		// nothing happens
	}

	@Test
	public void testVolume75() throws Exception {
		addBindingConfig(new DimmerItem("Ulux_AudioVolume"), "{switchId=1, type='AUDIO_VOLUME'}");

		handleMessage("06:91:00:00:4B:00");

		verify(eventPublisher).postUpdate("Ulux_AudioVolume", new DecimalType(75));
	}

}
