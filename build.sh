#!/bin/bash
rm -rf lab1/src/*.class
javac -cp . lab1/src/*.java
2 "tsim --speed 15 Lab1.map" "java -cp .:lab1/src/ Lab1 22 22 15"
