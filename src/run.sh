#!/usr/bin/env bash
rm *.class
cd Interface
rm *.class
cd ..
javac Main.java Interface/Home.java Interface/GeneralJStuff.java Interface/Reviews.java
java Main