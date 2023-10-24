#!/bin/bash
java -Dfile.encoding=UTF8 -cp ./lib/nx-config-converter.jar:./lib/* com.norconex.cfgconverter.xml.ConverterApp "$1" "$2"
