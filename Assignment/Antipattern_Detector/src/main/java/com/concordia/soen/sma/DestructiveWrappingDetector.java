

import org.eclipse.jdt.core.dom.*;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class DestructiveWrappingDetector extends ASTVisitor {
    private CompilationUnit cu;
    private String filePath;
    private Writer writer;

    public DestructiveWrappingDetector(CompilationUnit cu, String filePath, Writer writer) {
        this.cu = cu;
        this.filePath = filePath;
        this.writer = writer;
    }

    @Override
    public boolean visit(CatchClause node) {
        Block catchBlock = node.getBody();
        List<Statement> statements = catchBlock.statements();

        if (statements.size() == 1 && statements.get(0) instanceof ThrowStatement) {
            // Check if the catch block wraps the original exception in a new exception before throwing it
            ThrowStatement throwStatement = (ThrowStatement) statements.get(0);
            Expression exception = throwStatement.getExpression();

            if (exception instanceof ClassInstanceCreation) {
                ClassInstanceCreation cic = (ClassInstanceCreation) exception;
                String exceptionName = cic.getType().toString();
                String catchExceptionName = node.getException().getName().getIdentifier();

                if (!exceptionName.equals(catchExceptionName)) {
                    int lineNumber = cu.getLineNumber(node.getStartPosition());
                    try {
                        writer.write("Destructive Wrapping" + " ," + filePath + "," + lineNumber + "," + catchExceptionName + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (exception instanceof SimpleName) {
                // Check if the catch block rethrows the original exception without wrapping it in a new exception
                SimpleName simpleName = (SimpleName) exception;
                String exceptionName = simpleName.getIdentifier();
                String catchExceptionName = node.getException().getName().getIdentifier();

                if (exceptionName.equals(catchExceptionName)) {
                    int lineNumber = cu.getLineNumber(node.getStartPosition());
                    try {
                        writer.write("Destructive Wrapping" + " ," + filePath + "," + lineNumber + "," + catchExceptionName + "\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return super.visit(node);
    }
}



