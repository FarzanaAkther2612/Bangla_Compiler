import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String banglaCode = 
            "সংখ্যা ক = ১০; " +
            "সংখ্যা খ = ক + ৫; " +
            "যদি (খ) { " +
            "    সংখ্যা গ = ২০; " +
            "}";
            
        System.out.println("PHASE 1: LEXICAL ANALYSIS");
        Lexer lexer = new Lexer(banglaCode);
        SymbolTable symbolTable = new SymbolTable();
        
        System.out.println("PHASE 2: PARSING & SEMANTIC ANALYSIS");
        Parser parser = new Parser(lexer, symbolTable);
        List<StmtNode> ast = parser.parseProgram();
        
        System.out.println("PHASE 3: TARGET CODE GENERATION");
        String javaOutput = CodeGenerator.generate(ast);
        
        // java file generation
        try (FileWriter writer = new FileWriter("GeneratedOutput.java")) {
            writer.write(javaOutput);
            System.out.println("\nSUCCESS: 'GeneratedOutput.java' has been created!");
        } catch (IOException e) {
            System.err.println("File output error: " + e.getMessage());
        }
    }
}