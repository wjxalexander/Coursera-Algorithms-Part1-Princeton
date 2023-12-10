import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class WordNet {

    private final Digraph wordNet;
    // HashMap to store all nouns and related synset ids
    private final HashMap<String, Bag<Integer>> synMap;
    // HashMap to store synsets
    private final HashMap<Integer, String> synSets;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        synMap = new HashMap<String, Bag<Integer>>();
        synSets = new HashMap<Integer, String>();
        int count = readSynsets(synsets);
        wordNet = new Digraph(count);
        readHypernyms(hypernyms);

        // check that the input does correspond to a rooted DAG
        DirectedCycle dc = new DirectedCycle(wordNet);
        if (dc.hasCycle())
            throw new IllegalArgumentException("Input has cycle.");
        sap = new SAP(wordNet);

        // check that the input is single rooted
        // which means there is one and only one vertex has zero out degree
        int rootNum = 0;
        for (int v = 0; v < count; v++)
            if (wordNet.outdegree(v) == 0)
                rootNum++;
        if (rootNum != 1)
            throw new IllegalArgumentException("Input has " + rootNum + " roots.");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Input is null.");
        return synMap.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);

        Bag<Integer> idsA = synMap.get(nounA);
        Bag<Integer> idsB = synMap.get(nounB);

        return sap.length(idsA, idsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);

        Bag<Integer> idsA = synMap.get(nounA);
        Bag<Integer> idsB = synMap.get(nounB);

        int id = sap.ancestor(idsA, idsB);
        return synSets.get(id);
    }

    // helper methods
    private int readSynsets(String synsets) {
        if (synsets == null)
            throw new IllegalArgumentException("Argument is null");
        In in = new In(synsets);
        int count = 0;
        while (in.hasNextLine()) {
            count++;
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            synSets.put(id, parts[1]);
            String[] nouns = parts[1].split(" ");
            for (String n : nouns) {
                if (synMap.get(n) != null) {
                    Bag<Integer> bag = synMap.get(n);
                    bag.add(id);
                }
                else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(id);
                    synMap.put(n, bag);
                }
            }
        }
        return count;
    }

    private void readHypernyms(String hypernyms) {
        if (hypernyms == null)
            throw new IllegalArgumentException("Argument is null");
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int v = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int w = Integer.parseInt(parts[i]);
                wordNet.addEdge(v, w);
            }
        }
    }

    private void validateNoun(String noun) {
        if (!synMap.containsKey(noun))
            throw new IllegalArgumentException("Noun is not in wordNet");
    }

    // Unit test
    // public static void main(String[] args) {
    //
    // }

}