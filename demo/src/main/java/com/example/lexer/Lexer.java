package com.example.lexer;

import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private String input;
    private int position;
    private TableSymbol tableSymbol = new TableSymbol();

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    private char nextChar() {
        return input.charAt(position++);
    }

    public int getLine() {
        // retorna a linha em que esta a posicao atual
        int line = 1;
        for (int i = 0; i < position - 1; i++) {
            if (input.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    private void printError(String message) {
        System.out.println("Erro na linha " + getLine() + ": " + message);
        System.exit(1);
    }

    public Lexeme nextToken() {
        Lexeme lex = new Lexeme(null, ""); // lexema a ser retornado
        int state = 1; // estado inicial da maquina de estados
        while (true) {
            char c = '\0';
            if (position < input.length()) {
                c = nextChar(); // ler o proximo caractere
                // System.out.println("Caractere lido: " + c);
            } else {
                switch (state) {
                    case 2:
                        return new Lexeme(TokenType.RELOP, ">");
                    case 3:
                        return new Lexeme(TokenType.RELOP, "<");
                    case 4:
                        return new Lexeme(TokenType.EXCLAMATION, "!");
                    case 5:
                        printError("esperado '&' apos '&'"); // erro
                        return null;
                    case 6:
                        printError("esperado '|' apos '|'"); // erro
                        return null;
                    case 7:
                        printError("esperado '}' para fechar o literal"); // erro
                        return null;
                    case 8:
                        return new Lexeme(TokenType.INTEGER_CONST, lex.getValue());
                    case 9:
                        TokenType type = tableSymbol.getSymbolType(lex.getValue());
                        if (type != null) {
                            return new Lexeme(type, lex.getValue());
                        } else {
                            tableSymbol.addSymbol(lex.getValue(), TokenType.IDENTIFIER);
                            return new Lexeme(TokenType.IDENTIFIER, lex.getValue());
                        }
                    case 10:
                    case 11:
                        return new Lexeme(TokenType.FLOAT_CONST, lex.getValue());
                    case 12:
                        printError("esperado '=' apos ':'"); // erro
                        return null;
                    default:
                        return null;
                }
            }
            switch (state) {
                case 1:
                    if (c == '%') {
                        while (c != '\n') { // ignora comentarios
                            c = nextChar();
                            if (position >= input.length()) {
                                return null;
                            }
                        }
                    }
                    if (c == ' ' || c == '\n' || c == '\t' || (int) c == 13) { // ignora espacos em branco
                        state = 1;
                    } else if (c == '=') {
                        lex = new Lexeme(TokenType.RELOP, "=");
                        return lex;
                    } else if (c == '>') {
                        state = 2; // pode ser > ou >=
                    } else if (c == '<') {
                        state = 3; // pode ser < ou <=
                    } else if (c == '!') {
                        state = 4; // pode ser !=
                    } else if (c == ',') {
                        lex = new Lexeme(TokenType.COMMA, ",");
                        return lex;
                    } else if (c == ';') {
                        lex = new Lexeme(TokenType.SEMICOLON, ";");
                        return lex;
                    } else if (c == '(') {
                        lex = new Lexeme(TokenType.OPEN_PAR, "(");
                        return lex;
                    } else if (c == ')') {
                        lex = new Lexeme(TokenType.CLOSE_PAR, ")");
                        return lex;
                    } else if (c == '+') {
                        lex = new Lexeme(TokenType.ADDOP, "+");
                        return lex;
                    } else if (c == '-') {
                        lex = new Lexeme(TokenType.ADDOP, "-");
                        return lex;
                    } else if (c == '*') {
                        lex = new Lexeme(TokenType.MULOP, "*");
                        return lex;
                    } else if (c == '/') {
                        lex = new Lexeme(TokenType.MULOP, "/");
                        return lex;
                    } else if (c == '&') {
                        state = 5; // pode ser &&
                    } else if (c == '|') {
                        state = 6; // pode ser ||
                    } else if (c == '{') {
                        state = 7; // pode ser um literal
                    } else if (Character.isDigit(c)) {
                        state = 8; // pode ser um numero inteiro ou float
                        lex.setValue(lex.getValue() + c);
                    } else if (Character.isLetter(c) || c == '_') {
                        state = 9; // pode ser um identificador
                        lex.setValue(lex.getValue() + c);
                    } else if (c == ':') {
                        state = 12; // pode ser :=
                    } else {
                        printError("caractere invalido: " + c);
                        return null;
                    }
                    break;
                case 2:
                    if (c == '=') {
                        lex = new Lexeme(TokenType.RELOP, ">=");
                        return lex;
                    } else {
                        lex = new Lexeme(TokenType.RELOP, ">");
                        position--; // devolve o caractere lido para ser lido novamente
                        return lex;
                    }
                case 3:
                    if (c == '=') {
                        lex = new Lexeme(TokenType.RELOP, "<=");
                        return lex;
                    } else {
                        lex = new Lexeme(TokenType.RELOP, "<");
                        position--; // devolve o caractere lido para ser lido novamente
                        return lex;
                    }
                case 4:
                    if (c == '=') {
                        lex = new Lexeme(TokenType.RELOP, "!=");
                        return lex;
                    } else {
                        lex = new Lexeme(TokenType.INTEGER, "!");
                        position--; // devolve o caractere lido para ser lido novamente
                        return lex;
                    }
                case 5:
                    if (c == '&') {
                        lex = new Lexeme(TokenType.MULOP, "&&");
                        return lex;
                    } else {
                        printError("esperado '&' apos '&'");
                        return null;
                    }
                case 6:
                    if (c == '|') {
                        lex = new Lexeme(TokenType.ADDOP, "||");
                        return lex;
                    } else {
                        printError("esperado '|' apos '|'");
                        return null;
                    }
                case 7:
                    if (c == '}') {
                        lex = new Lexeme(TokenType.LITERAL, lex.getValue());
                        return lex;
                    } else if (c == '\n') {
                        printError("esperado '}' para fechar o literal");
                        return null;
                    } else {
                        lex.setValue(lex.getValue() + c);
                    }
                    break;
                case 8:
                    if (Character.isDigit(c)) {
                        lex.setValue(lex.getValue() + c);
                    } else if (c == '.') {
                        state = 10; // float ainda sem o digito apos o ponto
                        lex.setValue(lex.getValue() + c);
                    } else {
                        lex = new Lexeme(TokenType.INTEGER_CONST, lex.getValue());
                        position--; // devolve o caractere lido para ser lido novamente
                        return lex;
                    }
                    break;
                case 9:
                    if (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
                        lex.setValue(lex.getValue() + c);
                    } else {
                        TokenType type = tableSymbol.getSymbolType(lex.getValue());
                        if (type != null) {
                            lex = new Lexeme(type, lex.getValue());
                        } else {
                            tableSymbol.addSymbol(lex.getValue(), TokenType.IDENTIFIER);
                            lex = new Lexeme(TokenType.IDENTIFIER, lex.getValue());
                        }
                        position--; // devolve o caractere lido para ser lido novamente
                        return lex;
                    }
                    break;
                case 10:
                    if (Character.isDigit(c)) {
                        lex.setValue(lex.getValue() + c);
                        state = 11; // float com digito apos o ponto
                    } else {
                        printError("esperado digito apos '.'");
                        return null;
                    }
                    break;
                case 11:
                    if (Character.isDigit(c)) {
                        lex.setValue(lex.getValue() + c);
                    } else {
                        lex = new Lexeme(TokenType.FLOAT_CONST, lex.getValue());
                        position--; // devolve o caractere lido para ser lido novamente
                        return lex;
                    }
                    break;
                case 12:
                    if (c == '=') {
                        lex = new Lexeme(TokenType.ASSIGNER, ":=");
                        return lex;
                    } else {
                        printError("esperado '=' apos ':'");
                        return null;
                    }
                default:
                    break;
            }

        }
    }

    public TableSymbol getTableSymbol() {
        return tableSymbol;
    }
}
