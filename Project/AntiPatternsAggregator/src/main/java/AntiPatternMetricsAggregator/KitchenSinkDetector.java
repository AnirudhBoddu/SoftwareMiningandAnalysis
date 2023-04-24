package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KitchenSinkDetector extends ASTVisitor {

    private CompilationUnit cu;
    private File file;

    static int count= 0;
    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");
    private static final int THROWS_THRESHOLD = 3;

    public KitchenSinkDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    /*
    This method looks for method declarations that have a large number of throws clauses (>3).
     */
    @Override
    public boolean visit(MethodDeclaration md) {
        if(!temp.getName().equals(file.getName())){
            count= 0;
        }
        int throwsCount = md.thrownExceptionTypes().size();
        List<String> thrownExceptions = new ArrayList<>();
        for (Object thrownType : md.thrownExceptionTypes()) {
            thrownExceptions.add(((Type) thrownType).toString());
        }

        if (throwsCount > THROWS_THRESHOLD) {
            count++;
        } else {
            for (Object thrownType : md.thrownExceptionTypes()) {
                if (!isThrownByMethod(thrownType.toString(), md)) {
                    count++;
                }
            }
        }
        temp= file;
        return super.visit(md);
    }

    /*
    This method checks whether a given exception type is thrown by the given method.
     */
    private boolean isThrownByMethod(String exceptionType, MethodDeclaration md) {
        for (Object thrownType : md.thrownExceptionTypes()) {
            if (((Type) thrownType).toString().equals(exceptionType)) {
                return true;
            }
        }
        return false;
    }

    public static int KitchenSinkCount(){
        return count;
    }
}
