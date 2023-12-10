package partOne.DequesandRandomizedQueues;

/* *****************************************************************************
 *  Name: Jingxin.wang
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node<T> {
        private T item;
        private Node<T> next;
        private Node<T> prev;

        public Node(T source) {
            item = source;
        }

        public Node() {
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> nextNode) {
            next = nextNode;
        }

        public T getItem() {
            return item;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public void setPrev(Node<T> prevNode) {
            prev = prevNode;
        }
    }


    private final Node<Item> first = new Node<>();
    private final Node<Item> last = new Node<>();
    private int size = 0;

    // construct an empty deque
    public Deque() {
        first.setNext(last);
        last.setPrev(first);
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }


    private Node<Item> generateNewNode(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("illegal null argument");
        }
        return new Node<Item>(item);
    }


    // add the item to the front
    public void addFirst(Item item) {
        Node<Item> newNode = generateNewNode(item);
        first.getNext().setPrev(newNode);
        newNode.setNext(first.getNext());
        newNode.setPrev(first);
        first.setNext(newNode);
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        Node<Item> newNode = generateNewNode(item);
        last.getPrev().setNext(newNode);
        newNode.setPrev(last.getPrev());
        newNode.setNext(last);
        last.setPrev(newNode);
        size++;
    }

    private void removeNode(Node<Item> node) {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
        node.setPrev(null);
        node.setNext(null);
        size--;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("No elements found in the deque");
        }
        Node<Item> firstNode = first.getNext();
        removeNode(firstNode);
        return firstNode.getItem();
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("No elements found in the deque");
        }
        Node<Item> lastNode = last.getPrev();
        removeNode(lastNode);
        return lastNode.getItem();
    }

    // return an iterator over items in order from front to back
    @Override
    public Iterator<Item> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Item> {
        private int currentIndex;
        private Node<Item> currentNode;

        public MyIterator() {
            currentIndex = 0;
            currentNode = first;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the collection");
            }
            Node<Item> node = currentNode.getNext();
            currentIndex++;
            currentNode = currentNode.getNext();
            return node.getItem();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            deque.addLast(in.readString());
        }
        deque.removeFirst();
        deque.removeLast();
        deque.removeLast();
        deque.removeFirst();
        Iterator<String> iterator = deque.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.println(element);
        }
    }

}
