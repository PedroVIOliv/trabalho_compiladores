package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import com.example.lexical.Lexeme;
import com.example.lexical.Lexer;

public class Main {
    public static void main(String[] args) {

        try {
            // Caminho do arquivo do programa
            int program_number = 1; // Número do programa padrão
            try ( // Ler o número do programa a partir da entrada do usuário
                    Scanner scanner = new Scanner(System.in)) {
                System.out.print("Digite o número do programa (1-10): ");
                program_number = scanner.nextInt();
            }
            // Validar o número do programa
            if (program_number < 1 || program_number > 10) {
                System.out.println("Número de programa inválido.");
                return;
            }

            String filePath = "program_" + program_number + ".txt";

            // Ler o conteúdo do arquivo
            String programContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Passar o conteúdo lido para o lexer
            Lexer lexer = new Lexer(programContent);

            System.out.println("--- LEITURA DE TOKENS ---");

            while (true) {
                Lexeme lexema = lexer.nextToken();
                if (lexema == null) {
                    break;
                }
                System.out.println(lexema);

            }
            System.out.println("\n--- TABELA DE SÍMBOLOS ---");
            System.out.println(lexer.getTableSymbol());
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

    }
}