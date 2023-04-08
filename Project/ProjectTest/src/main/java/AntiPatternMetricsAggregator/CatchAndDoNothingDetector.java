package AntiPatternMetricsAggregator;

import org.eclipse.jdt.core.dom.*;


import java.io.File;
import java.io.IOException;


public class CatchAndDoNothingDetector extends ASTVisitor{

    static int count;
    private CompilationUnit cu;
    static File temp= new File("C:\\Users\\ss161\\Downloads\\apache-ant-1.9.7-src\\example.java");
    File file;

    public CatchAndDoNothingDetector(CompilationUnit cu, File file) {
    this.cu = cu;
    this.file= file;
    }

        @Override
        public boolean visit(CatchClause node1) {

            if(!temp.getName().equals(file.getName())){
                count= 0;
            }
            Block body = node1.getBody();
            if (body.statements().isEmpty()) {
                count++;
            }

            temp= file;

            return super.visit(node1);
        }

        public static int CatchAndDoNothingCount(){
            return count;
        }
    }
