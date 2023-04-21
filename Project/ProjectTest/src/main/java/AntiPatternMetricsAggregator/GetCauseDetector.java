package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;

import java.io.File;

class GetCauseDetector extends ASTVisitor {

    private CompilationUnit cu;
    private File file;

    static int count= 0;
    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    public GetCauseDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    @Override
    public boolean visit(MethodInvocation node) {
        if(!temp.getName().equals(file.getName())){
            count= 0;
        }
        if (node.getName().getIdentifier().equals("getCause")) {
            node.resolveMethodBinding();
            Expression expr = node.getExpression();
            String className= expr.resolveTypeBinding()!=null ?(expr.resolveTypeBinding().getQualifiedName()):null;
            Class<?> clazz=null;
            if (className != null) {
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    // No action
                }
            }
            if(clazz != null && Throwable.class.isAssignableFrom(clazz));
            {
                count++;
            }
        }
        temp= file;
        return super.visit(node);
    }
    public static int GetCauseCount(){
        return count;
    }
}
