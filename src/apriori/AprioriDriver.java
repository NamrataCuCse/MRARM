package apriori;

/*
 * USAGE: home input output minsup 
 */

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class AprioriDriver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
				
		int res = ToolRunner.run(new AprioriDriver(), args);
        System.exit(res);
	}

	@Override
	public int run(String[] arg0) throws Exception {
		
		try{
			
			JobConf itemsetsJob = new JobConf(AprioriDriver.class);
			itemsetsJob.setJobName("Frequent Itemset generation");
			
			/*
			 * GIVE THE MINIMUM SUPPORT 
			 */
			itemsetsJob.set("MinSupport",arg0[2]);
			
			itemsetsJob.setOutputKeyClass(Text.class);
		    itemsetsJob.setOutputValueClass(IntWritable.class);
			
			itemsetsJob.setMapperClass(AprioriMapper.class);
		    itemsetsJob.setReducerClass(AprioriReducer.class);
		    itemsetsJob.setPartitionerClass(AprioriPartitioner.class);
		    
		    /*
		     * EACH FOR ITEMSETS OF SIZE 1,2,3
		     */
		    itemsetsJob.setNumReduceTasks(4); 
		    
		    itemsetsJob.setInputFormat(TextInputFormat.class);
		    itemsetsJob.setOutputFormat(TextOutputFormat.class);
		    
		    FileInputFormat.setInputPaths(itemsetsJob, new Path(arg0[0]));
		    FileOutputFormat.setOutputPath(itemsetsJob, new Path(arg0[1]));
	   
		    JobClient.runJob(itemsetsJob);
					
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return 0;		
	}	
}