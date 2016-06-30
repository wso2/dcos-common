#!/usr/bin/env python
import socket;
import sys;


def main():
    host = sys.argv[1]
    port = sys.argv[2]
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        result = s.connect_ex((host, int(port)))
        print result
    except:
        print -1
        s.close()
        sys.exit(1)
    s.close()
    sys.exit(0)


if __name__ == '__main__':
    main()
