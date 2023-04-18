import org.eclipse.jdt.core.dom.*;

import java.io.File;

public class OverCatchDetector extends ASTVisitor {

    private CompilationUnit cu;

    static int count;

    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    File file;

    public OverCatchDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file= file;
    }

    @Override
    public boolean visit(CatchClause node) {
        if(!temp.getName().equals(file.getName())){
            count= 0;
        }
        Type caughtType = node.getException().getType();
        if (caughtType.isSimpleType()) {
            ITypeBinding typeBinding = caughtType.resolveBinding();
            if (typeBinding != null && typeBinding.isSubTypeCompatible(
                    node.getAST().resolveWellKnownType("java.lang.Exception"))) {
                count++;
            }
        }

        temp= file;

        return super.visit(node);
    }

    public static int OverCatchCount(){
        return count;
    }

}
