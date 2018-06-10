from socket import *
from time import *
from datetime import *

serverName = 'localhost'
serverPort = 12000

daysOfWeek = {"Mon": 'M', "Tue": 'T', "Wed": 'W', "Thu": 'R', "Fri": 'F', "Sat": 'S', "Sun": 'U'}

clientSocket = socket(AF_INET, SOCK_DGRAM)
clientSocket.settimeout(1)

for i in range(1, 11):
    cTos_message = 'Ping ' + str(i) + ' ' + datetime.utcnow().strftime("%Y-%m-%d ") + daysOfWeek[datetime.utcnow().strftime("%a")] +  datetime.utcnow().strftime(" %H:%M ") + 'UTC'
    print(cTos_message)
    clientSocket.sendto(str.encode(cTos_message),(serverName, serverPort))
    start_time = clock()
    
    try:
        returnedMessage, serverAddress = clientSocket.recvfrom(2048)
        print (bytes.decode(returnedMessage), "\nRTT:", round(((clock() - start_time)*1000),3), "\n")
    except timeout:
        print("Request timed out\n")

clientSocket.close()