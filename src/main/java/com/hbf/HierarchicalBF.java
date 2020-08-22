package com.hbf;

import java.util.*;
import java.util.stream.Collectors;

public class HierarchicalBF {

    private Integer level;

    private Double t;

    private Node rootNode = null;

    private class Node {

        BloomFilter node;
        Node rightNode;
        Node leftNode;

        Node(BloomFilter node){
            this.node = node;
            this.rightNode=null;
            this.leftNode=null;
        }
    }

    public  HierarchicalBF(List<BloomFilter> nodeBloomFilters, Double t) {

            level = (int) Math.floor(Math.log(nodeBloomFilters.size()) / Math.log(2));
            this.t = t;

            System.out.println("hierarchy level in bloom filter " + level);

            var currBlooms =  GetNodeArrayFromBF(nodeBloomFilters);

            while(level>0) {

                var nextLevel = new ArrayList<Node>();


                var iter = 0;
                while(iter < currBlooms.size()-1) {


                    var node1 = currBlooms.get(iter);
                    var node2 = currBlooms.get(iter+1);

                    var parentNode = UnionBloomFilters(node1, node2);

                    parentNode.leftNode = node1;
                    parentNode.rightNode = node2;

                    rootNode = parentNode;

                    nextLevel.add(parentNode);

                    iter=iter+2;

                }
                currBlooms=nextLevel;



                level--;
            }
    }

    public void PrintStructure() {

        System.out.println("printin the hierarchical Structure... ");
        System.out.println("");

        BinaryTreeIter(rootNode,level);


    }

    public void BinaryTreeIter(Node rootNode, int level) {

        if(rootNode == null)
            return;
        else
            System.out.println(String.format("printing the level %s ->  bloom filter %s", level, rootNode.node));

        if(rootNode.leftNode!=null)
            BinaryTreeIter(rootNode.leftNode, level+1);

        if(rootNode.rightNode!=null)
            BinaryTreeIter(rootNode.rightNode, level+1);

    }

    public QueryResult Search(BloomFilter query) {

        var headNode = rootNode;
       return SearchBFS(headNode,query);

    }

    private QueryResult SearchBFS(Node node, BloomFilter query) {

        var nodeVisited = 0;
        var queryResults = new ArrayList<QueryResult>();
        Queue<Node> queue = new LinkedList<>();

        queue.add(node);

        while(queue.size()>0) {

            var curr = queue.poll();

            if(curr!=null) {

                nodeVisited++;

                if(curr.leftNode == null || curr.rightNode == null) {

                    var res = GreaterThanThreshold(curr.node,query);
                    if(res>0)
                        queryResults.add(new QueryResult(curr.node,res));
                }
                else if(GreaterThanThreshold(curr.node,query)>0) {
                    queue.add(curr.leftNode);
                    queue.add(curr.rightNode);
                }
                else {
                    queryResults.add(new QueryResult(curr.node,Boolean.FALSE,0));
                }

            }
        }

        var res = queryResults.stream().filter(x->x.ReachedLeafNode==Boolean.TRUE).collect(Collectors.toList());
        var queryResult = CalculateLeastHammingSpace(res, query);
       queryResult.NodeVisited=nodeVisited;
        return queryResult;
    }

    private QueryResult CalculateLeastHammingSpace(List<QueryResult> queryResults, BloomFilter query) {

        if(queryResults.size()<=0) return new QueryResult();

        var highestMatchedResult = queryResults.get(0);
        var minXORCardinality=Integer.MAX_VALUE;

        for(var queryMatched : queryResults) {

            var res = (BloomFilter) queryMatched.resultBloomFilter.clone();
            res.xor(query);
            var currCardinality = res.cardinality();
            if (currCardinality < minXORCardinality) {
                highestMatchedResult = queryMatched;
                minXORCardinality = currCardinality;
            }
        }
           if( (minXORCardinality*1.0/query.size()) >= t  )
               return new QueryResult();

        return highestMatchedResult;
    }

    private int GreaterThanThreshold(BloomFilter node, BloomFilter query) {

        var res = (BloomFilter) node.clone();
        res.and(query);

        if(res.cardinality() >= (t*query.cardinality())) {

         //   System.out.println("length after matching" + res.cardinality());
            return res.cardinality();
        }

        return 0;
    }

    private List<Node> GetNodeArrayFromBF(List<BloomFilter> bloomFilters) {
        var nodes = new ArrayList<Node>();
        for(var bf : bloomFilters) {
            nodes.add(new Node(bf));
        }

        return nodes;
    }

    private Node UnionBloomFilters(Node node1, Node node2) {

        // union of two vectors here node1 OR node2

        var unionNode = (BloomFilter) node1.node.clone();

        unionNode.or(node2.node);

        return new Node(unionNode);
    }

    private void PrintingDetails() {

        //   TreeSet queryTreeSet = new TreeSet();
        //  queryTreeSet.addAll(query.document.bagOfWords);
        //   System.out.println(queryTreeSet);

        //  System.out.println("queryResult.resultBloomFilter.document.bagOfWords");
        //  System.out.println(queryResult.resultBloomFilter.document.bagOfWords);

        //  TreeSet<String> intersection = new TreeSet<>(queryResult.resultBloomFilter.document.bagOfWords); // use the copy constructor
        // intersection.retainAll(query.document.bagOfWords);


        //   System.out.println("\n query and matched query intersection " + intersection);

        //  System.out.println("\n intersected query size " + intersection.size());
    }

}
