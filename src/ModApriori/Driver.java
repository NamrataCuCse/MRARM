/* in this code the support is taken as 80 by default and confidence 60%..make changes in the reducer*/




package ModApriori;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import apriori.AprioriMapper;

public class Driver extends Configured implements Tool {

  @Override
	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.out
					.printf("Two parameters are required for DriverNLineInputFormat- <input dir> <output dir>\n");
			return -1;
		}

		Job job = new Job(getConf());
		job.setJobName("Apriori Algorithm");
		job.setJarByClass(Driver.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setInputFormatClass(NLineInputFormat.class);
		NLineInputFormat.addInputPath(job, new Path(args[0]));
		job.getConfiguration().setInt("mapreduce.input.lineinputformat.linespermap", 10000);
		job.setMapperClass(AMapper.class);
		job.setPartitionerClass(APartitioner.class);
		job.setReducerClass(AReducer.class);
		job.setNumReduceTasks(4);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		
		Job job1 = new Job(getConf());
		job1.setJobName("Association Rule");
		job1.setMapOutputKeyClass(Text.class);
		job1.setMapOutputValueClass(IntWritable.class);
	
		FileInputFormat.addInputPath(job1, new Path(args[1]));
		job1.setMapperClass(CMapper.class);
		job1.setReducerClass(CReducer.class);
		FileOutputFormat.setOutputPath(job1, new Path(args[2]));
		
		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
		
	}

	public static void main(String[] args) throws Exception {
		long startTime = System.nanoTime();	
		
		
		int exitCode = ToolRunner.run(new Configuration(),new Driver(), args);
		

long endTime = System.nanoTime();
System.out.printf("\n\n\n\nMined frequent itemset in %d milliseconds.\n", 
       (endTime - startTime) / 1_000_000);		

			
		
		System.exit(exitCode);
		
	}
}
