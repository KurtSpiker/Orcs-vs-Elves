

public class Entity
{
    public static final char DEFAULT_APPEARANCE = 'X';
    public static final char ELF = 'E';
    public static final char EMPTY = ' ';
    public static final char ORC = 'O';
    public static final int DEFAULT_HP = 1;
    public static final int ORC_DAMAGE = 3;
    public static final int ELF_DAMAGE = 7;
    public static final int ORC_HP = 10;
    public static final int ELF_HP = 15;
    public int turnCheck = 0;

    private char appearance;
    private int hitPoints;
    private int damage;

    public Entity()
    {
        setAppearance(DEFAULT_APPEARANCE);
        setHitPoints(DEFAULT_HP);
    }

    public Entity(char newAppearance)
    {
        appearance = newAppearance;
        hitPoints = DEFAULT_HP;
        damage = ORC_DAMAGE;
    }

    public Entity(char newAppearance, int newHitPoints, int newDamage)
    {
        setAppearance(newAppearance);
        setDamage(newDamage);
        setHitPoints(newHitPoints);
    }

    public char getAppearance()
    {
        return(appearance);
    }

    public int getDamage()
    {
        return(damage);
    }

    public int getHitPoints()
    {
        return(hitPoints);
    }

    private void setAppearance(char newAppearance)
    {
        appearance = newAppearance;
    }

    public int getTurnCheck()
    { 
        return(turnCheck);
    }

    public void setTurnCheck(int newTurnCheck)
    {
        turnCheck = newTurnCheck;
    }

    public void takeHit()
    {
        if (appearance == 'O')
        {
            setHitPoints(getHitPoints() - ELF_DAMAGE);
            
        }
        else if (appearance == 'E')
        {
            setHitPoints(getHitPoints() - ORC_DAMAGE);
        }
    }  

    private void setDamage(int newDamage)
    {
        if (newDamage < 1) 
        {
            System.out.println("Damage must be 1 or greater");
        }
        else
        {
            damage = newDamage;
        }        
    }

    private void setHitPoints(int newHitPoints)
    {
        hitPoints = newHitPoints;
    }


}