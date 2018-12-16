import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class TrieMatchingExtended implements Runnable {
	class Node
	{
		public static final int Letters =  4;
		public static final int NA      = -1;
		public int next [];
		public boolean patternEnd;

		Node ()
		{
			next = new int [Letters];
			Arrays.fill (next, NA);
			patternEnd = false;
		}
	}

	List <Integer> solve (String text, int n, List <String> patterns) {
		List <Integer> result = new ArrayList <Integer> ();
		List<Node> trie = buildTrie(patterns);

		int index = 0;
		while (index < text.length()) {
			Node currentNode = trie.get(0);
			for (int i = index; i < text.length(); i++) {
				char c = text.charAt(i);
				int nextNodeIndex = currentNode.next[letterToIndex(c)];
				if (nextNodeIndex != Node.NA) {
					currentNode = trie.get(nextNodeIndex);

					// is leaf
					if (currentNode.patternEnd) {
						result.add(index);
						break;
					}
				} else {
					break;
				}
			}

			++index;
		}

		return result;
	}

	private int letterToIndex (char letter)
	{
		switch (letter)
		{
			case 'A': return 0;
			case 'C': return 1;
			case 'G': return 2;
			case 'T': return 3;
			default: assert (false); return Node.NA;
		}
	}

	List<Node> buildTrie(List <String> patterns) {
		List<Node> trie = new ArrayList<>();

		trie.add(new Node());

		patterns.forEach(p -> {
			Node currentNode = trie.get(0);

			for (char c : p.toCharArray()) {
				int nextNodeIndex = currentNode.next[letterToIndex(c)];
				if (nextNodeIndex != Node.NA) {
					currentNode = trie.get(nextNodeIndex);
				} else {
					Node newNode = new Node();
					trie.add(newNode);
					currentNode.next[letterToIndex(c)] = trie.size() - 1;
					currentNode = newNode;
				}
			}

			currentNode.patternEnd = true;
		});

		return trie;
	}

	public void run () {
		try {
			BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
			String text = in.readLine ();
		 	int n = Integer.parseInt (in.readLine ());
		 	List <String> patterns = new ArrayList <String> ();
			for (int i = 0; i < n; i++) {
				patterns.add (in.readLine ());
			}

			List <Integer> ans = solve (text, n, patterns);

			for (int j = 0; j < ans.size (); j++) {
				System.out.print ("" + ans.get (j));
				System.out.print (j + 1 < ans.size () ? " " : "\n");
			}
		}
		catch (Throwable e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}

	public static void main (String [] args) {
		new Thread (new TrieMatchingExtended ()).start ();
	}
}
