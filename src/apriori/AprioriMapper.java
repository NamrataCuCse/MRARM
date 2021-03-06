package apriori;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class AprioriMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
	
	private Text itemset = new Text();
	
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> im_output, Reporter reporter)
			throws IOException {
		
		String transaction = value.toString();
		
		System.out.println("Set obtained -> "+transaction);
		
		List<String> itemsets = getItemSets(transaction.split(","));
		System.out.println("Mapping the powerset -> "+ itemsets);
		for(String itmset : itemsets){				
			itemset.set(itmset.replaceAll(" ", ""));
			im_output.collect(itemset, new IntWritable(1));
		}			
	}
	
	
	
	
	/*
	 * GET POWER SET OF THE INPUT (EXCLUDING THE EMPTY SET)
	 */
	
	private List<String> getItemSets(String[] items) {
				
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
}