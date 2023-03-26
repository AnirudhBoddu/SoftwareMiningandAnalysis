package com.concordia.soen;

import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;

public class EmptyCatchClauseFinder {
	public static void main(String args[]) {
		ASTParser parser = ASTParser.newParser(AST.JLS19);
		for (String fileName : args) {
			String src;
			try {
				src = StaticAnalysisDemo.read(fileName);
			} catch (IOException e) {
				System.out.println("Error reading the file "+e.getMessage());
				continue;
			}
			parser.setSource(src.toCharArray());
			ASTNode root = parser.createAST(null);
			root.accept(new Visitor());
		}
	}
	
	static class Visitor extends ASTVisitor{
		@Override
		public boolean visit(CatchClause c) {
			if(c.getBody().statements().isEmpty())
				System.out.println(c.toString());
			return true;
		}
	}
}
