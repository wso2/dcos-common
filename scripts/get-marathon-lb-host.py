#!/usr/bin/python
import sys, json;

def main():
    data = json.load(sys.stdin)
    if (data is None or 'tasks' not in data):
        print "Error! Marathon application for marathon-lb is not found"
        sys.exit(1)

    if (len(data['tasks']) == 0):
        print "Error! No tasks found for marathon-lb"
        sys.exit(1)

    if ('host' not in data['tasks'][0]):
        print "Host is not found for marathon-lb task"
        sys.exit(1)

    print data['tasks'][0]['host']
    sys.exit(0)

if __name__ == "__main__":
    main()
