package com.concordia.soen;

import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TryStatement;

public class NestedTryBlockDetector {
	public static void main(String args[]) {
		ASTParser parser = ASTParser.newParser(AST.JLS19);
		for (String fileName : args) {
			String src;
			try {
				src = StaticAnalysisDemo.read(fileName);
			} catch (IOException e) {
				System.out.println("Error reading the file " + e.getMessage());
				continue;
			}
			parser.setSource(src.toCharArray());
			ASTNode root = parser.createAST(null);
			root.accept(new Visitor());
		}
	}

	static class   extends ASTVisitor {

		private CompilationUnit compilationUnit;

		@Override
		public boolean visit(TryStatement node) {
			checkForNestedTryBlock(node, node.getParent());
			return true;
		}

		@Override
		public boolean visit(CatchClause node) {
			checkForNestedTryBlock(node.getBody(), node.getParent());
			return true;
		}

		private void checkForNestedTryBlock(Statement statement, ASTNode parentNode) {
			if (statement instanceof TryStatement) {
				TryStatement tryStatement = (TryStatement) statement;
				List<Statement> tryStatements = tryStatement.getBody().statements();
				for (Statement tryStmt : tryStatements) {
					if (tryStmt instanceof TryStatement) {
						int lineNumber = compilationUnit.getLineNumber(tryStmt.getStartPosition());
						String className = compilationUnit.getJavaElement().getElementName();
						System.out.println("Nested try block found at line " + lineNumber + " in class " + className
								+ ": " + tryStmt);
						checkForNestedTryBlock(tryStmt, tryStatement);
					}
				}
				List<CatchClause> catchClauses = tryStatement.catchClauses();
				for (CatchClause catchClause : catchClauses) {
					List<Statement> catchStatements = catchClause.getBody().statements();
					for (Statement catchStmt : catchStatements) {
						if (catchStmt instanceof TryStatement) {
							int lineNumber = compilationUnit.getLineNumber(catchStmt.getStartPosition());
							String className = compilationUnit.getJavaElement().getElementName();
							System.out.println("Nested try block found at line " + lineNumber + " in class " + className
									+ ": " + catchStmt);
							checkForNestedTryBlock(catchStmt, tryStatement);
						}
					}
				}
			}
		}

		@Override
		public boolean visit(CompilationUnit node) {
			compilationUnit = node;
			return super.visit(node);
		}
	}

}
