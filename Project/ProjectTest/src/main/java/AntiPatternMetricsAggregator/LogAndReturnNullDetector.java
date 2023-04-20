package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.*;
import java.io.File;

public class LogAndReturnNullDetector extends ASTVisitor{

    static int count= 0;

    CompilationUnit cu;

    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    File file;

    public LogAndReturnNullDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    @Override
    public boolean visit(CatchClause node) {

        if(!temp.getName().equals(file.getName())){
            count= 0;
        }

        Block catchBody = node.getBody();
        if (catchBody.statements().size() == 1 &&
                catchBody.statements().get(0) instanceof ReturnStatement &&
                ((ReturnStatement) catchBody.statements().get(0)).getExpression() == null) {
            count++;
        }

        temp= file;
        return super.visit(node);
    }

    public static int countLogAndReturnNullDetector(){
        return count;
    }
}


