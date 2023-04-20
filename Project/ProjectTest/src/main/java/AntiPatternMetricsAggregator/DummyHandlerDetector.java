package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.*;

import java.io.File;

public class DummyHandlerDetector extends ASTVisitor {

    static int count= 0;

    CompilationUnit cu;

    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    File file;

    public DummyHandlerDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    public boolean visit(CatchClause node) {
        // Check if catch block only prints stack trace
        if(!temp.getName().equals(file.getName())){
            count= 0;
        }

        Block catchBody = node.getBody();
        if (catchBody.statements().size() == 1 &&
                catchBody.statements().get(0) instanceof ExpressionStatement &&
                ((ExpressionStatement) catchBody.statements().get(0)).getExpression() instanceof MethodInvocation &&
                "printStackTrace".equals(((MethodInvocation) ((ExpressionStatement) catchBody.statements().get(0)).getExpression()).getName().getIdentifier())) {

                count++;
        }

        temp= file;
        return super.visit(node);
    }

    public static int countDummyHandler(){
        return count;
    }

}
