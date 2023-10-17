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
    private HashSet<Node> closedList = new HashSet<>();
    private GameLogic rules = new GameLogic();
    private String actionList;

    public String executeSearch(int width, int height, char[][] mapData, char[][] itemsData){

        MapData map = new MapData(width, height, mapData);

        Node initialNode = new Node(itemsData, map, 0, -1, '\0');
        openList.add(initialNode);
        openListIdentifiers.add(initialNode.getIdentifier()); // Add the identifier to the openListIdentifiers set

        while(!(openList.isEmpty())){

            // gets node with least f-score
            Node currentNode = openList.poll();

            // DEBUG ===============================
            System.out.println("|Parent Node|");
            currentNode.toStringMap(map);
            currentNode.toStringInfo();
            System.out.println("current node pulled from open list"); 
            // ======================================

            ArrayList<Node> successors = currentNode.generateSuccessors(rules, currentNode, map);

            System.out.println("\n|successors generated|\n"); // DEBUG

            for(int i = 0; i < successors.size(); i++){

                Node currentSuccessor = successors.get(i);

                // DEBUG ===============================
                System.out.println("\n|successor node #" + (i+1) + "|");
                currentSuccessor.toStringMap(map);
                // ======================================

                // end if goal
                if(currentSuccessor.isGoal(map)){
                    actionList = backtrackPath(currentSuccessor, closedList);
                    return actionList;
                }

                System.out.println("successor node #" + (i+1) + " is not goal"); // DEBUG

                // else, 
                if(evaluateSuccessor(currentSuccessor)){
                    openList.add(currentSuccessor);
                    openListIdentifiers.add(currentSuccessor.getIdentifier()); // Add the identifier to the openListIdentifiers set
                    System.out.println("successor node added to open list"); // DEBUG
                }
                else { // DEBUG
                    System.out.println("successor node NOT added to open list"); // DEBUG
                }

            }
                // Add the current node to the closed list
                closedList.add(currentNode);
                System.out.println("node Added");
        }
        
        return actionList;
    }

    private boolean evaluateSuccessor(Node successorNode){

        int successorIdentifier = successorNode.getIdentifier();
        boolean shouldAdd = true;

        PriorityQueue<Node> openListClone = new PriorityQueue<Node>(openList);
    
        // Check if the successor is already in the open list with a lower fCost
        if(openListIdentifiers.contains(successorIdentifier)){
            for(int i = 0; i < openListClone.size(); i++){
                Node nodeCopy = openListClone.poll();
                if(nodeCopy.getFCost() < successorNode.getFCost() && nodeCopy.getIdentifier() == successorNode.getIdentifier())
                    shouldAdd = false;
            }
        }

        // Check if the successor is already in the closed list with a lower fCost
        if (shouldAdd) {
            for (Node closedListNode : closedList) {
                if (closedListNode.getIdentifier() == successorIdentifier && closedListNode.getFCost() < successorNode.getFCost()) {
                    shouldAdd = false;
                    break;
                }
            }
        }
    

        return shouldAdd;
    }

    public String backtrackPath(Node goalNode, HashSet<Node> closedList) {
        // TODO
        return null;
    }
}

