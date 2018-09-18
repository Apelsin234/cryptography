package ru.kravchenko99.cryptography;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class VigenereCipher {
    private static final double[] LetterFrequency = {0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.02228, 0.02015, 0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406, 0.06749, 0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758, 0.00978, 0.0236, 0.0015, 0.01975, 0.00074};
    private static final String OUTPUT = "homework1/src/main/resources/output/answer";

    private static final double MATCH_INDEX = 0.055;

    private final String fileName;

    public VigenereCipher(final String fileName) {
        this.fileName = fileName;
    }

    private double checkMatch(final String src, final int shift) {
        final int len = src.length();
        int res = 0;
        for (int i = 0; i < len; i++) {
            if (src.charAt(i) == src.charAt((i + shift) % len)) {
                res++;
            }
        }
        return res;
    }

    private void spitToGroups(final String s, final List<StringBuilder> list, final int k) {
        for (int i = 0; i < s.length(); i++) {
            list.get(i % k).append(s.charAt(i));
        }
    }

    private int findLetter(final String s) {
        final int[] freqLet = new int[26];
        for (int i = 0; i < s.length(); i++) {
            final int ind = s.charAt(i) - 'a';
            freqLet[ind]++;
        }
        int ind = -1;
        int max = -1;
        for (int i = 0; i < freqLet.length; i++) {
            if (freqLet[i] > max) {
                ind = i;
                max = freqLet[i];
            }
        }
        return ind;
    }

    public void run() throws IOException {
        final String cipher = Files.lines(Paths.get(fileName), UTF_8).collect(Collectors.joining());
        final int cipherLen = cipher.length();
        int len = 2;

        for (; len < Math.min(20, cipherLen / 2); len++) {
            final double indexM = checkMatch(cipher, len) / cipherLen;
            System.out.println("len = " + len + " : " + indexM);
            if (indexM > MATCH_INDEX) {
                break;
            }
        }
        final List<StringBuilder> groups = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            groups.add(new StringBuilder());
        }
        spitToGroups(cipher, groups, len);
        int oftenLetter = getOftenLetter();
        final int[] distances = new int[len];
        System.out.println("oftenLetter = " + oftenLetter);
        final StringBuilder secretWord = new StringBuilder();

        for (int i = 0; i < len; i++) {
            final StringBuilder group = groups.get(i);
            final int letter = findLetter(group.toString());
            final int distance = (letter - oftenLetter + 26) % 26;
            distances[i] = distance;

            secretWord.append((char) (distance + 'a'));

            System.out.println("letter = " + letter + ", distance = " + distance);
        }

        System.out.println("secret word : " + secretWord);
        final StringBuilder result = new StringBuilder();

        for (int i = 0; i < cipherLen; i++) {
            result.append((char) ((cipher.charAt(i) - 'a' + 26 - distances[i % distances.length]) % 26 + 'a'));
        }


        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT))) {
            writer.write(result.toString());
        }


    }

    private static int getOftenLetter() {

        int ind = -1;
        double max = -1;
        for (int i = 0; i < LetterFrequency.length; i++) {
            if (LetterFrequency[i] > max) {
                ind = i;
                max = LetterFrequency[i];
            }
        }
        return ind;
    }
}
