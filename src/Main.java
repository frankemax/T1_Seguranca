import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//Max Franke

public class Main {
    private static String rawText;
    private static int keySize;
    private static String key;
    private static String decryptText;

    private static double coincidenceChain;
    private static char freqLetter;
    private static List<Character> freqChain;
    private static int iter = 0;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int numero;

        System.out.println("""
                    ___                           _            \s
                   /   \\___  ___ _ __ _   _ _ __ | |_ ___  _ __\s
                  / /\\ / _ \\/ __| '__| | | | '_ \\| __/ _ \\| '__|
                 / /_//  __/ (__| |  | |_| | |_) | || (_) | |  \s
                /___,' \\___|\\___|_|   \\__, | .__/ \\__\\___/|_|  \s
                                      |___/|_|                  \s""");

        System.out.println("==========================================================================");
        System.out.println("O texto esta em qual linguagem? (1 = Portugues / 2 = Ingles) ");
        setLanguageConfigs(scanner.nextInt());


        System.out.println("Reading encrypted file...");
        readFile();
        System.out.println("File read ! File size: " + rawText.length());
        System.out.println("==========================================================================");

        questioning:
        while (true) {
            freqLetter = freqChain.get(iter);
            System.out.println("Discovering key size...");
            searchKeySize();
            System.out.println("Key Size found! : " + keySize);
            System.out.println("==========================================================================");

            System.out.println("Discovering key...");
            discoverKey(keySize);
            System.out.println("Key found! : " + key);
            System.out.println("==========================================================================");

            System.out.println("Starting decryption... ");
            decryptMessage(key, keySize);
            System.out.println("Message decrypted successfully!");
            System.out.println("==========================================================================");

            System.out.println("Seems right?: (1 = Y / 2 = N) ");
            System.out.println(decryptText.substring(0, 50));
            numero = scanner.nextInt();

            if (numero == 1) {
                System.out.println("Generating report...");
                writeFile(decryptText);
                System.out.println("Report generated successfully !! See output.txt");
                break questioning;
            }

            System.out.println("==========================================================================");
            System.out.println("Trying again...");

            iter++;
        }


    }

    public static void setLanguageConfigs(int number) {
        if (number == 1) {
            freqChain = new ArrayList(Arrays.asList('a', 'e', 'o', 's', 'i'));
            coincidenceChain = 0.072723;
        }

        if (number == 2) {
            freqChain = new ArrayList(Arrays.asList('e', 't', 'a', 'o', 'i'));
            coincidenceChain = 0.065;
        }
    }

    public static void readFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("cypherTexts/DemCifrado.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                //sb.append(System.lineSeparator());
                line = br.readLine();
            }
            rawText = sb.toString();
        }
    }

    public static void writeFile(String s) {
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(s);
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void searchKeySize() {
        for (int i = 1; i < 20; i++) {
            if (coincidenceIndex(i)) {
                //System.out.println("keySize :" + i);
                keySize = i;
                break;
            }
        }
    }

    private static void decryptMessage(String key, int keySize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rawText.length(); i++) {
            if ((rawText.charAt(i) - key.charAt(i % keySize) + 97) < 97) {
                sb.append((char) (rawText.charAt(i) - key.charAt(i % keySize) + 97 + 26));
            } else {
                sb.append((char) (rawText.charAt(i) - key.charAt(i % keySize) + 97));
            }
        }
        //System.out.println("message: " + sb.toString());
        decryptText = sb.toString();

    }

    public static boolean coincidenceIndex(int keySize) {
        double total = 0;
        for (int i = 0; i < keySize; i++) {
            long n = 0;
            double sum = 0.0;
            int[] alphabet = new int[26];

            for (int j = i; j < rawText.length(); j += keySize) {
                alphabet[rawText.charAt(j) - 97]++;
                n++;
            }

            for (int j = 0; j < 26; j++) {
                sum = sum + (alphabet[j] * (alphabet[j] - 1));
            }

            double ic = sum / (n * (n - 1));

            //System.out.println(total);
            total += ic;
        }

        total = total / keySize;
        //System.out.println(keySize + " " + total);
        return total < coincidenceChain + 0.005 && total > coincidenceChain - 0.005;

    }

    public static void discoverKey(int keySize) {
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < keySize; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < rawText.length(); j += keySize) {
                sb.append(rawText.charAt(j));
            }

            keyBuilder.append((char) (getDistance(getFreqLetter(sb.toString()), freqLetter) + 97));
            //System.out.println("distance: " + (char)(getDistance(getFreqLetter(sb.toString()), freqLetter)+97));
        }


        //System.out.println("key: " + keyBuilder.toString());


        key = keyBuilder.toString();
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
        }

        return (char) letter;

    }

    public static int getDistance(char a, char b) {
        if (a - b < 0) {
            return a - b + 26;
        }
        return a - b;
    }

}
