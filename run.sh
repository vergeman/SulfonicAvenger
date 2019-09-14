#!/bin/bash

MAVEN_OPTS=-Djava.library.path=gamelib mvn compile exec:java -Dexec.mainClass=org.vergeman.sulfonicavenger.MyGame
