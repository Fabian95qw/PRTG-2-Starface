# PRTG-2-Starface

## Installation PRTG-Monitor-Server:

* Place the Starface.bat and the PRTGClient.jar inside of the "Custom Sensors\EXEXML" (Defaulf path: "C:\Program Files (x86)\PRTG Network Monitor\Custom Sensors\EXEXML")

* Requires Java to work

The parameters for the .bat are as follows: %host [PORT] [PASSWORT] [SENSORNAME] [TRUSTALLCA] [DEBUG]

* %host ==> Is a Param of the prtg-monitor. It's the IP/DNS entry of the host, of this sensor.
* PORT ==> The listener port on the starface pbx
* SENSORNAME ==> The name of the Sensor given in the Module-Add On, and Sensor Configuration on the PRTG-Monitor Server. You can merge multiple Add-Ons/Channels into one Sensor by using the same Sensorname.
* TRUSTALLCA ==> a Boolean to Trust all CA's, is required if the pbx doesn't have a valid certificate
* DEBUG ==> a Boolean, which will output everything in the console. WARNING THIS WILL CAUSE A MALFORMED XML EXCEPTION IN THE PRTG MONITOR! DEBUG USE ONLY

## Installation Starface:

* Import the Module trough the Admin GUI ==> Module ==> Module library ==> Import module
* Configure the port, and the password for the client access, and the refresh timer
* Open the given port via ssh, or direct access:

* iptables -I INPUT 3 -p tcp -m tcp --dport [PORT] -j ACCEPT
* service iptables save

* Install Add-On Modules, or create your own. (For examples see: https://github.com/Fabian95qw/PRTG-2-Starface/tree/master/bin/server)
