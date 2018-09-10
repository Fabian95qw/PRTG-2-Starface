# PRTG-2-Starface

## Installation PRTG-Monitor-Server:

* Place the Starface.bat and the PRTGClient.jar inside of the "Custom Sensors\EXEXML" (Defaulf path: "C:\Program Files (x86)\PRTG Network Monitor\Custom Sensors\EXEXML")

* Requires Java to work

The parameters for the .bat are as follows: %host [PORT] [PASSWORT] [TRUSTALLCA] [LOGLOCATION]

* %host ==> Is a Param of the prtg-monitor. It's the IP/DNS entry of the host, of this sensor.
* PORT ==> The listener port on the starface pbx
* TRUSTALLCA ==> a Boolean to Trust all CA's, is required if the pbx doesn't have a valid certificate
* LOGLOCATION ==> The loglocation of the simply written logger. can be null

## Installation Starface:

* Import the Module trough the Admin GUI ==> Module ==> Module library ==> Import module
* Configure the port, and the password for the client access, and the refresh timer
* Open the given port via ssh, or direct access:

* iptables -I INPUT 3 -p tcp -m tcp --dport [PORT] -j ACCEPT
* service iptables save

* Install Add-On Modules, or create your own. (For examples see: https://github.com/Fabian95qw/PRTG-2-Starface/tree/master/bin/server)
