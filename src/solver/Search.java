package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/* Notes
 * - There could be something wrong with closedList hashmap logic, specifically in the key logic
 * - Furthermore, it might be better to use a hashSet for closedList since hashMap functionality is unused
 * - In evaluateSuccessor(), the openList is iterated through to find matching nodes; can possibly be optimized
 */

public class Search {

    private PriorityQueue<Node> openList = new PriorityQueue<Node>();
    private Map<Integer, Node> closedList = new HashMap<Integer, Node>();
    private GameLogic rules = new GameLogic();
    private String actionList;

    public String executeSearch(int width, int height, char[][] mapData, char[][] itemsData){

        Node initialNode = new Node(width, height, itemsData, mapData, 0, -1, '\0');
        openList.add(initialNode);

        while(!(openList.isEmpty())){

            Node currentNode = openList.poll();

            System.out.println("|Parent Node|"); // DEBUG
            currentNode.toStringMap(); // DEBUG
            currentNode.toStringInfo();
            System.out.println("current node pulled from open list"); // DEBUG

            ArrayList<Node> successors = currentNode.generateSuccessors(rules, currentNode);

            System.out.println("\n|successors generated|\n"); // DEBUG

            for(int i = 0; i < successors.size(); i++){

                Node currentSuccessor = successors.get(i);

                System.out.println("\n|successor node #" + (i+1) + "|"); // DEBUG
                currentSuccessor.toStringMap(); // DEBUG
                //currentSuccessor.toStringInfo(); // DEBUG
                

                if(currentSuccessor.isGoal()){
                    actionList = backtrackPath(currentSuccessor, closedList);
                    return actionList;
                }
                System.out.println("successor node #" + (i+1) + " is not goal"); // DEBUG
                
                if(evaluateSuccessor(currentNode, currentSuccessor)){
                    openList.add(currentSuccessor);
                    System.out.println("successor node added to open list"); // DEBUG
                }
                else{ // DEBUG
                    System.out.println("successor node NOT added to open list"); // DEBUG
                }
                
            }

            closedList.put(currentNode.getIdentifier(), currentNode);
            System.out.println("current node added to closed list"); // DEBUG
            
        }
        
        return actionList;
    }

    // TODO there seems to be an issue here
    private boolean evaluateSuccessor(Node parentNode, Node successorNode){

        int successorIdentifier = successorNode.getIdentifier();
        boolean shouldAdd = true;

        PriorityQueue<Node> openListClone = new PriorityQueue<Node>(openList);
    
        // if successor already in openList && openList node w/ same identifier < successor identifier
        for(int i = 0; i < openListClone.size(); i++){
            Node nodeCopy = openListClone.poll();
            if(nodeCopy.getFCost() < successorNode.getFCost() && nodeCopy.getIdentifier() == successorNode.getIdentifier())
                shouldAdd = false;
        }

        // if successor already in closedList && closedList node w/ same identifier < successor identifier
        if(closedList.containsKey(successorIdentifier) && closedList.get(successorIdentifier).getFCost() < successorNode.getFCost())
            shouldAdd = false;
        
        return shouldAdd;
    }

    // TODO back track doesnt work
    public String backtrackPath(Node goalNode, Map<Integer, Node> closedList) {
        StringBuilder actions = new StringBuilder();
        Node currentNode = goalNode;
    
        // Start from the goal node and follow the parent links
        while (currentNode != null) {
            if (currentNode.getActionUsed() != '\0') {
                actions.insert(0, currentNode.getActionUsed());
            }
            int parentIdentifier = currentNode.getParentIdentifier();
    
            System.out.println("Current Node Identifier: " + currentNode.getIdentifier());
            System.out.println("Parent Identifier: " + parentIdentifier);
    
            currentNode = closedList.get(parentIdentifier);
        }
    
        System.out.println("Path: " + actions.toString());
        return actions.toString();
    }
    
}
