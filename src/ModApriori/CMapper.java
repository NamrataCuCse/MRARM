package ModApriori;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class CMapper extends Mapper<LongWritable, Text, Text, Text>{
	
	private Text itemset = new Text();
	private Text val = new Text();
	
	 @Override
	    public void map(LongWritable key, Text value, Context context)
	          throws IOException, InterruptedException {
		 String[] valueSplit = value.toString().split("\t");
			itemset.set(valueSplit[0].replaceAll(" ", ""));	//[1,2,3]
			val.set("0;" + valueSplit[1]); //2
			System.out.println(val);
			//original itemset
			
			context.write(itemset, val);
			
			String[] items = valueSplit[0].replace("[", "").replace("]", "").split(",");
			
			if(items.length > 1){
			
				List<String> subitemsets = getItemSets(items);
				
				for(String itmset : subitemsets){
					
					itemset.set(itmset.replaceAll(" ", ""));
					val.set(valueSplit[0] + ";" + valueSplit[1]);
					context.write(itemset, val);
				}
			}	
			}			
		
	//generate powersets of exactly n-1 size
	private List<String> getItemSets(String[] items) {
		
		List<String> itemsets = new ArrayList<String>();		
		int n = items.length;			
		int[] masks = new int[n];
		
		for (int i = 0; i < n; i++)
            masks[i] = (1 << i);
		
		for (int i = 0; i < (1 << n); i++){				
			List<String> newList = new ArrayList<String>(n);
			for (int j = 0; j < n; j++){
                if ((masks[j] & i) != 0){
                	newList.add(items[j]);
                	
                }
                
                if(j == n-1 && newList.size() == n-1){	              		
                	itemsets.add(newList.toString());
                	
                }
			}				
		} 
		System.out.println(itemsets);
		return itemsets;
	}

}
