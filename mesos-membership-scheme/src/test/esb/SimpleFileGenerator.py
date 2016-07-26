#!/usr/bin/python
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
#

import xml.etree.cElementTree as ET
import time
import uuid
import os

while True:
    root = ET.Element("root")
    doc = ET.SubElement(root, "doc")
    output_path = "/tmp/mesos-artifacts/esb/in"

    ET.SubElement(doc, "time").text = time.strftime("%c")

    tree = ET.ElementTree(root)
    filename = str(uuid.uuid1()) + ".xml"
    tree.write(os.path.join(output_path, filename))
    time.sleep(0.1)