package com.concordia.soen;

import java.io.IOException;

import org.eclipse.jdt.core.dom.*;

public class MethodDeclarationCounter {

	public static void main(String args[]) throws IOException {
		ASTParser parser = ASTParser.newParser(AST.JLS19);
		for (String fileName : args) {
			String src = StaticAnalysisDemo.read(fileName);
			parser.setSource(src.toCharArray());
			ASTNode root = parser.createAST(null);

			Visitor visitor = new Visitor();
			root.accept(visitor);
			System.out.println(fileName + " count of methods: " + visitor.count);
		}
	}

	static class Visitor extends ASTVisitor {
		int count = 0;

		@Override
		public boolean visit(MethodDeclaration d) {
			count += 1;
			return true;
		}
	}
}
