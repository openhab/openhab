# Fritzbox Binding (using TR064 protocol)

This is a binding for communication with AVM Fritz!Box using SOAP requests (TR064 protocol).

It has been tested on:

* 7270
* 7330SL (v6.54)
* 7360SL (v6.30)
* 7390
* 6360 Cable (v6.04)
* 6490 Cable (v7.02)
* 7490
* 7580 (v7.12)
* 7590 (v6.92)

## Features

* Detect if MAC is online in network (presence detection)
* Switching on/off 2,4Hz Wi-Fi, 5GHz Wi-Fi and Guest Wi-Fi (if any)
* Getting external IP address of fbox
* Getting fbox model name
* Call monitor
 * Switch Item: Receives "ON" state when call is incoming
 * Call Items: Shows external and internal number for incoming/outgoing calls
 * Resolve external call number to phonebook name
* Enabling/disabling telephone answering machines (TAMs)
* Enabling/disabling of call deflection
* Getting new messages per TAM
* Getting missed calls for the last x days
* Getting DSL/WAN statistics for monitoring connection quality
* Reboot

## Prerequisites

* Enable TR064: In the webui goto "Heimnetz" - "Netzwerkeinstellungen": enable option "Zugriff für Anwendungen zulassen" (enabled by default)
* Only if you want to use the call monitor feature (items starting with callmonitor_...), enable the interface by dialing `#96*5*`. You may disable it again by dialing `#96*4*`.

## Binding Configuration

This binding can be configured in the file `services/fritzboxtr064.cfg`.

| Property | Default | Required | Description |
|-------------|---------|:--------:|-------------|
| url         |         |   Yes    | URL. Either use `http://<fbox-ip>:49000` or `https://<fbox-ip>:49443` (https preferred!) |
| refresh     | 60000   |   No     | Refresh interval in milliseconds |
| user        | dslf-config |  No  | User Name.  Only use this value if you configured a user in fbox webui/config!  It is recommended to switch to authentication by username in fritzbox config and add a separate config user for this binding. |
| pass        |         |   Yes    | Password |
| phonebookid | 0       |   No     | PhoneBook ID if you use a different phonebook (like a Google-synced phonebook). |


## Item Configuration

```
String  fboxName            "FBox Model [%s]"           {fritzboxtr064="modelName"}
String  fboxManufacturer    "FBox Manufacturer [%s]"    {fritzboxtr064="manufacturerName"}
String  fboxSerial          "FBox Serial [%s]"          {fritzboxtr064="serialNumber"}
String  fboxVersion         "FBox Version [%s]"         {fritzboxtr064="softwareVersion"}
Number  fboxUptime          "FBox Uptime [%d s]"        {fritzboxtr064="upTime"}
// get wan ip if FritzBox establishes the internet connection (e. g. via DSL)
String  fboxWanIP           "FBox WAN IP [%s]"          {fritzboxtr064="wanip"}
// get wan ip if FritzBox uses internet connection of external router
String  fboxWanIPExternal   "FBox external WAN IP [%s]" {fritzboxtr064="externalWanip"}
Switch  fboxWifi24          "2,4GHz Wi-Fi"               {fritzboxtr064="wifi24Switch"}
Switch  fboxWifi50          "5,0GHz Wi-Fi"               {fritzboxtr064="wifi50Switch"}
Switch  fboxGuestWifi       "Guest Wi-Fi"                {fritzboxtr064="wifiGuestSwitch"}
Contact cFboxMacOnline      "Presence (Wi-Fi) [%s]"      {fritzboxtr064="maconline:11-11-11-11-11-11" }
Switch  fboxReboot          "Reboot"                    {fritzboxtr064="reboot"}

// WAN statistics

String  fboxWanAccessType "FBox WAN access type [%s]" {fritzboxtr064="wanWANAccessType"}
Number  fboxWanLayer1UpstreamMaxBitRate "FBox WAN us max bit rate [%s]" {fritzboxtr064="wanLayer1UpstreamMaxBitRate"}
Number  fboxWanLayer1DownstreamMaxBitRate "FBox WAN ds max bit rate [%s]" {fritzboxtr064="wanLayer1DownstreamMaxBitRate"}
String  fboxWanPhysicalLinkStatus "FBox WAN physical link status [%s]" {fritzboxtr064="wanPhysicalLinkStatus"}
Number  fboxWanTotalBytesSent "WAN total bytes sent [%s]" {fritzboxtr064="wanTotalBytesSent"}
Number  fboxWanTotalBytesReceived "WAN total bytes received [%s]" {fritzboxtr064="wanTotalBytesReceived"}

// DSL statistics

Contact fboxDslEnable       "FBox DSL Enable [%s]"      {fritzboxtr064="dslEnable"}
String  fboxDslStatus       "FBox DSL Status [%s]"      {fritzboxtr064="dslStatus"}
Number  fboxDslUpstreamCurrRate "DSL Upstream Current [%s mbit/s]" {fritzboxtr064="dslUpstreamCurrRate"}
Number  fboxDslDownstreamCurrRate "DSL Downstream Current [%s mbit/s]" {fritzboxtr064="dslDownstreamCurrRate"}
Number  fboxDslUpstreamMaxRate "DSL Upstream Max [%s mbit/s]" {fritzboxtr064="dslUpstreamMaxRate"}
Number  fboxDslDownstreamMaxRate "DSL Downstream Max [%s mbit/s]" {fritzboxtr064="dslDownstreamMaxRate"}
Number  fboxDslUpstreamNoiseMargin "DSL Upstream Noise Margin [%s dB*10]" {fritzboxtr064="dslUpstreamNoiseMargin"}
Number  fboxDslDownstreamNoiseMargin "DSL Downstream Noise Margin [%s dB*10]" {fritzboxtr064="dslDownstreamNoiseMargin"}
Number  fboxDslUpstreamAttenuation "DSL Upstream Attenuation [%s dB*10]" {fritzboxtr064="dslUpstreamAttenuation"}
Number  fboxDslDownstreamAttenuation "DSL Downstream Attenuation [%s dB*10]" {fritzboxtr064="dslDownstreamAttenuation"}
Number  fboxDslFECErrors "DSL FEC Errors [%s]" {fritzboxtr064="dslFECErrors"}
Number  fboxDslHECErrors "DSL HEC Errors [%s]" {fritzboxtr064="dslHECErrors"}
Number  fboxDslCRCErrors "DSL CRC Errors [%s]" {fritzboxtr064="dslCRCErrors"}

// only when using call monitor
Switch  fboxRinging         "Phone ringing [%s]"                {fritzboxtr064="callmonitor_ringing" }
Switch  fboxRinging_Out     "Phone ringing [%s]"                {fritzboxtr064="callmonitor_outgoing" }
Switch  fboxCallConnecting  "Call established [%s]"             {fritzboxtr064="callmonitor_active" }
Call    fboxIncomingCall    "Incoming call: [%1$s to %2$s]"     {fritzboxtr064="callmonitor_ringing" } 
Call    fboxOutgoingCall    "Outgoing call: [%1$s to %2$s]"     {fritzboxtr064="callmonitor_outgoing" }
Call    fboxConnectedCall   "Call established [%1$s to %2$s]"   {fritzboxtr064="callmonitor_active" }

// resolve numbers to names based on phonebook
Call    fboxIncomingCallResolved    "Incoming call: [%1$s to %2$s]"     {fritzboxtr064="callmonitor_ringing:resolveName" } 

// Telephone answering machine (TAM) items
// Number after tamSwitch is ID of configured TAM, start with 0
Switch  fboxTAM0Switch   "Answering machine ID 0"       {fritzboxtr064="tamSwitch:0"}
Number  fboxTAM0NewMsg   "New Messages TAM 0 [%s]"      {fritzboxtr064="tamNewMessages:0"}

// Missed calls: specify the number of last days which should be searched for missed calls
Number  fboxMissedCalls  "Missed Calls [%s]"            {fritzboxtr064="missedCallsInDays:5"}

// Call deflection items
// Number after callDeflectionSwitch is ID of configured call deflection
// The ID count includes the entries from the "Call Blocks" page.
// If you have no "Call Blocks", the first entry on the "Call Diversions" page has ID 0.
// If you have 3 "Call Blocks", the first entry on the "Call Diversions" page has ID 3.
Switch  fboxCD0Switch    "Call Deflection ID 0"         {fritzboxtr064="callDeflectionSwitch:0"}
```

## Examples and Hints

### Sitemap

For the `Call` items use `Text` elements in your sitemap.

### Map for Presence Detection

Use a map for presence detection item:

Create file `transform/presence.map` and add:

```
OPEN=present
CLOSED=not present
NULL=unknown
```

Now, as item configuration use (the addon "Map Transformation" must be installed):

```
Contact cFboxMacOnline      "Presence (Wifi) [MAP(presence.map):%s]"    <presence>       {fritzboxtr064="maconline:11-22-33-44-55-66"}
```

### Rules

If you need the caller name (resolved from the fritzbox phonebook) in a rule, extract it like this:

```
rule "Phone is ringing"
when
    // fboxRinging is a switch item which switches to ON if call is detected
    Item fboxRinging changed from OFF to ON 
then
    logInfo("Call recognition", "Generating caller name message...")
    
    val incCall = fboxIncomingCall.state as StringListType
    val callerNumber = incCall.getValue(1)
    val incCallResolved = fboxIncomingCallResolved.state as StringListType
    val callerName = incCallResolved.getValue(1)

    // do something with callerName
end

rule "Reboot Fritzbox on Connectionloss"
when
    // to be determined externally
    Item internetConnection changed from ON to OFF
then
    sendCommand("fboxReboot", ON)
end
```
