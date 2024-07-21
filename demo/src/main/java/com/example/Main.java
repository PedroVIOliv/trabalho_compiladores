package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.example.lexer.Lexeme;
import com.example.lexer.Lexer;

public class Main {
    public static void main(String[] args) {

        try {
            // Caminho do arquivo do programa
            String filePath = "program.txt";

            // Ler o conteúdo do arquivo
            String programContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Passar o conteúdo lido para o lexer
            Lexer lexer = new Lexer(programContent);

            while (true) {
                Lexeme lexema = lexer.nextToken();
                if (lexema == null) {
                    break;
                }
                System.out.println(lexema);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}