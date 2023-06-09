package AntiPatternMetricsAggregator;// This class is used to read a java project directory and detect exceptional handling anti-patterns in the code.

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class ProjectReader {
    private static final String[] FILE_EXTENSIONS = new String[] { "java" };
    static Map<String,Integer> hm= new HashMap<>();

    public static void main(String[] args) throws IOException {
        String directoryPath = "D:\\hibernate-orm-5.2.0\\hibernate-orm-5.2.0";
        Collection<File> files = FileUtils.listFiles(new File(directoryPath), FILE_EXTENSIONS, true);
        List<String[]> output = new ArrayList<>();

        for (File file : files) {
            String filePath = file.getAbsolutePath();
            try {
                int countCatchAndReturnNull, countOverCatch, countCatchAndDoNothing, countNestedTryBlock,
                        countGetCause, countKitchenSink, countDestructiveWrapping, countOverCatchAndAbort,
                        countCatchGeneric, countInterruptedException, countIncompleteImplementation, countDummyHandler,
                        countLogAndReturnNull, countLogAndThrow,countThrowWithinFinally, countMultiLineLogs, countGenericThrows;
                CompilationUnit cu = parseFile(filePath);
                cu.accept(new CatchAndReturnNullDetector(cu, file));
                cu.accept(new OverCatchDetector(cu, file));
                cu.accept(new CatchAndDoNothingDetector(cu, file));
                cu.accept(new NestedTryBlockDetector(cu, file));
                cu.accept(new GetCauseDetector(cu, file));
                cu.accept(new KitchenSinkDetector(cu, file));
                cu.accept(new DestructiveWrappingDetector(cu, file));
                cu.accept(new OverCatchAndAbortDetector(cu, file));
                cu.accept(new CatchGenericDetector(cu, file));
                cu.accept(new InterruptedExceptionDetector(cu, file));
                cu.accept(new IncompleteImplementationDetector(cu, file));
                cu.accept(new DummyHandlerDetector(cu, file));
                cu.accept(new LogAndReturnNullDetector(cu, file));
                cu.accept(new LogAndThrowDetector(cu,file));
                cu.accept(new ThrowWithinFinallyDetector(cu, file));
                cu.accept(new MultiLineLogDetector(cu, file));
                cu.accept(new ThrowsGenericDetector(cu, file));

                countCatchAndReturnNull = CatchAndReturnNullDetector.CatchAndReturnNullCount();
                countOverCatch = OverCatchDetector.OverCatchCount();
                countCatchAndDoNothing = CatchAndDoNothingDetector.CatchAndDoNothingCount();
                countNestedTryBlock = NestedTryBlockDetector.NestedTryBlockCount();
                countGetCause =  GetCauseDetector.GetCauseCount();
                countKitchenSink = KitchenSinkDetector.KitchenSinkCount();
                countDestructiveWrapping = DestructiveWrappingDetector.DestructiveWrappingCount();
                countOverCatchAndAbort = OverCatchAndAbortDetector.OverCatchAndAbortCount();
                countCatchGeneric = CatchGenericDetector.getCatchGenericCount();
                countInterruptedException= InterruptedExceptionDetector.InterruptedExceptionCount();
                countIncompleteImplementation= IncompleteImplementationDetector.countIncompleteImplementation();
                countDummyHandler= DummyHandlerDetector.countDummyHandler();
                countLogAndReturnNull= LogAndReturnNullDetector.countLogAndReturnNullDetector();
                countLogAndThrow =  LogAndThrowDetector.LogAndThrowCount();
                countThrowWithinFinally=ThrowWithinFinallyDetector.ThrowWithinFinallyDetectorCount();
                countMultiLineLogs=MultiLineLogDetector.getMultiLineLogCount();
                countGenericThrows=ThrowsGenericDetector.getGenericThrowsCount();

                output.add(new String[] {file.getName(), String.valueOf(countCatchAndReturnNull), String.valueOf(countOverCatch),
                        String.valueOf(countCatchAndDoNothing), String.valueOf(countNestedTryBlock), String.valueOf(countGetCause),
                        String.valueOf(countKitchenSink), String.valueOf(countDestructiveWrapping), String.valueOf(countOverCatchAndAbort),
                        String.valueOf(countCatchGeneric), String.valueOf(countInterruptedException), String.valueOf(countIncompleteImplementation),
                        String.valueOf(countDummyHandler), String.valueOf(countLogAndReturnNull), String.valueOf(countLogAndThrow),String.valueOf(countThrowWithinFinally),
                        String.valueOf(countMultiLineLogs), String.valueOf(countGenericThrows)});
            } catch (IOException e) {
                System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            }
        }

        try (CSVPrinter printer = new CSVPrinter(new FileWriter("anti-patternsCount.csv"), CSVFormat.DEFAULT)) {
            printer.printRecord("Filename", "countCatchAndReturnNull", "countOverCatch", "countCatchAndDoNothing",
                    "countNestedTryBlock", "countGetCause", "countKitchenSink", "countDestructiveWrapping", "countOverCatchAndAbort", "countCatchGeneric",
                    "countInterruptedException", "countIncompleteImplementation","countDummyHandler","countLogAndReturnNull", "countLogAndThrow",
                    "countThrowWithinFinally", "countMultiLineLogs", "countGenericThrows");
            for (String[] row : output) {
                printer.printRecord(row);
            }
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
