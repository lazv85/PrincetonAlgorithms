import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformDecoding {

	private int first;
	private String t;
	private Map<Character, List<Integer>> hashArrayT;
	private char[] sortedT;
	private int[] next;
	private String originalString;

	private int getFirst(String s){
		int first = 0;
		for(int i=0;i<4;i++){
			 first = first*256 + s.charAt(i);
			}
		return first;
	}

	public TransformDecoding(String s) {
		hashArrayT = new HashMap<>();

		this.first = getFirst(s);

		this.t = s.substring(4);
		sortedT = new char[t.length()];
		next = new int[t.length()];

		for (int i = 0; i < t.length(); i++) {
			sortedT[i] = t.charAt(i);
			if (hashArrayT.containsKey(t.charAt(i))) {
				hashArrayT.get(t.charAt(i)).add(i);
			} else {
				hashArrayT.put(t.charAt(i), new ArrayList<>());
				hashArrayT.get(t.charAt(i)).add(i);
			}
		}
		Arrays.sort(sortedT);

		for (int i = 0; i < sortedT.length; i++) {
			next[i] = hashArrayT.get(sortedT[i]).get(0);
			hashArrayT.get(sortedT[i]).remove(0);
		}
		
		StringBuilder sb = new StringBuilder();
		
		int elem = first;
		for (int i = 0; i < next.length; i++) {
			sb.append(sortedT[elem]);
			elem = next[elem];
		}
		originalString = sb.toString();
	}

	public String getOriginalString() {
		return originalString;
	}

}
