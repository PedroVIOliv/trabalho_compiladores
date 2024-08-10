package com.example.lexical;

import java.util.HashMap;
import java.util.Map;

public class TableSymbol {
    private Map<String, TokenType> table;

    public TableSymbol() {
        table = new HashMap<>();
        table.put("=", TokenType.RELOP);
        table.put(">", TokenType.RELOP);
        table.put(">=", TokenType.RELOP);
        table.put("<", TokenType.RELOP);
        table.put("<=", TokenType.RELOP);
        table.put("!=", TokenType.RELOP);
        table.put("+", TokenType.ADDOP);
        table.put("-", TokenType.SUBOP);
        table.put("||", TokenType.ADDOP);
        table.put("*", TokenType.MULOP);
        table.put("/", TokenType.MULOP);
        table.put("&&", TokenType.MULOP);
        table.put(",", TokenType.COMMA);
        table.put(";", TokenType.SEMICOLON);
        table.put("(", TokenType.OPEN_PAR);
        table.put(")", TokenType.CLOSE_PAR);
        table.put(":=", TokenType.ASSIGNER);
        table.put("app", TokenType.APP);
        table.put("var", TokenType.VAR);
        table.put("init", TokenType.INIT);
        table.put("return", TokenType.RETURN);
        table.put("integer", TokenType.INTEGER);
        table.put("real", TokenType.REAL);
        table.put("if", TokenType.IF);
        table.put("end", TokenType.END);
        table.put("then", TokenType.THEN);
        table.put("else", TokenType.ELSE);
        table.put("repeat", TokenType.REPEAT);
        table.put("until", TokenType.UNTIL);
        table.put("read", TokenType.READ);
        table.put("write", TokenType.WRITE);
        table.put("!", TokenType.EXCLAMATION);

    }

    private TokenType get(String symbol) {
        return table.get(symbol);
    }

    private boolean contains(String symbol) {
        return table.containsKey(symbol);
    }

    public TokenType getSymbolType(String symbol) {
        if (contains(symbol)) {
            return get(symbol);
        }
        return null;
    }

    public void addSymbol(String value, TokenType identifier) {
        table.put(value, identifier);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, TokenType> entry : table.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

}
