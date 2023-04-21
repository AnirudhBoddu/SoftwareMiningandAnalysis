package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.*;

import java.io.File;

public class LogAndThrowDetector extends ASTVisitor {
    private final CompilationUnit cu;
    private final File file;
    private static int logAndThrowCount = 0;

    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    public LogAndThrowDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    public static int LogAndThrowCount() {
        return logAndThrowCount;
    }

    public boolean visit(CatchClause node) {

        if(!temp.getName().equals(file.getName())){
            logAndThrowCount= 0;
        }

        boolean hasLogStatement = false;
        boolean hasThrowStatement = false;

        Block body = node.getBody();

        // Check if the catch block contains a log statement
        for (Object obj : body.statements()) {
            if (obj instanceof ExpressionStatement) {
                ExpressionStatement stmt = (ExpressionStatement) obj;
                Expression expr = stmt.getExpression();
                if (expr instanceof MethodInvocation) {
                    MethodInvocation methodInvocation = (MethodInvocation) expr;
                    if (methodInvocation.getName().getIdentifier().equals("log") ||
                            methodInvocation.getName().getIdentifier().equals("error") ||
                            methodInvocation.getName().getIdentifier().equals("warn")) {
                        hasLogStatement = true;
                        break;
                    }
                }
            }
        }

        // Check if the catch block contains a throw statement
        for (Object obj : body.statements()) {
            if (obj instanceof ThrowStatement) {
                hasThrowStatement = true;
                break;
            }
        }

        // If both log and throw statements are present, increment the count
        if (hasLogStatement && hasThrowStatement) {
            logAndThrowCount++;
        }
        temp= file;
        return true;
    }

}

