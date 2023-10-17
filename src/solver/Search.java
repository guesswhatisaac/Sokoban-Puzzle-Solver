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
            ArrayList<Node> successors = currentNode.generateSuccessors(rules, currentNode);

            // DEBUG
            for(int i = 0; i < successors.size(); i++){
                System.out.println("successor count: " + successors.size());
                successors.get(i).toStringMap();
                successors.get(i).toStringInfo();                
            }

            int x = 1;
            if(x == 1)
                return null;

            //

            for(int i = 0; i < successors.size(); i++){

                Node currentSuccessor = successors.get(i);

                if(currentSuccessor.isGoal()){
                    actionList = backtrackPath(currentSuccessor, closedList);
                    return actionList;
                }
                
                if(evaluateSuccessor(currentNode, currentSuccessor))
                    openList.add(currentSuccessor);

            }

            closedList.put(currentNode.getIdentifier(), currentNode);
        }
        
        return actionList;
    }

    private boolean evaluateSuccessor(Node parentNode, Node successorNode){

        int successorIdentifier = successorNode.getIdentifier();

        boolean shouldAdd = true;

        PriorityQueue<Node> openListClone = new PriorityQueue<Node>(openList);
    
        // if successor already in openList && openList node w/ same identifier < successor identifier
        for(int i = 0; i < openListClone.size(); i++){
            if(openList.poll().getFCost() < successorNode.getFCost())
                shouldAdd = false;
        }

        // if successor already in closedList && closedList node w/ same identifier < successor identifier
        if(closedList.containsKey(successorIdentifier) && closedList.get(successorIdentifier).getFCost() < successorNode.getFCost())
            shouldAdd = false;
        
        return shouldAdd;
    }

    public String backtrackPath(Node goalNode, Map<Integer, Node> closedList) {
        StringBuilder actions = new StringBuilder();
        Node currentNode = goalNode;
    
        // Start from the goal node and follow the parent links
        while (currentNode != null) {
            if (currentNode.getActionUsed() != '\0') {
                actions.insert(0, currentNode.getActionUsed());
            }
            int parentIdentifier = currentNode.getParentIdentifier();
            currentNode = closedList.get(parentIdentifier);
        }
    
        return actions.toString();
    }
}
