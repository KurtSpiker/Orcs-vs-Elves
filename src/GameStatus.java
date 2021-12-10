
public class GameStatus
{
    public static boolean debugModeOn = false;
    private int turnsInactive;
    public boolean currentTurnInactive;
    public GameStatus()
    {
        turnsInactive = 0;
        currentTurnInactive = true;
    }
    //"A couple of methods used to check for inactive turns"
    public int getTurnsInactive()
    {
        return turnsInactive;
    }
    public void resetTurnsInactive()
    {
        turnsInactive = 0;
    }
    public void incrementTurnsInactive()
    {
        turnsInactive++;
    }
}
