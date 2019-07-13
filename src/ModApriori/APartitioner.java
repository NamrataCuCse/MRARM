package ModApriori;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class APartitioner extends
  	Partitioner<Text, IntWritable> {

	
	@Override
	public int getPartition(Text key, IntWritable val, int numReduceTasks) {
		// TODO Auto-generated method stub
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
}