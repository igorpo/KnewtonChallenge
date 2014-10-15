import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Igor Pogorelskiy
 * Knewton Coding Challenge
 * Compilation: javac ArtistPairProblem.java
 * Execution:   java ArtistPairProblem
 */

/**
 * Given 1000 customers of LastFM and their favorite artists (no more than 50 per customer),
 * we must find each pairing of artists that appears in at least 50 different 
 * customers' top faves. 
 * 
 * Storage: approximately O(P), where P = all possible combinations of pairings
 * of 2 artists. Thus, if n is the number of artists, n choose 2 will be the 
 * biggest storage component. There are smaller storage components of no more than
 * 2 * 50 * C, if C is the number of customers (since no more than 50 faves per customer) 
 * when we initialize the array and ArrayList to hold each customers' favorites,
 * but these are insignificant due to the pairings in the HashMap which dominates.
 * 
 * Time: approximately O(C * A * A) time, if C = number of customers and 
 * A = number of artists in the favorites of each customer. What saves us here
 * is the fact that accessing and putting into a HashMap is amortized to O(1), 
 * so it is relatively quick to get and store all of the information. 
 * Here, since we know that A is at most 50, we can approximate that the 
 * algorithm would run in O(C) time, and would only depend on the amount of 
 * customers reviewed. If, instead, we fixed the Customers and varied the Artists/Customer
 * than we have a slightly slower algorithm of O(A * A). (We assume constants 
 * do not matter in our analysis).
 * 
 * In reality, however, it is unlikely that the number of favorited artists 
 * should be greater than 50 per customer (it may even be less, as more useful data
 * can be gathered if we limit the favorites to roughly 20 or so). It makes sense
 * that we would review the data of any given amount of customers, so, I believe
 * that in a real world application, this algorithm would roughly run in O(C) time.
 * @author Igor Pogorelskiy
 */
public class ArtistPairProblem {
	
	private static HashMap<String, Integer> artistPairs = new HashMap<String, Integer>(); 
	
	/**
	 * Insert Pairs of Artists into HashMap based on each Customer's favorites
	 * We can sort each customers favorite artists to limit the number of 
	 * combinations that we need to iterate through. 
	 * Treat mappings (Artist A, Artist B) = (Artist B, Artist A) w/ sorting
	 * 
	 * Design choice: Although using the arraylist here seems excessive, and takes 
	 * up more space, the algorithm runs a bit quicker than iteration through 
	 * a regular sorted array with a for loop, due to the 
	 * for each loop (it gives a slight performance advantage here since it 
	 * computes the limit of the arraylist index only once, while a for loop
	 * would continue doing so causing slight inefficiency). 
	 * In general, gave up a little bit of space for a little bit of time.  
	 * @param file
	 * 				input file of Artists
	 * @param minTimes
	 * 				number of times that a pair of artists must appear to be logged
	 */
	private static void insertIntoMap(File file, int minTimes) {
		Scanner sc;
		try {
			sc = new Scanner(new FileInputStream(file));
			String line = "";
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				String[] artistsPerCustomer = line.split(",");
				Arrays.sort(artistsPerCustomer);
				
				// using array list allows slightly quicker access with a for
				// each than a for loop
				ArrayList<String> artists = new ArrayList<String>(Arrays.asList(artistsPerCustomer));
				
				for (String artist : artists) {
					// due to sort, we do not care about last artist
					// he/she is already mapped to
					for (int i = artists.indexOf(artist); i < artists.size() - 1; i++) {
						String pair = "(" + artist + ", " + artists.get(i + 1) + ")";
						if (artistPairs.containsKey(pair)) {
							int n = artistPairs.get(pair) + 1;
							artistPairs.put(pair, n);
						} else {
							artistPairs.put(pair, 1);
						}
					}
				}
			}
			findPairsAppearingAtLeastXTimes(minTimes);
		} catch (FileNotFoundException e) {
			System.out.println("Error: File could not be loaded");
			e.printStackTrace();
		} 
	}
	
	/**
	 * Log the pairs that we find to the console
	 * @param minTimes
	 * 				least number of times a pair of artists must appear to 
	 * 				be logged to stdout
	 */
	private static void findPairsAppearingAtLeastXTimes(int minTimes) {
		for (Map.Entry<String, Integer> pair : artistPairs.entrySet()) {
			if (pair.getValue() > minTimes) {
				System.out.println(pair.getKey() + " appears " + pair.getValue() + " times! ");
			}
		}
	}

	public static void main(String[] args) {
		File file = new File("Artist_lists_small.txt");
		insertIntoMap(file, 50);
	}
}
