package AntiPatternMetricsAggregator;

import java.io.File;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TryStatement;

public class ThrowWithinFinallyDetector extends ASTVisitor {
	private CompilationUnit cu;
	private File file;
	private static int throwWithinFinallyCount;
	static File temp = new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

	public ThrowWithinFinallyDetector(CompilationUnit cu, File file) {
		this.cu = cu;
		this.file = file;
	}

	@Override
	public boolean visit(TryStatement node) {
		if (!temp.getName().equals(file.getName())) {
			throwWithinFinallyCount = 0;
		}
		
		/*
		 * for (Object obj : node.getBody().statements()) { if (obj instanceof
		 * Statement) { Statement statement = (Statement) obj; statement.accept(this); }
		 * }
		 */

		// Visit the statements in the finally block (if any)
		if (node.getFinally() != null) {
			for (Object obj : node.getFinally().statements()) {
				if (obj instanceof TryStatement) {
					TryStatement tryStatement = (TryStatement) obj;
					// Check if the try-catch block contains a throw statement
					if (tryStatement.getBody().statements().stream().anyMatch(s -> s.toString().contains("throw"))) {
						throwWithinFinallyCount++;
						break;
					}
				}
			}
		}
		temp = file;
		return super.visit(node);

	}

	public static int ThrowWithinFinallyDetectorCount() {
		return throwWithinFinallyCount;
	}
}
