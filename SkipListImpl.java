import java.util.Iterator;
import java.util.Random;

// Skeleton for skip list implementation.

public class SkipListImpl<T extends Comparable<? super T>> implements SkipList<T> {

	public class Entry<T> {
		T element;
		Entry<T>[] nextPointers;
		int num_levels;

		Entry(T x, int level) {
			element = x;
			nextPointers = new Entry[level + 1];
			this.num_levels = nextPointers.length;
		}

		Entry<T> next() {
			return nextPointers[0];
		}

	}

	Entry<T> header;
	Entry<T> tail;
	int size;
	int maxLevel;
	Random rand = new Random();

	SkipListImpl() {
		SkipListImpl(0);
	}

	private void SkipListImpl(int maxLevel) {
		this.maxLevel = maxLevel;
		header = new Entry<T>(null, 0);
		tail = header;
		size = 0;
	}

	public Entry<T>[] find(T x) {
		Entry<T>[] prev = new Entry[maxLevel + 1];
		Entry<T> p = header;
		for (int i = maxLevel; i >= 0; i--) {
			while ((p.nextPointers[i] != null) && (p.nextPointers[i].element.compareTo(x) < 0)) {
				p = p.nextPointers[i];
			}
			prev[i] = p;
		}
		return prev;
	}

	@Override
	public boolean add(T x) {
		if (contains(x)) {
			return false;
		} else {
			int nodeHeight = levelGenerator(maxLevel);
			Entry<T> n = new Entry<T>(x, nodeHeight);
			Entry<T>[] prev = find(x);
			for (int i = 0; i <= nodeHeight; i++) {
				n.nextPointers[i] = prev[i].nextPointers[i];
				prev[i].nextPointers[i] = n;
			}
			if ((prev[0].next() == null) || (tail.equals(prev[0]))) {
				tail = n;
			}
		}
		size++;
		rebuild();
		return true;
	}

	private int levelGenerator(int maxLevel) {
		int l = 0;
		while (l < maxLevel) {
			if (rand.nextBoolean()) {
				break;
			} else
				l++;
		}
		return l;
	}

	@Override
	public T ceiling(T x) {
		Entry<T>[] prev = find(x);
		if (prev[0].next() == null) {
			return null;
		} else {
			return prev[0].next().element;
		}
	}

	@Override
	public boolean contains(T x) {
		Entry<T>[] prev = find(x);
		if ((prev[0].next() != null) && (prev[0].next().element.compareTo(x) == 0)) {
			return true;
		}
		return false;
	}

	@Override
	public T findIndex(int n) {
		int index = 0;
		Entry<T> p = header.next();
		if (size == 0 || n >= size) {
			return null;
		} else {
			while (index < n) {
				p = p.next();
				index++;
			}
			return p.element;
		}
	}

	@Override
	public T first() {
		return header.next().element;
	}

	@Override
	public T floor(T x) {
		Entry<T>[] prev = find(x);
		if (prev[0].next() == null) {
			return prev[0].element;
		} else {
			if (prev[0].next().element.equals(x)) {
				return x;
			} else {
				return prev[0].element;
			}
		}
	}

	@Override
	public boolean isEmpty() {
		if (size != 0) {
			return true;
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new SLIterator<>(header);
	}

	private class SLIterator<E> implements Iterator<E> {
		Entry<E> cursor, prev;

		SLIterator(Entry<E> head) {
			cursor = head;
			prev = null;
		}

		public boolean hasNext() {
			return ((cursor.next() != null) && (cursor.next() != null));
		}

		public E next() {
			prev = cursor;
			cursor = cursor.next();
			return cursor.element;
		}

		public void remove() {
			prev.nextPointers[0] = cursor.next();
			prev = null;
		}
	}

	@Override
	public T last() {
		return tail.element;
	}

	@Override
	public void rebuild() { //check when to increase maxLevel and when to decrease maxLevel
		Entry<T>[] newList;
		int newMaxLevel = (int) Math.ceil((Math.log(size + 1) / Math.log(2)));
		if (maxLevel < newMaxLevel) {
			newList = new Entry[size];
			rebuild(newList, 0, size - 1, maxLevel + 1);
			rebuildList(newList, maxLevel + 1);
			this.maxLevel = maxLevel + 1;
		} else if (maxLevel > newMaxLevel) {
			newList = new Entry[size];
			rebuild(newList, 0, size - 1, maxLevel - 1);
			rebuildList(newList, maxLevel - 1);
			this.maxLevel = maxLevel - 1;
		}
	}

	private void rebuild(Entry<T>[] newList, int start, int end, int newMaxLevel) { //recursively create entries of new levels
		if (start <= end) {
			if (newMaxLevel == 0) {
				for (int i = start; i <= end; i++) {
					newList[i] = new Entry<T>(null, 0);
				}
			} else {
				int mid = (start + end) / 2;
				newList[mid] = new Entry<T>(null, newMaxLevel);
				rebuild(newList, start, mid - 1, newMaxLevel - 1);
				rebuild(newList, mid + 1, end, newMaxLevel - 1);

			}
		}
	}

	private void rebuildList(Entry<T>[] newList, int newMaxLevel) { //set up links for newly created skip list
		Entry<T> newHeader = new Entry<T>(null, newMaxLevel);
		Entry<T>[] prev = new Entry[newMaxLevel + 1]; //store the current entry to update its nextPointers later
		Entry<T> p = header.next();
		int newListIndex = 0;
		int headerNextIndex = 0; // variable to keep track of next index at which nextPointers has to be filled 
		while (p != null) {
			for (int i = 0; i < newList[newListIndex].num_levels; i++) {
				if (headerNextIndex <= newMaxLevel && headerNextIndex == i) {
					newHeader.nextPointers[headerNextIndex] = newList[newListIndex];
					headerNextIndex++;
				}
				if (prev[i] != null) {
					prev[i].nextPointers[i] = newList[newListIndex]; //update the nextPointers of previously stored entry
				}
				prev[i] = newList[newListIndex]; //store the current entry to update its nextPointers later
			}
			newList[newListIndex].element = p.element;
			newListIndex++;
			p = p.next();
		}
		tail = newList[newList.length - 1];
		this.header = newHeader;
	}

	@Override
	public T remove(T x) {
		Entry<T>[] prev = find(x);
		if (contains(x)) {
			Entry<T> n = prev[0].next();
			for (int i = 0; i <= maxLevel; i++) {
				if (prev[i].nextPointers[i] == n) {
					prev[i].nextPointers[i] = n.nextPointers[i];
				} else
					break;
			}
			size--;
			rebuild();
			return n.element;
		} else {
			return null;
		}
	}

	@Override
	public int size() {
		return size;
	}
}