#!/bin/bash
rm -rf lab1/src/*.class
javac -cp . lab1/src/*.java
2 "tsim --speed 5 Map.map" "java -cp .:lab1/src/ Main 10 5"
