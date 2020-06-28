import java.io.*;
import java.util.*;

class Node
{
	public static final int Letters =  4;
	public static final int NA      = -1;
	public int next [];

	Node ()
	{
		next = new int [Letters];
		Arrays.fill (next, NA);
	}
}

public class TrieMatching implements Runnable {
	int letterToIndex (char letter)
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

	List <Integer> solve (String text, int n, List <String> patterns) {
		List <Integer> result = new ArrayList <Integer> ();
		List<Node> trie = buildTrie(patterns);

		int index = 0;
		while (index < text.length()) {
			String prefix = text.substring(index);

			Node currentNode = trie.get(0);
			for (char c : prefix.toCharArray()) {
				int nextNodeIndex = currentNode.next[letterToIndex(c)];
				if (nextNodeIndex != Node.NA) {
					currentNode = trie.get(nextNodeIndex);

					boolean isLeaf = Arrays.stream(currentNode.next).allMatch(nodeIndex -> nodeIndex == Node.NA);
					if (isLeaf) {
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
		new Thread (new TrieMatching ()).start ();
	}
}
