package solver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

/* Notes
 * - In evaluateSuccessor(), the openList is iterated through to find matching nodes; can possibly be optimized
 */

public class Search {

    private PriorityQueue<Node> openList = new PriorityQueue<>();
    private HashSet<Integer> openListIdentifiers = new HashSet<>();
    private ArrayList<Node> closedList = new ArrayList<>();
    private HashSet<Integer> closedListIdentifiers = new HashSet<>();
    private GameLogic rules = new GameLogic();
    private String actionList;

    public String executeSearch(int width, int height, char[][] mapData, char[][] itemsData) {

        MapData map = new MapData(width, height, mapData);

        Node initialNode = new Node(itemsData, map, 0, -1, '\0', rules);
        openList.add(initialNode);
        openListIdentifiers.add(initialNode.getIdentifier());

        while (!openList.isEmpty()){

            Node currentNode = openList.poll();

            // DEBUG ===============================
            System.out.println("|Parent Node|");
            currentNode.toStringMap(map);
            currentNode.toStringInfo();
            System.out.println("current node pulled from open list");
            //======================================

            ArrayList<Node> successors = currentNode.generateSuccessors(rules, map);

            for (Node currentSuccessor : successors) {
    
                //DEBUG ===============================
                System.out.println("\n|successor node|");
                currentSuccessor.toStringMap(map);
                //====================================== 

                if (currentSuccessor.isGoal(map)) {
                    actionList = backtrackPath(currentSuccessor, closedListIdentifiers);
                    currentSuccessor.toStringMap(map);
                    currentSuccessor.toStringInfo();
                    return actionList;
                }

                //System.out.println("successor node is not the goal"); // DEBUG

                if (evaluateSuccessor(currentSuccessor)) {
                    openList.add(currentSuccessor);
                    openListIdentifiers.add(currentSuccessor.getIdentifier());
                    //System.out.println("successor node added to open list"); // DEBUG
                } else {
                    //System.out.println("successor node NOT added to open list"); // DEBUG
                }
            }

            // Add the current node to the closed list
            closedListIdentifiers.add(currentNode.getIdentifier());
            closedList.add(currentNode);
            //System.out.println("node Added");
        }

        return actionList;
    }

    private boolean evaluateSuccessor(Node successorNode) {

        int successorIdentifier = successorNode.getIdentifier();

        if (openListIdentifiers.contains(successorIdentifier)) {
            // Check if the successor is already in the open list with a lower fCost
            for (Node nodeCopy : openList) {
                if (nodeCopy.getIdentifier() == successorIdentifier && nodeCopy.getFCost() < successorNode.getFCost()) {
                    return false;
                }
            }
        }

        if (closedListIdentifiers.contains(successorIdentifier)) {
            // Check if the successor is already in the closed list with a lower fCost
            return false;
        }

        return true;
    }

    public String backtrackPath(Node goalNode, HashSet<Integer> closedListIdentifiers) {
        // TODO: Implement path backtracking
        return null;
    }
}