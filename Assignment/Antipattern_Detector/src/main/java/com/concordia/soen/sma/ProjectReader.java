// This class is used to read a java project directory and detect exceptional handling anti-patterns in the code.
package com.concordia.soen.sma;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.*;

public class ProjectReader {
	
	// The main method of the class which reads a java file input directory and detects anti-patterns in each file.
	public static void main(String[] args) throws IOException {
		String directoryPath = "D:\\SoftwareMiningandAnalysis\\hibernate-orm";
		//String directoryPath="C:\\Users\\editi\\Desktop\\SOEN6591\\hibernate-orm-main";
		Collection<File> files = FileUtils.listFiles(new File(directoryPath), new String[] { "java" }, true);

		// Create a FileWriter object to write to a file
		FileWriter csvWriter = new FileWriter("output.csv");
		csvWriter.append("File Path, Line Number, Type\n");

		for (File file : files) {
			String filePath = file.getAbsolutePath();
			try {
				// Pass the FileWriter object to the NestedTryBlockDetector instance
				detectExceptionalHandlingAntiPatterns(filePath, csvWriter);
				//Pass  FileWriter object to the GetCauseDetector
				detectExceptionalHandlingAntiPatternGetCause(file,directoryPath,filePath, csvWriter);
			} catch (IOException e) {
				System.err.println("Error reading file " + filePath + ": " + e.getMessage());
			}
		}

		// Close the FileWriter object after processing all files
		csvWriter.close();
	}

	private static void detectExceptionalHandlingAntiPatternGetCause(File file, String directoryPath, String filePath,
			FileWriter csvWriter) throws IOException {
		byte[] fileBytes = Files.readAllBytes(file.toPath());
        String fileContents = new String(fileBytes, Charset.defaultCharset());
        // Creates a new parser instance for the specified API level.
        ASTParser parser = ASTParser.newParser(AST.JLS19);
        // Parses the source code and returns the root AST node.
        parser.setSource(fileContents.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        
        
        String[] classpathEntries = System.getProperty("java.class.path").split(File.pathSeparator);
        
        parser.setEnvironment(classpathEntries, new String[] { directoryPath }, new String[] { "UTF-8" }, true);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setUnitName(directoryPath);
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        cu.accept(new GetCauseDetector(cu,filePath,csvWriter));
		
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