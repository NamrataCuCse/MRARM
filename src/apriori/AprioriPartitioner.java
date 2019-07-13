package apriori;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Partitioner;

public class AprioriPartitioner implements Partitioner<Text, IntWritable> {
	
	@Override
	public int getPartition(Text key, IntWritable value, int numReduceTasks) {
		
		int keySize = key.toString().replace("[", "").replace("]", "").split(",").length;
		
		if(numReduceTasks == 0)
            return 0;	
		if(keySize == 1)
			return 0;
		else if(keySize == 2)
			return 1;
		else if(keySize == 3)
			return 2;
		else 
			return 3;
	}

	@Override
	public void configure(JobConf arg0) {
		// TODO Auto-generated method stub
		
	}
}