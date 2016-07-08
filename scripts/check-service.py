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
import sys, json;

def main():
    try:
        data = json.load(sys.stdin)
        if (data is None or 'tasksUnhealthy' not in data or
            'tasksHealthy' not in data or 'tasksStaged' not in data):
            print "Invalid json input: ", data
            sys.exit(1)
        tasksHealthy = int(data['tasksHealthy'])
        tasksUnhealthy = int(data['tasksUnhealthy'])
        tasksStaged = int(data['tasksStaged'])
        if (tasksHealthy == 0 or tasksUnhealthy != 0 or tasksStaged != 0):
            sys.exit(1)
    except Exception as e:
        print e
        sys.exit(1)

if __name__ == '__main__':
    main()
