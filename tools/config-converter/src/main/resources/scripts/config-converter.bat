@echo off
java -Dfile.encoding=UTF8 -cp .\lib\converterJar.jar;.\lib\* com.norconex.cfgconverter.xml.ConverterApp %1 %2
