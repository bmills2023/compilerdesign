import java.io.*;
import java.util.*;
public class FSM
{
  public static void main(String[] args) throws IOException
  {
    File inFile=new File("FSM.txt");
    if(!inFile.exists())
    {
      System.out.println("File does not exist.");
      System.exit(0);
    }//end if file doesnt exist
    Scanner input = new Scanner(inFile);
    int numRows=15;
    int numCols=11;
    int[][] fsm = new int[numRows][numCols];
    for(int i=0; i<numRows;++i)
    {
      for(int j=0; j<numCols; ++j)
      {
        int currentInt=input.nextInt();
        fsm[i][j] =currentInt;
        //System.out.println(currentInt);
      }
    }//while file is not yet fully read
    for(int i=0; i<numRows;++i)
    {
      for(int j=0; j<numCols; ++j)
      {
        System.out.print(fsm[i][j]);
      }
      System.out.println();
    }
    
    
  }//end main
  
  
  
  
}//end class