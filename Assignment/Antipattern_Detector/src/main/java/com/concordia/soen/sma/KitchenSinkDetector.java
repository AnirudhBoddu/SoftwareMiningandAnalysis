package main.java.com.concordia.soen.sma;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.io.IOException;
import java.io.Writer;

public class KitchenSinkDetector extends ASTVisitor {

    private CompilationUnit cu;
    private String filePath;
    private Writer writer;
    private static final int THROWS_THRESHOLD = 5;

    public KitchenSinkDetector(CompilationUnit cu, String filePath, Writer writer) {
        this.cu = cu;
        this.filePath = filePath;
        this.writer = writer;
    }

    @Override
    public boolean visit(MethodDeclaration md) {
        int throwsCount = md.thrownExceptionTypes().size();

        if (throwsCount > THROWS_THRESHOLD) {
            int lineNumber = cu.getLineNumber(md.getName().getStartPosition());
            try {
                writer.write("Throws Kitchen Sink"+" ,"+filePath + "," + lineNumber + "," + md.getName().getIdentifier() + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.visit(md);
    }

}
