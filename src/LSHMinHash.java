import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class LSHMinHash<T>
{
	
	private final int hashes[];
	private final int numOfHashes;
	private final int numOfSets;
	private final Set<T> setA;
	private final Set<T> setB;
	private final Map<T, boolean[]> matrix;
	private final int[][] minHashes;
	public LSHMinHash(double e, Set<T> setA, Set<T> setB){
	    this.numOfHashes = (int)(1 / (e * e));
	    this.numOfSets = 2;
	    this.setA = setA;
	    this.setB = setB;
	    matrix = buildSetMatrix(setA, setB);
	    minHashes = initMinHashes(numOfSets, numOfHashes);
	    hashes = computeHashes(numOfHashes);
	}
	private Map<T,boolean[]> buildSetMatrix(Set<T> setA, Set<T> setB) {

	    Map<T,boolean[]> matrix = new HashMap<T,boolean[]>();

	    for(T element : setA){
	        matrix.put(element, new boolean[] { true, false } );
	    }

	    for(T element : setB){
	        if(matrix.containsKey(element)){
	            matrix.put(element, new boolean[] { true, true } );
	        }else if(!matrix.containsKey(element)){
	            matrix.put(element, new boolean[] { false, true } );
	        }
	    }

	    return matrix;
	}

	private int[][] initMinHashes(int numOfSets, int numOfHashes) {
	    int[][] minHashes = new int[numOfSets][numOfHashes];

	    for (int i = 0; i < numOfSets; i++) {
	        for (int j = 0; j < numOfHashes; j++) {
	            minHashes[i][j] = Integer.MAX_VALUE;
	        }
	    }
	    return minHashes;
	}

	private int[] computeHashes(int numOfHashes) {
	    int[] hashes = new int[numOfHashes];
	    Random r = new Random(31);

	    for (int i = 0; i < numOfHashes; i++){
	        int a = (int)r.nextInt();
	        int b = (int)r.nextInt();
	        int c = (int)r.nextInt();
	        hashes[i] = (int)((a * (a * b * c >> 4) + a * a * b * c + c) & 0xFFFFFFFF);
	    }
	    return hashes;
	}

	private void computeMinHashForSet(Set<T> set, int setIndex){
	    int hashIndex = 0;

	    for(T element : matrix.keySet()) {
	        for (int i = 0; i < numOfHashes; i++) {
	            if(set.contains(element)) {
	                int hashValue = hashes[hashIndex];
	                if (hashValue < minHashes[setIndex][hashIndex]) {
	                    minHashes[setIndex][hashIndex] = hashValue;
	                }
	            }
	        }
	        hashIndex++;
	    }
	}

	private double computeMinHash(int[][] minHashes, int numOfHashes) {
	    int identicalMinHashes = 0;
	    for (int i = 0; i < numOfHashes; i++){
	        if (minHashes[0][i] == minHashes[1][i]) {
	            identicalMinHashes++;
	        }
	    }
	    return (1.0 * identicalMinHashes) / numOfHashes;
	}

	public double findSimilarities() {
	    computeMinHashForSet(setA, 0);
	    computeMinHashForSet(setB, 1);
	    return computeMinHash(minHashes, numOfHashes);
	}


}
