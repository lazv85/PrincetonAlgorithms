import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

	private Item[] items;
	private boolean[] removedItems;
	private int queueSize = 1;
	private int cnt = 0;
	private int highMark = 0;

	@SuppressWarnings("unchecked")
	private void resizeItems(int size) {

		Item[] newItems = (Item[]) new Object[size];

		int counter = 0;
		for (int i = 0; i < queueSize; i++) {
			if (!removedItems[i]) {
				newItems[counter++] = items[i];
			}
		}

		items = newItems;
		queueSize = size;

		setRemovedItems(size);
		highMark = cnt;

	}

	private void setRemovedItems(int size) {
		removedItems = new boolean[size];
		for (int i = 0; i < size; i++) {
			if (i < cnt) {
				removedItems[i] = false;
			} else {
				removedItems[i] = true;
			}
		}
	}

	// construct an empty randomized queue
	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		items = (Item[]) new Object[queueSize];
		setRemovedItems(queueSize);
	}

	// is the queue empty?
	public boolean isEmpty() {
		return cnt == 0;
	}

	// return the number of items on the queue
	public int size() {
		return cnt;
	}

	// add the item
	public void enqueue(Item item) {
		if (item == null) {
			throw new java.lang.NullPointerException();
		}

		if (highMark >= queueSize) {
			resizeItems(queueSize * 2);
		}

		removedItems[highMark] = false;
		items[highMark] = item;
		highMark++;
		cnt++;

	}

	// remove and return a random item
	public Item dequeue() {
		if (cnt == 0) {
			throw new java.util.NoSuchElementException();
		}
		int n = StdRandom.uniform(queueSize);

		while (removedItems[n]) {
			n = StdRandom.uniform(queueSize);
		}
		Item item = items[n];
		removedItems[n] = true;
		cnt--;

		if (cnt < queueSize / 4) {
			resizeItems(queueSize / 2);
		}

		return item;
	}

	// return (but do not remove) a random item
	public Item sample() {
		if (cnt == 0) {
			throw new java.util.NoSuchElementException();
		}
		
		int n = StdRandom.uniform(queueSize);

		while (removedItems[n]) {
			n = StdRandom.uniform(queueSize);
		}
		Item item = items[n];
		return item;
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}

	private class ItemIterator implements Iterator<Item> {
		private int size;
		private boolean[] iterRemovedItems;

		ItemIterator() {
			size = cnt;
			iterRemovedItems = new boolean[queueSize];
			for (int i = 0; i < queueSize; i++) {
				iterRemovedItems[i] = removedItems[i];
			}
		}

		@Override
		public boolean hasNext() {
			return size != 0;
		}

		@Override
		public Item next() {
			if (size == 0) {
				throw new java.util.NoSuchElementException();
			}

			int n = StdRandom.uniform(queueSize);

			while (iterRemovedItems[n]) {
				n = StdRandom.uniform(queueSize);
			}
			Item item = items[n];
			iterRemovedItems[n] = true;
			size--;
			return item;
		}

		@Override
		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}

	}

	// unit testing

	public static void main(String[] args) {
		RandomizedQueue<Integer> rq = new RandomizedQueue<>();

		for (int i = 0; i < 10; i++) {
			rq.enqueue(i);
			rq.enqueue(i * 2);
			System.out.println(rq.dequeue());
		}

	}

}
