

import org.eclipse.jdt.core.dom.*;
import java.io.File;
import java.util.List;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;

public class IncompleteImplementationDetector extends ASTVisitor{

    static int count= 0;

    CompilationUnit cu;

    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");

    File file;

    public IncompleteImplementationDetector(CompilationUnit cu, File file) {
        this.cu = cu;
        this.file = file;
        new detectIncompleteImplementationInCatchClauseBody(cu,file);
        new detectIncompleteImplementationInCatchClauseFinallyBlock(cu, file);
        new detectIncompleteImplementationInCatchClauseParameter(cu,file);
        new detectIncompleteImplementationInMethodThrowsClause(cu,file);
    }

    public static Integer IncompleteImplementationCount() {
        return count;
    }


    private class detectIncompleteImplementationInMethodThrowsClause extends ASTVisitor {

        CompilationUnit cu;
        File file;

        detectIncompleteImplementationInMethodThrowsClause(CompilationUnit cu, File file) {

            this.cu = cu;
            this.file = file;
        }

        @Override
        public boolean visit(MethodDeclaration node) {
            List thrownExceptions = node.thrownExceptionTypes();
            for (Object thrownException : thrownExceptions) {
                if (thrownException == null) {
                    count++;
                }
            }

            temp= file;
            return super.visit(node);
        }
    }

        private class detectIncompleteImplementationInCatchClauseParameter extends ASTVisitor {

            CompilationUnit cu;
            File file;

            detectIncompleteImplementationInCatchClauseParameter(CompilationUnit cu, File file) {

                this.cu = cu;
                this.file = file;
            }

            @Override
            public boolean visit(CatchClause node) {
                SingleVariableDeclaration exceptionDeclaration = node.getException();
                Type exceptionType = exceptionDeclaration.getType();
                if (exceptionType == null) {
                    count++;
                }

                temp= file;
                return super.visit(node);
            }
        }

        private class detectIncompleteImplementationInCatchClauseBody extends ASTVisitor {

            CompilationUnit cu;
            File file;

            detectIncompleteImplementationInCatchClauseBody(CompilationUnit cu, File file) {
                this.cu =cu;
                this.file= file;
            }

            @Override
            public boolean visit(CatchClause node) {
                Block catchBody = node.getBody();
                if (catchBody.statements().isEmpty()) {
                    count++;
                }

                temp= file;
                return super.visit(node);
            }
        }

        private class detectIncompleteImplementationInCatchClauseFinallyBlock extends ASTVisitor {

            CompilationUnit cu;
            File file;

            detectIncompleteImplementationInCatchClauseFinallyBlock(CompilationUnit cu, File file){
                this.cu= cu;
                this.file= file;
            }

            @Override
            public boolean visit(TryStatement node) {
                Block finallyBlock = node.getFinally();
                if (finallyBlock != null && finallyBlock.statements().isEmpty()) {
                    count++;
                }
                return super.visit(node);
            }

        }
    }



