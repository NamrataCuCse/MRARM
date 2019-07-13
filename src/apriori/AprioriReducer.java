package apriori;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class AprioriReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, Text> {


	private int Minsup;    
	
	public void configure(JobConf job){      
	    Minsup =Integer.parseInt(job.get("MinSupport"));
	}
	@Override
	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		
		int sum = 0;
	    while (values.hasNext()) {
	    	sum += values.next().get();
	    }	
	    System.out.println("Reducing " + "key:" + key + " value:" + sum);
	    if(sum >= Minsup) {	
	    output.collect(key, new Text(Integer.toString(sum)));
	    }
	   
	    }
}