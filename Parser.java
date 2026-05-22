import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;
    private final SymbolTable symbolTable;
    private Token currentToken;

    public Parser(Lexer lexer, SymbolTable symbolTable) {
        this.lexer = lexer;
        this.symbolTable = symbolTable;
        this.currentToken = lexer.nextToken();
    }

    private void consume(TokenType type) {
        if (currentToken.type == type) {
            currentToken = lexer.nextToken();
        } else {
            System.err.println("Syntax Error: Expected " + type + " but found " + currentToken.type + " ('" + currentToken.lexeme + "')");
            recover();
        }
    }
    private void recover() {
        while (currentToken.type != TokenType.SEMICOLON && currentToken.type != TokenType.EOF) {
            currentToken = lexer.nextToken();
        }
        if (currentToken.type == TokenType.SEMICOLON) {
            currentToken = lexer.nextToken(); 
        }
    }
    public List<StmtNode> parseProgram() {
        List<StmtNode> statements = new ArrayList<>();
        while (currentToken.type != TokenType.EOF) {
            try {
                StmtNode stmt = parseStatement();
                if (stmt != null) statements.add(stmt);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                recover();
            }
        }
        return statements;
    }

    private StmtNode parseStatement() {
        if (currentToken.type == TokenType.TYPE_INT || currentToken.type == TokenType.TYPE_STRING) {
            return parseDeclaration();
        } else if (currentToken.type == TokenType.IF) {
            return parseIfStatement();
        } 
        else if (currentToken.type == TokenType.WHILE) { 
            return parseWhileStatement();
        }
        throw new RuntimeException("Syntax Error: Unexpected statement starter '" + currentToken.lexeme + "'");
    }

    private StmtNode parseDeclaration() {
        TokenType type = currentToken.type; 
        consume(type);

        String varName = currentToken.lexeme;
        consume(TokenType.IDENTIFIER);

        consume(TokenType.ASSIGN); 
        
        ExprNode expr = parseExpression();

        if (type == TokenType.TYPE_INT && !(expr instanceof NumberNode || expr instanceof BinOpNode || expr instanceof VariableNode)) {
            throw new RuntimeException("Semantic Error: Type mismatch. Cannot assign non-integer to variable '" + varName + "'");
        }
        
        consume(TokenType.SEMICOLON); 

        symbolTable.declare(varName, type); 
        
        return new VarDeclStmtNode(type, varName, expr);
    }

    private StmtNode parseIfStatement() {
        consume(TokenType.IF);
        consume(TokenType.LPAREN);
        ExprNode condition = parseExpression();
        consume(TokenType.RPAREN);
        
        consume(TokenType.LBRAC);
        List<StmtNode> body = new ArrayList<>();
        while (currentToken.type != TokenType.RBRAC && currentToken.type != TokenType.EOF) {
            body.add(parseStatement());
        }
        consume(TokenType.RBRAC);
        
        return new IfStmtNode(condition, body);
    }

    private StmtNode parseWhileStatement() {
        consume(TokenType.WHILE);
        consume(TokenType.LPAREN);
        ExprNode condition = parseExpression();
        consume(TokenType.RPAREN);
        
        consume(TokenType.LBRAC);
        List<StmtNode> body = new ArrayList<>();
        while (currentToken.type != TokenType.RBRAC && currentToken.type != TokenType.EOF) {
            body.add(parseStatement());
        }
        consume(TokenType.RBRAC);
        
        return new WhileStmtNode(condition, body);
    }

    private ExprNode parseExpression() {
        ExprNode left = parsePrimary();
        while (currentToken.type == TokenType.PLUS || currentToken.type == TokenType.MINUS) {
            String op = currentToken.lexeme;
            consume(currentToken.type);
            ExprNode right = parsePrimary();
            left = new BinOpNode(left, op, right);
        }
        return left;
    }

    private ExprNode parsePrimary() {
        if (currentToken.type == TokenType.NUMBER) {
            String val = currentToken.lexeme;
            consume(TokenType.NUMBER);
            return new NumberNode(val);
        } else if (currentToken.type == TokenType.IDENTIFIER) {
            String name = currentToken.lexeme;
    
            symbolTable.lookup(name); 
            consume(TokenType.IDENTIFIER);
            return new VariableNode(name);
        }
        throw new RuntimeException("Syntax Error: Unexpected token in expression '" + currentToken.lexeme + "'");
    }
}