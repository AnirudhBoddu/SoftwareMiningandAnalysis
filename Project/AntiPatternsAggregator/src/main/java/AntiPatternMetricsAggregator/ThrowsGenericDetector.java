package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.util.List;

public class ThrowsGenericDetector extends ASTVisitor {
    private CompilationUnit cu;
    static int count;
    static File temp = new File("example.java");
    File file;

    public ThrowsGenericDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        if (!temp.getName().equals(file.getName())) {
            count = 0;
        }

        List<TypeParameter> typeParameters = node.typeParameters();
        for (TypeParameter typeParameter : typeParameters) {
            List<Type> bounds = typeParameter.typeBounds();
            for (Type bound : bounds) {
                if (bound instanceof SimpleType) {
                    SimpleType simpleType = (SimpleType) bound;
                    String typeName = simpleType.getName().getFullyQualifiedName();
                    if (typeName.equals("Exception")) {
                        count++;
                    }
                }
            }
        }

        temp = file;

        return super.visit(node);
    }

    public static int getGenericThrowsCount() {
        return count;
    }
}

