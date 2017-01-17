import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class probablisticCounting {
	static int m = 15000;
	static String source = "";

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("C:/InternetTrafficMeasurement/FlowTraffic.txt"));
		String line;
		FileWriter out = new FileWriter(new File("output_pc.csv"));
		String src = null;
		line = br.readLine(); // to read the headers
		List<String> destn = new ArrayList<String>();

		while ((line = br.readLine()) != null) {
			String[] t = line.split(" +");

			if (t.length >= 2) {
				if (src == null) {
					src = t[0];
					destn.add(t[1]);
					System.out.println("First Source is:" + src);
				} else {
					if (t[0].equalsIgnoreCase(src)) // add destns to the current
													// source
					{
						destn.add(t[1]);
					} else // new source is encountered, so hash the previous
							// destn
					{
						int[] bitMap = new int[m];
						Set<String> destnSet = new HashSet<String>();
						int noOnes = 0;
						Double estimator, Vm;
						for (int i = 0; i < m; i++) {
							bitMap[i] = 0;
						}

						for (String t1 : destn) {
							int hashValue, index;

							hashValue = t1.hashCode();
							//System.out.println(hashValue);
							index = Math.abs(hashValue % m);
							//System.out.println(index);
							bitMap[index] = 1;

							destnSet.add(t1);
						}

						for (int i = 0; i < m; i++) {
							if (bitMap[i] == 1)
								noOnes++;
						}
						Vm = (double) (m - noOnes) / m;
						System.out.println("number of 1's" + noOnes);
						System.out.println("Vm" + Vm);
						System.out.println("estimator" + -(m * Math.log(Vm)));
						estimator = -(m * Math.log(Vm));
						System.out.println(src + "," + destnSet.size() + "," + estimator + "\n");
						out.write(src + "," + destnSet.size() + "," + estimator + "\n");
						src = t[0];
						destn.clear();
						destn.add(t[1]);
					}
				}
			}
		}
		out.close();
		br.close();
	}
}
