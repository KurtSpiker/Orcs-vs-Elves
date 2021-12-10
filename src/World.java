import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;


public class World
{
    public static final int SIZE = 10;
    public static final int ORCS_WIN = 0;
    public static final int ELVES_WIN = 1;
    public static final int DRAW = 2;
    public static final int CEASE_FIRE = 3;
    public static final int NOT_OVER = 4;
    public String option1 = "d";
    public String option2 = "D";

    private Entity [][] aWorld;
    private GameStatus gameState = new GameStatus();

  
    // Post-condition: the position of the characters in the
    // starting input file will determine the position of the
    // objects in the array. The type of character in the file
    // determines the appearance of the Entity(O or D) or if the
    // element is empty (spaces become null elements)
    public World()
    {
        aWorld = new Entity[SIZE][SIZE];
        int r;
        int c;
        for (r = 0; r < SIZE; r++)
        {
            for (c = 0; c < SIZE; c++)
            {
                aWorld[r][c] = null;
            }
        }
        aWorld = FileInitialization.read();
    }

    // Displays array elements one at a time one row per line
    // Each element is bound above, below, left and right by 
    // bounding lines
    //"I modified the Display to appropriately display the boundary lines"
    public void display()
    {
        int r = -1;
        int c = -1;
        int b = -1;
        for (r = 0; r < SIZE; r++)
        {   
            for (b = 0; b < SIZE; b++)
                System.out.print(" -");
                System.out.println();
            System.out.print("|");
            for (c = 0; c < SIZE; c++)
            {
                if(aWorld[r][c] == null)
                    System.out.print(" " + "|"); 
                else
                    System.out.print(aWorld[r][c].getAppearance() + "|"); 
            }

            System.out.println();
        }
        for (b = 0; b < SIZE; b++)
            System.out.print(" -");   
        System.out.println();
        
    }
    //"This is a check boundary check that we will be using when looking at adjacent indicies"
    private boolean checkIndices(int r, int c)
    {
        if(r < 0 || r >= SIZE || c < 0 || c >= SIZE || aWorld[r][c] == null)
        {
            return false;
        }
  
        return true;
    }
    
    //"Our starting point of the main while loop, also the place where we can keep the check for debug mode"
    //"It holds main functions like display , and gameloop which holds the actual function which determines actions"
    public void start()
    {        
        Scanner Scanner = new Scanner(System.in);
        display();
        while (true)
        {
            if (GameStatus.debugModeOn = true)
            {
                System.out.println("Debug mode is active");
            }
            
            System.out.print("Press enter to continue: ");
            String input = Scanner.nextLine();
            if (input == option1 || input == option2)
            {
                GameStatus.debugModeOn = true; 
            }
            Scanner.close();
            gameState.currentTurnInactive = true;
            gameloop();
            display();
           //"A couple checks to see if the game has recived a specific ending at the end of each full rotation "
            if (gameState.currentTurnInactive)
            {
                gameState.incrementTurnsInactive();
            }  
            else
            {
                gameState.resetTurnsInactive();
            }        
            if (checkGameOver())
            {
                break;
            }
        }   
    }
    //"Our function for determining if and entity is removed from battle due to lack of hitpoints"
    private void unitDiesCheck(int r , int c)
    {
        if (aWorld[r][c].getHitPoints() <= 0)
        {
            aWorld[r][c] = null;
        }
    }
    //"Our debug string for Attacking"
    public void debugAttack(int rDef, int cDef, int rAtk, int cAtk)
    {
        if (GameStatus.debugModeOn = true)
        {
            System.out.println("ATTACK");
            System.out.println("--Entitiy " + aWorld[rAtk][cAtk].getAppearance() + " @ (r/c) (" + rAtk + "/" + cAtk + ") attacking for " + aWorld[rAtk][cAtk].getDamage() + " damage");
            System.out.println("--Opponent " + aWorld[rDef][cDef].getAppearance() + " with (hp): " + aWorld[rDef][cDef].getHitPoints() + " @ (row/column): ("+ rDef + "/" + cDef + ") Damage=" + aWorld[rAtk][cAtk].getDamage() + "  new hp: " + (aWorld[rDef][cDef].getHitPoints() - aWorld[rAtk][cAtk].getDamage()));
        }
    }
    //"Debug string for the inactive count"
    public void debugInactive()
    {
        if ((GameStatus.debugModeOn = true) && (gameState.getTurnsInactive() >= 1))
        {
            System.out.println("The current number of inactive concecutive turns is " + gameState.getTurnsInactive());
        }
    }
    //"Debug string for the movement of entities"
    public void debugMove(int r, int c, int rDest, int cDest)
    {
        System.out.println("MOVEMENT");
        System.out.println("--Checking move for (r/c):=(" + r + "/" + c + ")");
        System.out.println("--Destination (r/c):=(" + rDest + "/" + cDest + ")");
        System.out.println("--Entity at (r/c):=(" + r + "/" + c + ")");
        if ((rDest >= 0 && cDest >= 0) && (rDest < 10 && cDest < 10) && (aWorld[rDest][cDest] == null))
        {
            System.out.println("--will move to the empty location (r/c):=(" + rDest + "/" + cDest + ")");
        }
        else
        {
            System.out.println("--will not move, because the position is occupied");
        }
    }
    
    //"Take turn is the main option select in the code, It first determines what the entity is, and then "
    //" it runs through the options of the index in the array. First checking all available spots for attack"
    //"and then it will lastly check the corresponding space given the entitys movement and decides to move or not"

    private void takeTurn(int r, int c)
    {
        //"identity check"
        if (aWorld[r][c].getAppearance() == 'E')
        {
            //"Attack check"
          if (checkIndices(r-1,c-1) && aWorld[r-1][c-1].getAppearance() == 'O')
          { 
            debugAttack(r,c,r-1,c-1);
            aWorld[r-1][c-1].takeHit();
            unitDiesCheck(r-1,c-1);
            gameState.currentTurnInactive = false;
          }
          else if(checkIndices(r-1,c) && aWorld[r-1][c].getAppearance() == 'O')
          {
            debugAttack(r,c,r-1,c);  
            aWorld[r-1][c].takeHit();
            unitDiesCheck(r-1,c);
            gameState.currentTurnInactive = false;
          }
          else if(checkIndices(r-1,c+1) && aWorld[r-1][c+1].getAppearance() == 'O')
          {
            debugAttack(r,c,r-1,c+1);  
            aWorld[r-1][c+1].takeHit();
            unitDiesCheck(r-1,c+1);
            gameState.currentTurnInactive = false;
          }
          else if(checkIndices(r,c-1) && aWorld[r][c-1].getAppearance() == 'O')
          {
            debugAttack(r,c,r,c-1);  
            aWorld[r][c-1].takeHit();
            unitDiesCheck(r,c-1);
            gameState.currentTurnInactive = false;
          }
          else if(checkIndices(r,c+1) && aWorld[r-1][c-1].getAppearance() == 'O')
          {
            debugAttack(r,c,r,c+1);  
            aWorld[r][c+1].takeHit();
            unitDiesCheck(r,c+1);
            gameState.currentTurnInactive = false;
          }
          else if(checkIndices(r+1,c-1) && aWorld[r+1][c-1].getAppearance() =='O')
          {
            debugAttack(r,c,r+1,c-1);  
            aWorld[r+1][c-1].takeHit();
            unitDiesCheck(r+1,c-1);
            gameState.currentTurnInactive = false;
          }
          else if(checkIndices(r+1,c) && aWorld[r+1][c].getAppearance() == 'O')
          {
            debugAttack(r,c,r+1,c);
            aWorld[r+1][c].takeHit();
            unitDiesCheck(r+1,c);
            gameState.currentTurnInactive = false;
          }
          else if(checkIndices(r+1,c+1) && aWorld[r+1][c+1].getAppearance() == 'O')
          {
            debugAttack(r,c,r+1,c+1);  
            aWorld[r+1][c+1].takeHit();
            unitDiesCheck(r+1,c+1);
            gameState.currentTurnInactive = false;
          }
          //"And finally movement check and proceedings"
          else if(r-1 >= 0 && c-1 >= 0 && (aWorld[r-1][c-1] == null))
          {
            debugMove(r,c,r-1,c-1);
            aWorld[r - 1][c - 1] = aWorld[r][c];
            aWorld[r][c] = null;
            gameState.currentTurnInactive = false;
          }
        }
        //"This then proceeds the same way but if the index is an orc"
        else if (aWorld[r][c].getAppearance() == 'O')
        {   //"orc attack option"
            if (checkIndices(r-1,c-1) && aWorld[r-1][c-1].getAppearance() == 'E')
            {
                debugAttack(r,c,r-1,c-1);
                aWorld[r-1][c-1].takeHit();
                unitDiesCheck(r-1,c-1);
                gameState.currentTurnInactive = false; 
            }
            else if(checkIndices(r-1,c) && aWorld[r-1][c].getAppearance() == 'E')
            {
                debugAttack(r,c,r-1,c);
                aWorld[r-1][c].takeHit();
                unitDiesCheck(r-1,c);
                gameState.currentTurnInactive = false;
            }
            else if(checkIndices(r-1,c+1) && aWorld[r-1][c+1].getAppearance() == 'E')
            {
                debugAttack(r,c,r-1,c+1);
                aWorld[r-1][c+1].takeHit();
                unitDiesCheck(r-1,c+1);
                gameState.currentTurnInactive = false;
            }
            else if(checkIndices(r,c-1) && aWorld[r][c-1].getAppearance() == 'E')
            {
                debugAttack(r,c,r,c-1);
                aWorld[r][c-1].takeHit();
                unitDiesCheck(r,c-1);
                gameState.currentTurnInactive = false;
            }
            else if(checkIndices(r,c+1) && aWorld[r][c+1].getAppearance() == 'E')
            {
                debugAttack(r,c,r,c+1);
                aWorld[r][c+1].takeHit();
                unitDiesCheck(r,c+1);
                gameState.currentTurnInactive = false;
            }
            else if(checkIndices(r+1,c-1) && aWorld[r+1][c-1].getAppearance() == 'E')
            {
                debugAttack(r,c,r+1,c-1);
                aWorld[r+1][c-1].takeHit();
                unitDiesCheck(r+1,c-1);
                gameState.currentTurnInactive = false;
            }
            else if(checkIndices(r+1,c) && aWorld[r+1][c].getAppearance() == 'E')
            {
                debugAttack(r,c,r+1,c);
                aWorld[r+1][c].takeHit();
                unitDiesCheck(r+1,c);
                gameState.currentTurnInactive = false;
            }
            else if(checkIndices(r+1,c+1) && aWorld[r+1][c+1].getAppearance() == 'E')
            {
                debugAttack(r,c,r+1,c+1);
                aWorld[r+1][c+1].takeHit();
                unitDiesCheck(r+1,c+1);
                gameState.currentTurnInactive = false;
            }
            //"orc movement option"
            else if(r+1 < SIZE && c+1 < SIZE  && (aWorld[r+1][c+1] == null))
            {
                debugMove(r,c,r+1,c+1);
                aWorld[r + 1][c + 1] = aWorld[r][c];
                aWorld[r][c] = null;
                gameState.currentTurnInactive = false;
            } 
        
        debugInactive();    
        }   
    }

    private boolean checkGameOver()
    {   //"This section is for counters related to battles ending due to abscence of the opposite entity"
        int elfCount = 0; 
        int orcCount = 0;
        
        for (int x = 0; x < SIZE; x++)
        {
            for (int y = 0; y < SIZE; y++)
            {
                if (aWorld[x][y] != null)
                {
                    if (aWorld[x][y].getAppearance() == 'O')
                    {
                        orcCount++;
                    }
                    else if (aWorld[x][y].getAppearance() == 'E')
                    {
                        elfCount++;
                    }
                } 
            } 
        }
        if((elfCount == 0) && (orcCount == 0))
        {
            System.out.print("No entities remain, the game is a draw");
            return true;
        } 
        else if(orcCount == 0)
        {
            System.out.print("No Orcs remain, the Elves have won the battle!"); 
            return true; 
        }
                
        else if(elfCount == 0)
        {
            System.out.print("No Elves remain, the Orcs have won the battle!");
            return true;  
        }
        else if (gameState.getTurnsInactive() >= 10)
        {
            System.out.print("A Ceasefire has been called by both sides!");
            return true;
        }
        return false;
    }
    //"This is a check to see if a unit has moved prior, ie to prevent an orc from moving form the top of the"
    //"screen to the bottom by assigning each index a tag , and then removing them at the end of a full turn"
    private void gameloop()
    {
        for (int r = 0; r < SIZE; r++)
        {
            for (int c = 0; c < SIZE; c++)
            {
                if ((aWorld[r][c] != null) && (aWorld[r][c].getTurnCheck() == 0))
                {
                    aWorld[r][c].turnCheck = 1;
                    takeTurn(r,c); 
                } 
            }    
        }
        for (int t = 0; t < SIZE; t++)
        {
            for (int u = 0; u < SIZE; u++)
            {
                if ((aWorld[t][u] != null) && (aWorld[t][u].getTurnCheck() == 1))
                {
                    aWorld[t][u].setTurnCheck(0);
                }
            }
        }                             
    }
}