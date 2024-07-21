package com.example.lexer;

public enum TokenType {
    RELOP, // "=" | ">" | ">=" | "<" | "<=" | "!="
    ADDOP, // "+" | "-" | "||"
    MULOP, // "*" | "/" | "&&"
    CONSTANT, // integer_const | float_const
    INTEGER_CONST, // digit+
    FLOAT_CONST, // digit+ "." digit+
    LITERAL, // "{"caractere*"}"
    IDENTIFIER, // (letter |“_” ) (letter | digit | “_”)*
    LETTER, // [a-zA-Z]
    DIGIT, // [0-9]
    CARACTERE, // um dos 256 caracteres do conjunto ASCII, exceto “}” e quebra de linha
    COMMA, // ,
    SEMICOLON, // ;
    OPEN_PAR, // (
    CLOSE_PAR, // )
    APP, // app
    VAR, // var
    INIT, // init
    RETURN, // return
    INTEGER, // integer
    REAL, // real
    IF, // if
    END, // end
    THEN, // then
    ELSE, // else
    REPEAT, // repeat
    UNTIL, // until
    READ, // read
    WRITE, // write
    EXCLAMATION, // !
    ASSIGNER, // :=

}