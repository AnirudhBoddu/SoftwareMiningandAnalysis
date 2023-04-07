// This class is used to read a java project directory and detect exceptional handling anti-patterns in the code.
package com.concordia.soen.sma;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.*;

public class ProjectReader {
	
	// The main method of the class which reads a java file input directory and detects anti-patterns in each file.
	public static void main(String[] args) {
		
		String directoryPath = "D:\\SoftwareMiningandAnalysis\\hibernate-orm";
		
		// Lists all files in a directory and its sub-directories (matching .java extension).
		Collection<File> files = FileUtils.listFiles(new File(directoryPath), new String[] { "java" }, true);
		
		for (File file : files) {
			String filePath = file.getAbsolutePath();
			try {
				detectExceptionalHandlingAntiPatterns(filePath); // Detects exceptional handling anti-patterns in each file.
			} catch (IOException e) {
				System.err.println("Error reading file " + filePath + ": " + e.getMessage()); // If an error occurs while reading a file, exception will be caught here and the message will be printed to the console.
			}
		}
	}

	// Method which uses Eclipse JDT library to create an AST parse tree and detect nested try blocks in the source code.
	private static void detectExceptionalHandlingAntiPatterns(String filePath) throws IOException {
		
		// Creates a new parser instance for the specified API level.
		ASTParser parser = ASTParser.newParser(AST.JLS19);
		
		// Reads a file into a String with a specific encoding.
		String source = FileUtils.readFileToString(new File(filePath), Charset.defaultCharset());
		
		// Parses the source code and returns the root AST node.
		parser.setSource(source.toCharArray());
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		
		// Visits each AST node in a top-down traversal and detects nested try blocks to identify exceptional handling anti-patterns in the source code.
		cu.accept(new NestedTryBlockDetector(cu, filePath));
	}
}