package main;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);

        while (true){
            System.out.println("Please enter absolute path for file: /home/....");
            String inFile = in.next();
            System.out.println("Please enter constant name fo result files");
            String outFile = in.next() + "_";
            System.out.println("Please enter directory name fo result files");
            String outDir = in.next();
            System.out.println("Please enter number of result files");
            String num = in.next();

            if (!(inFile.equals(null) && outFile.equals(null) && outDir.equals(null) && num.equals(null))){
                SplitFileService.splitFile(inFile, outFile, outDir, num);
                break;
            }
        }
    }
}
