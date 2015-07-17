package org.openhab.binding.ulux.internal.handler.messages;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.openhab.binding.ulux.internal.ump.AbstractUluxMessageTest;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;

public class EventMessageHandlerTest extends AbstractUluxMessageTest {

	@Test
	public void testNoKeyPressed() throws Exception {
		addBindingConfig(new SwitchItem("Key_1"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='1'}");
		addBindingConfig(new SwitchItem("Key_2"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='2'}");
		addBindingConfig(new SwitchItem("Key_3"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='3'}");
		addBindingConfig(new SwitchItem("Key_4"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='4'}");

		handleMessage("06:51:00:00:00:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.OFF);
	}

	@Test
	public void testKey1Pressed() throws Exception {
		addBindingConfig(new SwitchItem("Key_1"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='1'}");
		addBindingConfig(new SwitchItem("Key_2"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='2'}");
		addBindingConfig(new SwitchItem("Key_3"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='3'}");
		addBindingConfig(new SwitchItem("Key_4"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='4'}");

		handleMessage("06:51:00:00:01:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.ON);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.OFF);
	}

	@Test
	public void testKey2Pressed() throws Exception {
		addBindingConfig(new SwitchItem("Key_1"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='1'}");
		addBindingConfig(new SwitchItem("Key_2"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='2'}");
		addBindingConfig(new SwitchItem("Key_3"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='3'}");
		addBindingConfig(new SwitchItem("Key_4"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='4'}");

		handleMessage("06:51:00:00:02:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.ON);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.OFF);
	}

	@Test
	public void testKey3Pressed() throws Exception {
		addBindingConfig(new SwitchItem("Key_1"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='1'}");
		addBindingConfig(new SwitchItem("Key_2"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='2'}");
		addBindingConfig(new SwitchItem("Key_3"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='3'}");
		addBindingConfig(new SwitchItem("Key_4"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='4'}");

		handleMessage("06:51:00:00:04:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.ON);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.OFF);
	}

	@Test
	public void testKey4Pressed() throws Exception {
		addBindingConfig(new SwitchItem("Key_1"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='1'}");
		addBindingConfig(new SwitchItem("Key_2"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='2'}");
		addBindingConfig(new SwitchItem("Key_3"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='3'}");
		addBindingConfig(new SwitchItem("Key_4"), "{switchId=1, actorId=1, type='KEY', additionalConfiguration='4'}");

		handleMessage("06:51:00:00:08:00");

		verify(eventPublisher).postUpdate("Key_1", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_2", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_3", OnOffType.OFF);
		verify(eventPublisher).postUpdate("Key_4", OnOffType.ON);
	}

}
