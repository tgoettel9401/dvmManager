# dvmManager
 
This repository provides a small application for managing the DVM tournament of DSJ online. It is meant to import
SWT-Files (not implemented so far) and create LiChess-Challenges based on the tournament paired in SWT. This is
necessary because LiChess so far does not provide a functionality for pairing Team-Swiss-Tournaments by default. 

For providing local testing purposes without SWT-Files the Spring-Profile db-init is used. Is requires a file 
testUser.properties in the classpath of the application (folder resources). For details please see Class 
TestUserProperties in initialization-package. 

Application can be forked and used from anybody without cost. Please mind that this repository was created for the 
single purpose of enabling the online-tournaments of DSJ and not for every case you may run into.   
