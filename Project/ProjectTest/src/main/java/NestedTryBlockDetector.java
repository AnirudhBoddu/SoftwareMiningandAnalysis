import org.eclipse.jdt.core.dom.*;

import java.io.File;

public class NestedTryBlockDetector extends ASTVisitor {

    private CompilationUnit cu;
    private File file;

    static int count= 0;
    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    public NestedTryBlockDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    @Override
    public boolean visit(TryStatement node) {

        if(!temp.getName().equals(file.getName())){
            count= 0;
        }

        Block tryBlock = node.getBody();

        // If the try block exists and it contains at least one statement...
        if (tryBlock != null && !tryBlock.statements().isEmpty()) {

            // Iterate through each statement in the try block...
            for (Object obj : tryBlock.statements()) {
                Statement statement = (Statement) obj;

                // If a statement is a TryStatement node...
                if (statement instanceof TryStatement) {
                    // Get the line number of the try statement node and print to the console.
                    int lineNumber = cu.getLineNumber(node.getStartPosition());
                    count++;
                    // Visit the nested try statement recursively
                    ((TryStatement) statement).accept(this);
                }
            }
        }
        temp= file;
        return true;
    }

    @Override
    public boolean visit(CatchClause node) {

        if(!temp.getName().equals(file.getName())){
            count= 0;
        }

        Block catchBlock = node.getBody();

        // If the catch block exists and it contains at least one statement...
        if (catchBlock != null && !catchBlock.statements().isEmpty()) {

            // Iterate through each statement in the catch block...
            for (Object obj : catchBlock.statements()) {
                Statement statement = (Statement) obj;

                // If a statement is a TryStatement node...
                if (statement instanceof TryStatement) {
                    count++;
                    // Visit the nested try statement recursively
                    ((TryStatement) statement).accept(this);
                }
            }
        }
        temp= file;
        return true;
    }

    public static int NestedTryBlockCount(){
        return count;
    }
}
