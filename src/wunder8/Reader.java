package wunder8;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Reader {

	private HashMap<Integer, ArrayList<String>> bookMap;
	private static String INPUT_FILE_NAME = "alaston_salissa.txt";
	private static String OUTPUT_FILE_NAME = "wunderJoe.txt";
	
	public Reader() {
		bookMap = new HashMap<Integer, ArrayList<String>>(); // key = word length, value = the actual words
	}
		
	public HashMap<Integer, ArrayList<String>> readFile() throws IOException {
		String filePath = new File("").getAbsolutePath();
		File file = new File(filePath + "/" + INPUT_FILE_NAME);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

		String line = null;
		while ((line = br.readLine()) != null) {
	        if (!(line.startsWith("*"))) {
	            String[] lineArray = line.split("\\s+");
	            for(String word : lineArray) {
	            	int key = word.length();
	            	if(word.length() > 0) {
	            		this.add(key, word, this.bookMap);
	            	}
	            }
	        }
		}
		return this.bookMap;
	}
	
	private void add(int key, String value, HashMap<Integer, ArrayList<String>> bookMap) {
		ArrayList<String> arrayList = (bookMap.get(key) == null) ? new ArrayList<String>() : bookMap.get(key);
		arrayList.add(value);
		 
		bookMap.put(key, arrayList);
	}
	
	public void writeBook(ArrayList<String> book) throws UnsupportedEncodingException {
		PrintWriter writer;
		try {
			writer = new PrintWriter(OUTPUT_FILE_NAME, "UTF-8");
			for(int i = 0; i < book.size(); i++) {
				String line = book.get(i);
				writer.println(line);				
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}