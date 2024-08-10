package com.example.syntatic;

import com.example.lexical.Lexeme;
import com.example.lexical.Lexer;
import com.example.lexical.TokenType;

public class Syntaxer {
    private String input;
    private int position;
    private int line;
    private Lexer lexer;
    private Lexeme currentLexeme;
    private TokenType currentToken;

    public Syntaxer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 0;
        this.lexer = new Lexer(input);
        this.currentLexeme = null;
        this.currentToken = null;
    }

    public void run() {
        nextToken();
        completeSyntax();
        System.out.println("Sintaxe correta!");
    }

    private void nextToken() {
        currentLexeme = lexer.nextToken();
        if (currentLexeme != null) {
            currentToken = currentLexeme.getType();
            System.out.println(currentToken);
            line = lexer.getLine();
        } else {
            currentToken = null;
        }
    }

    private void eat(TokenType token) {
        if (currentToken == token) {
            nextToken();
        } else {
            System.err.println(
                    "Erro de sintaxe na linha " + line + ": esperado " + token + ", encontrado " + currentToken);
            System.exit(1);
        }
    }

    private void completeSyntax() {
        program();
        if (currentToken != null) {
            System.err.println("Erro de sintaxe na linha " + line + ": esperado EOF, encontrado " + currentToken);
            System.exit(1);
        }

    }

    private void program() {
        System.out.println("program");
        switch (currentToken) {
            case APP:
                eat(TokenType.APP);
                eat(TokenType.IDENTIFIER);
                body();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line + ": esperado APP, encontrado " + currentToken);
                System.exit(1);
        }

    }

    private void body() {
        System.out.println("body");
        switch (currentToken) {
            case INIT:
            case VAR:
                optional_declaration_list();
                eat(TokenType.INIT);
                statement_list();
                eat(TokenType.RETURN);
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado INIT ou VAR, encontrado " + currentToken);
                System.exit(1);

        }
    }

    private void optional_declaration_list() {
        System.out.println("optional_declaration_list");
        switch (currentToken) {
            case VAR:
                eat(TokenType.VAR);
                declaration_list();
                break;
            case INIT:
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado VAR ou INIT, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void declaration_list() {
        System.out.println("declaration_list");
        switch (currentToken) {
            case INTEGER:
            case REAL:
                declaration();
                declaration_list_tail();
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado INTEGER ou REAL, encontrado " + currentToken);
                System.exit(1);

        }
    }

    private void declaration_list_tail() {
        System.out.println("declaration_list_tail");
        switch (currentToken) {
            case INIT:
                break;
            case SEMICOLON:
                eat(TokenType.SEMICOLON);
                declaration();
                declaration_list_tail();
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado INIT ou SEMICOLON, encontrado "
                                + currentToken);
                System.exit(1);
        }
    }

    private void declaration() {
        System.out.println("declaration");
        switch (currentToken) {
            case INTEGER:
            case REAL:
                type();
                identifier_list();
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado INTEGER ou REAL, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void identifier_list() {
        System.out.println("identifier_list");
        switch (currentToken) {
            case IDENTIFIER:
                eat(TokenType.IDENTIFIER);
                identifier_list_tail();
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado IDENTIFIER, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void identifier_list_tail() {
        System.out.println("identifier_list_tail");
        switch (currentToken) {
            case INIT:
            case SEMICOLON:
                break;
            case COMMA:
                eat(TokenType.COMMA);
                eat(TokenType.IDENTIFIER);
                identifier_list_tail();
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado INIT, SEMICOLON ou COMMA, encontrado "
                                + currentToken);
                System.exit(1);
        }
    }

    private void type() {
        System.out.println("type");
        switch (currentToken) {
            case INTEGER:
                eat(TokenType.INTEGER);
                break;
            case REAL:
                eat(TokenType.REAL);
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado INTEGER ou REAL, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void statement_list() {
        System.out.println("statement_list");
        switch (currentToken) {
            case IDENTIFIER:
            case IF:
            case REPEAT:
            case READ:
            case WRITE:
                statement();
                statement_list_tail();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado IDENTIFIER, IF, REPEAT, READ ou WRITE, encontrado " + currentToken);
                System.exit(1);

        }
    }

    private void statement_list_tail() {
        System.out.println("statement_list_tail");
        switch (currentToken) {
            case RETURN:
            case END:
            case ELSE:
            case UNTIL:
                break;
            case SEMICOLON:
                eat(TokenType.SEMICOLON);
                statement();
                statement_list_tail();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado RETURN, END, ELSE, UNTIL ou SEMICOLON, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void statement() {
        switch (currentToken) {
            case IDENTIFIER:
                assignment_statement();
                break;
            case IF:
                if_statement();
                break;
            case REPEAT:
                repeat_statement();
                break;
            case READ:
                read_statement();
                break;
            case WRITE:
                write_statement();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado IDENTIFIER, IF, REPEAT, READ ou WRITE, encontrado " + currentToken);
                System.exit(1);
        }

    }

    private void assignment_statement() {
        System.out.println("assignment_statement");
        switch (currentToken) {
            case IDENTIFIER:
                eat(TokenType.IDENTIFIER);
                eat(TokenType.ASSIGNER);
                simple_expression();
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado IDENTIFIER, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void if_statement() {
        System.out.println("if_statement");
        switch (currentToken) {
            case IF:
                eat(TokenType.IF);
                condition();
                eat(TokenType.THEN);
                statement_list();
                if_statement_tail();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line + ": esperado IF, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void if_statement_tail() {
        System.out.println("if_statement_tail");
        switch (currentToken) {
            case ELSE:
                eat(TokenType.ELSE);
                statement_list();
                eat(TokenType.END);
                break;
            case END:
                eat(TokenType.END);
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado ELSE ou END, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void repeat_statement() {
        System.out.println("repeat_statement");
        switch (currentToken) {
            case REPEAT:
                eat(TokenType.REPEAT);
                statement_list();
                statement_suffix();
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado REPEAT, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void statement_suffix() {
        System.out.println("statement_suffix");
        switch (currentToken) {
            case UNTIL:
                eat(TokenType.UNTIL);
                condition();
                break;
            default:
                System.err.println(
                        "Erro de sintaxe na linha " + line + ": esperado UNTIL, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void read_statement() {
        System.out.println("read_statement");
        switch (currentToken) {
            case READ:
                eat(TokenType.READ);
                eat(TokenType.OPEN_PAR);
                eat(TokenType.IDENTIFIER);
                eat(TokenType.CLOSE_PAR);
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line + ": esperado READ, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void write_statement() {
        System.out.println("write_statement");
        switch (currentToken) {
            case WRITE:
                eat(TokenType.WRITE);
                eat(TokenType.OPEN_PAR);
                writable();
                eat(TokenType.CLOSE_PAR);
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line + ": esperado WRITE, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void writable() {
        System.out.println("writable");
        switch (currentToken) {
            case LITERAL:
                eat(TokenType.LITERAL);
                break;
            case IDENTIFIER:
            case OPEN_PAR:
            case EXCLAMATION:
            case SUBOP:
            case CONSTANT:
                simple_expression();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado LITERAL, IDENTIFIER, OPEN_PAR, EXCLAMATION, SUBOP ou CONSTANT, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void condition() {
        System.out.println("condition");
        switch (currentToken) {
            case SUBOP:
            case EXCLAMATION:
            case OPEN_PAR:
            case IDENTIFIER:
            case CONSTANT:
                expression();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado SUBOP, EXCLAMATION, OPEN_PAR, IDENTIFIER ou CONSTANT, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void expression() {
        System.out.println("expression");
        switch (currentToken) {
            case IDENTIFIER:
            case OPEN_PAR:
            case EXCLAMATION:
            case SUBOP:
            case CONSTANT:
                simple_expression();
                expression_tail();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado IDENTIFIER, OPEN_PAR, EXCLAMATION, SUBOP ou CONSTANT, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void expression_tail() {
        System.out.println("expression_tail");
        switch (currentToken) {
            case RETURN:
            case SEMICOLON:
            case THEN:
            case END:
            case ELSE:
            case UNTIL:
            case CLOSE_PAR:
                break;
            case RELOP:
                eat(TokenType.RELOP);
                simple_expression();
                expression_tail();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado RETURN, SEMICOLON, THEN, END, ELSE, UNTIL, CLOSE_PAR ou RELOP, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void simple_expression() {
        System.out.println("simple_expression");
        switch (currentToken) {
            case IDENTIFIER:
            case OPEN_PAR:
            case EXCLAMATION:
            case SUBOP:
            case CONSTANT:
                term();
                simple_expression_tail();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado IDENTIFIER, OPEN_PAR, EXCLAMATION, SUBOP ou CONSTANT, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void simple_expression_tail() {
        System.out.println("simple_expression_tail");
        switch (currentToken) {
            case RETURN:
            case SEMICOLON:
            case THEN:
            case END:
            case ELSE:
            case UNTIL:
            case CLOSE_PAR:
            case RELOP:
                break;
            case ADDOP:
            case SUBOP:
                addop_or_subop();
                term();
                simple_expression_tail();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado RETURN, SEMICOLON, THEN, END, ELSE, UNTIL, CLOSE_PAR, RELOP, ADDOP ou SUBOP, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void term() {
        switch (currentToken) {
            case IDENTIFIER:
            case OPEN_PAR:
            case EXCLAMATION:
            case SUBOP:
            case CONSTANT:
                factor_a();
                term_tail();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado IDENTIFIER, OPEN_PAR, EXCLAMATION, SUBOP ou CONSTANT, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void term_tail() {
        switch (currentToken) {
            case RETURN:
            case SEMICOLON:
            case THEN:
            case END:
            case ELSE:
            case UNTIL:
            case CLOSE_PAR:
            case RELOP:
            case ADDOP:
            case SUBOP:
                break;
            case MULOP:
                eat(TokenType.MULOP);
                factor_a();
                term_tail();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado RETURN, SEMICOLON, THEN, END, ELSE, UNTIL, CLOSE_PAR, RELOP, ADDOP, SUBOP ou MULOP, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void factor_a() {
        switch (currentToken) {
            case IDENTIFIER:
            case OPEN_PAR:
            case EXCLAMATION:
            case SUBOP:
            case CONSTANT:
                factor_a_prefix();
                factor();
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado IDENTIFIER, OPEN_PAR, EXCLAMATION, SUBOP ou CONSTANT, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void factor_a_prefix() {
        switch (currentToken) {
            case OPEN_PAR:
            case CONSTANT:
            case IDENTIFIER:
                break;
            case EXCLAMATION:
                eat(TokenType.EXCLAMATION);
                break;
            case SUBOP:
                eat(TokenType.SUBOP);
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado OPEN_PAR, CONSTANT, IDENTIFIER, EXCLAMATION ou SUBOP, encontrado "
                        + currentToken);
                System.exit(1);
        }
    }

    private void factor() {
        switch (currentToken) {
            case IDENTIFIER:
                eat(TokenType.IDENTIFIER);
                break;
            case OPEN_PAR:
                eat(TokenType.OPEN_PAR);
                expression();
                eat(TokenType.CLOSE_PAR);
                break;
            case CONSTANT:
                eat(TokenType.CONSTANT);
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado IDENTIFIER, OPEN_PAR ou CONSTANT, encontrado " + currentToken);
                System.exit(1);
        }
    }

    private void addop_or_subop() {
        switch (currentToken) {
            case ADDOP:
                eat(TokenType.ADDOP);
                break;
            case SUBOP:
                eat(TokenType.SUBOP);
                break;
            default:
                System.err.println("Erro de sintaxe na linha " + line
                        + ": esperado ADDOP ou SUBOP, encontrado " + currentToken);
                System.exit(1);
        }
    }

}
