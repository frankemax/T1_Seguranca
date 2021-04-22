import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//Max Franke
//Por mais que o relatorio apenas mostre a codificação na lingua inglesa, o algoritmo é o mesmo, por isso deve funcionar
//da mesma maneira para o portugues, claro: alterando-se os valores que se referem a lingua, bem como uma mudanca na
//estrategia para pegar a terceira letra mais comum.

public class Main {
    private static String rawText;
    private static int keySize;
    private static String key;
    private static String decryptText;
    private static double marginError = 0.01;//altere a margem aqui
    private static String fileName = "DemCifrado.txt"; //altere o texto aqui

    private static String language;
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
            try {
                freqLetter = freqChain.get(iter);
            } catch (Exception e) {
                System.out.println("Limite de tentativas excedido");
                System.exit(0);
            }
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
            //estrategia para o portugues se da em pegar apenas a 3a letra mais comum, pois a 1a e a 2a sao muito proximas as probabilidades
            freqChain = new ArrayList(Arrays.asList('o'));
            coincidenceChain = 0.072723;
            language = "pt";
        }

        if (number == 2) {
            //estrategia para o ingles eh mais simples, como eh espacado as probabilidades, vai se testando entre as 5 primeiras
            freqChain = new ArrayList(Arrays.asList('e', 't', 'a', 'o', 'i'));
            coincidenceChain = 0.065;
            language = "en";
        }
    }

    public static void readFile() throws IOException {
        //le o arquivo e armazena na variavel global String rawText
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                //sb.append(System.lineSeparator());
                line = br.readLine();
            }

            rawText = sb.toString().toLowerCase();
        }
    }

    public static void writeFile(String s) {
        //escreve o conteudo da string s no arquivo output.txt
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
        //tenta achar o tamanho da chave entre 1 e 50, com o resultado obtido do indice de coincidencia
        for (int i = 1; i < 50; i++) {
            if (coincidenceIndex(i)) {
                //System.out.println("keySize :" + i);
                keySize = i;
                break;
            }
        }
        //tratamento de erro
        if (keySize == 0) {
            System.out.println("Não foi possível encontrar o tamanho da chave, aumente o limite do incide de coincidencia");
            System.exit(0);
        }
    }

    private static void decryptMessage(String key, int keySize) {
        //decrypta a msg com a chave percorrendo toda a string
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
        //aplica a formula do ci com o controle da frequencia das letras armazenadas pelo array, se estiver entre o
        //limite designado, o indice é o que foi passado por parametro
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

            total += ic;
        }

        total = total / keySize;
        //System.out.println(keySize + " " + total);

        //margem de erro do indice é 0.01, pode precisar ser mudado
        return total < coincidenceChain + marginError && total > coincidenceChain - marginError;

    }

    public static void discoverKey(int keySize) {
        //descobre a chave dado o tamanho da chave
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < keySize; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < rawText.length(); j += keySize) {
                sb.append(rawText.charAt(j));
            }
            //acha a letra mais frequente, depois compara a distancia com a mais frequente na lingua, e por ultimo
            // aplica essa distancia para descobrir as letras da chave
            keyBuilder.append((char) (getDistance(getFreqLetter(sb.toString()), freqLetter) + 97));
        }

        key = keyBuilder.toString();
    }

    public static char getFreqLetter(String s) {
        //procura e retorna a letra mais frequente em uma string s
        int[] alphabet = new int[26];

        long max = 0;
        int letter = 0;

        for (int i = 0; i < s.length(); i++) {
            alphabet[s.charAt(i) - 97]++;
        }

        for (int j = 0; j < alphabet.length; j++) {
            if (alphabet[j] > max) {
                max = alphabet[j];
                letter = j;
            }
        }

        if (language.equals("en")) {
            return (char) (letter + 97);

        } else {
            //https://www.tutorialspoint.com/Java-program-to-find-the-3rd-largest-number-in-an-array
            int[] array = new int[26];
            System.arraycopy(alphabet, 0, array, 0, 26);
            int size = array.length;
            Arrays.sort(array);

            int first = array[size - 3];
            for (int i = 0; i < alphabet.length; i++) {
                if (first == alphabet[i]) {
                    return (char) (i + 97);
                }
            }
            return 'a';
        }
    }

    public static int getDistance(char a, char b) {
        //decobre a distancia entre duas letras, geralmente uma letra a aleatoria, com a letra b (uma das letras mais comuns da lingua)
        if (a - b < 0) {
            return a - b + 26;
        }
        return a - b;
    }

}
