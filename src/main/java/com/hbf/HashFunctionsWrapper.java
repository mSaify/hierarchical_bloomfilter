package com.hbf;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.commons.codec.digest.MurmurHash2;
import org.apache.commons.lang3.StringUtils;


public class HashFunctionsWrapper {

        Optional<Integer> randomSeed = Optional.of(0);
        ArrayList<Integer> randomSeeds = new ArrayList<>();

        Integer hashFunctionSize;

        public HashFunctionsWrapper(int noOfHashFunctions) {

            hashFunctionSize = noOfHashFunctions;
            HashGenerator(noOfHashFunctions);
        }

        private void HashGenerator ( Integer maxHashFunctions){
            IntStream.range(0, maxHashFunctions)
                    .forEach(val -> randomSeeds.add(val));
        }

        public int getHashValue(Integer hashFunctionIndex,String val) {

            randomSeed = Optional.ofNullable(randomSeeds.get(hashFunctionIndex));
            byte[] bytes = StringUtils.getBytes(val, Charset.defaultCharset());
            return Math.abs(MurmurHash2.hash32(bytes, bytes.length, randomSeed.orElseGet(()->0)));


        }
    }
