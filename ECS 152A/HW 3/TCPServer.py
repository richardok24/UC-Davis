# simple (non-concurrent) TCP server example
from socket import *
serverPort = 12000
listeningSocket = socket(AF_INET, SOCK_STREAM)
listeningSocket.bind(('localhost', serverPort))
listeningSocket.listen(0)
print('Server ready, socket', listeningSocket.fileno(), 'listening on localhost :', serverPort)
try:
    while True:
        connectionSocket, client_addr = listeningSocket.accept()
        data = connectionSocket.recv(1024)
        while data:
            print("Data received from client: {}".format(client_addr))
            print("Data: {}".format(data.decode('ascii')))
            print("Sending the response back to the client")
            connectionSocket.sendall(data)
            data = connectionSocket.recv(1024)
        print("Client ({}) connection has been terminated. Waiting for another client!!".format(client_addr))
        print('Closing socket', connectionSocket.fileno())
        connectionSocket.close()
finally:
    connectionSocket.close()
