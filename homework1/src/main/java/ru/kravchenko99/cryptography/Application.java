package ru.kravchenko99.cryptography;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        final VigenereCipher vigenereCipher = new VigenereCipher("homework1/src/main/resources/ciphers/006.cipher");

        vigenereCipher.run();
    }
}
