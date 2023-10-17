package solver;

import java.util.HashSet;

/* Notes
 * - Add corral deadlocks
 */

public class DeadlockDetector {

    // uses generateIdentifier() from GameLogic to create key
    HashSet<Integer> deadlockedStates = new HashSet<>();

    // node to access level data and rules to access generateidentifiers
    public boolean isDeadlocked(Node node, GameLogic rules) {

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

    private boolean freezeDeadlockDetector(Node node, GameLogic rules) {

        return false;
    }

    private void addToState(Node node){
        // if state is deadlock, add to hashset
    }




}
