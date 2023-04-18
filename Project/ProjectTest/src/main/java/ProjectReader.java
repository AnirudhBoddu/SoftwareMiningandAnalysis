// This class is used to read a java project directory and detect exceptional handling anti-patterns in the code.

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.File;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLOutput;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProjectReader {
    private static final String[] FILE_EXTENSIONS = new String[] { "java" };
    static Map<String,Integer> hm= new HashMap<>();

    public static void main(String[] args) throws IOException {
        String directoryPath = "C:\\Users\\ss161\\Downloads\\hibernate-orm-5.2.0\\hibernate-orm-5.2.0";
        Collection<File> files = FileUtils.listFiles(new File(directoryPath), FILE_EXTENSIONS, true);

        for (File file : files) {
            String filePath = file.getAbsolutePath();
            try {
                CompilationUnit cu = parseFile(filePath);
                cu.accept(new CatchAndReturnNullDetector(cu, file));
            } catch (IOException e) {
                System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            }
            hm.put(file.getName(), CatchAndReturnNullDetector.CatchAndReturnNullCount());
        }

        Iterator hmIterator= hm.entrySet().iterator();

        while(hmIterator.hasNext()){
            Map.Entry m = (Map.Entry)hmIterator.next();
            System.out.println(m.getKey()+" "+m.getValue());
        }

    }

    private static CompilationUnit parseFile(String filePath) throws IOException {
        String source = FileUtils.readFileToString(new File(filePath), Charset.defaultCharset());
        ASTParser parser = ASTParser.newParser(AST.JLS19);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setUnitName(filePath);
        return (CompilationUnit) parser.createAST(null);
    }

}
