/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package incremantalmining;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;


/*class MyReader extends BufferedReader implements java.io.Serializable
{
	//MyReader(){}
	public MyReader(FileReader f){ super(f);}
}
 */
class transactional_array implements java.io.Serializable
{
	public int prevele[]=new int[2];
        public int nextele[]=new int [2];
	boolean flag = false;  //for future use
}
 class Inverted_Matrix implements java.io.Serializable
{
	public int idx[] = new int[5];  //idx[0]=item,idx[1]=support, idx[2]=location idx[3]=original item idx[3]=original item frequency of transaction array after sorting   for storing itemname ans its support
	public Vector<transactional_array> trans_arr=new Vector<transactional_array>();
     
     
        //public transactional_array trans_arr[]=null; // storing row of transactional array
}

public class Inverted_Matrix_Gen implements java.io.Serializable
{
    	public short  dataArray[][]=null;
	protected boolean inputFormatOkFlag = true;
public static long startTime1;
public static long endTime1;
        public Inverted_Matrix IM[] = null;
    protected String  fileName   = null;
    /** Command line argument for number of columns. */	
    protected int     numCols    = 0;
    /** Command line argument for number of rows. */
    protected int     numRows    = 0;
    /** Command line argument for % support (default = 20%). */
    protected double  support    = 20.0;
    /** Minimum support value in terms of number of rows. */
    protected double  minSupport = 0;
    /** Command line argument for % confidence (default = 80%). */
    protected double  confidence = 80.0;
    /** The number of one itemsets (singletons). */
    protected int numOneItemSets = 0;
    
    
   protected long fileReadStartTime;
   protected long fileReadTime;
    
   protected long fileWriteStartTime;
   protected long fileWriteTime;
   
   protected boolean errorFlag  = true;
    /** Input format OK flag( default = <TT>true</TT>). */
 //   protected boolean inputFormatOkFlag = true;

  /** Flag to indicate whether system has data or not. */
    private boolean haveDataFlag = false;
    /** Flag to indicate whether input data has been sorted or not. */
    private boolean isOrderedFlag = false;
    /** Flag to indicate whether input data has been sorted and pruned or 
    not. */
    private boolean isPrunedFlag = false;
    
    // Other fields
    
    /** The input stream. */
    protected transient BufferedReader fileInput;
    /** The file path */
    protected transient File filePath = null;
	//int count_arr[][] = null;

protected long startTime;
    protected long endTime;
    
     int NoOfFreqItem=0;
     
private short[] dataArrayLine;  //store data of one line at a time in short data type

     
     public static void main(String args[]) throws Exception
     {
        startTime1=System.currentTimeMillis();
         
         Inverted_Matrix_Gen rd = new Inverted_Matrix_Gen();
        rd.genInvert_Mat();
//        rd.displayInvertedMatrix();
     endTime1=System.currentTimeMillis();
     
     //System.out.println("Time to generate Inverted Matrix is"+(endTime1-startTime1));
     
     
     }
    
     
     public Vector <Inverted_Matrix> genInvert_Mat() throws Exception
     {
         
         Vector <Inverted_Matrix> IM=new Vector<Inverted_Matrix>();
         int rowIndex=0;
         
         fileReadStartTime=System.currentTimeMillis();
         File fi1=new File("D:/IM_Objects/numRows.data");
        if (fi1.exists())
        {
            fileReadStartTime=System.currentTimeMillis();
            
            FileInputStream fis1=new FileInputStream("D:/IM_Objects/numRows.data");
            ObjectInputStream ois1=new ObjectInputStream(fis1);
            numRows=(int) ois1.readObject();
        }
        System.out.println("*******"+numRows); 
         
//	fileName="test2.txt";
	//` Open the file
	if (filePath==null) openFileName(fileName);
	else openFilePath();
	
	// get first row.
        
	String line = fileInput.readLine();
        int loc=0;
        
        
       File fi=new File("D:/IM_Objects/IM.data");
        if (fi.exists())
        {
            FileInputStream fis=new FileInputStream("D:/IM_Objects/IM.data");
            ObjectInputStream ois=new ObjectInputStream(fis);
            IM=(Vector<Inverted_Matrix>) ois.readObject();
        }
        
        fileReadTime=System.currentTimeMillis()-fileReadStartTime;
        
        System.out.println("Time to read file is ----------> "+ fileReadTime);
        
        // FileInputStream fis=new FileInputStream("d:/IM.data");
        //ObjectInputStream ois=new ObjectInputStream(fis);
        //IM=     (Vector<Inverted_Matrix>) ois.readObject();
                
        
        //displayInvertedMarix(IM);
        
        //System.out.println("New IM is ");
        
        
	while (line != null) {
            checkLine(rowIndex+1, line);     
            if(!inputFormatOkFlag) break;  // check the transaction should not contain other than digit and whitespace(i.e contains nuberical items only)
          
            StringTokenizer dataLine = new StringTokenizer(line);
            int numberOfTokens = dataLine.countTokens();
	    if (numberOfTokens == 0) break;
	    // Convert input string to a sequence of short integers
	    short[] dataArrayLine = binConversion(dataLine,numberOfTokens);
	    // Check for "null" input
             //aaaaa //if (!checkLineOrdering(rowIndex+1, dataArrayLine)) break;   //item in the transactions  shold be ascending order order order of its numberical value
	    if (dataArrayLine != null) {
	        
//                for (int colIndex=0;colIndex<dataArrayLine.length;colIndex++)
//				System.out.print(dataArrayLine[colIndex]+"\t");
//                System.out.print("\n");
               
                
                Inverted_Matrix I,prevI;//=new Inverted_Matrix();
                
                
                
                
        
        //displayInvertedMarix(IM);
        
        //System.out.println("After Reading");
        
                short prevItem=-1;
                for (int colIndex=0;colIndex<dataArrayLine.length;colIndex++)
                {
                    short item=dataArrayLine[colIndex];
                    
                    I=getItemFromIM(item, IM);
                    if(I!=null)
                   {
                        I.idx[1]++;
                        I.idx[4]++;
                        transactional_array ta=new transactional_array();
                        if(prevItem==-1)
                        {       ta.prevele[0]=-1;
                                ta.prevele[1]=-1;
                                I.trans_arr.add(ta);
                        //        IM.add(I);
                        }
                        else
                        {
                            prevI=getItemFromIM(prevItem, IM);
                            ta.prevele[0]=IM.indexOf(prevI);
                            ta.prevele[1]=prevI.trans_arr.size()-1;
                            I.trans_arr.add(ta);
                           // IM.add(I);
                            prevI.trans_arr.lastElement().nextele[0]=IM.indexOf(I);
                            prevI.trans_arr.lastElement().nextele[1]=I.trans_arr.size()-1;
                        }
                   }
                    else
                    {
                        I=new Inverted_Matrix();
                        I.idx[0]=I.idx[3]=item;
                        I.idx[1]=1;
                        I.idx[2]=loc++;
                        I.idx[4]=1;
                        transactional_array ta=new transactional_array();
                        IM.add(I);
                        if(prevItem==-1)
                        {       ta.prevele[0]=-1;
                                ta.prevele[1]=-1;
                                I.trans_arr.add(ta);
          //                      IM.add(I);
                                
                        }
                        else
                        {
                            prevI=getItemFromIM(prevItem, IM);
                            ta.prevele[0]=IM.indexOf(prevI);
                            ta.prevele[1]=prevI.trans_arr.size()-1;
                            I.trans_arr.add(ta);
        //                    IM.add(I);
                            prevI.trans_arr.lastElement().nextele[0]=IM.indexOf(I);
                            prevI.trans_arr.lastElement().nextele[1]=I.trans_arr.size()-1;
                        }
                    }
                    
                    if(colIndex==dataArrayLine.length-1)
                    {
                                I.trans_arr.lastElement().nextele[0]=-1;
                                I.trans_arr.lastElement().nextele[1]=-1;
                    }
                    prevItem=item;
                
                }
                
	     }
            rowIndex++;
            line = fileInput.readLine();
	    }
        
        numRows=numRows+rowIndex;
	System.out.println(numRows);
	// Close file
	closeFile();
        
        fileWriteStartTime=System.currentTimeMillis();
        
        FileOutputStream fos=new FileOutputStream("d:/IM_Objects/IM.data");
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(IM);
        
        
        
        
        FileOutputStream fos1=new FileOutputStream("d:/IM_Objects/numRows.data");
        ObjectOutputStream oos1=new ObjectOutputStream(fos1);
        oos1.writeObject(numRows);
        
        fileWriteTime=System.currentTimeMillis()-fileWriteStartTime;
        System.out.println("Time to write file is ------------>"+ fileWriteTime);
        
        //    return IM;
        sortIndex(IM);
        
        
        
      displayInvertedMarix(IM);
    //System.out.println("size of In in Number of Bytes: "+(float)sizeOf(IM).length/(1024*1024) +"MB");
        //FileOutputStream fos=new FileOutputStream("d:/IM.data");
        //ObjectOutputStream oos=new ObjectOutputStream(fos);
        //oos.writeObject(IM);
        return IM;
     }
     
     
     
     public static byte[] sizeOf(Object obj) throws java.io.IOException
{
ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject);
objectOutputStream.writeObject(obj);
objectOutputStream.flush();
objectOutputStream.close();
byteObject.close();

return byteObject.toByteArray();
}
     
     
     public void sortIndex(Vector<Inverted_Matrix>IM)
     {
         
        
                boolean isOrdered;
		int index;

		do
		{
			isOrdered = true;
			index = 0;
			while (index < IM.size()-1)//countArray.length - 1))
			{
                                
				if (IM.get(index).idx[1] <= IM.get(index+1).idx[1]) {
                                    
                                         index++;
                                }
				else
				{
					isOrdered = false;
					// Swap
                                        int temp;
                                        
                                        temp=IM.get(index).idx[0];
                                        IM.get(index).idx[0]=IM.get(index+1).idx[0];
                                        IM.get(index+1).idx[0]=temp;
					
                                        temp=IM.get(index).idx[1];
                                        IM.get(index).idx[1]=IM.get(index+1).idx[1];
                                        IM.get(index+1).idx[1]=temp;
					
                                        
                                        temp=IM.get(index).idx[2];
                                        IM.get(index).idx[2]=IM.get(index+1).idx[2];
                                        IM.get(index+1).idx[2]=temp;
					
                                        
					index++;
				}
			}
		} while (isOrdered == false);
         }
     
     public void displayInvertedMarix(Vector <Inverted_Matrix> IM)
     {
            Iterator<Inverted_Matrix> itr=IM.iterator();
            while(itr.hasNext())
            {
                    Inverted_Matrix I=itr.next();
                    System.out.print(I.idx[0]+","+I.idx[1]+","+I.idx[2]+","+I.idx[3]+","+I.idx[4]+"  \t");
                    Iterator<transactional_array> ita=I.trans_arr.iterator();
                    while(ita.hasNext())
                    {
                        transactional_array ta=ita.next();
                        System.out.print(ta.prevele[0]+","+ta.prevele[1]+"<->"+ta.nextele[0]+","+ta.nextele[1]+"\t");
                    }
                    System.out.println();
                    
            }
     }
     
     protected Inverted_Matrix getItemFromIM(short item,Vector<Inverted_Matrix> IM )
     {
         Iterator<Inverted_Matrix> itr=IM.iterator();
         while(itr.hasNext())
         {
            Inverted_Matrix I =itr.next();
            if(I.idx[0]==item)
            {    
                //return (IM.get(IM.indexOf(I)));
                return I;
            }
         }
         
     return null;
     }
     
     void test(){
         
        
  //       inputDataSet();
		//rd.showData();
		//int count_arr[][]=null;
	//	count_arr = countSingles();
                
                //not require to count singles for incremetal IM generation
	///	int count_arr_orig [][]= new int[count_arr.length][2];
		//count_arr_orig=count_arr;


		//System.out.println("Frequency of Items:");
	/*	for (int i = 1; i < count_arr.length; i++)
		{
			for (int j = 0; j < count_arr[i].length; j++)
			{
				count_arr_orig[i][j] = count_arr[i][j];			
		//		System.out.print(count_arr[i][j]+"\t");
			}
		//	System.out.println();
		}
	
        * /
        */
	//	rd.sort();

		//no need to oreder the items according to its frequece
           //     orderCountArray(count_arr);
			
		    /*System.out.println("Acsending order of Items:");
			for (int i = 1; i < count_arr_orig.length; i++)
			{
			//	for (int j = 0; j < count_arr[i].length; j++)
			//	{
					System.out.print(count_arr_orig[i][0] + "\t");
					System.out.print(count_arr_orig[i][1] );
			//	}
				System.out.println();
			}
			*/
//		generate_IM();
//		rd.displayInvertedMatrix();

     }
     
     public Inverted_Matrix_Gen()
	{
		//fileName = "C:/disertation/test.txt";
		//fileName = "C:/disertation/test1.txt";
        // fileName = "C:/disertation/papertest.txt";
        //fileName="sample.txt";
	//fileName="samplepart1.txt";
        //fileName="samplepart2.txt";
        //fileName="tst1.txt";
          fileName="mashroom.txt";
          //fileName="mashroom1.txt";
          //fileName="mashroomF.txt";
          //fileName="mashroomS.txt";
            //fileName="mshr1.txt";
           //fileName="mshr2.txt";
        //fileName="tst2.txt";
        //fileName="tst3.txt";
        //fileName="tst23.txt";
        
        }

     
     
    /*public void inputDataSet()
	{    
        // Read the file
//	readFile();
        
	// Check ordering (only if input format is OK)		
	if (inputFormatOkFlag) {
	    if (checkOrdering()) {
                System.out.println("Number of records = " + numRows);
		countNumCols();
		System.out.println("Number of columns = " + numCols);
		minSupport = (numRows * support)/100.0;
        	System.out.println("Min support       = " + 
				twoDecPlaces(minSupport) + " (records)"); 
		}
	    else {
	        System.out.println("Error reading file: " + fileName + "\n");
		System.exit(1);
		}
	    }
	}
    
    protected void countNumCols() {
        int maxAttribute=0;
	
	// Loop through data array	
        for(int index=0;index<dataArray.length;index++) {
	    int lastIndex = dataArray[index].length-1;
	    if (dataArray[index][lastIndex] > maxAttribute)
	    		maxAttribute = dataArray[index][lastIndex];	    
	    }
	
	numCols        = maxAttribute;
	numOneItemSets = numCols; 	// default value only
	}
    */
       protected double twoDecPlaces(double number)
	{
		int numInt = (int)((number + 0.005) * 100.0);
		number = ((double)numInt) / 100.0;
		return (number);
	}


    
    
    
    protected boolean checkOrdering() {
        boolean result = true; 
	
	// Loop through input data
	for(int index=0;index<dataArray.length;index++) {
	    if (!checkLineOrdering(index+1,dataArray[index])) {	    	
		haveDataFlag = false;
		result=false;
		}
	    }
	    
	// Return 
	return(result);
	}
  
     private boolean checkLineOrdering(int lineNum, short[] itemSet) {
        for (int index=0;index<itemSet.length-1;index++) {
	    if (itemSet[index] >= itemSet[index+1]) {
		JOptionPane.showMessageDialog(null,"FILE FORMAT ERROR:\n" +
	       		"Attribute data in line " + lineNum + 
			" not in numeric order");
		return(false);
		}
	    }    
	
	// Default return
	return(true);
	}
    
    /*protected int[][] countSingles()
	{

		// Dimension and initialize count array

		int[][] countArray = new int[numCols+1][2];
		for (int index = 0; index < countArray.length; index++)
		{
			countArray[index][0] = index;
			countArray[index][1] = 0;
		}

		// Step through input data array counting singles and incrementing
		// appropriate element in the count array

		for (int rowIndex = 0; rowIndex < dataArray.length; rowIndex++)
		{
			if (dataArray[rowIndex] != null)
			{
				for (int colIndex = 0; colIndex < dataArray[rowIndex].length;
							colIndex++)
					countArray[dataArray[rowIndex][colIndex]][1]++;
			}
		}

		// Return

		return (countArray);
	}
*/
	  
    
    /*private void orderCountArray(int[][] countArray)
	{
				int attribute, quantity;
				boolean isOrdered;
		int index;

		do
		{
			isOrdered = true;
			index = 1;
			while (index < (countArray.length - 1))
			{
				if (countArray[index][1] <= countArray[index + 1][1]) index++;
				else
				{
					isOrdered = false;
					// Swap
					attribute = countArray[index][0];
					quantity = countArray[index][1];
					countArray[index][0] = countArray[index + 1][0];
					countArray[index][1] = countArray[index + 1][1];
					countArray[index + 1][0] = attribute;
					countArray[index + 1][1] = quantity;
					// Increment index
					index++;
				}
			}
		} while (isOrdered == false);
	}

*/    

//	public void generate_IM()
//	{
//		IM = new Inverted_Matrix[numCols+1];
//		//IM.idx = count_arr;
//
//		for (int i = 0; i < IM.length; i++)
//		{
//			IM[i] = new Inverted_Matrix();
//			//IM[i].trans_arr = new transaction_Array[]
//
//		}
//
//			//		int last = count_arr.length-1;
//
//
//			Inverted_Matrix test = new Inverted_Matrix();
//		//System.out.println("numof rows in array: " + count_arr.length+"\tcols:" +numCols+"\tIML:"+IM.length);
//		//	System.out.println("Hello");
//	//	for (int row = 1; row < IM.length; row++)
//	//	{
////			IM[1].idx[0][0] = 10;  //item name(i.e. column name)
//			//IM[row].idx[0] = count_arr[row][0];  //item name(i.e. column name)
//			//IM[row].idx[1] = count_arr[row][1];  //support of frequency
//		//	System.out.println(IM[row].idx[0][0]+","+IM[row].idx[0][1]);
//
//			
//			
//			//last--;
//	//	}
// 
//                
//                //following loop is not usefull
//		for (int i = 1; i < IM.length; i++)
//		{
//		//	IM[i] = new Inverted_Matrix();
//			//IM[i].trans_arr = new transactional_array[IM[i].idx[1]+1];  // need user vector or ArrayList class in order to allocate memory
//                        IM[i].trans_arr=new Vector<transactional_array>();
//                        
//                   // for (int j = 1; j < IM[i].trans_arr.length; j++)
//			//	IM[i].trans_arr[j] = new transactional_array();
//
//                               
//		}
//
//
//		/*for (int row = 1; row < IM.length; row++)
//		{
//			//			IM[1].idx[0][0] = 10;  //item name(i.e. column name)
//			System.out.print(row + "\t");
//			System.out.print(IM[row].idx[0][0]+"\t");
//			System.out.println(IM[row].idx[0][1]);
//			
//			//last--;
//		}*/
//		/*
//		
//		 * System.out.println("Original array");
//		for (int row = 0; row < dataArray.length; row++)
//		{
//			for (int col = 0; col < dataArray[row].length; col++)
//			{
//				System.out.print(dataArray[row][col]);
//			}
//			System.out.println();
//		}
//		*/
//		//System.out.println("transacion sorted according to its frequency");
//
//		for (int row = 0; row < dataArray.length; row++)
//		//for (int row = 0; row < 1; row++)
//		{
//			short rowArray[] = new short[dataArray[row].length];
//			rowArray = dataArray[row];
//			/*System.out.println("\nBefore Sorting\n");
//			for (int i = 0; i < rowArray.length; i++)
//			{
//				System.out.print(rowArray[i]);
//			}
//		*/
//                        //not required for Incremental IM genreation
//		//	tranSort(rowArray,count_arr_orig);
//		/*	System.out.println("\nAfter Sorting\n");	
//			for (int i = 0; i < rowArray.length ; i++)
//		
//			{
//				System.out.print(rowArray[i]);
//			}
//		*/	
//		//	System.out.println("\nhello");
//			for (int i = 0; i < rowArray.length-1; i++)
//			{ 
//				int idx=findIndex(rowArray[i],IM);  //index for the current attribute in IM
//				int idx1=findIndex(rowArray[i+1],IM); //index for the next attribute in IM
//				int tidx=findTranIndex(IM[idx].trans_arr); //transaction index for the current attribute
//				int tidx1 = findTranIndex(IM[idx1].trans_arr); //transaction index for next the current attribute
//				//System.out.println(idx +"  "+ idx1 +"  "+ tidx +"  "+ tidx1);
//
//				IM[idx].trans_arr[tidx].ele[0] = idx1;
//				IM[idx].trans_arr[tidx].ele[1] = tidx1;
//				IM[idx].trans_arr[tidx].flag = true;
//                                
//                                
//			//	IM[idx].trans_arr[1].ele[0][0] = idx1;
//			//	IM[idx].trans_arr[1].ele[0][1] = tidx1;
//			//	IM[idx].trans_arr[1].flag = true;
//			}
//			int idx = findIndex(rowArray[rowArray.length-1], IM);  //index for the current attribute in IM
//			int tidx = findTranIndex(IM[idx].trans_arr); //transaction index for the current attribute
//			IM[idx].trans_arr[tidx].ele[0] = -1;
//			IM[idx].trans_arr[tidx].ele[1] = -1;
//			IM[idx].trans_arr[tidx].flag = true;
//
//		}
//			
//			/*
//			 
//			 Display complete Inverted Matrix
//			 
//			 */
//
//
//		
//		/*	System.out.println("Inveted Matrix ");
//			for (int i = 1; i < IM.length; i++)
//			{
//				System.out.print(i+"\t"+IM[i].idx[0][0] + "," + IM[i].idx[0][1] +"  ");
//				for (int j = 1; j < IM[i].trans_arr.length; j++)
//				{
//					System.out.print(IM[i].trans_arr[j].ele[0][0] + "," + IM[i].trans_arr[j].ele[0][1] + "  ");
//				}
//				System.out.println();
//			}
//
//		*/
//			
//			/*
//			for (int i = 0; i < rowArray.length; i++)
//				System.out.print(rowArray[i] + "\t");
//			System.out.println();
//			*/				
//		
//  }
//
//	void displayInvertedMatrix()
//	{
//		System.out.println("Inveted Matrix ");
//		for (int i = 1; i < IM.length; i++)
//		{
//			System.out.print(i + "\t" + IM[i].idx[0] + "," + IM[i].idx[1] + "  ");
//			for (int j = 1; j < IM[i].trans_arr.length; j++)
//			{
//				System.out.print(IM[i].trans_arr[j].ele[0] + "," + IM[i].trans_arr[j].ele[1] + "  ");
//			}
//			System.out.println();
//		}
//	
//	}
//
//	public int findTranIndex(transactional_array ta[])
//	{
//		int i;
//		for ( i = 1; i < ta.length; i++)
//			if (ta[i].flag == false)
//				break;
//		return i;		
//	}
//
//	public int findIndex(short a, Inverted_Matrix I[])
//	{
//		int i;
//		for ( i = 0; i < I.length; i++)
//		{
//			if (a == I[i].idx[0])
//				break;
//				//		return i;
//		}
//		return i;
//	}
//
//	public void tranSort(short trans[], int countArray[][])
//	{
//
//
//		short attribute;
//		boolean isOrdered;
//		int index;
//
//		do
//		{
//			isOrdered = true;
//			index = 0;
//		/*	System.out.println("before sorting len of: " + trans.length);
//			System.out.println("\nBefore Sorting");
//			for (int i = 0; i < trans.length ; i++)
//			{
//				System.out.print(trans[i]);
//			}
//		*/	
//			while (index < (trans.length -1))
//			{
//				if (countArray[trans[index]][1] <=  countArray[trans[index + 1]][1]) index++;
//				else
//				{
//					isOrdered = false;
//
//					attribute = trans[index];
//					trans[index] = trans[index + 1];
//					trans[index + 1] = attribute;
//					// Swap
//					/*attribute = countArray[index][0];
//					quantity = countArray[index][1];
//					countArray[index][0] = countArray[index + 1][0];
//					countArray[index][1] = countArray[index + 1][1];
//					countArray[index + 1][0] = attribute;
//					countArray[index + 1][1] = quantity;
//					 */
//					// Increment index
//					index++;
//				}
//			}
//		} while (isOrdered == false);
//		
//		/*System.out.println("After sorting len of: " + trans.length);
//
//		System.out.println("\nAfter Sorting\n");
//		for (int i = 0; i < trans.length ; i++)
//		{
//			System.out.print(trans[i]);
//		}*/
//	}
//	
//
//      public void readFile() {    
//        try {
//	    // Dimension data structure
//	    inputFormatOkFlag=true;
//	    //numRows = getNumberOfLines(fileName);
//	    if (inputFormatOkFlag) {
//	       // dataArray = new short[numRows][];	
//	        // Read file	
//		System.out.println("Reading input file: " + fileName); 
//	        readInputDataSet();
//		}
//	    else System.out.println("Error reading file: " + fileName + "\n");
//	    }
//	catch(IOException ioException) { 
//	    System.out.println("Error reading File");
//	    closeFile();
//	    System.exit(1);
//	    }	 
//	}    
//    
       protected void closeFile() {
        if (fileInput != null) {
	    try {
	    	fileInput.close();
		}
	    catch (IOException ioException) {
		//JOptionPane.showMessageDialog(this,"Error Closeing File", 
		//	 "Error: ",JOptionPane.ERROR_MESSAGE);
		}
	    }
	}

//      
//     // public void readInputDataSet() throws IOException {  
//        public void readInputDataLine() throws IOException {  
//        int rowIndex=0;
//	
//	// Open the file
//	if (filePath==null) openFileName(fileName);
//	else openFilePath();
//	
//	// get first row.
//	String line = fileInput.readLine();	
//	while (line != null) {
//	    StringTokenizer dataLine = new StringTokenizer(line);
//            int numberOfTokens = dataLine.countTokens();
//	    if (numberOfTokens == 0) break;
//	    // Convert input string to a sequence of short integers
//	    short[] dataArrayLine = binConversion(dataLine,numberOfTokens);
//	    // Check for "null" input
//	    if (dataArrayLine != null) {
//	        
//                
//                
//		}
//	    
//	    
//	    
//	    // get next line
//            line = fileInput.readLine();
//	    }
//	
//	// Close file
//	closeFile();
//	}	
//
//      
    protected short[] binConversion(StringTokenizer dataLine, 
    				int numberOfTokens) {
        short number;
	short[] newItemSet = null;
	
	// Load array
	
	for (int tokenCounter=0;tokenCounter < numberOfTokens;tokenCounter++) {
            number = new Short(dataLine.nextToken()).shortValue();
	    newItemSet = realloc1(newItemSet,number);
	    }
	
	// Return itemSet	
	
	return(newItemSet);
	}

     public short[] realloc1(short[] oldItemSet, short newElement)
	{

		// No old item set

		if (oldItemSet == null)
		{
			short[] newItemSet = { newElement };
			return (newItemSet);
		}

		// Otherwise create new item set with length one greater than old 
		// item set

		int oldItemSetLength = oldItemSet.length;
		short[] newItemSet = new short[oldItemSetLength+1];

		// Loop

		int index;
		for (index = 0; index < oldItemSetLength; index++)
			newItemSet[index] = oldItemSet[index];
		newItemSet[index] = newElement;

		// Return new item set

		return (newItemSet);
	}
//  
//    
//    
       private void openFilePath() {
	try {
	    // Open file
	    FileReader file = new FileReader(filePath);
	    fileInput = new BufferedReader(file);
	    }
	catch(IOException ioException) {
	    //JOptionPane.showMessageDialog(this,"Error Opening File", 
		//	 "Error: ",JOptionPane.ERROR_MESSAGE);
	    }
	}
//
//      
//      
      protected void openFileName(String nameOfFile) {
	try {
	    // Open file
	    FileReader file = new FileReader(nameOfFile);
	    fileInput = new BufferedReader(file);
	    }
	catch(IOException ioException) {
	    //JOptionPane.showMessageDialog(this,"Error Opening File", 
		//	 "Error: ",JOptionPane.ERROR_MESSAGE);
	    }
	}
//
//      
//      
//    //protected int getNumberOfLines(String nameOfFile) throws IOException {
//      protected int readLine(String nameOfFile) throws IOException {
//        int counter = 0;
//	
//	// Open the file
//	if (filePath==null) openFileName(nameOfFile);
//	else openFilePath();
//	
//	// Loop through file incrementing counter
//	// get first row.
//	
//	String line = fileInput.readLine();	
//	// line = fileInput.read();	
//	while (line != null) {
//	    checkLine(counter+1,line);
//	    StringTokenizer dataLine = new StringTokenizer(line);
//            int numberOfTokens = dataLine.countTokens();
//	    if (numberOfTokens == 0) break;
//	    counter++;	 
//            line = fileInput.readLine();
//	    }
//	
//	// Close file and return
//        closeFile();
//	return(counter);
//	}
//
     protected void checkLine(int counter, String str) {
    
        for (int index=0;index <str.length();index++) {
            if (!Character.isDigit(str.charAt(index)) &&
	    			!Character.isWhitespace(str.charAt(index))) {
		JOptionPane.showMessageDialog(null,"FILE INPUT ERROR:\n" +
					"charcater on line " + counter + 
					" is not a digit or white space");	        
		inputFormatOkFlag = false;
		haveDataFlag = false;
		break;
		}
	    }
	}
}