// This class is used to detect nested try blocks and report them to the console.
package com.concordia.soen.sma;

import org.eclipse.jdt.core.dom.*;

public class NestedTryBlockDetector extends ASTVisitor {
	
	private CompilationUnit cu;
	private String filePath;

	public NestedTryBlockDetector(CompilationUnit cu, String filePath) {
		this.cu = cu;
		this.filePath = filePath;
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
					System.out.println("Nested try block detected in line " + lineNumber + " of " + filePath);
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
					System.out.println("Nested try block detected in catch block in line " + lineNumber + " of " + filePath);
				}
			}
		}
		return true;
	}
}
