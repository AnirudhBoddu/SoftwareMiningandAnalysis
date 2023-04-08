package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.*;

import java.io.File;

public class CatchGenericDetector extends ASTVisitor {
    private final CompilationUnit cu;
    private final File file;
    private static int catchGenericCount = 0;

    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    public CatchGenericDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    public static int getCatchGenericCount() {
        return catchGenericCount;
    }

    @Override
    public boolean visit(CatchClause node) {
        if(!temp.getName().equals(file.getName())){
            catchGenericCount= 0;
        }
        boolean isGenericCatch = node.getException().getType().isSimpleType()
                && ((SimpleType) node.getException().getType()).getName().getFullyQualifiedName().equals("Exception");
        if (isGenericCatch) {
            catchGenericCount++;
        }
        temp= file;
        return super.visit(node);
    }
}

