import org.eclipse.jdt.core.dom.*;

import java.io.File;

public class CatchAndReturnNullDetector extends ASTVisitor {

    static int count= 0;
    private CompilationUnit cu;

    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");
    File file;


    public CatchAndReturnNullDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file= file;
    }

    @Override
    public boolean visit(CatchClause node) {

        if(!temp.getName().equals(file.getName())){
            count= 0;
        }
        if (node.getBody() != null) {
            if (node.getBody().statements().size() == 1) {
                if (node.getBody().statements().get(0) instanceof ReturnStatement) {
                    ReturnStatement returnStmt = (ReturnStatement) node.getBody().statements().get(0);
                    if (returnStmt.getExpression() == null) {
                        count++;
                    }
                }
            }
        }

        temp= file;

        return super.visit(node);
    }

    public static int CatchAndReturnNullCount(){
        return count;
    }


}
