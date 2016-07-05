#!/usr/bin/env python
# ------------------------------------------------------------------------
#
# Copyright 2016 WSO2, Inc. (http://wso2.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License

# ------------------------------------------------------------------------
import socket;
import sys;


def main():
    if len(sys.argv) != 3:
        print "Invalid arguments", sys.argv
        sys.exit(1)

    host = str.strip(sys.argv[1])
    port = str.strip(sys.argv[2])
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        result = s.connect_ex((host, int(port)))
        s.close()
        sys.exit(result)
    except Exception, e:
        print e
        s.close()
        sys.exit(1)


if __name__ == '__main__':
    main()
