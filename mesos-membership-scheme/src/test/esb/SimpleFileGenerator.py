#!/usr/bin/python
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