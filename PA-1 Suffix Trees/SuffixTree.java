import java.util.*;
import java.io.*;

public class SuffixTree {

    class Node {
        public ArrayList<String> nextEdges = new ArrayList<>();
        public ArrayList<Node> nextNodes = new ArrayList<>();
        public boolean patternEnd;
        public int offset;
    }

    // Build a suffix tree of the string text and return a list
    // with all of the labels of its edges (the corresponding
    // substrings of the text) in any order.
    public List<String> computeSuffixTreeEdges(String text) {
        List<String> result = new ArrayList<String>();

        text = text.substring(0, text.lastIndexOf('$'));

        List<String> suffixes = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            suffixes.add(text.substring(i));
        }

        result.add("$");
        Node tree = buildTree(suffixes);
        LinkedList<Node> queue = new LinkedList<>();
        queue.push(tree);

        while(!queue.isEmpty()) {
            Node node = queue.poll();

            for (int i = 0; i < node.nextNodes.size(); i++) {
                String e = node.nextEdges.get(i);
                Node n = node.nextNodes.get(i);
                queue.push(n);

                if (n.patternEnd) {
                    if (n.nextNodes.isEmpty()) {
                        result.add(e.concat("$"));
                    } else {
                        result.add(e);
                        result.add("$");
                    }
                } else {
                    result.add(e);
                }
            }
        }

        return result;
    }

    Node buildTree(List<String> inputPatterns) {
        Node root = new Node();

        for (int offset = 0; offset < inputPatterns.size(); offset++) {
            Node currentNode = root;
            String p = inputPatterns.get(offset);

            while (p.length() > 0) {
                Integer edgeIndex = null;
                for (int i = 0; i < currentNode.nextEdges.size(); i++) {
                    if (currentNode.nextEdges.get(i).charAt(0) == p.charAt(0)) {
                        edgeIndex = i;
                        break;
                    }
                }

                if (edgeIndex == null) {
                    currentNode.nextEdges.add(p);
                    Node newNode = new Node();
                    newNode.offset = offset;
                    newNode.patternEnd = true;
                    currentNode.nextNodes.add(newNode);
                    break;
                }

                if (currentNode.nextEdges.get(edgeIndex).contentEquals(p)) {
                    currentNode.nextNodes.get(edgeIndex).patternEnd = true;
                    break;
                }

                int commonPrefixEnd = 0;
                String edge = currentNode.nextEdges.get(edgeIndex);
                for (int i = 0; i < edge.length() && i < p.length(); i++) {
                    if (edge.charAt(i) == p.charAt(i)) {
                        commonPrefixEnd = i;
                    } else {
                        break;
                    }
                }
                String commonPrefix = p.substring(0, commonPrefixEnd + 1);
                p = p.substring(commonPrefixEnd + 1);

                if (commonPrefix.length() < edge.length()) {
                    String restEdge = currentNode.nextEdges.get(edgeIndex).substring(commonPrefixEnd + 1);
                    Node nodeB = currentNode.nextNodes.get(edgeIndex);


                    Node nodeA = new Node();
                    nodeA.nextNodes.add(nodeB);
                    nodeA.nextEdges.add(restEdge);

                    currentNode.nextNodes.set(edgeIndex, nodeA);
                    currentNode.nextEdges.set(edgeIndex, commonPrefix);
                    if (p.isEmpty()) {
                        nodeA.patternEnd = true;
                    }

                    currentNode = nodeA;
                } else {
                    currentNode = currentNode.nextNodes.get(edgeIndex);
                }
            }
        }

        return root;
    }

    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    static public void main(String[] args) throws IOException {
        new SuffixTree().run();
    }

    public void print(List<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        List<String> edges = computeSuffixTreeEdges(text);
        print(edges);
    }
}
