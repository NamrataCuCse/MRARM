package apriori;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class AprioriMain {

	public static String InputFile="/home/hadoop/Desktop/large.txt"; //transaction file
   public static String outputFile="/home/hadoop/Desktop/output.txt";//output fil
    public static Map<String,Integer> one=new HashMap<String,Integer>();
    public static Map<String,Integer> two=new HashMap<String,Integer>();
    public static Map<String,Integer> three=new HashMap<String,Integer>();
    public static Map<String,Integer> other=new HashMap<String,Integer>();
    
    public static int minsup=3;
    		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 long startTime = System.nanoTime();
		BufferedReader br=null;
		FileReader fr=null;
		FileWriter fw=null;
		BufferedWriter bw=null;
		try{
				String CurrentLine;

				br = new BufferedReader(new FileReader(InputFile));

				while ((CurrentLine = br.readLine()) != null) {
					ReadMap(CurrentLine);
				}//end of while
				
				br.close();
				
				Prune(one);
				Prune(two);
				Prune(three);
				Prune(other);

				fw= new FileWriter(outputFile);
	            bw= new BufferedWriter(fw);
	            //put the number of transactions into the output file
	            bw.write("\n\n\nSize- 1-Frequent-Itemset :: " + "\n"+ one);
	            bw.write("\n\n\nSize- 2-Frequent-Itemset :: " + "\n"+ two);
	            bw.write("\n\n\nSize- 3-Frequent-Itemset :: " + "\n"+ three);
	            bw.write("\n\n\nSize- N-Frequent-Itemset :: " + "\n"+ other);
	            bw.close();
				
				
			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (br != null)
						br.close();

					if (fr != null)
						fr.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}
		long endTime = System.nanoTime();
		 System.out.printf("Mined frequent itemset in %d milliseconds.\n", 
                 (endTime - startTime) / 1_000_000);

		}
	
static void ReadMap(String Line)
	{
	if(Line!=""){
		String[] val=Line.split(",");	
		List<String> itemsets = getItemSets(val);
				
		for(String itmset : itemsets){				
			int keySize = itmset.toString().replace("[", "").replace("]", "").split(",").length;
			if (keySize==1)
			{ 
				if (one.get(itmset) == null) {
                one.put(itmset, 1);
				}
					else {
						int newValue = Integer.valueOf(String.valueOf(one.get(itmset)));
						newValue++;
						one.put(itmset, newValue);
						}
			}
			else if (keySize==2)
			{ 
				if (two.get(itmset) == null) {
                two.put(itmset, 1);
				}
					else {
						int newValue = Integer.valueOf(String.valueOf(two.get(itmset)));
						newValue++;
						two.put(itmset, newValue);
						}
			}
		
		else if (keySize==3)
		{ 
			if (three.get(itmset) == null) {
            three.put(itmset, 1);
			}
				else {
					int newValue = Integer.valueOf(String.valueOf(three.get(itmset)));
					newValue++;
					three.put(itmset, newValue);
					}
		}
		else
			{ 
				if (other.get(itmset) == null) {
	            other.put(itmset, 1);
				}
					else {
						int newValue = Integer.valueOf(String.valueOf(other.get(itmset)));
						newValue++;
						other.put(itmset, newValue);
						}
			}
	}
		}
	} //end of function			
			
			

/*
 * GET POWER SET OF THE INPUT (EXCLUDING THE EMPTY SET)
 */

private static List<String> getItemSets(String[] items) {
			
	List<String> itemsets = new ArrayList<String>();	
	int n = items.length;		
	int[] masks = new int[n];// we will use this mask to check any bit is set in binary representation of value i.
	for (int i = 0; i < n; i++)
		masks[i] = (1 << i); //equivalent to Math.pow(2,n)
			
	for (int i = 0; i < (1 << n); i++)
	{		
		List<String> newList = new ArrayList<String>(n);
		for (int j = 0; j < n; j++)
		{
			if ((masks[j] & i) != 0) // If result is !=0 (or >0) then bit is set. 	
			{     
				newList.add(items[j]); // include the corresponding element from a given set in a subset.
	        }
	                
	        if(j == n-1 && newList.size() > 0 && newList.size() < 5){
	        	itemsets.add(newList.toString());
	        }
		}
	}
	        			
	return itemsets;
}
public static void Prune(Map<String,Integer> hmap)
{
	for (Iterator<Entry<String, Integer>> it = hmap.entrySet().iterator(); it.hasNext();) {
		 Entry<String, Integer> e = it.next();
		 int t=e.getValue();		
		 if (t<minsup) {
		  it.remove();  
		 }
		}
}
}
