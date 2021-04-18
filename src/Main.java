import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static String rawText;
    private static double coincidenceChain = 0.065;
    private static char freqLetter = 'e';

    public static void main(String[] args) throws IOException {
        readFile();
        searchKeySize();


        //System.out.println(rawText);
    }

    public static void readFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("20201-teste1.txt"))) {
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


        for (int i = 1; i < 10; i++) {
            if (ic(i)) {
                System.out.println("keySize :" + i);
                decript(i);

                break;
            }
        }


    }

    public static boolean ic(int keySize) {
        for (int i = 0; i < keySize; i++) {
            long n = 0;
            double sum = 0.0;
            int[] alphabet = new int[26];

            for (int j = i; j < rawText.length() - 2; j = j + keySize) {
                //System.out.println(">>>" + rawText.length());
                //System.out.println(rawText.charAt(j));

                alphabet[rawText.charAt(j) - 97]++;
                n++;
            }

            for (int j = 0; j < 26; j++) {
                sum = sum + (alphabet[j] * (alphabet[j] - 1));
            }

            double ic = sum / (n * (n - 1));
            //System.out.println("ic: " + ic);

            if (ic < coincidenceChain + 0.003 && ic > coincidenceChain - 0.003)
                return true;
        }

        return false;

    }

    public static String decript(int keySize) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rawText.length() - 2; i = i + keySize) {
            sb.append(rawText.charAt(i));

        }
        getDistance(getFreqLetter(sb.toString()), freqLetter);

        return "";
    }

    public static char getFreqLetter(String s) {
        int[] alphabet = new int[26];
        long n = 0;
        long max = 0;
        int letter = 0;


        for (int i = 0; i < s.length(); i++) {
            alphabet[rawText.charAt(i) - 97]++;
            n++;
        }
        for (int j = 0; j < alphabet.length; j++) {
            if (alphabet[j] > max) {
                max = alphabet[j];
                letter = j + 97;
            }
        }
        System.out.println((char) letter);

        return (char) letter;

    }

    public static int getDistance(char a, char b) {
        System.out.println(Math.abs(a - b));
        return Math.abs(a - b);

    }


}
