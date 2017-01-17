import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author vazra
 *
 */
public class Virtual_bitmap2 {
	public static void main(String[] args) {

		final String FILENAME ="C:/InternetTrafficMeasurement/flow_final.txt";
		//final String FILENAME = "C:/InternetTrafficMeasurement/250_FlowTraffic.txt";

		readfile(FILENAME);

	}

	public static void readfile(String FILENAME) {
		// TODO Auto-generated method stub
		String[] parts = null;
		// Initialising s as 9100 since the cardinality for a src is 9060
		int s = 2500;
		// Choosing m
		int m = 500000000;
		ArrayList<ArrayList<Integer>> UniversalHashList = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> XsrcMasterList = new ArrayList<ArrayList<Integer>>();
		List masterDstList = new ArrayList<ArrayList<String>>();
		String[] BitArray = new String[m];
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream("output_virbitmap.csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String src = null;
		HashMap<String, List> srcDstMap = new HashMap<String, List>();
		ArrayList<Integer> R = new ArrayList<Integer>();
		R = genRandomArray(s);
		// initializing bit array
		for (int i = 0; i < m; i++) {
			BitArray[i] = "False";
		}
		// reading input file and writing to HashMap<src,dstlist>
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				parts = sCurrentLine.trim().split("\\s+");
				src = parts[0];
				String dst = parts[1];
				if (!srcDstMap.containsKey(src)) {
					ArrayList<String> dstList = new ArrayList<String>();
					dstList.add(dst);
					srcDstMap.put(src, dstList);
					// hashing(B,convertIPAddress(tokens[0]));
				} else
					srcDstMap.get(src).add(dst);

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < srcDstMap.size(); i++) {
			ArrayList<Integer> masterHashList = new ArrayList<Integer>();
			ArrayList<Integer> XsrcList = new ArrayList<Integer>();
			UniversalHashList.add(masterHashList);
			XsrcMasterList.add(XsrcList);
			// System.out.println("XsrcmasterList" + XsrcList);

		}
		Iterator srcDstMapIt = srcDstMap.entrySet().iterator();
		for (int i = 0; i < srcDstMap.size(); i++) {
			ArrayList<Integer> XsrcList = XsrcMasterList.get(i);
			ArrayList<Integer> masterHashList = UniversalHashList.get(i);
			while (srcDstMapIt.hasNext()) {
				Map.Entry pair = (Map.Entry) srcDstMapIt.next();
				src = pair.getKey().toString();
				List dstList = (List) pair.getValue();
				masterDstList.add(dstList);
				Iterator dstListIt = dstList.iterator();
				while (dstListIt.hasNext()) {
					int hashedSrc = masterHashSetGen(src, dstListIt.next().toString(), s, R);
					// String[] partsGeneratedStuff =
					// generatedStuff.trim().split(":");
					// String generatedIndex = partsGeneratedStuff[0];
					// String hashedSrc = partsGeneratedStuff[1];
					masterHashList.add(hashedSrc % BitArray.length);
					// System.out.println(masterHashList);
					XsrcList.add(hashedSrc);
					// System.out.println(XsrcList);
					BitArray[Math.abs(hashedSrc % (BitArray.length))] = "True";

				}
			}

		}

		double bitMapCount = BitMapCount(BitArray, BitArray.length, s);
		// System.out.println("bitMapCount"+bitMapCount);

		int XsrcIndexperDst = 0;
		Iterator srcDstMapItrecons = srcDstMap.entrySet().iterator();
		while (srcDstMapItrecons.hasNext()) {
			double XsrcRatio;
			Map.Entry pair = (Map.Entry) srcDstMapItrecons.next();
			src = pair.getKey().toString();
			// System.out.println("src" + src);
			List dstList = (List) pair.getValue();
			masterDstList.add(dstList);
			Iterator dstListIt = dstList.iterator();
			String[] srcArray = new String[s];
			double count = 0;
				while(dstListIt.hasNext()) {
					XsrcIndexperDst = XsrcIndexperDst(XsrcMasterList, masterDstList, s, BitArray, src,dstListIt.next().toString(), R) % m;
					// System.out.println("Value"+BitArray[XsrcIndexperDst]);
					if (BitArray[XsrcIndexperDst] == "True") {
						count++;
			}
				}
			//System.out.println("count per source" + count + "," + (s - count));
			XsrcRatio = -s * Math.log((s-count)/s);
			double estimatedSpread = -bitMapCount + XsrcRatio;
			if(estimatedSpread<=0)
			{
				estimatedSpread=0;
			}
			out.println(dstList.size() + "," + estimatedSpread);
		}

	}

	// Calculate k2=-sln(Vs) for each source
	public static int XsrcIndexperDst(ArrayList<ArrayList<Integer>> XsrcMasterList, List masterDstList, int s,
			String[] BitArray, String src, String dst, ArrayList<Integer> R) {
		ArrayList<Double> XsrcRatio = new ArrayList<Double>();
		double ratio = 0.0;
		int XsrcIndexperDst = 0;

		// System.out.println("Count of zeroes in BitMap" +
		// XsrcMasterList.get(i));
		// Reconstruct the array
		ArrayList<Integer> XsrcRecons = null;
		XsrcIndexperDst = masterHashSetGen(src, dst, s, R);
		// System.out.println("XsrcIndexperDst"+XsrcIndexperDst);
		/*
		 * ratio = (double) (s - ((ArrayList<ArrayList<Integer>>)
		 * masterDstList.get(i)).size()) / s; System.out.println("XsrcRatio" +
		 * ratio); // System.out.println(ratio+" "+(s - //
		 * ((ArrayList<ArrayList<Integer>>) masterDstList.get(i)).size()));
		 */

		return XsrcIndexperDst;
	}

	// Calculate k1=-sln(Vm)
	public static double BitMapCount(String[] bitArray, int length, int s) {
		double count = 0;

		for (int i = 0; i < length; i++) {
			if (bitArray[i] == "False") {
				count++;
			}

		}
		// System.out.println("Count of zeroes in BitMap" + count);
		double ratio = count / length;
		System.out.println("Bitmap" + length + count + ratio);
		if (ratio == 0.0) {
			// System.out.println("Length0" + length);
			return length;
		} else {
			// System.out.println("Length01" + length);
			// System.out.println("Length" + -(length * Math.log(ratio)));
			// System.out.println( Math.log(ratio));
			return (-(s * Math.log(ratio)));
		}

	}

	// Hash function for src H(src exor R[i])
	public static int masterHashSetGen(String src, String dst, int s, ArrayList<Integer> R) {
		// TODO Auto-generated method stub

		long ipDecimalSrc = ipToLong(src);
		Long xoredVal;
		int generatedIndex = genIndex(s, dst);
		// System.out.println(generatedIndex);
		xoredVal = ipDecimalSrc ^ R.get(generatedIndex);
		// System.out.println(xoredVal);
		int hashedSrc = Math.abs(MurmurHash.hashLong(xoredVal));
		// String hashedOutputs = generatedIndex + ":" + hashedSrc;
		return hashedSrc;
	}

	// Conversion of dotted decimal to decimal(long)
	public static long ipToLong(String ipAddress) {

		String[] ipAddressInArray = ipAddress.split("\\.");

		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {

			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);
			result += ip * Math.pow(256, power);

		}

		return result;
	}

	// Hash function for destination given by H(dst)mod s
	public static int genIndex(int size, String dst) {
		// System.out.println("DST" + dst);
		int index = 0;
		long ipDecimalDst = ipToLong(dst);
		long hashedDecimal = MurmurHash.hashLong(ipDecimalDst);
		index = (int) Math.abs((hashedDecimal % size));
		return index;
		// TODO Auto-generated method stub

	}

	// Method to generate randome array of numbers
	public static ArrayList<Integer> genRandomArray(int s) {
		// TODO Auto-generated method stub
		ArrayList<Integer> R = new ArrayList<Integer>();
		int i = 0;
		Random randNum = new Random();
		for (i = 0; i < s; i++) {
			int randNumber = MurmurHash.hashLong(i);
			R.add(randNumber);

		}

		return R;
	}

}
