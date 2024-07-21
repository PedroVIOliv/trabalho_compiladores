package com.example.lexer;

public class Lexeme {
    private TokenType type;
    private String value;

    public Lexeme(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    @Override
    public String toString() {
        String valueToPrint = value.replace("\n", "\\n");
        return "Lexema{" +
                "tipo=" + type +
                ", valor='" + valueToPrint + '\'' +
                '}';
    }
}
