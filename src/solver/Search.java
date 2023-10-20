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
    private GameLogic rules;
    private String actionList;

    public String executeSearch(int width, int height, char[][] mapData, char[][] itemsData) {

        MapData map = new MapData(width, height, mapData);
        rules = new GameLogic(map);

        Node initialNode = new Node(itemsData, map, 0, -1, '\0', rules);
        openList.add(initialNode);
        openListIdentifiers.add(initialNode.getIdentifier());

        while (!openList.isEmpty()){

            Node currentNode = openList.poll();

            /*// DEBUG ===============================
            System.out.println("|Parent Node|");
            currentNode.toStringMap(map);
            currentNode.toStringInfo();
            System.out.println("current node pulled from open list");
            //======================================  */

            ArrayList<Node> successors = currentNode.generateSuccessors(rules, map);
        
            if (currentNode.isGoal()) {
                actionList = backtrackPath(currentNode, closedList, map);
                currentNode.toStringMap(map);
                currentNode.toStringInfo();
                System.out.println("Current Node isGoal, return ActionList");
                return actionList;
            }

            for (Node currentSuccessor : successors) {
    
                /* DEBUG ===============================
                System.out.println("\n|successor node|");
                currentSuccessor.toStringMap(map);
                //====================================== */

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

        System.out.println("A* Finished. ActionList returned.");
        return actionList;
    }


    private boolean evaluateSuccessor(Node successorNode){
        int successorIdentifier = successorNode.getIdentifier();

        // Check if the successor is already in the open list (does not check for f cost)
        if(openListIdentifiers.contains(successorIdentifier)){
            return false;
        }

        // Check if the successor is already in the closed list with a lower fCost
        if(closedListIdentifiers.contains(successorIdentifier)){
            for (Node closedListNode : closedList) {
                if (closedListNode.getIdentifier() == successorIdentifier && closedListNode.getFCost() < successorNode.getFCost()) {
                    return false;
                }
            }
        }
        
        return true;
    }


    public String backtrackPath(Node goalNode, ArrayList<Node> closedList, MapData map) {
        StringBuilder actions = new StringBuilder();
        Node currentNode = goalNode;
    
        while (currentNode.getActionUsed() != '\0') {

            // Find the parent node in the closed list
            Node parent = findNodeInClosedList(currentNode, closedList, map);

            // Retrieve the action that led to the current node
            char action = currentNode.getActionUsed();
            actions.insert(0, action);

            // Move to the parent node for the next iteration
            currentNode = parent;
        }
    
        System.out.println("A* Search Finished");
        return actions.toString();
    }

    public Node findNodeInClosedList(Node current, ArrayList<Node> closedList, MapData map) {

        for (Node node : closedList){
            if (current.getParentIdentifier() == node.getIdentifier())
                return node;
        }

        // parent node is not found, 
        System.out.println("Parent not found for identifier: " + current.getParentIdentifier());
        current.toStringMap(map);
        
        return null;
    }
}
