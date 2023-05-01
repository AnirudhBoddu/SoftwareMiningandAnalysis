package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class MultiLineLogDetector extends ASTVisitor {
    private CompilationUnit cu;
    private static int count;
    private static File temp = new File("example.java");
    private File file;

    public MultiLineLogDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        if (!temp.getName().equals(file.getName())) {
            count = 0;
        }

        Block body = node.getBody();
        if (body == null) {
            return super.visit(node);
        }

        body.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodInvocation node) {
                String logLevel = node.getName().getIdentifier();

                if (logLevel.matches("(debug|info|warn|error)")) {
                    String logMessage = (String) node.arguments().stream()
                            .map(expr -> expr.toString())
                            .reduce("", (s1, s2) -> String.valueOf(s1) + String.valueOf(s2));

                    if (logMessage.contains("\n")) {
                        count++;
                    }
                }

                return super.visit(node);
            }
        });

        temp = file;

        return super.visit(node);
    }

    public static int getMultiLineLogCount() {
        return count;
    }
}

