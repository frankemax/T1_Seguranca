import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static String rawText;

    public static void main(String[] args) throws IOException {
        readFile();
        searchKeySize();


        //System.out.println(rawText);
    }

    public static void readFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("20201-teste1.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            rawText = sb.toString();
        } finally {
            br.close();
        }
    }

    public static void searchKeySize() {
        double coincidenceChain = 0.065;
        double randomChain = 0.038;

        for (int i = 0; i < 10; i++) {
            System.out.println("keySize :" + i);
            ic(i);

        }


    }

    public static double ic(int keySize) {
        long n = 0;
        double sum = 0.0;
        double ic = 0.0;
        int[] alphabet = new int[26];


        int letter = 0;

        int counter = 0;
        for (int i = 0; i < keySize; i++) {
            for (int j = i; j < 20; j = j + keySize) {
                System.out.println(j);
            }
        }
        /*for(int i = 0; i < s.length(); i++){
            letter = s.charAt(i) - 97;
            if(letter >= 0 && letter < 26){
                alphabet[letter]++;
                n++;
            }
        }*/

        for (int j = 0; j < 26; j++) { // IC calculation based on counted frequencies
            sum = sum + (alphabet[j] * (alphabet[j] - 1));
        }

        ic = sum / (n * (n - 1)); // Formula of N(N-1) as seen in class

        System.out.println(ic);
        return ic;
    }

}
