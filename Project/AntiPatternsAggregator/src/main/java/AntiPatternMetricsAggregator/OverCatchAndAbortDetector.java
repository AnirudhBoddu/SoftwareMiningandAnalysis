package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.*;

import java.io.File;

public class OverCatchAndAbortDetector extends ASTVisitor {
    private CompilationUnit cu;
    private File file;
    private static int overCatchAndAbortCount;
    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    public OverCatchAndAbortDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    @Override
    public boolean visit(CatchClause node) {
        if(!temp.getName().equals(file.getName())){
            overCatchAndAbortCount= 0;
        }
        Block body = node.getBody();
        if (body != null) {
            for (Object statement : body.statements()) {
                if (statement instanceof ThrowStatement) {
                    String exceptionName = ((ThrowStatement) statement).getExpression().toString();
                    if (exceptionName.equals("System.exit") || exceptionName.equals("Runtime.getRuntime().exit")) {
                        overCatchAndAbortCount++;
                        break;
                    }
                }
            }
        }
        temp= file;
        return super.visit(node);
    }

    public static int OverCatchAndAbortCount() {
        return overCatchAndAbortCount;
    }
}

