# openhab-ulux-binding

This is a work-in-progress openHAB binding for u::Lux switches.

## Building the binding

You have to symlink both subdirectories into the `bundles/binding` directory of the main openHAB source tree
and add them to the `modules` section in `bundles/binding/pom.xml`.

## Configuring the binding

Insert into your `openhab.cfg`

	# needed for every switch, mapping from switch id to IP address
	#ulux:switch.x=x.x.x.x
	ulux:switch.61=192.168.1.61

	# not used at the moment
	#ulux:designId=1

	# not used at the moment
	#ulux:projectId=1

	# should not be needed
	#ulux:bind_address=10.0.0.27

## Configuring your items

The configuration string consists of switch id, actor id and message name (default is EditValue).

### Switching an actor on and off

Send ON or OFF to a switch item with message type "EditValue" (which is the default).

`Switch Ulux_Switch  "Switch"     <none> { ulux="1:2:EditValue" }`

or simply

`Switch Ulux_Switch  "Switch"     <none> { ulux="1:2" }`

### Playing an audio file stored on the switch

Send the index of the audio file (starting at 1) to a number item with message type "AudioPlayLocal". 

`Number Ulux_Audio   "Audio"      <none> { ulux="1:0:AudioPlayLocal"}`

### Switching the display on and off

Send ON or OFF to a switch item with the message type "Activate".

`Switch Ulux_Display "Display"    <none> { ulux="1:0:Activate" }`

### The page displayed on the switch

`Number Ulux_Page    "Page [%d]"  <none> { ulux="1:0:PageIndex" }`

### Displaying a decimal value, e.g. temperature

`Number Ulux_Value   "Value [%d]" <none> { ulux="1:1:EditValue" }`

