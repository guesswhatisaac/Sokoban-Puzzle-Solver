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
        ArrayList<Position> targets = map.getTargets();

        for (Position targetPos : targets) {
            markReachable(visited, rules, map, targetPos.getX(), targetPos.getY());
        }

        return visited;
    }

    private void markReachable(boolean[][] visited, GameLogic rules, MapData map, int x, int y) {
        if (!rules.isValidCrateMovement('l', x, y, map.getMapData()) &&
            !rules.isValidCrateMovement('r', x, y, map.getMapData()) &&
            !rules.isValidCrateMovement('u', x, y, map.getMapData()) && 
            !rules.isValidCrateMovement('d', x, y, map.getMapData())) {
                System.out.println("simple deadlock found");
                visited[x][y] = false;
                return;
            }
            
        visited[x][y] = true;

        markReachable(visited, rules, map, x - 1, y);
        markReachable(visited, rules, map, x + 1, y);
        markReachable(visited, rules, map, x, y - 1);
        markReachable(visited, rules, map, x, y + 1);
    }

    private boolean freezeDeadlockDetector(Node node, GameLogic rules) {

        return false;
    }

    private void addToState(Node node){
        // if state is deadlock, add to hashset
    }




}