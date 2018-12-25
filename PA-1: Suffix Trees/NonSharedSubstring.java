import java.io.*;
import java.math.*;
import java.util.*;

public class NonSharedSubstring implements Runnable {
    class Node {
        ArrayList<String> nextEdges = new ArrayList<>();
        ArrayList<Node> nextNodes = new ArrayList<>();
        int stringIndex;
        int offset;
        boolean patternEnd;
    }

    private String solve(String p, String q) {
        Node root = new Node();

        buildTree(root, getSuffixes(p), 0b01);
        buildTree(root, getSuffixes(q), 0b10);

        String result = find(root, "");

        return result;
    }

    private String find(Node node, String substring) {
        String smallest = null;
        for (int j = 0; j < node.nextNodes.size(); j++) {
            Node n = node.nextNodes.get(j);
            String edge = node.nextEdges.get(j);
            String currentString = substring + edge;
            String shortestCurrentString = substring + edge.charAt(0);

            if (n.stringIndex == 0b01) {
                if (smallest == null || smallest.length() > shortestCurrentString.length()) {
                    smallest = shortestCurrentString;
                }
            } else {
                currentString = find(n, currentString);
                if (currentString == null) continue;
                if (smallest == null || currentString.length() < smallest.length()) {
                    smallest = currentString;
                }
            }
        }

        return smallest;
    }

    private String[] getSuffixes(String text) {
        String[] suffixes = new String[text.length()];
        for (int i = 0; i < text.length(); i++) {
            suffixes[i] = text.substring(i);
        }

        return suffixes;
    }

    private void buildTree(Node root, String[] inputPatterns, int stringIndex) {
        for (int offset = 0; offset < inputPatterns.length; offset++) {
            Node currentNode = root;
            String p = inputPatterns[offset];

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
                    newNode.stringIndex |= stringIndex;
                    currentNode.nextNodes.add(newNode);
                    break;
                }

                if (currentNode.nextEdges.get(edgeIndex).contentEquals(p)) {
                    currentNode.nextNodes.get(edgeIndex).stringIndex |= stringIndex;
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
                    nodeA.stringIndex = nodeB.stringIndex | stringIndex;
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
                    currentNode.stringIndex |= stringIndex;
                }
            }
        }
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String p = in.readLine();
            String q = in.readLine();

            String ans = solve(p, q);

            System.out.println(ans);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new Thread(new NonSharedSubstring()).start();
    }
}
