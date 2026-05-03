import java.util.*;

enum TokenType {
    TYPE_INT, 
    TYPE_STRING, 
    IF, 
    ELSE, 
    IDENTIFIER, 
    NUMBER, 
    STRING_LIT, 
    ASSIGN, 
    PLUS, 
    MINUS, 
    LPAREN, 
    RPAREN, 
    LBRAC, 
    RBRAC,
    SEMICOLON, 
    EOF,
}

public class Lexer {
     final String source;
     final Map<String, TokenType> keywords;
     int p = 0;

    public Lexer(String source) {
        this.source = source;
        this.keywords = new HashMap<>();
        keywords.put("সংখ্যা", TokenType.TYPE_INT);
        keywords.put("বাক্য", TokenType.TYPE_STRING);
        keywords.put("যদি", TokenType.IF);
        keywords.put("নাহলে", TokenType.ELSE);
    }

    public Token nextToken() {
        skipWhitespace();
        if (p >= source.length()) return new Token(TokenType.EOF, "");

        char current = source.charAt(p);

        if (Character.isDigit(current)) {
            return readNumber();
        }

        if (isBanglaLetter(current)) {
            return readIdentifier();
        }

        if (current == '=') { p++; return new Token(TokenType.ASSIGN, "="); }
        if (current == '+') { p++; return new Token(TokenType.PLUS, "+"); }
        if (current == '-') { p++; return new Token(TokenType.MINUS, "-"); }
        if (current == '(') { p++; return new Token(TokenType.LPAREN, "("); }
        if (current == ')') { p++; return new Token(TokenType.RPAREN, ")"); }
        if (current == '{') { p++; return new Token(TokenType.LBRAC, "{"); }
        if (current == '}') { p++; return new Token(TokenType.RBRAC, "}"); }
        if (current == ';') { p++; return new Token(TokenType.SEMICOLON, ";"); }

        throw new RuntimeException("Unknown character: " + current);
    }

    private void skipWhitespace() {
        while (p < source.length() && Character.isWhitespace(source.charAt(p))) {
            p++;
        }
    }

    private Token readNumber() {
        StringBuilder sb = new StringBuilder();
        while (p < source.length() && Character.isDigit(source.charAt(p))) {
            sb.append(source.charAt(p));
            p++;
        }
        return new Token(TokenType.NUMBER, sb.toString());
    }

    private Token readIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (p < source.length() && (isBanglaLetter(source.charAt(p)) || Character.isDigit(source.charAt(p)))) {
            sb.append(source.charAt(p));
            p++;
        }
        String text = sb.toString();
        TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
        return new Token(type, text);
    }

    private boolean isBanglaLetter(char c) {
        return c >= '\u0980' && c <= '\u09FF';
    }

    // --- TEST MAIN METHOD ---
    public static void main(String[] args) {
        // A sample program in your new language
        String code = "সংখ্যা ক = ১০; যদি (ক) { সংখ্যা খ = ২০; }"; 
        
        Lexer lexer = new Lexer(code);
        Token token;
        System.out.println("Tokenizing Bangla Source Code...");
        do {
            token = lexer.nextToken();
            System.out.println(token);
        } while (token.type != TokenType.EOF);
    }
}


class Token {
    final TokenType type;
    final String lexeme;

    Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "Token(" + type + ", '" + lexeme + "')";
    }
}