import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileInitialization
{
    public static Entity[][] read() {
        Entity [][] temp = new Entity[World.SIZE][World.SIZE];
        String line = null;
        int r, c;
        char letter;
        BufferedReader br = null;
        FileReader fr = null;
        FileContainer aContainer = null;
        try {
            aContainer = checkingFile(br,fr);
            br = aContainer.getBufferedReader();
            fr = aContainer.getFileReader();
            line = br.readLine();
            if (line == null)
                System.out.println("Empty file, nothing to read");
            r = 0;
            while (line != null) {   
                c = 0;
                while(c < World.SIZE) {
                    letter = line.charAt(c);
                    temp[r][c] = createEntity(letter);
                    c = c + 1;
                }
                line = br.readLine();
                if (line != null)
                    r = r + 1;
            }
            fr.close();
        }
        catch (FileNotFoundException e) {
            String location = System.getProperty("user.dir");
            System.out.println("Input file not in " + location);
        }
        catch (IOException e) {
            System.out.println("General file input problem " + 
                               "reading starting positions");
        }
        return(temp);
    }
    
    private static FileContainer checkingFile(BufferedReader br, FileReader fr)
    {
        FileContainer aContainer = null;    
        Scanner in = new Scanner(System.in);
        String filename = null;
        boolean fileFound = false;
        while (fileFound == false)
        {
            try
            {
                System.out.print("Name of file containing starting positions: ");
                filename = in.nextLine();   
	        fr = new FileReader(filename);
                br = new BufferedReader(fr);
                fileFound = true;
            }
            catch (FileNotFoundException ex)
            {
                String location = System.getProperty("user.dir");
                System.out.println("Input file not in " + location);
            }

         }
         aContainer = new FileContainer(br,fr); 
         return(aContainer);
    }
 


    private static Entity createEntity(char letter)
    {
        Entity e = null;
        switch(letter)
        {
            case Entity.ORC:
                e = new Entity(Entity.ORC,Entity.ORC_HP,Entity.ORC_DAMAGE);
                break;

            case Entity.ELF:
                e = new Entity(Entity.ELF,Entity.ELF_HP,Entity.ELF_DAMAGE);
                break;
            case Entity.EMPTY:
                e = null;
                break;
            default:
                System.out.println("Error: Invalid character [" + letter + 
                                   "] in file");
        }
        return(e);
    }
}