import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

	private Item item;

	private Deque<Item> first = null;
	private Deque<Item> last = null;
	private Deque<Item> firstNext = null;
	private Deque<Item> lastNext = null;

	private int cnt = 0;

	// construct an empty deque
	public Deque() {

	}

	// is the deque empty?
	public boolean isEmpty() {
		return cnt == 0;
	}

	// return the number of items on the deque
	public int size() {
		return cnt;
	}

	// add the item to the front
	public void addFirst(Item item) {
		if (item == null) {
			throw new java.lang.NullPointerException();
		}

		Deque<Item> elem = new Deque<Item>();
		elem.item = item;

		if (cnt != 0) {

			elem.firstNext = first;
			first.lastNext = elem;
		}

		first = elem;
		if (cnt == 0) {
			last = first;
		}
		cnt++;
	}

	// add the item to the end
	public void addLast(Item item) {
		if (item == null) {
			throw new java.lang.NullPointerException();
		}

		Deque<Item> elem = new Deque<Item>();
		elem.item = item;

		if (cnt != 0) {
			elem.lastNext = last;
			last.firstNext = elem;

		}
		last = elem;
		if (cnt == 0) {
			first = last;
		}
		cnt++;
	}

	// remove and return the item from the front
	public Item removeFirst() {
		if (cnt == 0) {
			throw new java.util.NoSuchElementException();
		}
		Item i = first.item;
		first = first.firstNext;
		cnt--;
		return i;
	}

	// remove and return the item from the end
	public Item removeLast() {
		if (cnt == 0) {
			throw new java.util.NoSuchElementException();
		}
		Item i = last.item;
		last = last.lastNext;
		cnt--;
		return i;
	}

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator() {
		return new ItemIterator();
	}

	private class ItemIterator implements Iterator<Item>{
		private int size = cnt;
		Deque<Item> pointer = first;
		
		@Override
		public boolean hasNext() {
			return size != 0;
		}

		@Override
		public Item next() {
			if(size == 0){
				throw new java.util.NoSuchElementException();
			}
			Item item = pointer.item;
			pointer = pointer.firstNext;
			size --;
			return item;
		}
		
		@Override
		public void remove(){
			throw new java.lang.UnsupportedOperationException();
		}
		
	}
	
	public static void main(String[] args) {
		Deque<Integer> dq = new Deque<>();
		for (int i = 0; i < 10; i++) {
			dq.addFirst(i);
		}

		for(Integer i: dq){
			System.out.println(i);
		}
		
		for(Integer i: dq){
			System.out.println(i);
		}
	}
	
	

}
