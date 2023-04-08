package main.java.com.concordia.soen.sma;

import java.io.IOException;
import java.io.Writer;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

class GetCauseDetector extends ASTVisitor {
	

	private CompilationUnit cu;
	private String filePath;
	private Writer writer;

	public GetCauseDetector(CompilationUnit cu, String filePath, Writer writer) {
		this.cu = cu;
		this.filePath = filePath;
		this.writer = writer;
	}

    @Override
    public boolean visit(MethodInvocation node) {
        if (node.getName().getIdentifier().equals("getCause")) {
            	node.resolveMethodBinding();
            	Expression expr = node.getExpression();
            	String className= expr.resolveTypeBinding()!=null ?(expr.resolveTypeBinding().getQualifiedName()):null;
            	Class<?> clazz=null;
				if (className != null) {
					try {
						clazz = Class.forName(className);
					} catch (ClassNotFoundException e) {
						// No action
					}
				}
            	if(clazz != null && Throwable.class.isAssignableFrom(clazz));
            	{
	            	int lineNumber = cu.getLineNumber(node.getStartPosition());
	            	try {
						writer.write(filePath + "," + lineNumber + "," + "getCause" + "\n");
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					//System.out.println("Found getCause call on throwable at line " + lineNumber + " of " + filePath);
            	}
            	
            
        }
        return super.visit(node);
    }
}
