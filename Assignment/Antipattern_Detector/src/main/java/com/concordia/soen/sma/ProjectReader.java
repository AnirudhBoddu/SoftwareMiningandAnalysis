// This class is used to read a java project directory and detect exceptional handling anti-patterns in the code.
package com.concordia.soen.sma;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.*;

public class ProjectReader {
	
	// The main method of the class which reads a java file input directory and detects anti-patterns in each file.
	public static void main(String[] args) throws IOException {
		String directoryPath = "D:\\SoftwareMiningandAnalysis\\hibernate-orm";
		Collection<File> files = FileUtils.listFiles(new File(directoryPath), new String[] { "java" }, true);

		// Create a FileWriter object to write to a file
		FileWriter csvWriter = new FileWriter("output.csv");
		csvWriter.append("File Path, Line Number, Type\n");

		for (File file : files) {
			String filePath = file.getAbsolutePath();
			try {
				// Pass the FileWriter object to the NestedTryBlockDetector instance
				detectExceptionalHandlingAntiPatterns(filePath, csvWriter);
			} catch (IOException e) {
				System.err.println("Error reading file " + filePath + ": " + e.getMessage());
			}
		}

		// Close the FileWriter object after processing all files
		csvWriter.close();
	}

	private static void detectExceptionalHandlingAntiPatterns(String filePath, FileWriter writer) throws IOException {
		ASTParser parser = ASTParser.newParser(AST.JLS19);
		String source = FileUtils.readFileToString(new File(filePath), Charset.defaultCharset());
		parser.setSource(source.toCharArray());
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		// Pass the FileWriter object to the NestedTryBlockDetector instance
		cu.accept(new NestedTryBlockDetector(cu, filePath, writer));
	}

}