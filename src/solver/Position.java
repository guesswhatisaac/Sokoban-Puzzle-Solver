package solver;

/* Notes
 * - updatePosition does not account for for negative positions (ex. (-1, 0)); may cause issues on gameLogic
 */

public class Position {
    
    int x;
    int y;

    public Position(int xInput, int yInput){
        this.x = xInput;
        this.y = yInput;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void updateX(int value){
        this.x += value;
    }

    public void updateY(int value){
        this.y += value;
    }


    // compares if positions are equal or not
    public boolean isEqual(Position object){

        if(this.x == object.getX() && this.y == object.getY()){
            return true;
        }
        else{
            return false;
        }

    }

    public void updatePosition(char move){

        switch(move){
            case 'u': y = y-1; break;
            case 'd': y = y+1; break;
            case 'l': x = x-1; break;
            case 'r': x = x+1; break;
        }
    }

}
