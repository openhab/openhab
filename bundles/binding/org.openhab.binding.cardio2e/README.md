# <span class="c20 c24">Cardio2e Binding</span>

<span class="c0">The openHAB Cardio2e binding allows one to connect to Secant Cardio IIé home automation system installations. Dimming or switching lights on and off, switching devices on and off, activating roller shutters, executing escenarios, managing HVACs or activating system security are only some examples.</span>

<span class="c3">To access your Cardio system you either need an RS-232 interface (like e.g. a Prolific PL-232 based USB to RS-232 converter interface), and a DB-9 to RJ-11 cable suitable for either Cardio PC or Cardio ACC port. You can use this</span> <span class="c37">[schema](https://github.com/openhab/openhab1-addons/files/999699/PcCardio_Cable.pdf&sa=D&ust=1528711214015000)</span><span class="c3"> to build your Cardio PC port cable</span><span class="c33">.</span>

<span class="c33">N</span><span class="c33">ote: You can also use the Cardio accessory port (ACC) instead of PC port, so you must crimp the RJ-11 in reverse order, exchange RD and TD signals from the DB-9 connector with respect to the previous PC schematic</span> <span class="c33">(pins 2 and 3)</span> <span class="c2">and do not connect pin 7.</span>

## <span class="c16">Binding Configuration</span>

<span class="c3">This binding can be configured in the file</span> <span class="c14">configurations/openhab.cfg</span> <span class="c3">(openHAB 1) or  </span><span class="c14">conf/services/cardio2e.cfg</span> <span class="c3">(openHAB 2)</span><span class="c0">.</span>

<a id="t.91c536f4f2c525306d6d2e720fdb7f6e035cf670"></a><a id="t.0"></a>

<table class="c39">

<tbody>

<tr class="c10">

<td class="c17" colspan="1" rowspan="1">

<span class="c3 c25">Property</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c3 c25">Default</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c3 c25">Required</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c3 c25">Description</span>

</td>

</tr>

<tr class="c26">

<td class="c17" colspan="1" rowspan="1">

<span class="c0">port</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0"></span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">Yes</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Serial RS-232 port  
Examples: '/dev/ttyUSB0' for Linux, 'COM1' for Windows.</span>

</td>

</tr>

<tr class="c9">

<td class="c17" colspan="1" rowspan="1">

<span class="c3">programcode</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">00000</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Installer program code for login.</span>

</td>

</tr>

<tr class="c38">

<td class="c17" colspan="1" rowspan="1">

<span class="c3">securitycode</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0"></span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c3">S</span><span class="c3">ecurity code for arm / disarm alarm.</span>

</td>

</tr>

<tr class="c15">

<td class="c17" colspan="1" rowspan="1">

<span class="c0">zones</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">false</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Alarm zones state detection. Enables alarm zones state detection (by default 'false' for minimum use of resources).</span>

</td>

</tr>

<tr class="c10">

<td class="c17" colspan="1" rowspan="1">

<span class="c0">zoneUnchangedMinRefreshDelay</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">600000</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Minimum delay in milliseconds for zone detection refresh when no state changes succeed (by default '600000' milliseconds = 10 minutes).</span>

</td>

</tr>

<tr class="c10">

<td class="c17" colspan="1" rowspan="1">

<span class="c0">datetimeMaxOffset</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">15</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Date and time maximum offset allowed (in minutes) for progressive (step by step, minute by minute) date and time state update.  
Special values: '0' will remove offset limit, '-1' will disable progressive update and will remove offset limit, '-2' will do unconditional update without any filter even if current date and time of Cardio IIé matches the update.</span>

</td>

</tr>

<tr class="c15">

<td class="c17" colspan="1" rowspan="1">

<span class="c3">firstUpdateWillSetDatetime</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">false</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Always will set Cardio IIé clock on first date and time update from last binding start, even if allowedDatetimeUpdateHour was set.</span>

</td>

</tr>

<tr class="c9">

<td class="c17" colspan="1" rowspan="1">

<span class="c3">allowedDatetimeUpdateHour</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">-1</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Allows date and time updates on specified hour only (for example, a safe hour when no events will be triggered by Cardio's schedules).  
Valid values are from '0' to '23' ('-1' disables hour restriction).</span>

</td>

</tr>

<tr class="c9">

<td class="c17" colspan="1" rowspan="1">

<span class="c0">testmode</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">false</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c3">E</span><span class="c3">nables fake port console test mode, for developer debug purposes only.  
Warning: Real communication with Cardio 2é will not work if enabled!</span>

</td>

</tr>

<tr class="c9">

<td class="c17" colspan="1" rowspan="1">

<span class="c0">minDelayBetweenReceivingAndSending</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">200</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Minimum delay in milliseconds between receiving and sending, for expert tuning only.  
By default '200' milliseconds (tested safe value).</span>

</td>

</tr>

<tr class="c15">

<td class="c17" colspan="1" rowspan="1">

<span class="c3">minDelayBetweenSendings</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">300</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Minimum delay in milliseconds between sendings, for expert tuning only.  
By default '300' milliseconds (tested safe value).</span>

</td>

</tr>

<tr class="c10">

<td class="c17" colspan="1" rowspan="1">

<span class="c3">filterUnnecessaryCommand</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">false</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Avoid sending commands when the last value of the object reported by Cardio IIé is the same value as the command value.</span>

</td>

</tr>

<tr class="c10">

<td class="c17" colspan="1" rowspan="1">

<span class="c3">filterUnnecessaryReverseModeUpdate</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0">true</span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Avoid sending updates (reverse mode) when the last value of the object reported by Cardio IIé is the same value as the update value.</span>

</td>

</tr>

<tr class="c5">

<td class="c17" colspan="1" rowspan="1">

<span class="c3">smartSendingEnabledObjectTypes</span>

</td>

<td class="c8" colspan="1" rowspan="1">

<span class="c0"></span>

</td>

<td class="c12" colspan="1" rowspan="1">

<span class="c0">No</span>

</td>

<td class="c21" colspan="1" rowspan="1">

<span class="c0">Comma separated list of smart sending enabled object types. Smart sending enabled means that no contradictory commands will be stored in the sending buffer for that object type, so that if a command directed to a specific object exists in sending buffer and a new command is received for the same object, the command stored will be replaced by the new one.  
Valid values are 'LIGHTING', 'RELAY', 'HVAC_CONTROL', 'DATE_AND_TIME', 'SCENARIO', 'SECURITY', 'ZONES_BYPASS' and 'CURTAIN'.</span>

</td>

</tr>

</tbody>

</table>

### <span class="c20 c29 c25 c28">Example</span>

<span class="c7 c20">port=/dev/ttyUSB0  
programcode=00000  
securitycode=12345  
zones=true  
filterUnnecessaryCommand=true  
smartSendingEnabledObjectTypes=LIGHTING, RELAY, HVAC_CONTROL, DATE_AND_TIME, SCENARIO, SECURITY, ZONES_BYPASS, CURTAIN  
</span>

## <span class="c16">Items Configuration</span>

### <span class="c20 c25 c28 c29">Description</span>

<span class="c0">In order to bind an item to a Cardio IIé system you need to provide configuration settings. The easiest way to do so is to add binding information in your 'item file' (in the folder configurations / items`). The syntax for the Cardio2e binding configuration string is explained here:</span>

*   <span class="c3 c25">LIGHTING:</span><span class="c3">  
    </span><span class="c7">c2e="LIGHTING,object_number"  
    </span><span class="c3">where ‘object_number’ is a number between 1 and 160 that represents the light number you want to control. You can bind both ‘Switch’ and ‘Dimmer’ items types.  
    </span><span class="c1">Reverse mode</span><span class="c3">: Can be enabled adding ‘!’ symbol before ‘LIGHTING’ (example:</span> <span class="c7">c2e="!LIGHTING,20"</span><span class="c3">), so the Cardio object will be considered as a control, not an actuator. You can bind in reverse mode an unused lighting Cardio control in order to send commands to openHAB item, and to receive item updates (you can enable a unused Cardio lighting control by assigning it a fake X10 address).  
    </span><span class="c1">Dimmer correction</span><span class="c3">: Can be enabled by adding ‘%’ symbol before ‘object_number’ (example:</span> <span class="c7">c2e="LIGHTING,%1"</span><span class="c3">), in order to consider Cardio lighting 1% values as 0% (powered off). This correction is necessary when Cardio is programmed to turn on a light by presence, since when power on time expires, Cardio sends a 1% value the DMI instead of 0% power off value (in fact, in practice, any value less than 10% in a DM1 will turn off the light).  
    </span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its ‘LIGHTING’ objects after executing a command, so we recommend that you add "autoupdate=false" in the item settings to make sure that the item's value always matches Cardio’s value (example:</span> <span class="c7">Dimmer My_Light {c2e="LIGHTING,2", autoupdate=false}</span><span class="c0">). Not applicable when "reverse mode" is used.  
    </span>
*   <span class="c3 c25">RELAY:</span><span class="c3">Option #1:</span> <span class="c7">c2e="RELAY,object_number"  
    </span><span class="c3">where ‘object_number’ is a number between 1 and 40 that represents the relay number you want to control. You can only bind ‘Switch’ items type.  
    Option #2:</span> <span class="c7">c2e="RELAY,shutter_up_object_number,shutter_down_object_number"  
    </span><span class="c3">where ‘shutter_up_object_number’ and ‘shutter_down_object_number’ are numbers between 1 and 40 that represents the relay numbers of a pair of timed relays used to move shutter up and down. You can only bind ‘Rollershutter’ items type.  
    </span><span class="c1">Reverse mode</span><span class="c3">: Can be enabled adding ‘!’ symbol before ‘RELAY’ (example:</span> <span class="c7">c2e="!RELAY,4"</span><span class="c3">), so the Cardio object will be considered as a control, not an actuator. You can bind in reverse mode an unused relay Cardio control in order to send commands to openHAB item, and to receive item updates.  
    </span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its ‘RELAY’ objects after executing a command, so we recommend that you add "autoupdate=false" in the item settings to make sure that the item's value always matches Cardio’s value (example:</span> <span class="c7">RollerShutter My_Shutter {c2e="RELAY,5,6", autoupdate=false}</span><span class="c0">). Not applicable when "reverse mode" is used.  
    </span>
*   <span class="c3 c25">HVAC_TEMPERATURE:</span><span class="c3">  
    </span><span class="c7">c2e="HVAC_TEMPERATURE,hvac_zone"  
    </span><span class="c3">can be bind to a ‘Number’ item type, where ‘hvac_zone’ is a value between 1 and 5 that represents the Cardio IIè HVAC zone number you want to receive temperature value states from (the unit of measurement, ºC or ºF, will be the same that was set in Cardio system config).  
    Example:</span> <span class="c7">Number My_Temperature {c2e="HVAC_TEMPERATURE,1"}</span><span class="c0">).  
    </span>
*   <span class="c3 c25">HVAC_CONTROL:</span><span class="c3">  
    </span><span class="c1">One parameter options</span><span class="c3">:</span> <span class="c7">c2e="HVAC_CONTROL,hvac_zone"  
    </span><span class="c3">where ‘hvac_zone’ is a value between 1 and 5 that represents the Cardio IIè HVAC zone number you want to control. You can bind to a ‘Switch’ item in order to simply switch on / off Cardio IIè HVAC zone (example:</span> <span class="c7">Switch My_HVAC_Switch {c2e="HVAC_CONTROL,1"}</span><span class="c3">). You also can bind to a ‘Number’ item in order to control HVAC zone with KNX standard internetworking DPT 20.105 HVAC control values (auto=0, heating=1, cooling= 3, off=6), and no KNX compliant values for Cardio 2è additional submodes (normal=254, economy=255). Example:</span> <span class="c7">Number My_HVAC_KNX_Mode {[optional_KNX_binding_config],c2e="HVAC_CONTROL,1",autoupdate=false}</span><span class="c3">).  
    </span><span class="c1">Two parameters options</span><span class="c3">:</span> <span class="c7">c2e="HVAC_CONTROL,hvac_zone,function"  
    </span><span class="c3">where ‘hvac_zone’ is a value between 1 and 5 that also represents the Cardio IIè HVAC zone number, and ‘function’ is the HVAC function you want to control: “FAN” for fan control, “AUTO”, “COOLING” and “HEATING” for HVAC mode switch, and “ECONOMY” and “NORMAL” for additional submodes. You can bind to a ‘Switch’ item in order to switch on / off Cardio IIè HVAC functions, or you can bind to a ‘Number’ item in order to adjust cooling and heating setpoints (</span><span class="c23">Cardio IIè has two different setpoints, one for cooling and one for heating, and</span> <span class="c3">the unit of measurement, ºC or ºF, will be the same that was set in Cardio system config</span><span class="c23">).</span> <span class="c3">E</span><span class="c3">xamples:  
    </span><span class="c7">Switch My_HVAC_Fan "Fan" {c2e="HVAC_CONTROL,1,FAN"}  
    Switch My_HVAC_Auto "Auto mode"        {c2e="HVAC_CONTROL,1,AUTO"}  
    Switch My_HVAC_Heating "Heating mode" {c2e="HVAC_CONTROL,1,HEATING"}  
    Switch My_HVAC_Cooling "Cooling mode" {c2e="HVAC_CONTROL,1,COOLING"}  
    Switch My_HVAC_Eco "Economy submode" {c2e="HVAC_CONTROL,1,ECONOMY"}  
    Switch My_HVAC_Normal "Normal submode" {c2e="HVAC_CONTROL,1,NORMAL"}  
    Number My_HVAC_Cooling_Setpoint "Cooling setpoint [%.1f °C]" {c2e="HVAC_CONTROL,1,COOLING"}  
    Number My_HVAC_Heating_Setpoint "Heating setpoint [%.1f °C]" {c2e="HVAC_CONTROL,1,HEATING"}  
    </span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its ‘HVAC_CONTROL’ objects after executing a command, so we recommend that you add "autoupdate=false" in the item settings to make sure that the item's value always matches Cardio’s value (</span><span class="c7">Switch My_HVAC_Switch {c2e="HVAC_CONTROL,1",autoupdate=false}</span><span class="c0">).  
    </span>
*   <span class="c3 c25">ZONES:</span><span class="c3">  
    </span><span class="c7">c2e="ZONES,zone_number,zone_type"  
    </span><span class="c3">can be bind to a ‘Contact’ or ‘Switch’ item type, where ‘zone_number’ is a value between 1 and 16 that represents the Cardio IIè alarm zone number you want to monitor, and ‘zone_type’ is a value that specifies zone type defined in Cardio system config: ‘OPEN’ for NO (normally open), ‘CLOSED’ for NC (normally closed) and ‘NORMAL’ for EOL (end of line resistor).  
    </span><span class="c1">Invert value</span><span class="c3">: Can be enabled adding ‘!’ symbol before ‘ZONES’, so the reported status of the Cardio alarm zone will be inverted.  
    Examples:  
    </span><span class="c7">Contact My_Contact {c2e="ZONES,1,NORMAL"}</span><span class="c3">  
    </span><span class="c7">Switch My_Contact {c2e="ZONES,1,CLOSED"}</span><span class="c3">  
    </span><span class="c7">Contact My_Contact {c2e="!ZONES,2,OPEN"}</span><span class="c3">  
    </span><span class="c7">Switch My_Contact {c2e="!ZONES,2,NORMAL"}</span><span class="c0">  
    Warning: Alarm zones state detection is disabled by default for minimum use of resources. In order to monitorize alarm zones you must set parameter zones=true in binding configuration.  
    </span>
*   <span class="c3 c25">ZONES_BYPASS:</span><span class="c3">  
    </span><span class="c7">c2e="ZONES_BYPASS,zone_number"  
    </span><span class="c3">can be bind to a ‘Switch’ item type, where ‘zone_number’ is a value between 1 and 16 that represents the Cardio IIè alarm zone number you want to bypass (example:</span> <span class="c7">Switch My_Zone_Bypass {c2e="ZONES_BYPASS,1"}</span><span class="c3">.  
    </span><span class="c1">Invert value</span><span class="c3">: Can be enabled adding ‘!’ symbol before ‘ZONES_BYPASS’, so the operating mode of the Cardio alarm zone bypass will be inverted, so that the item will indicate whether the zone will be armed for Cardio security system or not (example:</span> <span class="c7">Switch My_Zone_Armed {c2e="!ZONES_BYPASS,1"}</span><span class="c3">.  
    </span><span class="c1">Note about autoupdate</span><span class="c0">: Cardio will not reports the status of its ‘ZONES_BYPASS’ objects after executing a command, but it does it after arming security system. So we recommend that you DO NOT add "autoupdate=false" in the item settings.  
    </span>
*   <span class="c3 c25">DATE_AND_TIME:</span><span class="c3">  
    </span><span class="c7">c2e="DATE_AND_TIME"  
    </span><span class="c3">can be bind to a ‘DateTime’ item type. When a date and time value command is received Cardio’s clock will be unconditionally set to item date and time value.</span> <span class="c23">However, upon receiving an update, the clock will be updated based on the criteria defined in the binding configuration (see “</span><span class="c3">datetimeMaxOffset”, “firstUpdateWillSetDatetime” and “allowedDatetimeUpdateHour” config parameters)</span><span class="c3">.  
    Because Cardio’s clock usually lags behind, you can use NTP binding in order to keep the clock on time, for example, sending updates every 15 minutes.  
    Time sync example with OH1 NTP binding:</span> <span class="c7">DateTime My_date_and_time {ntp="Europe/Madrid:es_ES",  c2e="DATE_AND_TIME"</span><span class="c3">).  
    Time sync example with OH2 NTP binding:</span> <span class="c7">DateTime My_date_and_time {channel="ntp:ntp:cardiosync:dateTime",  c2e="DATE_AND_TIME"</span><span class="c0">).  
    </span>
*   <span class="c3 c25">SCENARIO:</span><span class="c3">  
    </span><span class="c7">c2e="SCENARIO"  
    </span><span class="c0">can be bind to ‘Number’ items type. When a number value command between 0 and 41 is received, the corresponding Cardio scene number between 1 and 42 will be activated (command value + 1).  
    Note that numbers between 0 and 41 are used to provide direct compatibility with the scene numbers range used in protocols such as KNX, where the first scene is encoded with the number 0.  
    </span>
*   <span class="c3 c25">SECURITY:</span><span class="c3">  
    </span><span class="c7">c2e="SECURITY"  
    </span><span class="c3">can be bind to ‘Switch’ and ‘Number’ items type.  
    Using a ‘Switch’ item we can arm / disarm Cardio security system by ON / OFF commands as well as receive state updates (securitycode must be previusly set in config file).  
    </span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its ‘SECURITY’ objects after executing a command, so we recommend that you add "autoupdate=false" in the item settings to make sure that the item's value always matches Cardio’s value (example:</span> <span class="c7">Switch Security_Arm {c2e="SECURITY", autoupdate=false}</span><span class="c3">).</span> <span class="c1">Error code</span><span class="c3">: If security system could not be armed, we can show the reason as numeric error code value update received in a linked 'Number' item (example:</span> <span class="c7">Number Security_Error {c2e="SECURITY", autoupdate=false}</span><span class="c3">).  
    Likewise, we can configure an entry in the sitemap that translates those values by its corresponding description. Sitemap example:  
    </span><span class="c7">Text item=Security_Error label="ERROR # [%d]" visibility=[Security_Error>0] labelcolor=[Security_Error>0="red"] valuecolor=[Security_Error>0="red"]  
    Text item=Security_Error label="(security code is not valid)" icon="none" visibility=[Security_Error==4] labelcolor=[Security_Error>0="red"]  
    Text item=Security_Error label="(there are open zones)" icon="none" visibility=[Security_Error==16] labelcolor=[Security_Error>0="red"]  
    Text item=Security_Error label="(there is a power problem)" icon="none" visibility=[Security_Error==17] labelcolor=[Security_Error>0="red"]  
    Text item=Security_Error label="(unknown reason)" icon="none" visibility=[Security_Error==18] labelcolor=[Security_Error>0="red"]</span><span class="c0">  
    </span>
*   <span class="c3 c25">CURTAIN:</span><span class="c3">  
    </span><span class="c7">c2e="CURTAIN,object_number"  
    </span><span class="c3">where ‘object_number’ is a number between 1 and 80 that represents the shutter number you want to control. You can bind both ‘RollerShutter’ and ‘Dimmer’ items types (no STOP or MOVE commands are supported, and 100% value means shutter down). Note that ‘CURTAIN’ objects are only available in lastest Cardio IIé firmware versions.  
    </span><span class="c1">Reverse mode</span><span class="c3">: Can be enabled adding ‘!’ symbol before ‘CURTAIN’’ (example:</span> <span class="c7">c2e="!CURTAIN,13"</span><span class="c3">), so the Cardio object will be considered as a control, not an actuator. You can bind  in reverse mode an unused curtain Cardio control in order to send commands to openHAB item, and to receive item updates.  
    </span><span class="c1">Autoupdate</span><span class="c3">: Cardio always reports the status of its ‘CURTAIN’ objects after executing a command, so we recommend that you add "autoupdate=false" in the item settings to make sure that the item's value always matches Cardio’s value (example: Dimmer My_Light {</span><span class="c7">c2e="CURTAIN,3", autoupdate=false}</span><span class="c3">). Not applicable when "reverse mode" is used.  
    </span>

<span class="c20 c28 c30"></span>
