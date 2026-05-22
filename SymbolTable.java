import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private final Map<String, TokenType> table = new HashMap<>();

    public void declare(String name, TokenType type) {
        if (table.containsKey(name)) {
            throw new RuntimeException("Error: Variable " + name + " already declared.");
        }
        table.put(name, type);
    }

    public TokenType lookup(String name) {
        if (!table.containsKey(name)) {
            throw new RuntimeException("Error: Variable " + name + " is not defined.");
        }
        return table.get(name);
    }
}