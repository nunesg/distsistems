# server.py
#!/usr/bin/python                           # This is server.py file

import socket                               # Import socket module

s = socket.socket()                         # Create a socket object
host = socket.gethostname()                 # Get local machine name
port = 12345                                # Reserve a port for your service.
s.bind((host, port))                        # Bind to the port

s.listen(5)                                 # Now wait for client connections.
while True:
    # Establish connection with client.
    c, addr = s.accept()
    print('Got connection from', addr)
    while True:
        str = c.recv(1024)
        print(f"receive: {str.decode()}")
        if str.decode() == 'stop':
            break
        print('send:')
        ans = input()
        c.send(ans.encode())
    # c.send('Thank you for connecting'.encode())
    c.close()                                # Close the connection
