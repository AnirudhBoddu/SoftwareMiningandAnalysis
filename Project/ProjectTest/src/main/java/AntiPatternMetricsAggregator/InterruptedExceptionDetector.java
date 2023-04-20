import org.eclipse.jdt.core.dom.*;

import java.io.File;

public class InterruptedExceptionDetector extends ASTVisitor {

    static int count= 0;

    private CompilationUnit cu;

    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    File file;

    public InterruptedExceptionDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    @Override
    public boolean visit(CatchClause node) {

        if(!temp.getName().equals(file.getName())){
            count= 0;
        }

        // Check if the caught exception is InterruptedException
        if (node.getException().getType().toString().equals("InterruptedException")) {
            // Check if the catch block does not take appropriate action
            if (node.getBody().statements().isEmpty()) {
                count++;
            }
        }
        temp= file;

        return super.visit(node);
    }

    public static int InterruptedExceptionCount() {
        return count;
    }
}
