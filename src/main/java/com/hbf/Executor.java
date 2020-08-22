package com.hbf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Executor {

    static Integer m = 16384;
    static  Integer k = 5;
    static Double t = 0.8;
    static String corpus_folder = "corpus_folder/20_newgroups/test_directory";
    static String query_folder = "query_folder";
    static HashFunctionsWrapper hashFunctions;


    public static void main(String[] args) {
        //corpus folder query folder m k t

        var corpusFolder = args.length>0 ? args[1] : corpus_folder;
        var queryFolder = args.length>1 ? args[2] : query_folder;
        m = args.length>2 ? Integer.parseInt(args[3]) : m;
        k = args.length>3 ? Integer.parseInt(args[4]) : k;
        t = args.length>4 ? Double.parseDouble(args[5]) : t;

        System.out.println("corpus folder " + corpusFolder );
        System.out.println("query folder " + queryFolder );

        System.out.println("m  - " + m ); // no of bits to be used
        System.out.println("k -  " + k ); // no of hash functions
        System.out.println("t -  " + t ); //  0 < t < 1 to decide the percentage of 1 bits for processing next level of nodes

        System.out.println("");

        //k hash Functions
        hashFunctions = new HashFunctionsWrapper(k);

        // get document bloom Filters from corpus files
        List<Document> inputDoc = new ArrayList<Document>();
        var documentBloomFilters = GetBloomFiltersFromInput(corpusFolder,inputDoc);

        Set<String> totalBagOfWords = new HashSet<String>();

        //total unique words in the document
        inputDoc.stream().forEach(x->
                {
                      totalBagOfWords.addAll(x.bagOfWords);
                }
        );

        System.out.println("total unique words in corpus " + totalBagOfWords.size());

        // create hierarchical bloom filter
        var hbf = new HierarchicalBF(documentBloomFilters, t);
        //  hbf.PrintStructure();

        // get query bloom filters from query files
        List<Document> queryDoc = new ArrayList<Document>();
        var queryBloomFilters = GetBloomFiltersFromInput(queryFolder,queryDoc);

        var sumOfNodesVisited=0;


        for(var query : queryBloomFilters) {

            System.out.println("");
            System.out.println("for query " + query.document.filePath);

            var result = hbf.Search(query);
            sumOfNodesVisited = sumOfNodesVisited + result.NodeVisited;
            PrintPerQueryResult(result);
        }

        if (inputDoc.size()>0) {
            System.out.println("");
            System.out.println("Average Tree nodes visited: " + sumOfNodesVisited / queryDoc.size());
            System.out.println("total number of articles in folder: " + inputDoc.size());
        }

    }


    private static List<BloomFilter> GetBloomFiltersFromInput(String inputFolder,  List<Document> inputDoc) {

        //read and convert all documents to bloom filter
        List<BloomFilter> documentBloomFilters = new ArrayList<>();
        var documents = Utils.readFolder(new File(inputFolder),inputDoc);

        documents.forEach(document->

                documentBloomFilters.add(
                        new BloomFilter(
                                m,
                                hashFunctions,
                                document
                        )
                ));

        return documentBloomFilters;
    }



    public static void PrintPerQueryResult(QueryResult queryResult) {

        System.out.println("nodes visited "  + queryResult.NodeVisited);

        if(queryResult.ReachedLeafNode) {
            System.out.println("document matched " + queryResult.resultBloomFilter.document.filePath);
            //PrintingDetails();
        }
        else
            System.out.println("document matched EMPTY");


    }
}
