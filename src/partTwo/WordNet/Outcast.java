/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class Outcast {
    private final WordNet net;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = nouns[0];
        int d = dSum(nouns[0], nouns);
        for (int i = 1; i < nouns.length; i++) {
            int tempdist = dSum(nouns[i], nouns);
            if (d < tempdist) {
                d = tempdist;
                outcast = nouns[i];
            }
        }
        return outcast;
    }

    private int dSum(String noun, String[] nouns) {
        return Arrays.stream(nouns).mapToInt(item -> net.distance(noun, item)).sum();
    }


    public static void main(String[] args) {

    }
}
