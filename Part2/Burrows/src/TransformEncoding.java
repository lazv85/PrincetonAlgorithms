
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransformEncoding {
	private List<SuffixElem> list = new ArrayList<>();
	private int first = -1;

	private static class SuffixElem implements Comparable<SuffixElem> {
		private String s;
		private int index;

		SuffixElem(String s, int index) {

			this.s = s;
			this.index = index;
		}

		private char getT() {
			if (index == 0) {
				return s.charAt(s.length() - 1);
			}
			
			return s.charAt(index - 1);
		}

		@Override
		public String toString() {
			return "[s=" + s + ", index=" + index + "]";
		}

		@Override
		public int compareTo(SuffixElem that) {
			for (int i = 0, i1 = this.index - s.length(), i2 = that.index - s.length(); i < s.length(); i++, i1++, i2++) {

				int ind1 = this.index + i;
				if (i1 >=0) {
					ind1 =  i1;
				}
				int ind2 = that.index + i;

				if (i2>=0) {
					ind2 = i2;
				}
				char c1 = this.s.charAt(ind1);
				char c2 = that.s.charAt(ind2);

				if (c1 < c2)
					return -1;
				if (c1 > c2)
					return 1;
			}
			return 0;
		}
	}

	public TransformEncoding(String s) {
		int len = s.length();
		for (int i = 0; i < len; i++) {
			list.add(new SuffixElem(s, i));
		}
		Collections.sort(list);
	}

	public String transformedString() {
		StringBuilder sb = new StringBuilder();
		for (SuffixElem se : list) {
			sb.append(se.getT());
		}
		return sb.toString();
	}

	public int index(int i) {
		return list.get(i).index;
	}

	public int getFirst() {
		if (first != -1) {
			return first;
		}

		int cnt = 0;
		for (SuffixElem se : list) {
			if (se.index == 0) {
				first = cnt;
				return cnt;
			}
			cnt++;
		}
		return cnt;
	}
}
