// This class is used to detect nested try blocks and report them to the console.
package com.concordia.soen.sma;

import org.eclipse.jdt.core.dom.*;

import java.io.IOException;
import java.io.Writer;

public class NestedTryBlockDetector extends ASTVisitor {
	
	private CompilationUnit cu;
	private String filePath;

	private Writer writer;

	public NestedTryBlockDetector(CompilationUnit cu, String filePath, Writer writer) {
		this.cu = cu;
		this.filePath = filePath;
		this.writer = writer;
	}

	@Override
	public boolean visit(TryStatement node) {
		
		Block tryBlock = node.getBody();
		
		// If the try block exists and it contains at least one statement...
		if (tryBlock != null && !tryBlock.statements().isEmpty()) {

			// Iterate through each statement in the try block...
			for (Object obj : tryBlock.statements()) {
				Statement statement = (Statement) obj;
				
				// If a statement is a TryStatement node...
				if (statement instanceof TryStatement) {
					
					// Get the line number of the try statement node and print to the console.
					int lineNumber = cu.getLineNumber(node.getStartPosition());
					try {
						writer.write(filePath + "," + lineNumber + "," + "try" + "\n");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean visit(CatchClause node) {
		
		Block catchBlock = node.getBody();
		
		// If the catch block exists and it contains at least one statement...
		if (catchBlock != null && !catchBlock.statements().isEmpty()) {
			
			// Iterate through each statement in the catch block...
			for (Object obj : catchBlock.statements()) {
				Statement statement = (Statement) obj;
				
				// If a statement is a TryStatement node...
				if (statement instanceof TryStatement) {
					
					// Get the line number of the catch statement node and print to the console.
					int lineNumber = cu.getLineNumber(node.getStartPosition());
					try {
						writer.write(filePath + "," + lineNumber + "," + "catch" + "\n");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return true;
	}
}
