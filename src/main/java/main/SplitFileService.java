package main;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SplitFileService {

    public static void splitFile(String nameInputFile, String constantPartOfFile,
                                 String outputDirectoryName, String numFiles) throws IOException {

        // For debug with configuration file
        /*
    public static void splitFile() throws IOException {

        String nameInputFile = null;
        String constantPartOfFile = null;
        String outputDirectoryName = null;
        String numFiles = null;

        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("configSplitter.properties")) {
            properties.load(input);

            nameInputFile = properties.getProperty("nameInputFile");
            constantPartOfFile = properties.getProperty("constantPartOfFile");
            outputDirectoryName = properties.getProperty("outputDirectoryName");
            numFiles = properties.getProperty("numFiles");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assert numFiles != null;
        // Проверка типа в java
        // System.out.println(numFiles.getClass().getName());
        */

        long numSplits = Long.parseLong(numFiles);

        Splitter splitterLog = new Splitter(nameInputFile, constantPartOfFile, outputDirectoryName, numSplits);
        if (splitterLog.makeDirectoryAndPathForOutFiles()){
            splitterLog.splitAndWriteNewFiles();
        } else {
            System.out.println("Program was stoped. Directory was not created. Please delete old \"result\" directory or enter correct data.");
        }
    }
}
