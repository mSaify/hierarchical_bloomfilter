package com.hbf;

import java.util.BitSet;

public class BloomFilter extends BitSet {

    public Document document;

    private int m;
    private int k;
    private HashFunctionsWrapper hashFunctions;

    public BloomFilter(int m, int k, Document d) {

        super(m);

        this.m=m;
        this.k=k;
        document=d;

    }

    public BloomFilter(int m, HashFunctionsWrapper hashFunctions, Document d) {

        super(m);

        this.m=m;
        this.hashFunctions = hashFunctions;
        document=d;

        GenerateBloomFilter();

        PrintingBits();

    }

    public BloomFilter() {

    }

    private void PrintingBits() {

       // System.out.println(String.format("bloom filter %s for document %s" , this, document.filePath ));


    }

    private void GenerateBloomFilter() {

        for( String word : document.bagOfWords ) {

            if(!word.equals(" ") && !word.equals("") && !word.equals("\t")) {

                for (int i = 0; i < hashFunctions.hashFunctionSize; i++) { //get hashValue from all hash Functions

                    var hashValue = hashFunctions.getHashValue(i, word);
                    this.set(hashValue % m);


                }
            }
        }
    }
}
