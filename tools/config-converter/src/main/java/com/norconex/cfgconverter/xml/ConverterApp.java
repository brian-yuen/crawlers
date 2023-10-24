package com.norconex.cfgconverter.xml;

import com.norconex.commons.lang.xml.XML;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
public class ConverterApp {
    public static void main(String[] args) {
        // Check if the correct number of arguments is provided
        if (args.length != 2) {
            System.out.println("Usage: ConverterApp <input-xml-file> <output-xml-file>");
            System.exit(1);
        }

        // Parse command-line arguments
        String inputFilePath = args[0];
        String outputFilePath = args[1];

        // Validate input and output file paths
        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        if (!inputFile.exists() || inputFile.isDirectory()) {
            System.err.println("Input file does not exist or is a directory: " + inputFilePath);
            System.exit(1);
        }

        try {
            // Read the XML content from the input file
            String xmlContent = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);

            // Create an XML object from the content
            XML inputXml = new XML(xmlContent);

            // Create a FileWriter to write the output to a file
            FileWriter writer = new FileWriter(outputFile);

            // Create an instance of XmlToXmlV4ConfigConverter
            XmlToXmlV4ConfigConverter converter = new XmlToXmlV4ConfigConverter();

            // Convert the XML content and write it to the output file
            converter.convert(inputXml, writer);

            // Close the writer
            writer.close();

            System.out.println("Conversion completed successfully.");
        } catch (IOException e) {
            System.err.println("Error during conversion: " + e.getMessage());
            System.exit(1);
        }
    }
}



