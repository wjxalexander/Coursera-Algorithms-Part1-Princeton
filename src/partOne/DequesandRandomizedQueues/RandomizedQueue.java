package partOne.DequesandRandomizedQueues;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int realSize;

    // construct an empty randomized queue
    public RandomizedQueue() {
        realSize = 0;
        queue = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return realSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return realSize;
    }

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < size(); i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("illegal null argument");
        }
        if (size() == queue.length) {
            resize(queue.length * 2);
        }
        queue[realSize++] = item;
    }

    private void swap(int i, int j) {
        if (i < 0 || j < 0 || i >= queue.length || j >= queue.length) {
            throw new ArrayIndexOutOfBoundsException("unexpected index");
        }
        Item temp = queue[i];
        queue[i] = queue[j];
        queue[j] = temp;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() == 0) {
            throw new NoSuchElementException("No elements found in the deque");
        }
        int r = StdRandom.uniformInt(realSize);
        Item dequeuedItem = queue[r];
        swap(r, size() - 1);
        queue[size() - 1] = null;
        realSize--;
        if (size() > 0 && size() == queue.length / 4) {
            resize(queue.length / 2);
        }
        return dequeuedItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0) {
            throw new NoSuchElementException("No elements found in the deque");
        }
        int r = StdRandom.uniformInt(realSize);
        return queue[r];
    }

    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Item> {
        private int index;
        private int[] shuffledPos;

        public MyIterator() {
            shuffledPos = new int[size()];
            for (int i = 0; i < size(); i++) {
                shuffledPos[i] = i;
            }
            index = 0;
            StdRandom.shuffle(shuffledPos);
        }

        @Override
        public boolean hasNext() {
            return index < shuffledPos.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the collection");
            }

            int pos = shuffledPos[index++];
            return queue[pos];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            randomizedQueue.enqueue(in.readString());
        }
        randomizedQueue.sample();
        randomizedQueue.dequeue();
        Iterator<String> iterator = randomizedQueue.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.println(element);
        }
    }

}