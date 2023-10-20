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
        boolean[][] visited = new boolean[map.getWidth()][map.getHeight()];
        boolean[][] searched = new boolean[map.getWidth()][map.getHeight()];
        ArrayList<Position> targets = map.getTargets();
    
        for (Position targetPos : targets) {
            System.out.println("target started");
            markReachable(visited, rules, map, targetPos.getY(), targetPos.getX(), searched); 
        }
    
        return visited;
    }
    
    private void markReachable(boolean[][] visited, GameLogic rules, MapData map, int x, int y, boolean[][] searched) {
        System.out.println("function x: " + x + " y: " + y);
    
        if (x >= 0 && y >= 0 && x < map.getWidth() && y < map.getHeight() && !searched[x][y]) {
            System.out.println("valid x and y, x and y within map borders, placement not yet searched\n");
    
            searched[x][y] = true;
    
            if (!rules.isValidCrateMovement(y, x, map.getMapData())) { 
                System.out.println("simple deadlock found at x: " + y + ", y: " + x + " invalid\n");
                visited[x][y] = false;
            } else {
                visited[x][y] = true;
                System.out.println("visited and valid\n");
            }
    
            if (x - 1 > 0)
                markReachable(visited, rules, map, y, x - 1, searched); 
            else
                System.out.println("Cannot move left. Out of bounds");
    
            if (x + 1 < map.getWidth())
                markReachable(visited, rules, map, y, x + 1, searched); 
            else
                System.out.println("Cannot move right. Out of bounds");
    
            if (y - 1 > 0)
                markReachable(visited, rules, map, y - 1, x, searched);
            else
                System.out.println("Cannot move up. Out of bounds");
    
            if (y + 1 < map.getHeight())
                markReachable(visited, rules, map, y + 1, x, searched); 
            else
                System.out.println("Cannot move down. Out of bounds");
    
        } else {
            System.out.println("border or searched already\n");
        }
    }
    
    private boolean freezeDeadlockDetector(Node node, GameLogic rules) {

        return false;
    }

    private void addToState(Node node){
        // if state is deadlock, add to hashset
    }




}