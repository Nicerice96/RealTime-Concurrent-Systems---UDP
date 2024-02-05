# RealTime-Concurrent-Systems---UDP
# SYSC 3303 Winter 2024 Assignment 2
## Carleton University
### Department of Systems and Computer Engineering
- SYSC 3303 RealTime Concurrent Systems Winter 2024
## Assignment Overview
This repository contains the implementation of a basic three-part system for SYSC 3303 Winter 2024 Assignment 2. 
The system consists of a Client, an Intermediate Host, and a Server. 
The client sends requests to the intermediate host, which forwards them to the server. The server processes the requests 
and sends responses back through the intermediate host to the client.
## Project Structure
### Client Class:
- The Client class is responsible for generating read and write packets.
- The client creates a DatagramSocket to send and receive packets.
- It alternates between sending read and write requests to the intermediate host.
- Each request is formatted according to the assignment specifications.
- The client prints out the information contained in each packet before sending it to the intermediate host.
- Upon receiving a response from the intermediate host, the client prints out the information received, including the byte array.

### Intermediate Class:

- The Intermediate class acts as an intermediary between the client and the server.
- It creates two DatagramSockets, one for receiving requests from the client (port 23) and one for sending and receiving data.
- The intermediate host continuously waits to receive a request from the client.
- Upon receiving a request, it prints out the information, forms a packet containing the received data, and sends it to the server (port 69).
- It then waits to receive a response from the server, prints out the information, and sends the response back to the client.

### Server Class:

- The Server class creates a DatagramSocket to receive requests from the intermediate host (port 69).
- It continuously waits to receive a request from the intermediate host.
- The server validates the packet format, prints out the information, and sends a response back to the intermediate host.
- If the packet is valid, it sends either 0 3 0 1 for a read request or 0 4 0 0 for a write request.
- The server prints out the information contained in the response packet.
- It then creates a new DatagramSocket for this response and sends the packet back to the intermediate host, closing the socket afterward.
## Running the Project
- Import the project into IntelliJ.
- Ensure that IntelliJ is configured for running multiple programs concurrently.
- Run the Client, Intermediate, and Server in the order: Server, Intermediate, _then_ Client
```cmd

::Navigate to the src directory of the project::

----Terminal One----:
javac Server.java
java Server
//execute first

----Terminal Two----:
javac Intermediate.java
java Intermediate
//execute second

----Terminal Three----:
javac Client.java
java Client
//execute third
```

# UML Diagrams
## Sequence Diagram:
![UML_Sequence_Diagram.drawio.png](UML_Sequence_Diagram.drawio.png)
[UML_Sequence_Diagram.drawio.pdf](UML_Sequence_Diagram.drawio.pdf)

## UML Diagram:

![UML_Class_Diagram.drawio.png](UML_Class_Diagram.drawio.png)

[UML_Class_Diagram.drawio.pdf](UML_Class_Diagram.drawio.pdf)


# MIT License:
Copyright (c) 2023 [Zarif Khan]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.