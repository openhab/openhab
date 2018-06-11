<html><body class="c35"><h1 class="c19" id="h.ktmr5oqhw0za"><span class="c20 c24">Cardio2e Binding</span></h1><p class="c6"><span class="c0">The openHAB Cardio2e binding allows one to connect to Secant Cardio II&eacute; home automation system installations. Dimming or switching lights on and off, switching devices on and off, activating roller shutters, executing escenarios, managing HVACs or activating system security are only some examples.</span></p><p class="c6"><span class="c3">To access your Cardio system you either need an RS-232 interface (like e.g. a Prolific PL-232 based USB to RS-232 converter interface), and a DB-9 to RJ-11 cable suitable for either Cardio PC or Cardio ACC port. You can use this </span><span class="c37"><a class="c22" href="https://github.com/openhab/openhab1-addons/files/999699/PcCardio_Cable.pdf&amp;sa=D&amp;ust=1528711214015000">schema</a></span><span class="c3">&nbsp;to build your Cardio PC port cable</span><span class="c33">.</span></p><p class="c6"><span class="c33">N</span><span class="c33">ote: You can also use the Cardio accessory port (ACC) instead of PC port, so you must crimp the RJ-11 in reverse order, exchange RD and TD signals from the DB-9 connector with respect to the previous PC schematic </span><span class="c33">(pins 2 and 3) </span><span class="c2">and do not connect pin 7.</span></p><h2 class="c32" id="h.4ensa2aa9gm"><span class="c16">Binding Configuration</span></h2><p class="c4"><span class="c3">This binding can be configured in the file </span><span class="c14">configurations/openhab.cfg </span><span class="c3">(openHAB 1) or &nbsp;</span><span class="c14">conf/services/cardio2e.cfg </span><span class="c3">(openHAB 2)</span><span class="c0">.</span></p><a id="t.91c536f4f2c525306d6d2e720fdb7f6e035cf670"></a><a id="t.0"></a><table class="c39"><tbody><tr class="c10"><td class="c17" colspan="1" rowspan="1"><p class="c11"><span class="c3 c25">Property</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c11"><span class="c3 c25">Default</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c11"><span class="c3 c25">Required</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c11"><span class="c3 c25">Description</span></p></td></tr><tr class="c26"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c0">port</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4 c31"><span class="c0"></span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">Yes</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Serial RS-232 port<br>Examples: &#39;/dev/ttyUSB0&#39; for Linux, &#39;COM1&#39; for Windows.</span></p></td></tr><tr class="c9"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c3">programcode</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">00000</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Installer program code for login.</span></p></td></tr><tr class="c38"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c3">securitycode</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4 c31"><span class="c0"></span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c3">S</span><span class="c3">ecurity code for arm / disarm alarm.</span></p></td></tr><tr class="c15"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c0">zones</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">false</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Alarm zones state detection. Enables alarm zones state detection (by default &#39;false&#39; for minimum use of resources).</span></p></td></tr><tr class="c10"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c0">zoneUnchangedMinRefreshDelay</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">600000</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Minimum delay in milliseconds for zone detection refresh when no state changes succeed (by default &#39;600000&#39; milliseconds = 10 minutes).</span></p></td></tr><tr class="c10"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c0">datetimeMaxOffset</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">15</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Date and time maximum offset allowed (in minutes) for progressive (step by step, minute by minute) date and time state update.<br>Special values: &#39;0&#39; will remove offset limit, &#39;-1&#39; will disable progressive update and will remove offset limit, &#39;-2&#39; will do unconditional update without any filter even if current date and time of Cardio II&eacute; matches the update.</span></p></td></tr><tr class="c15"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c3">firstUpdateWillSetDatetime</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">false</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Always will set Cardio II&eacute; clock on first date and time update from last binding start, even if allowedDatetimeUpdateHour was set.</span></p></td></tr><tr class="c9"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c3">allowedDatetimeUpdateHour</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">-1</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Allows date and time updates on specified hour only (for example, a safe hour when no events will be triggered by Cardio&#39;s schedules).<br>Valid values are from &#39;0&#39; to &#39;23&#39; (&#39;-1&#39; disables hour restriction).</span></p></td></tr><tr class="c9"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c0">testmode</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">false</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c3">E</span><span class="c3">nables fake port console test mode, for developer debug purposes only.<br>Warning: Real communication with Cardio 2&eacute; will not work if enabled!</span></p></td></tr><tr class="c9"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c0">minDelayBetweenReceivingAndSending</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">200</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Minimum delay in milliseconds between receiving and sending, for expert tuning only.<br>By default &#39;200&#39; milliseconds (tested safe value).</span></p></td></tr><tr class="c15"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c3">minDelayBetweenSendings</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">300</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Minimum delay in milliseconds between sendings, for expert tuning only.<br>By default &#39;300&#39; milliseconds (tested safe value).</span></p></td></tr><tr class="c10"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c3">filterUnnecessaryCommand</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">false</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Avoid sending commands when the last value of the object reported by Cardio II&eacute; is the same value as the command value.</span></p></td></tr><tr class="c10"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c3">filterUnnecessaryReverseModeUpdate</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4"><span class="c0">true</span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Avoid sending updates (reverse mode) when the last value of the object reported by Cardio II&eacute; is the same value as the update value.</span></p></td></tr><tr class="c5"><td class="c17" colspan="1" rowspan="1"><p class="c4"><span class="c3">smartSendingEnabledObjectTypes</span></p></td><td class="c8" colspan="1" rowspan="1"><p class="c4 c31"><span class="c0"></span></p></td><td class="c12" colspan="1" rowspan="1"><p class="c4"><span class="c0">No</span></p></td><td class="c21" colspan="1" rowspan="1"><p class="c4"><span class="c0">Comma separated list of smart sending enabled object types. Smart sending enabled means that no contradictory commands will be stored in the sending buffer for that object type, so that if a command directed to a specific object exists in sending buffer and a new command is received for the same object, the command stored will be replaced by the new one.<br>Valid values are &#39;LIGHTING&#39;, &#39;RELAY&#39;, &#39;HVAC_CONTROL&#39;, &#39;DATE_AND_TIME&#39;, &#39;SCENARIO&#39;, &#39;SECURITY&#39;, &#39;ZONES_BYPASS&#39; and &#39;CURTAIN&#39;.</span></p></td></tr></tbody></table><h3 class="c32" id="h.1cx49h468ue5"><span class="c20 c29 c25 c28">Example</span></h3><p class="c18"><span class="c7 c20">port=/dev/ttyUSB0<br>programcode=00000<br>securitycode=12345<br>zones=true<br>filterUnnecessaryCommand=true<br>smartSendingEnabledObjectTypes=LIGHTING, RELAY, HVAC_CONTROL, DATE_AND_TIME, SCENARIO, SECURITY, ZONES_BYPASS, CURTAIN<br></span></p><h2 class="c32" id="h.ag8ajs3vkrpu"><span class="c16">Items Configuration</span></h2><h3 class="c36" id="h.fn7qfexef0w"><span class="c20 c25 c28 c29">Description</span></h3><p class="c6"><span class="c0">In order to bind an item to a Cardio II&eacute; system you need to provide configuration settings. The easiest way to do so is to add binding information in your &#39;item file&#39; (in the folder configurations / items`). The syntax for the Cardio2e binding configuration string is explained here:</span></p><ul class="c27 lst-kix_h94fmnawr4i-0 start"><li class="c6 c13"><span class="c3 c25">LIGHTING:</span><span class="c3"><br></span><span class="c7">c2e=&quot;LIGHTING,object_number&quot;<br></span><span class="c3">where &lsquo;object_number&rsquo; is a number between 1 and 160 that represents the light number you want to control. You can bind both &lsquo;Switch&rsquo; and &lsquo;Dimmer&rsquo; items types.<br></span><span class="c1">Reverse mode</span><span class="c3">: Can be enabled adding &lsquo;!&rsquo; symbol before &lsquo;LIGHTING&rsquo; (example: </span><span class="c7">c2e=&quot;!LIGHTING,20&quot;</span><span class="c3">), so the Cardio object will be considered as a control, not an actuator. You can bind in reverse mode an unused lighting Cardio control in order to send commands to openHAB item, and to receive item updates (you can enable a unused Cardio lighting control by assigning it a fake X10 address).<br></span><span class="c1">Dimmer correction</span><span class="c3">: Can be enabled by adding &lsquo;%&rsquo; symbol before &lsquo;object_number&rsquo; (example: </span><span class="c7">c2e=&quot;LIGHTING,%1&quot;</span><span class="c3">), in order to consider Cardio lighting 1% values as 0% (powered off). This correction is necessary when Cardio is programmed to turn on a light by presence, since when power on time expires, Cardio sends a 1% value the DMI instead of 0% power off value (in fact, in practice, any value less than 10% in a DM1 will turn off the light).<br></span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its &lsquo;LIGHTING&rsquo; objects after executing a command, so we recommend that you add &quot;autoupdate=false&quot; in the item settings to make sure that the item&#39;s value always matches Cardio&rsquo;s value (example: </span><span class="c7">Dimmer My_Light {c2e=&quot;LIGHTING,2&quot;, autoupdate=false}</span><span class="c0">). Not applicable when &quot;reverse mode&quot; is used.<br></span></li><li class="c6 c13"><span class="c3 c25">RELAY:</span><span class="c3"><br>Option #1: </span><span class="c7">c2e=&quot;RELAY,object_number&quot;<br></span><span class="c3">where &lsquo;object_number&rsquo; is a number between 1 and 40 that represents the relay number you want to control. You can only bind &lsquo;Switch&rsquo; items type.<br>Option #2: </span><span class="c7">c2e=&quot;RELAY,shutter_up_object_number,shutter_down_object_number&quot;<br></span><span class="c3">where &lsquo;shutter_up_object_number&rsquo; and &lsquo;shutter_down_object_number&rsquo; are numbers between 1 and 40 that represents the relay numbers of a pair of timed relays used to move shutter up and down. You can only bind &lsquo;Rollershutter&rsquo; items type.<br></span><span class="c1">Reverse mode</span><span class="c3">: Can be enabled adding &lsquo;!&rsquo; symbol before &lsquo;RELAY&rsquo; (example: </span><span class="c7">c2e=&quot;!RELAY,4&quot;</span><span class="c3">), so the Cardio object will be considered as a control, not an actuator. You can bind in reverse mode an unused relay Cardio control in order to send commands to openHAB item, and to receive item updates.<br></span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its &lsquo;RELAY&rsquo; objects after executing a command, so we recommend that you add &quot;autoupdate=false&quot; in the item settings to make sure that the item&#39;s value always matches Cardio&rsquo;s value (example: </span><span class="c7">RollerShutter My_Shutter {c2e=&quot;RELAY,5,6&quot;, autoupdate=false}</span><span class="c0">). Not applicable when &quot;reverse mode&quot; is used.<br></span></li><li class="c6 c13"><span class="c3 c25">HVAC_TEMPERATURE:</span><span class="c3"><br></span><span class="c7">c2e=&quot;HVAC_TEMPERATURE,hvac_zone&quot;<br></span><span class="c3">can be bind to a &lsquo;Number&rsquo; item type, where &lsquo;hvac_zone&rsquo; is a value between 1 and 5 that represents the Cardio II&egrave; HVAC zone number you want to receive temperature value states from (the unit of measurement, &ordm;C or &ordm;F, will be the same that was set in Cardio system config).<br>Example: </span><span class="c7">Number My_Temperature {c2e=&quot;HVAC_TEMPERATURE,1&quot;}</span><span class="c0">).<br></span></li><li class="c6 c13"><span class="c3 c25">HVAC_CONTROL:</span><span class="c3"><br></span><span class="c1">One parameter options</span><span class="c3">: </span><span class="c7">c2e=&quot;HVAC_CONTROL,hvac_zone&quot;<br></span><span class="c3">where &lsquo;hvac_zone&rsquo; is a value between 1 and 5 that represents the Cardio II&egrave; HVAC zone number you want to control. You can bind to a &lsquo;Switch&rsquo; item in order to simply switch on / off Cardio II&egrave; HVAC zone (example: </span><span class="c7">Switch My_HVAC_Switch {c2e=&quot;HVAC_CONTROL,1&quot;}</span><span class="c3">). You also can bind to a &lsquo;Number&rsquo; item in order to control HVAC zone with KNX standard internetworking DPT 20.105 HVAC control values (auto=0, heating=1, cooling= 3, off=6), and no KNX compliant values for Cardio 2&egrave; additional submodes (normal=254, economy=255). Example: </span><span class="c7">Number My_HVAC_KNX_Mode {[optional_KNX_binding_config],c2e=&quot;HVAC_CONTROL,1&quot;,autoupdate=false}</span><span class="c3">). <br></span><span class="c1">Two parameters options</span><span class="c3">: </span><span class="c7">c2e=&quot;HVAC_CONTROL,hvac_zone,function&quot;<br></span><span class="c3">where &lsquo;hvac_zone&rsquo; is a value between 1 and 5 that also represents the Cardio II&egrave; HVAC zone number, and &lsquo;function&rsquo; is the HVAC function you want to control: &ldquo;FAN&rdquo; for fan control, &ldquo;AUTO&rdquo;, &ldquo;COOLING&rdquo; and &ldquo;HEATING&rdquo; for HVAC mode switch, and &ldquo;ECONOMY&rdquo; and &ldquo;NORMAL&rdquo; for additional submodes. You can bind to a &lsquo;Switch&rsquo; item in order to switch on / off Cardio II&egrave; HVAC functions, or you can bind to a &lsquo;Number&rsquo; item in order to adjust cooling and heating setpoints (</span><span class="c23">Cardio II&egrave; has two different setpoints, one for cooling and one for heating, and </span><span class="c3">the unit of measurement, &ordm;C or &ordm;F, will be the same that was set in Cardio system config</span><span class="c23">). </span><span class="c3">E</span><span class="c3">xamples:<br></span><span class="c7">Switch My_HVAC_Fan &quot;Fan&quot; {c2e=&quot;HVAC_CONTROL,1,FAN&quot;}<br>Switch My_HVAC_Auto &quot;Auto mode&quot;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{c2e=&quot;HVAC_CONTROL,1,AUTO&quot;}<br>Switch My_HVAC_Heating &quot;Heating mode&quot; {c2e=&quot;HVAC_CONTROL,1,HEATING&quot;}<br>Switch My_HVAC_Cooling &quot;Cooling mode&quot; {c2e=&quot;HVAC_CONTROL,1,COOLING&quot;}<br>Switch My_HVAC_Eco &quot;Economy submode&quot; {c2e=&quot;HVAC_CONTROL,1,ECONOMY&quot;}<br>Switch My_HVAC_Normal &quot;Normal submode&quot; {c2e=&quot;HVAC_CONTROL,1,NORMAL&quot;}<br>Number My_HVAC_Cooling_Setpoint &quot;Cooling setpoint [%.1f &deg;C]&quot; {c2e=&quot;HVAC_CONTROL,1,COOLING&quot;}<br>Number My_HVAC_Heating_Setpoint &quot;Heating setpoint [%.1f &deg;C]&quot; {c2e=&quot;HVAC_CONTROL,1,HEATING&quot;}<br></span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its &lsquo;HVAC_CONTROL&rsquo; objects after executing a command, so we recommend that you add &quot;autoupdate=false&quot; in the item settings to make sure that the item&#39;s value always matches Cardio&rsquo;s value (</span><span class="c7">Switch My_HVAC_Switch {c2e=&quot;HVAC_CONTROL,1&quot;,autoupdate=false}</span><span class="c0">). <br></span></li><li class="c6 c13"><span class="c3 c25">ZONES:</span><span class="c3"><br></span><span class="c7">c2e=&quot;ZONES,zone_number,zone_type&quot;<br></span><span class="c3">can be bind to a &lsquo;Contact&rsquo; or &lsquo;Switch&rsquo; item type, where &lsquo;zone_number&rsquo; is a value between 1 and 16 that represents the Cardio II&egrave; alarm zone number you want to monitor, and &lsquo;zone_type&rsquo; is a value that specifies zone type defined in Cardio system config: &lsquo;OPEN&rsquo; for NO (normally open), &lsquo;CLOSED&rsquo; for NC (normally closed) and &lsquo;NORMAL&rsquo; for EOL (end of line resistor).<br></span><span class="c1">Invert value</span><span class="c3">: Can be enabled adding &lsquo;!&rsquo; symbol before &lsquo;ZONES&rsquo;, so the reported status of the Cardio alarm zone will be inverted.<br>Examples:<br></span><span class="c7">Contact My_Contact {c2e=&quot;ZONES,1,NORMAL&quot;}</span><span class="c3"><br></span><span class="c7">Switch My_Contact {c2e=&quot;ZONES,1,CLOSED&quot;}</span><span class="c3"><br></span><span class="c7">Contact My_Contact {c2e=&quot;!ZONES,2,OPEN&quot;}</span><span class="c3"><br></span><span class="c7">Switch My_Contact {c2e=&quot;!ZONES,2,NORMAL&quot;}</span><span class="c0"><br>Warning: Alarm zones state detection is disabled by default for minimum use of resources. In order to monitorize alarm zones you must set parameter zones=true in binding configuration.<br></span></li><li class="c6 c13"><span class="c3 c25">ZONES_BYPASS:</span><span class="c3"><br></span><span class="c7">c2e=&quot;ZONES_BYPASS,zone_number&quot;<br></span><span class="c3">can be bind to a &lsquo;Switch&rsquo; item type, where &lsquo;zone_number&rsquo; is a value between 1 and 16 that represents the Cardio II&egrave; alarm zone number you want to bypass (example: </span><span class="c7">Switch My_Zone_Bypass {c2e=&quot;ZONES_BYPASS,1&quot;}</span><span class="c3">.<br></span><span class="c1">Invert value</span><span class="c3">: Can be enabled adding &lsquo;!&rsquo; symbol before &lsquo;ZONES_BYPASS&rsquo;, so the operating mode of the Cardio alarm zone bypass will be inverted, so that the item will indicate whether the zone will be armed for Cardio security system or not (example: </span><span class="c7">Switch My_Zone_Armed {c2e=&quot;!ZONES_BYPASS,1&quot;}</span><span class="c3">.<br></span><span class="c1">Note about autoupdate</span><span class="c0">: Cardio will not reports the status of its &lsquo;ZONES_BYPASS&rsquo; objects after executing a command, but it does it after arming security system. So we recommend that you DO NOT add &quot;autoupdate=false&quot; in the item settings.<br></span></li><li class="c6 c13"><span class="c3 c25">DATE_AND_TIME:</span><span class="c3"><br></span><span class="c7">c2e=&quot;DATE_AND_TIME&quot;<br></span><span class="c3">can be bind to a &lsquo;DateTime&rsquo; item type. When a date and time value command is received Cardio&rsquo;s clock will be unconditionally set to item date and time value. </span><span class="c23">However, upon receiving an update, the clock will be updated based on the criteria defined in the binding configuration (see &ldquo;</span><span class="c3">datetimeMaxOffset&rdquo;, &ldquo;firstUpdateWillSetDatetime&rdquo; and &ldquo;allowedDatetimeUpdateHour&rdquo; config parameters)</span><span class="c3">.<br>Because Cardio&rsquo;s clock usually lags behind, you can use NTP binding in order to keep the clock on time, for example, sending updates every 15 minutes.<br>Time sync example with OH1 NTP binding: </span><span class="c7">DateTime My_date_and_time {ntp=&quot;Europe/Madrid:es_ES&quot;, &nbsp;c2e=&quot;DATE_AND_TIME&quot;</span><span class="c3">).<br>Time sync example with OH2 NTP binding: </span><span class="c7">DateTime My_date_and_time {channel=&quot;ntp:ntp:cardiosync:dateTime&quot;, &nbsp;c2e=&quot;DATE_AND_TIME&quot;</span><span class="c0">).<br></span></li><li class="c6 c13"><span class="c3 c25">SCENARIO:</span><span class="c3"><br></span><span class="c7">c2e=&quot;SCENARIO&quot;<br></span><span class="c0">can be bind to &lsquo;Number&rsquo; items type. When a number value command between 0 and 41 is received, the corresponding Cardio scene number between 1 and 42 will be activated (command value + 1).<br>Note that numbers between 0 and 41 are used to provide direct compatibility with the scene numbers range used in protocols such as KNX, where the first scene is encoded with the number 0.<br></span></li><li class="c6 c13"><span class="c3 c25">SECURITY:</span><span class="c3"><br></span><span class="c7">c2e=&quot;SECURITY&quot;<br></span><span class="c3">can be bind to &lsquo;Switch&rsquo; and &lsquo;Number&rsquo; items type.<br>Using a &lsquo;Switch&rsquo; item we can arm / disarm Cardio security system by ON / OFF commands as well as receive state updates (securitycode must be previusly set in config file).<br></span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its &lsquo;SECURITY&rsquo; objects after executing a command, so we recommend that you add &quot;autoupdate=false&quot; in the item settings to make sure that the item&#39;s value always matches Cardio&rsquo;s value (example: </span><span class="c7">Switch Security_Arm {c2e=&quot;SECURITY&quot;, autoupdate=false}</span><span class="c3">). </span><span class="c1">Error code</span><span class="c3">: If security system could not be armed, we can show the reason as numeric error code value update received in a linked &#39;Number&#39; item (example: </span><span class="c7">Number Security_Error {c2e=&quot;SECURITY&quot;, autoupdate=false}</span><span class="c3">).<br>Likewise, we can configure an entry in the sitemap that translates those values by its corresponding description. Sitemap example:<br></span><span class="c7">Text item=Security_Error label=&quot;ERROR # [%d]&quot; visibility=[Security_Error&gt;0] labelcolor=[Security_Error&gt;0=&quot;red&quot;] valuecolor=[Security_Error&gt;0=&quot;red&quot;]<br>Text item=Security_Error label=&quot;(security code is not valid)&quot; icon=&quot;none&quot; visibility=[Security_Error==4] labelcolor=[Security_Error&gt;0=&quot;red&quot;]<br>Text item=Security_Error label=&quot;(there are open zones)&quot; icon=&quot;none&quot; visibility=[Security_Error==16] labelcolor=[Security_Error&gt;0=&quot;red&quot;]<br>Text item=Security_Error label=&quot;(there is a power problem)&quot; icon=&quot;none&quot; visibility=[Security_Error==17] labelcolor=[Security_Error&gt;0=&quot;red&quot;]<br>Text item=Security_Error label=&quot;(unknown reason)&quot; icon=&quot;none&quot; visibility=[Security_Error==18] labelcolor=[Security_Error&gt;0=&quot;red&quot;]</span><span class="c0"><br></span></li><li class="c6 c13"><span class="c3 c25">CURTAIN:</span><span class="c3"><br></span><span class="c7">c2e=&quot;CURTAIN,object_number&quot;<br></span><span class="c3">where &lsquo;object_number&rsquo; is a number between 1 and 80 that represents the shutter number you want to control. You can bind both &lsquo;RollerShutter&rsquo; and &lsquo;Dimmer&rsquo; items types (no STOP or MOVE commands are supported, and 100% value means shutter down). Note that &lsquo;CURTAIN&rsquo; objects are only available in lastest Cardio II&eacute; firmware versions.<br></span><span class="c1">Reverse mode</span><span class="c3">: Can be enabled adding &lsquo;!&rsquo; symbol before &lsquo;CURTAIN&rsquo;&rsquo; (example: </span><span class="c7">c2e=&quot;!CURTAIN,13&quot;</span><span class="c3">), so the Cardio object will be considered as a control, not an actuator. You can bind &nbsp;in reverse mode an unused curtain Cardio control in order to send commands to openHAB item, and to receive item updates.<br></span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its &lsquo;CURTAIN&rsquo; objects after executing a command, so we recommend that you add &quot;autoupdate=false&quot; in the item settings to make sure that the item&#39;s value always matches Cardio&rsquo;s value (example: Dimmer My_Curtain {</span><span class="c7">c2e=&quot;CURTAIN,3&quot;, autoupdate=false}</span><span class="c3">). Not applicable when &quot;reverse mode&quot; is used.<br></span></li></ul><p class="c31 c34"><span class="c20 c28 c30"></span></p></body></html>