package solver;

import java.util.*;

/* Notes
 * - Add corral deadlocks
 */

public class DeadlockDetector {

    // uses generateIdentifier() from GameLogic to create key
    HashSet<Integer> deadlockedStates = new HashSet<>();

    // node to access level data and rules to access generateidentifiers
    public boolean isDeadlocked(Node node, GameLogic rules, MapData map) {

        // check hashSet for deadlocked states, if there return true
        if(deadlockedStates.contains(node.getIdentifier())){
            System.out.println("Deadlock encountered already, return false.");
            return true;
        }
        else if(freezeDeadlockDetector(node, rules)){ // else, check if node is deadlock
            System.out.println("Freeze deadlock detected. Added to states and return false.");
            addToState(node);
            return true;
        }

        return false;
    }
    
    public boolean[][] simpleDeadlockDetector(GameLogic rules, MapData map) {
        
        boolean[][] visited = new boolean[map.getWidth()][map.getHeight()]; // stores nondeadlock positions for a*
        boolean[][] searched = new boolean[map.getWidth()][map.getHeight()]; // avoids loop-checking in markReachable
        ArrayList<Position> targets = map.getTargets();
    
        for (Position targetPos : targets)
            markReachable(visited, rules, map, targetPos.getY(), targetPos.getX(), searched); 
                
        return visited;
    }
    
    private void markReachable(boolean[][] visited, GameLogic rules, MapData map, int x, int y, boolean[][] searched) {
        // System.out.println("function x: " + x + " y: " + y);

        // if within borders and map position not yet evaluated,
        if (x >= 0 && y >= 0 && x < map.getWidth() && y < map.getHeight() && !searched[x][y]) {
    
            searched[x][y] = true;
    
            if (!rules.isValidCrateMovement(x, y, map)) // checks for deadlocks,
                visited[x][y] = false; // if found; invalid
            else
                visited[x][y] = true; // none found; valid
            
            // check for possible reachable positions
            markReachable(visited, rules, map, x - 1, y, searched);
            markReachable(visited, rules, map, x + 1, y, searched);
            markReachable(visited, rules, map, x, y - 1, searched);
            markReachable(visited, rules, map, x, y + 1, searched);
        }
    }
    
    private boolean freezeDeadlockDetector(Node node, GameLogic rules) {

        return false;
    }

    private void addToState(Node node){
        // if state is deadlock, add to hashset
    }




}