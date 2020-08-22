package com.hbf;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Document implements FileReader {

    public String  filePath;

    public Set<String> bagOfWords;

    public Document(String filePath) throws IOException {

        bagOfWords=new HashSet<>();
        this.filePath = filePath;
        //System.out.println(String.format("processing document %s", filePath));
        Utils.readDocument(filePath,this);
    }

    @Override
    public void processLine(String line) {

        Arrays.stream(line.split(" "))
                .forEach(x-> {
                    // if required stemming need to be done here
                    bagOfWords.add(x.toLowerCase());
                //    System.out.println(x.toLowerCase());
                });
    }
}
