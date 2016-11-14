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
### Pre requisites 
* Install ant to build openfire server and gradle to build smack client.
* Install eclipse and import smack-core, smack-java7, smack-resolver-javax, smack-sasl-javax and smack-tcp projects.
* Add smack-java7 as build dependency in smack-tcp.
* Download org.apache.commons.math3 jar and add it as external jar to resolve errors
* From terminal go inside each folder in the above sequence and run $ gradle eclipse build

### Openfire build and start server
Inside folder openfire_src/build run following command 

$ ant

Now after successful build go to folder openfire_src/target/openfire/bin and run

$ ./openfire.sh

Follow the steps in http://www.2daygeek.com/install-openfire-instant-messaging-server-on-ubuntu-centos-debian-fedora-mint-rhel-opensuse/# to "Configure openfile server" on the listening address displayed on terminal.

### Running Smack Client
Inside Smack-master/smack-tcp/src/main/java/org/jivesoftware/smack/tcp/client run XMPPOpenfireConnection.java


