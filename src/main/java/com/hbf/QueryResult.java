package com.hbf;

import org.jetbrains.annotations.NotNull;

public class QueryResult implements Comparable<QueryResult> {

    public BloomFilter resultBloomFilter;
    public Boolean ReachedLeafNode= Boolean.FALSE;
    public int NodeVisited;
    public int matchedBits;




    public QueryResult(BloomFilter filter, Boolean reachedLeafNode, int matchedBits) {
        this.ReachedLeafNode  = reachedLeafNode;
        this.resultBloomFilter =  filter;
        this.matchedBits=matchedBits;
        //this.NodeVisited = nodeVisited;
    }

    public QueryResult(BloomFilter filter, int matchedBits) {
        this.resultBloomFilter =  filter;
        this.ReachedLeafNode=Boolean.TRUE;
        this.matchedBits=matchedBits;
        //this.NodeVisited = nodeVisited;
    }


    public QueryResult() {}

    @Override
    public int compareTo(@NotNull QueryResult queryResult) {
          if (this.matchedBits>queryResult.matchedBits)
              return 1;
          else
              return -1;
    }
}
