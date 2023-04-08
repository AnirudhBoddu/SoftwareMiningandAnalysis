// This class is used to read a java project directory and detect exceptional handling anti-patterns in the code.
package main.java.com.concordia.soen.sma;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

public class ProjectReader {

	private static final String CSV_FILE_NAME = "output.csv";
	private static final String[] FILE_EXTENSIONS = new String[] { "java" };

	public static void main(String[] args) throws IOException {
		String directoryPath = "C:\\Users\\ANIRUDH BODDU\\Desktop\\Software Mining and Analysis\\hibernate-orm-5.2.0\\hibernate-orm-5.2.0";
		Collection<File> files = FileUtils.listFiles(new File(directoryPath), FILE_EXTENSIONS, true);
		FileWriter csvWriter = new FileWriter(CSV_FILE_NAME);
		csvWriter.append("Anti-Pattern, File Path, Line Number, Other details\n");
		System.out.println("Anti-Patterns extraction start...");
		for (File file : files) {
			String filePath = file.getAbsolutePath();
			try {
				CompilationUnit cu = parseFile(filePath);
				cu.accept(new NestedTryBlockDetector(cu, filePath, csvWriter));
				cu.accept(new GetCauseDetector(cu, filePath, csvWriter));
				cu.accept(new KitchenSinkDetector(cu, filePath, csvWriter));
				cu.accept(new DestructiveWrappingDetector(cu, filePath, csvWriter));
			} catch (IOException e) {
				System.err.println("Error reading file " + filePath + ": " + e.getMessage());
			}
		}
		System.out.println("Anti-Patterns extraction complete!");
		csvWriter.close();
	}

	private static CompilationUnit parseFile(String filePath) throws IOException {
		String source = FileUtils.readFileToString(new File(filePath), Charset.defaultCharset());
		ASTParser parser = ASTParser.newParser(AST.JLS19);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setUnitName(filePath);
		return (CompilationUnit) parser.createAST(null);
	}

}
