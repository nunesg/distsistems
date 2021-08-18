# client.py

#!/usr/bin/python                      # This is client.py file

import socket                          # Import socket module

s = socket.socket()                    # Create a socket object
host = socket.gethostname()            # Get local machine name
port = 12345                           # Reserve a port for your service.
s.connect((host, port))

while True:
    print('send:')
    str = input()
    s.send(str.encode())
    if str == 'stop':
        break
    answer = s.recv(1024)
    print(f"receive: {answer.decode()}")
s.close()
