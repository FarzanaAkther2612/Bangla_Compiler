import java.util.List;

public class CodeGenerator {
    
    public static String generate(List<StmtNode> program) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("public class GeneratedOutput {\n");
        sb.append("    public static void main(String[] args) {\n");
        
        for (StmtNode stmt : program) {
            sb.append(generateStatement(stmt, "        "));
        }
        
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }

    private static String generateStatement(StmtNode stmt, String indent) {
        if (stmt instanceof VarDeclStmtNode) {
            VarDeclStmtNode decl = (VarDeclStmtNode) stmt;
            // Map TokenType to Java primitive types
            String javaType = (decl.type == TokenType.TYPE_INT) ? "int" : "String";
            return indent + javaType + " " + decl.varName + " = " + generateExpression(decl.initializer) + ";\n";
        } 
        else if (stmt instanceof IfStmtNode) {
            IfStmtNode ifStmt = (IfStmtNode) stmt;
            StringBuilder sb = new StringBuilder();
            sb.append(indent).append("if (").append(generateExpression(ifStmt.condition)).append(" != 0) {\n");
            for (StmtNode inner : ifStmt.thenBlock) {
                sb.append(generateStatement(inner, indent + "    "));
            }
            sb.append(indent).append("}\n");
            return sb.toString();
        }

          
        else if (stmt instanceof WhileStmtNode) {
            WhileStmtNode whileStmt = (WhileStmtNode) stmt;
            StringBuilder sb = new StringBuilder();
            // In our toy language context, we check if condition != 0 for execution
            sb.append(indent).append("while (").append(generateExpression(whileStmt.condition)).append(" != 0) {\n");
            for (StmtNode inner : whileStmt.body) {
                sb.append(generateStatement(inner, indent + "    "));
            }
            sb.append(indent).append("}\n");
            return sb.toString();
        }
        return "";
    }

    private static String generateExpression(ExprNode expr) {
        if (expr instanceof NumberNode) {
            return convertNumerals(((NumberNode) expr).value);
        } else if (expr instanceof VariableNode) {
            return ((VariableNode) expr).name;
        } else if (expr instanceof BinOpNode) {
            BinOpNode binOp = (BinOpNode) expr;
            return "(" + generateExpression(binOp.left) + " " + binOp.operator + " " + generateExpression(binOp.right) + ")";
        }
        return "";
    }

    private static String convertNumerals(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c >= '০' && c <= '৯') {
                sb.append((char)(c - '০' + '0'));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
