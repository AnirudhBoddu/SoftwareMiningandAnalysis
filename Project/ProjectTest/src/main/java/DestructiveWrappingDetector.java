import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.util.List;

public class DestructiveWrappingDetector extends ASTVisitor {
    private CompilationUnit cu;
    private File file;

    static int count= 0;
    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    public DestructiveWrappingDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    /*
    This method looks for catch clauses in a try-catch block that might destructively wrap an exception.
    */
    @Override
    public boolean visit(CatchClause node) {
        if(!temp.getName().equals(file.getName())){
            count= 0;
        }
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
                    boolean isFalsePositive = false;

                    // Check if the new exception being thrown is semantically equivalent to the original exception
                    if (cic.arguments().size() == 1 && cic.arguments().get(0) instanceof SimpleName) {
                        SimpleName simpleName = (SimpleName) cic.arguments().get(0);
                        String argumentName = simpleName.getIdentifier();

                        if (argumentName.equals(catchExceptionName)) {
                            isFalsePositive = true;
                        }
                    }

                    // Check if the catch block modifies the original exception in any way
                    if (!isFalsePositive) {
                        for (Statement statement : statements) {
                            if (statement instanceof ExpressionStatement) {
                                ExpressionStatement expressionStatement = (ExpressionStatement) statement;
                                Expression expression = expressionStatement.getExpression();

                                if (expression instanceof MethodInvocation) {
                                    MethodInvocation methodInvocation = (MethodInvocation) expression;
                                    String methodName = methodInvocation.getName().getIdentifier();

                                    if (methodName.equals("printStackTrace") || methodName.equals("fillInStackTrace")) {
                                        isFalsePositive = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (!isFalsePositive) {
                        count++;
                    }
                }
            }
        }
        temp= file;
        return super.visit(node);
    }
    public static int DestructiveWrappingCount(){
        return count;
    }
}