# XMPP_Parking
## Consists of Openfire Server and Smack Clients.

The openfire server stores the map for parking lot and has the information of all the empty and occupied parking positions in the parking area. Parking Model consists of sensor attached to every parking slot. These sensors run Smack-tcp client to send their status to the server. Also every new car arriving runs a smack client as well and makes a request to openfire server to get and reserve a empty parking slot. All the connections and request goes over TCP with TLS authentication using XMPP Protocol. Multiple client tries to connect to server in separate threads over same socket. The service time of each client is noted to study the queuing delay r waiting time for customers to get their request served.Traffic coming to the server follows the poisson arrival.

## Code Changes:-
### Server side handling:-
* ClientStanzaHandler.java 
* AdminConsolePlugin.java

File Location:- 
* openfire_src/src/java/org/jivesoftware/openfire/net/ClientStanzaHandler.java 
* openfire_src/src/java/org/jivesoftware/openfire/container/AdminConsolePlugin


### Client side changes:- 
Created new folder client in smack-tcp. Clients thread are running and service time for each client is noted to evaluate prformance. Clients are arriving according to Poisson Distribution.

Folder Location:- smack-tcp/src/main/java/org/jivesoftware/smack/tcp/client
Files added:- 
* XMPPOpenfireConnection.java
* abstractClientWrapper.java
* clientsThread.java
* putClient.java
* getClient.java
* deleteClient.java


## Installation and run:-
### Openfire build and start server
Inside folder openfire_src/build run following command 

$ ant

Now after successful build go to folder openfire_src/target/openfire/bin and run

$ ./openfire.sh

### Running Smack Client
Inside Smack-master/smack-tcp/src/main/java/org/jivesoftware/smack/tcp/client run XMPPOpenfireConnection.java


