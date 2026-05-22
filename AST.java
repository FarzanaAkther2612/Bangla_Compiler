import java.util.List;


abstract class ASTNode {}


abstract class ExprNode extends ASTNode {}



class NumberNode extends ExprNode {
    String value;
    NumberNode(String value) { this.value = value; }
}


class VariableNode extends ExprNode {
    String name;
    VariableNode(String name) { this.name = name; }
}


class BinOpNode extends ExprNode {
    ExprNode left;
    String operator;
    ExprNode right;
    BinOpNode(ExprNode left, String operator, ExprNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}


abstract class StmtNode extends ASTNode {}


class VarDeclStmtNode extends StmtNode {
    TokenType type; 
    String varName;
    ExprNode initializer;
    VarDeclStmtNode(TokenType type, String varName, ExprNode initializer) {
        this.type = type;
        this.varName = varName;
        this.initializer = initializer;
    }
}


class IfStmtNode extends StmtNode {
    ExprNode condition;
    List<StmtNode> thenBlock;
    IfStmtNode(ExprNode condition, List<StmtNode> thenBlock) {
        this.condition = condition;
        this.thenBlock = thenBlock;
    }
}

class WhileStmtNode extends StmtNode {
    ExprNode condition;
    List<StmtNode> body;
    WhileStmtNode(ExprNode condition, List<StmtNode> body) {
        this.condition = condition;
        this.body = body;
    }
}
