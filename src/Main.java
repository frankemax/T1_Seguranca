import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static String rawText;
    private static double coincidenceChain = 0.065;
    private static double randomChain = 0.038;

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


                break;
            }
        }


    }

    public static boolean ic(int keySize) {


        for (int i = 0; i < keySize; i++) {

            int letter;
            long n = 0;
            double sum = 0.0;
            int[] alphabet = new int[26];

            for (int j = i; j < rawText.length() - 2; j = j + keySize) {

                letter = rawText.charAt(j) - 97;
                //System.out.println(">>>" + rawText.length());
                //System.out.println(rawText.charAt(j));
                alphabet[letter]++;
                n++;
            }

            for (int j = 0; j < 26; j++) { // IC calculation based on counted frequencies
                sum = sum + (alphabet[j] * (alphabet[j] - 1));
            }

            double ic = sum / (n * (n - 1)); // Formula of N(N-1) as seen in class
            //System.out.println("ic: " + ic);

            if (ic < coincidenceChain + 0.003 && ic > coincidenceChain - 0.003)
                return true;


        }

        return false;

    }


}
