/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package incremantalmining;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
/*
 * CofiTree_Mining.java
 *
 * Created on April 5, 2007, 12:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Sanjay
 */

class COFI_Tree
{
	short item;
	long freq_count;
	long parti_count;
	//ArrayList AL;
        Vector<COFI_Tree> V=null;
       // COFI_Tree son[] = null;
	COFI_Tree father = null;
        COFI_Tree next=null;
}

class Item_Header
{
	short item;
	long freq=0;
	COFI_Tree next = null;

}

class Candidate_Node
{
    short[] candi_item=null;
    long freq=0;
}



public class CofiTree_Mining
{
    protected static long startTime;
     protected static long matrixGenTime;
    protected static long endTime;
    protected static long startMiningTime;
    public COFI_Tree root = null;
    public ArrayList<Item_Header> headerlist=null;    
    //public Inverted_Matrix IM[] = null;
    public Vector<Inverted_Matrix> IM;
    int NoOfFreqItem=0;
    protected int     numRows    = 0;
    /** Command line argument for % support (default = 20%). */
    protected double  support    = 20.0;
    /** Minimum support value in terms of number of rows. */
    protected double  minSupport = 0;
    
    
    
    Vector<Integer>mineItems=new Vector<Integer>();
    /** Creates a new instance of CofiTree_Mining */

    public CofiTree_Mining() throws Exception
    {
        Inverted_Matrix_Gen img=new Inverted_Matrix_Gen();
        IM=img.genInvert_Mat();
       // IM=img.IM;
        numRows=IM.size();
        support=img.support;
    }
    
    public CofiTree_Mining(Inverted_Matrix_Gen img,double sup) throws Exception
    {
        IM=img.genInvert_Mat();
        
       // img.displayInvertedMarix(IM);
        numRows=img.numRows;
        
        System.out.println("numRows = "+ numRows);
        int numRows1=IM.size();
        System.out.println("numRows1 = "+ numRows1);
        
        //support=img.support;
        support=sup;
     						   
    }
    
    public static void main(String s[]) throws Exception
    {
        startTime=System.currentTimeMillis();
        System.out.println("*********** starting program time is ------------> "+startTime);
        Inverted_Matrix_Gen img=new Inverted_Matrix_Gen();
      
       matrixGenTime=System.currentTimeMillis()-startTime;
        System.out.println("********** Matrix Generation Time is ------------> "+matrixGenTime);
        //  img.genInvert_Mat();
        System.out.println("start mining");
        
        startMiningTime=System.currentTimeMillis();
        System.out.println("*********** Starting Mining at ------------> "+startMiningTime);
        
        CofiTree_Mining cft=new CofiTree_Mining(img,0.4);
        cft.mineCOFITree();
        
        
        
   //     CofiTree_Mining cft1=new CofiTree_Mining(img,0.4);
     //   cft1.mineCOFITree();
        
        System.out.println("Total No. OF frequent sets are :"+cft.NoOfFreqItem);
//        System.out.println("Total No. OF frequent sets are :"+cft1.NoOfFreqItem);
        System.out.println("Ok Done");
        endTime=System.currentTimeMillis();
        System.out.println("*********** Ending Mining at------------> "+endTime);
        System.out.println("*********** Total time taken for Mining is------------> "+(endTime-startMiningTime));
        System.out.println("*********** Total time taken for Program is------------> "+(endTime-startTime-img.fileReadTime-img.fileWriteTime));
    }
  
    void mineCOFITree()
	{

		//support = 0.4 ;//0.23;// for papertest; //7.0/18.0;
                //support = 8.0/18.0;
		int freq = (int)(support * numRows);
		System.out.println("freq: " + freq+"Support: "+support+ "NumRows: "+ numRows);
                
                
		int freq_loc=findPosition(freq);
                System.out.println("freq location: " + freq_loc);
                
                
                if(freq_loc==-1)
                {
                    System.out.println("Invalid support:");
                    return;
                }
                

		while (freq_loc < IM.size()-1)
		{

		//	int mined_item = IM[freq_loc].idx[0][0];
        	//	transactional_array ta[] = IM[freq_loc].trans_arr;
                        
                        createCOFITree(freq_loc);
                        
/*                        System.out.println("Tree is: ");
                        displayTree(root);
                        System.out.println("Header List is:");
                        dispayHeaderList();
  */                      
                        
                        getFrequentItemSet();
                        
                        
                        freq_loc++;
                        root=null;
                        headerlist=null;        
		
		
		}

	}
        
    int findPosition(int freq)
	{

	//	boolean f = true;
		for (int i = 0; i < IM.size(); i++)
		{
	//		f = false;
                        
			//System.out.println("elements    "+IM.get(i).idx[0]+","+IM.get(i).idx[1]);
			if (freq <= IM.get(i).idx[1])
				return i;
			
		}

		return -1;



			/*int low = 0, high = IM.length;
			int mid=0;
			while (low <high)
			{
				mid = (low + high) / 2 +1;

				if (freq == IM[mid].idx[0][1])
					break;
				else if (freq < IM[mid].idx[0][1])
					high = mid - 1;
				else
					low = mid + 1;
			}*/

		//	return mid;
	}

        	
    
    void createCOFITree(int freq_loc)
        {
            int mined_item = IM.get(freq_loc).idx[0];
            mineItems.add(mined_item);
            transactional_array ta[];// = IM.get(freq_loc).trans_arr;
           
            Vector <transactional_array>vta=IM.get(IM.get(freq_loc).idx[2]).trans_arr;
            ta=new transactional_array[vta.size()];
            
            Iterator<transactional_array> itr=vta.iterator();
            int i=0;
            
            System.out.println("******Transaction array at loc "+ freq_loc+"****** Mined Item "+ mined_item);
            while(itr.hasNext())
            {
            ta[i]=itr.next();
              //  System.out.print(ta[i].prevele[0]+","+ta[i].prevele[1]+"<->"+ta[i].nextele[0]+","+ta[i].nextele[1]+"\t");
                i++;
            }
            
            headerlist=new ArrayList();
           
            for(i=freq_loc+1;i<IM.size();i++)
            {
                Item_Header ihd=new Item_Header();
                ihd.item=(short)IM.get(i).idx[0];
                headerlist.add(ihd);
                ihd=null;
                
                //headerlist.
            }
            
            
            
            System.out.println("header:::******************############******************");
           // dispayHeaderList();
            
           // COFI_Tree [] CT;
           // headerlist.toArray(CT);
           
            //System.out.println("Content of HeaderList:");
            for( i=0;i<headerlist.size();i++)
            {
               // System.out.println(headerlist.get(i).item);
            }
            
            
            root=new COFI_Tree();
            root.item=(short)mined_item;
            root.freq_count=ta.length;  //frequency of root_item equal to the number of entry in the trasactional array
            root.parti_count=0;
            
            COFI_Tree current=root;
            current.V=new Vector();
         
            for(i=0; i< ta.length ; i++)
            {
                int rowindex=(int)ta[i].nextele[0];
                int colindex=(int)ta[i].nextele[1];
                
               // System.out.println(rowindex+","+colindex);
                Vector transItem=new Vector<Integer>();
                Vector transItem1=new Vector<Integer>();
                   
                //System.out.println("***********#############**********");
                while(rowindex!=-1 && colindex!=-1)
                {
                 //   System.out.println(rowindex+","+colindex);
                    int itemval=IM.get(rowindex).idx[3];
                    
                    if(ta.length<=IM.get(rowindex).idx[4])
                    {     
                        if(!mineItems.contains(itemval))
                        {     transItem.add(itemval);
                             transItem1.add(IM.get(rowindex).idx[4]);
                        }
                             //a=null;
                    }
                    
                      int rowindex1=(int)IM.get(rowindex).trans_arr.get(colindex).nextele[0];
                      colindex=(int)IM.get(rowindex).trans_arr.get(colindex).nextele[1];
                      rowindex=rowindex1;
                      
                      
                }
                
                
                 rowindex=(int)ta[i].prevele[0];
                 colindex=(int)ta[i].prevele[1];
                
               
                while(rowindex!=-1 && colindex!=-1)
                {
                    int itemval=IM.get(rowindex).idx[3];
                    
                    if(ta.length<=IM.get(rowindex).idx[4])
                    {
                        if(!mineItems.contains(itemval))
                        {     
                            transItem.add(itemval);
                            transItem1.add(IM.get(rowindex).idx[4]);
                        }
                        //int a[]=new int [2];
                        //a[0]=itemval;
                        //a[1]=IM.get(rowindex).idx[4];
                        //transItem.add(a);
                        //a=null;
                       // transItem.add(itemval);
                        //transItem1.add(IM.get(rowindex).idx[4]);
                        
                    }
                    
                int rowindex1=(int)IM.get(rowindex).trans_arr.get(colindex).prevele[0];
                    colindex=(int)IM.get(rowindex).trans_arr.get(colindex).prevele[1];
                    rowindex=rowindex1;
                }
                
            
    
                  //System.out.println("@@@@@@@@@@@@    Before After Sorting  @@@@@@@@@222");
               //     DispTransaction(transItem,transItem1);  
              sortTransactionBySupport(transItem,transItem1);  
             
              
              //System.out.println("@@@@@@@@@@@@  Trans After Sorting  @@@@@@@@@222");
              //DispTransaction(transItem,transItem1);  
                
                current=root;
                int transIndex=0;
                //while(rowindex!=-1 && colindex!=-1)
                while(transIndex<transItem.size())
                {
                    int itemval=(int)transItem.get(transIndex++);//IM.get(rowindex).idx[3];  //next item in transaction
                    
                    int k=0;
                     boolean found=false; 
              aaa:  while(k<current.V.size())
                    {
                        if(current.V.get(k).item==itemval)
                        {
                            COFI_Tree temp=current.V.get(k);
                            current.V.get(k).freq_count++;
                            
                            headerlist.get(getHeaderIndex((short)itemval)).freq++;//=current.V.get(k).freq_count;  
                            
                          //  System.out.println("No New node: "+current.V.get(k).item+ "  "+current.V.get(k).freq_count);
                            current=temp;
                           
                            found=true;
                                    break aaa;
                        }
                        else
                            found=false;
                        k++;        
                    }
                    
                    if(!found)
                    {
                        COFI_Tree son=new COFI_Tree();
                        son.V=new Vector();
                        
                        son.father=current;
                        son.freq_count=1;
                        son.item=(short)itemval;
                        son.parti_count=0;
                        
                        int headidx=getHeaderIndex((short)itemval);
                        
                        
                        
                        
                        if(headerlist.get(headidx).next==null)
                        {
                            headerlist.get(headidx).next=son;
                            headerlist.get(headidx).freq=1;      //son.freq_count;
                        }
                        else
                        {
                            COFI_Tree head=headerlist.get(headidx).next;
                            COFI_Tree prev=head;
                            while(head!=null)
                            {
                                prev=head;
                                head=head.next;
                            }
                            prev.next=son;
                            headerlist.get(headidx).freq++;
                        }
                        current.V.add(son);
                        current=son;
                    //    System.out.println("yes New Node:"+son.item +"  "+son.freq_count);
                    
                    }
                          
//                int rowindex1=(int)IM[rowindex].trans_arr[colindex].ele[0];
  //              int colindex1=(int)IM[rowindex].trans_arr[colindex].ele[1];
                //System.out.println("helo "+rowindex+"   "+colindex);
 //               rowindex=rowindex1;
  //              colindex=colindex1;
                        
                        
              
                }
                
            }      
            
            
            sortHeaderList();
            
            arrangeHeaderList();
            
            //System.out.println("Headerlist at last");
            for( i=0;i<headerlist.size();i++)
            {
                Item_Header temp=headerlist.get(i);
                //System.out.println(temp.item+ "  "+ temp.freq);
            }
            /*
            for(i=0;i<headerlist.size();i++)
            {
                COFI_Tree temp=headerlist.get(i).next;
                System.out.print("ITEM "+temp.item);
                while(temp!=null)
                {
                    System.out.print("\t"+temp.freq_count);
                    temp=temp.next;
                }
                System.out.println();
            
            }
            */
            
        }
        
        public void arrangeHeaderList()
        {
            	Item_Header attribute1,attribute2;
		boolean isOrdered;
		int index;

            //    System.out.println("Headerlist size is:"+headerlist.size());
                
		do
		{
			isOrdered = true;
			index = 0;
			while (index < headerlist.size()-1)
			{       
                              //  System.out.println("index:"+index+" "+headerlist.get(index).item+"index:"+ (index+1) +"  "+headerlist.get(index+1).item);
				if (!isdecendent(headerlist.get(index).next,headerlist.get(index+1).next)) index++;
				else
				{
					isOrdered = false;

					attribute1 = headerlist.get(index);
                                        attribute2 = headerlist.get(index+1);
                                        
                                 	//headerlist.get(index) = headerlist.get(index+1);
					 headerlist.remove(attribute1);
                                        headerlist.remove(attribute2);
                                       
                                        headerlist.add(index,attribute2);
                                        
                                        //headerlist.get(index+1) = attribute;
                                        headerlist.add(index+1,attribute1);
                                        
        				// Increment index
					index++;
				}
			}
		} while (isOrdered == false);
	
        }
        
        public boolean isdecendent(COFI_Tree father1,COFI_Tree son)
        {
            COFI_Tree current=son;
            while(current!=null)
            {    COFI_Tree son1=current; 
                 while(son1!=null)
                {
                    son1=son1.father;
                    if(son1==father1)
                        return true;
                }
                 current=current.next;
            }    
            return false;
        }
        
        
       public void DispTransaction(Vector<int[]>transItem,Vector<int[]>transItem1)
        {
            int index=0;
            while (index <transItem.size())//countArray.length - 1))
	    {
                
                System.out.println(transItem.get(index)+"\t" +transItem1.get(index));
                index++;
            }
        }
        
        public void sortTransactionBySupport(Vector<Integer>transItem,Vector<Integer>transItem1)
        {
        
                boolean isOrdered;
		int index;

		do
		{
			isOrdered = true;
			index = 0;
			while (index <transItem.size()-1)//countArray.length - 1))
			{
                    
                    
				if (transItem1.get(index) <= transItem1.get(index+1)) {
                                    
                                         index++;
                                }
				else
				{
					isOrdered = false;
					// Swap
                                        int temp;
                                        temp=transItem.get(index);
                                        transItem.setElementAt(transItem.get(index+1), index);//add(index, transItem.get(index+1));
                                        transItem.setElementAt(temp, index+1);//add(index+1,temp);
                                        
                                        
                                        temp=transItem1.get(index);
                                        transItem1.setElementAt(transItem1.get(index+1), index);//add(index, transItem.get(index+1));
                                        transItem1.setElementAt(temp, index+1);//add(index+1,temp);
                                        
                                        
                                        //temp=transItem1.get(index);
                                        //transItem1.add(index, transItem1.get(index+1));
                                        //transItem1.add(index+1,temp);
                                      
                //                        System.out.println("hello "+index+"  " +isOrdered );
                                        
					index++;
				}
			}
		} while (isOrdered == false);
        
        }
        
        
        
        public void sortHeaderList()
	{


		Item_Header attribute1,attribute2;
		boolean isOrdered;
		int index;
                
                do
		{
			isOrdered = true;
			index = 0;
			while (index < headerlist.size()-1)
			{       
                              //  System.out.println("index:"+index+" "+headerlist.get(index).item+"index:"+ (index+1) +"  "+headerlist.get(index+1).item);
				if (headerlist.get(index).freq >=headerlist.get(index+1).freq) index++;
				else
				{
					isOrdered = false;

					attribute1 = headerlist.get(index);
                                        attribute2 = headerlist.get(index+1);
                                        
                                 	//headerlist.get(index) = headerlist.get(index+1);
					 headerlist.remove(attribute1);
                                        headerlist.remove(attribute2);
                                       
                                        headerlist.add(index,attribute2);
                                        
                                        //headerlist.get(index+1) = attribute;
                                        headerlist.add(index+1,attribute1);
                                        
                                //        System.out.println("index:"+index+" "+headerlist.get(index).item+"index:"+ (index+1) +"  "+headerlist.get(index+1).item);
                                       // System.out.println("index:"+index+" "+headerlist.get(index).item+"index:"+ (index+1) +"  "+headerlist.get(index+1).item);
				
                                        
// Swap
					/*attribute = countArray[index][0];
					quantity = countArray[index][1];
					countArray[index][0] = countArray[index + 1][0];
					countArray[index][1] = countArray[index + 1][1];
					countArray[index + 1][0] = attribute;
					countArray[index + 1][1] = quantity;
					 */
					// Increment index
					index++;
				}
			}
		} while (isOrdered == false);
		
		/*System.out.println("After sorting len of: " + trans.length);

		System.out.println("\nAfter Sorting\n");
		for (int i = 0; i < trans.length ; i++)
		{
			System.out.print(trans[i]);
		}*/
	}
	
        public int getHeaderIndex(short item)
        {   int i;
            for(i=0;i<headerlist.size();i++)
            {
                if(headerlist.get(i).item==item)
                    break;
            }
            return i;
        }
        
        public void getFrequentItemSet()
        {
            //boolean copyflag=false;
            ArrayList<Candidate_Node>globlelist=new ArrayList();
            
            ArrayList<Candidate_Node>branchlist=new ArrayList();
            boolean copyflag=false;
          out1: for(int i=0;i<headerlist.size();i++)
            {
                COFI_Tree first = headerlist.get(i).next;
                //       boolean copyflag=false;
               // if (headerlist.get(i).freq<=4)//(long)(support*numRows))  //for non frequent item
                 if (headerlist.get(i).freq<=(long)(support*numRows))    
                      continue;
                     //break out1;                                       //branch is not generated
                 // boolean copyflag=false;
                   out2: while(first!=null)
                    {
                        
                        
                        COFI_Tree current=first;
                        ArrayList<COFI_Tree>branch=new ArrayList(); //no need of this variable
                        
                        ArrayList<Short>branch_item=new ArrayList();
                       // short itemset[]=new short[]
                        long F=first.freq_count-first.parti_count;
                            
                        if(F==0)
                        {
                            first=first.next;
                            continue out2;   //Items are already generated for that branch.
                        }
                        while(current!=null)
                        {
                            branch.add(current);  //no need of this branch varial
                            current.parti_count+=F;
                            
                            if(isFrequent(current.item))
                                branch_item.add(new Short(current.item));
                            if(current.father==null)
                                branch_item.add(new Short(current.item)); //add the item for root node
//                            System.out.println(current.item + "  "+current.freq_count);
                            current=current.father;
                            
                        }
                        
                      //  java.util.Arrays.sort();
                        //java.util.Arrays.c
                        branchlist=generateCandidate(branch_item,F);
                        
                        
                        if(copyflag==false)
                        {
                            for( int j=0;j<branchlist.size();j++)
                            {
                                globlelist.add(branchlist.get(j));
                            }
                            copyflag=true;
                        }
                        else
                        {
                            int gll=globlelist.size();
                            for(int glv=0;glv<gll;glv++)
                            {
                                for(int blv=0;blv<branchlist.size();blv++)
                                {
                                    if(globlelist.get(glv).candi_item.length==branchlist.get(blv).candi_item.length)
                                    {
                                        if(isCompare(globlelist.get(glv).candi_item,branchlist.get(blv).candi_item))
                                        {
                                            
                                            globlelist.get(glv).freq+=branchlist.get(blv).freq;
                                            branchlist.remove(blv);
                                        }
                                        /*else
                                        {
                                            globlelist.add(branchlist.get(blv));
                                        }*/
                                    }
                                }
                            }
                            
                           // Add all itemset which is not available in globle list
                        for(int blv=0;blv<branchlist.size();blv++)
                        {
                            globlelist.add(branchlist.get(blv));
                        }
                        
                        
                    }
                        first=first.next;
                        
                 }
             }
          
          
	

          System.out.println("Frequent ItemSets are:");
          
          for(int l=0;l<globlelist.size();l++)

          {
		if((long)(globlelist.get(l).freq)>=(long)(support*numRows))
		{
	            for(int m=0;m<globlelist.get(l).candi_item.length;m++)
	            {
        	        System.out.print(globlelist.get(l).candi_item[m] +"  ");
                    }
	            System.out.println("percentage : "+ (float) globlelist.get(l).freq/numRows*100.0);
		    NoOfFreqItem++;
		}
          
         }
          //NoOfFreqItem+=globlelist.size();
        }
        


        public boolean isCompare(short ga[],short ba[])
        {
            Arrays.sort(ga);
            Arrays.sort(ba);
            
            for(int i=0;i<ga.length;i++)
            {
                if(ga[i]!=ba[i])
                    return false;
            }
            return true;
        }
        
       public boolean isFrequent(short item)
        {
            for(int i=0;i<headerlist.size();i++)
            {
                //Discart non frequent item from the branch i.e. do not include that item in the candidate itemset
          //     System.out.println(headerlist.get(i).item + " "+ headerlist.get(i).freq);
                 //if(headerlist.get(i).item==item && (long)(headerlist.get(i).freq)>4)//(long)(support*numRows))
                if(headerlist.get(i).item==item && (long)(headerlist.get(i).freq)>(long)(support*numRows))
                    return true;
                              
            }
            return false;
        }
        
        public ArrayList<Candidate_Node> generateCandidate(ArrayList<Short>itemset,long F)
        {
            ArrayList<Candidate_Node>candilist=new ArrayList();
        
            short branch_items[]=new short[itemset.size()];    
            
            //System.out.println("Branches are:");
            int len=itemset.size()-1;
            for(int i=0;i<itemset.size();i++)
            {
                //System.out.print(itemset.get(i).shortValue()+ "  " );
                branch_items[i]=itemset.get(len--).shortValue();
             //  System.out.print(branch_items[i]+ "  ");
                
            }
            //candi_itemset=(int)Math.pow(2.0,branch_items.length-1)-1;
            short [][] items=new short[getCombinations(branch_items)][];
            //getCombinations(branch_items,items);
         
            combinations(branch_items,0,null,items,0);
            
            //System.out.println("Candidate Items:");

            for(int i=1;i<(int)Math.pow(2.0,branch_items.length-1);i++)
            {
                //for(int j=0;j<items[i].length;j++)
               // {
                    //sSystem.out.print(items[i][j]+" ");
                //}
                    Candidate_Node cd=new Candidate_Node();
                    cd.candi_item=items[i];
                    cd.freq=F;
                    candilist.add(cd);
                    
                //System.out.println("");
            }
            
            return candilist;
            
        }
        
        
        public int combinations(short[] inputSet, int inputIndex,
				short[] sofar, short[][] outputSet, int outputIndex)
	{
		short[] tempSet;
		int index = inputIndex;

		// Loop through input array

		while (index < inputSet.length)
		{
			tempSet = realloc1(sofar, inputSet[index]);
			outputSet[outputIndex] = tempSet;
			outputIndex = combinations(inputSet, index + 1,
					copyItemSet(tempSet), outputSet, outputIndex + 1);
			index++;
		}

		// Return

		return (outputIndex);
	}

	/* GET COMBINATTIONS */

	/** Gets the number of possible combinations of a given item set.
    @param set the given item set.
    @return number of possible combinations. */

	public int getCombinations(short[] set)
	{
		int counter = 0, numComb;

		numComb = (int)Math.pow(2.0, set.length) - 1;

		// Return

		return (numComb);
	}

	/* ---------------------------------------------------------------- */
	/*                                                                  */
	/*                            MISCELANEOUS                          */
	/*                                                                  */
	/* ---------------------------------------------------------------- */

	/* COPY ITEM SET */

	/** Makes a copy of a given itemSet. 
    @param itemSet the given item set.
    @return copy of given item set. */

	public short[] copyItemSet(short[] itemSet)
	{

		// Check whether there is a itemSet to copy
		if (itemSet == null) return (null);

		// Do copy and return
		short[] newItemSet = new short[itemSet.length];
		for (int index = 0; index < itemSet.length; index++)
		{
			newItemSet[index] = itemSet[index];
		}

		// Return
		return (newItemSet);
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

        
        
}
