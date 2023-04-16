package com.concordia.soen.sma;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class KitchenSinkDetector extends ASTVisitor {

    private CompilationUnit cu;
    private String filePath;
    private Writer writer;
    private static final int THROWS_THRESHOLD = 3;

    public KitchenSinkDetector(CompilationUnit cu, String filePath, Writer writer) {
        this.cu = cu;
        this.filePath = filePath;
        this.writer = writer;
    }

    /*
    This method looks for method declarations that have a large number of throws clauses (>3).
     */
    @Override
    public boolean visit(MethodDeclaration md) {
        int throwsCount = md.thrownExceptionTypes().size();
        List<String> thrownExceptions = new ArrayList<>();
        for (Object thrownType : md.thrownExceptionTypes()) {
            thrownExceptions.add(((Type) thrownType).toString());
        }

        if (throwsCount > THROWS_THRESHOLD) {
            int lineNumber = cu.getLineNumber(md.getName().getStartPosition());
            try {
                writer.write("Throws Kitchen Sink"+" ,"+filePath + "," + lineNumber + "," + md.getName().getIdentifier() + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            for (Object thrownType : md.thrownExceptionTypes()) {
                if (!isThrownByMethod(thrownType.toString(), md)) {
                    int lineNumber = cu.getLineNumber(md.getStartPosition());
                    try {
                        writer.write("Unnecessary Throws Clause" + ", " + filePath + "," + lineNumber + "," + md.getName().getIdentifier() + "," + thrownType + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return super.visit(md);
    }

    /*
    This method checks whether a given exception type is thrown by the given method.
     */
    private boolean isThrownByMethod(String exceptionType, MethodDeclaration md) {
        for (Object thrownType : md.thrownExceptionTypes()) {
            if (((Type) thrownType).toString().equals(exceptionType)) {
                return true;
            }
        }
        return false;
    }
}
