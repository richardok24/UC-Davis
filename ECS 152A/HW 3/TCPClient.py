# client
from socket import *
import os
serverName = 'localhost'
serverPort = 12000
clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect((serverName, serverPort))
print('Socket', clientSocket.fileno(), 'opened to server', serverName, ':', serverPort)
data = input("Enter the data to be sent (Type exit to exit)\n")
try:
    while data != "exit":
        clientSocket.sendall(data.encode('ascii'))
        print("Data has been sent to the server. Waiting for reply!!")
        print("Server reply: {}".format(clientSocket.recv(1024).decode('ascii')))
        data = input("Enter the data to be sent (Type exit to exit)\n")
finally:
    clientSocket.close()
