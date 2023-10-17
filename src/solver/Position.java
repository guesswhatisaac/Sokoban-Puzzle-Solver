package solver;

/* Notes
 * - updatePosition does not account for for negative positions (ex. (-1, 0)); may cause issues on gameLogic
 * - note that x and y are interchanged in some functions. this is due to the Node constructor's logic in reading the file
 */


public class Position {
    
    int x;
    int y;

    public Position(int xInput, int yInput){
        this.x = xInput;
        this.y = yInput;
    }

    public int getX(){ // Reversed due to board logic
        return this.y;
    }

    public int getY(){ // Reversed due to board logic
        return this.x;
    }

    // compares if positions are equal or not
    public boolean isEqual(Position object){
 
        if(this.x == object.getY() && this.y == object.getX())
            return true;
        else 
            return false;

    }

    public void updatePosition(char move){

        switch(move){
            case 'u': this.y = this.y-1; break;
            case 'd': this.y = this.y+1; break;
            case 'l': this.x = this.x-1; break;
            case 'r': this.x = this.x+1; break;
        }
    }

    public void toStringInfo(){ 
        System.out.println("Pos: " + "("+ y + ","+ x +")");
    }

}
