import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static String rawText;
    private static double coincidenceChain = 0.065;
    private static char freqLetter = 'e';
    private static String key = "";

    public static void main(String[] args) throws IOException {
        readFile();
        searchKeySize();

    }

    public static void readFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("message.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            rawText = sb.toString();
        }
    }

    public static void searchKeySize() {
        for (int i = 1; i < 20; i++) {
            if (ic(i)) {
                System.out.println("keySize :" + i);
                key = discoverKey(i);
                decryptMessage(key, i);

                break;
            }
        }
    }

    private static void decryptMessage(String key, int keySize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rawText.length() - 2; i++) {
            if ((rawText.charAt(i) - key.charAt(i % keySize) + 97) < 97) {
                sb.append((char) (rawText.charAt(i) - key.charAt(i % keySize) + 97 + 26));
            } else {
                sb.append((char) (rawText.charAt(i) - key.charAt(i % keySize) + 97));
            }
        }
        System.out.println("message: " + sb.toString());
    }

    public static boolean ic(int keySize) {
        double total = 0;
        for (int i = 0; i < keySize; i++) {
            long n = 0;
            double sum = 0.0;
            int[] alphabet = new int[26];

            for (int j = i; j < rawText.length() - 2; j += keySize) {
                alphabet[rawText.charAt(j) - 97]++;
                n++;
            }

            for (int j = 0; j < 26; j++) {
                sum = sum + (alphabet[j] * (alphabet[j] - 1));
            }

            double ic = sum / (n * (n - 1));

            total += ic;
        }

        total = total / keySize;
        return total < coincidenceChain + 0.003 && total > coincidenceChain - 0.003;

    }

    public static String discoverKey(int keySize) {
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < keySize; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < rawText.length() - 2; j += keySize) {
                sb.append(rawText.charAt(j));
            }

            keyBuilder.append((char) (getDistance(getFreqLetter(sb.toString()), freqLetter) + 97));
            //System.out.println("distance: " + (char)(getDistance(getFreqLetter(sb.toString()), freqLetter)+97));
        }


        System.out.println("key: " + keyBuilder.toString());


        return keyBuilder.toString();
    }

    public static char getFreqLetter(String s) {
        //System.out.println("s: " + s);
        int[] alphabet = new int[26];
        long max = 0;
        int letter = 0;

        for (int i = 0; i < s.length(); i++) {
            alphabet[s.charAt(i) - 97]++;
        }

        for (int j = 0; j < alphabet.length; j++) {
            if (alphabet[j] > max) {
                max = alphabet[j];
                letter = j + 97;
            }
            //System.out.println("letter: " + letter);
            //System.out.println((char)(j+97) + " " + alphabet[j]);
        }

        //System.out.println((char) letter);

        return (char) letter;

    }

    public static int getDistance(char a, char b) {
        if (a - b < 0) {
            return a - b + 26;
        }
        return a - b;
    }


}
