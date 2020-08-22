package com.hbf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Utils {



    public static void readDocument(String filepath,FileReader reader) throws IOException {

        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(filepath);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                reader.processLine(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }

    }


    public static List<Document> readFolder(File folderPath,List<Document> documents) {

       // System.out.println(String.format("reading all documents inside folder %s", folderPath));

        File[] fileNames = folderPath.listFiles();
        for(File file : fileNames){

            if(!file.getName().contains(".DS_Store") && !file.getName().contains(".Ds_Store")) {

                if (file.isDirectory()) {
                    readFolder(file, documents);
                } else {
                    try {
                        documents.add(new Document(file.getPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return documents;
    }
}
