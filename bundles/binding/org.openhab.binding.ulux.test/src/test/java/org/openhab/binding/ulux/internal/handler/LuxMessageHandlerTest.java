package org.openhab.binding.ulux.internal.handler;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.messages.LuxMessage;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.UnDefType;

public class LuxMessageHandlerTest extends AbstractHandlerTest<LuxMessage> {

	@Override
	protected AbstractMessageHandler<LuxMessage> createMessageHandler() {
		return new LuxMessageHandler();
	}

	@Test
	public void testInvalid() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Lux"), "1:0:Lux");

		handleMessage("08:03:00:00:00:00:00:00");

		verify(eventPublisher).postUpdate("Ulux_Lux", UnDefType.UNDEF);
	}

	@Test
	public void testValid() throws Exception {
		addBindingConfig(new SwitchItem("Ulux_Lux"), "1:0:Lux");

		handleMessage("08:03:00:00:13:02:01:00");

		verify(eventPublisher).postUpdate("Ulux_Lux", new DecimalType(531));
	}

}
