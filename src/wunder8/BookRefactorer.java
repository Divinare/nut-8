package wunder8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BookRefactorer {

	private HashMap<Integer, ArrayList<String>> bookMap;
	private ArrayList<String> book;

	private static int LINE_MAX_LENGTH = 80;

	private ArrayList<String> visited; // "27, 25, 23 etc."

	public BookRefactorer(HashMap<Integer, ArrayList<String>> bookMap) {
		this.bookMap = bookMap;
		this.book = new ArrayList<String>();
	}
	
	public void refactorBook() {		
		while(true) {
			String line = getNextLine();
			book.add(line);

			if(line.length() != 80 && countBookMapSize() == 0) {
				System.out.println("The end of the book.");
				break;
			}
		}
	}
	
	private int countBookMapSize() {
		int size = 0;
		for (int key : bookMap.keySet()) {
			ArrayList<String> arrayList = bookMap.get(key);
			size += arrayList.size();
		}
		return size;
	}
	
	private String getNextLine() {
		String line = "";
		String nextWord = getLongestWord(LINE_MAX_LENGTH);
		while(nextWord != null && (line + nextWord).length() <= LINE_MAX_LENGTH) {
			line += (line.length() != 0) ? " " + nextWord : nextWord;
			removeWord(nextWord);

			int targetLength = LINE_MAX_LENGTH - line.length() - 1; // -1 because we need to add a space between the words
			if(targetLength > 0) {
				nextWord = getLongestWord(targetLength);
				int gap = LINE_MAX_LENGTH - (line + " " + nextWord).length() - 1; // -1 because we need to add a space between the words
				if(getMaxWordLength() >= gap && getWord(gap) == null && gap >= 0) {
					if(isAllLengthOnes(line)) {
						break;
					}
					line = fillGapInLine(line);
				}
			}
		}
		return line;
	}
	
	private Boolean isAllLengthOnes(String line) {
		String[] lineArray = line.split(" ");
		Boolean isAllLengthOne = true;
		for(String word : lineArray) {
			if(word.length() != 1) {
				isAllLengthOne = false;
			}
		}
		return isAllLengthOne;
	}
	
	private String fillGapInLine(String line) {
		int countOfWords = 2;		
		while(true) {
			if(countOfWords > 2) {
				line = removeLastWordFromLine(line);
			}
			int[] wordLengths = new int[countOfWords];
			int gapBeforeNextWord = LINE_MAX_LENGTH - line.length();
			int gapWordLength = (int)Math.floor(gapBeforeNextWord/countOfWords);
			int totalLength = 0;
			for(int k = 0; k < countOfWords; k++) {
				int minLength = getMinWordLength();
				int nextWordLength = 0;
				if(k < countOfWords-1) {
					nextWordLength = (gapWordLength - 1);
				} else {
					gapWordLength = (gapBeforeNextWord - totalLength);
					nextWordLength = gapWordLength - 1;
				}
				nextWordLength = (nextWordLength >= minLength) ? nextWordLength : minLength;
				totalLength += nextWordLength + 1;
				wordLengths[k] = nextWordLength;
			}
			
			this.visited = new ArrayList<String>();
			String[] newWords = recursion(wordLengths);
			if(newWords == null) {
				break;
			}
			String gapFiller = "";
			for(int k = 0; k < newWords.length; k++) {
				gapFiller += (gapFiller.length() != 0) ? " " + newWords[k] : newWords[k];
			}
			if((line + " " + gapFiller).length() == LINE_MAX_LENGTH) {
				line += " " + gapFiller;
				for(int k = 0; k < newWords.length; k++) {
					removeWord(newWords[k]);
				}
				break;
			} else if(countOfWords > LINE_MAX_LENGTH/2) {
				break;
			}
			countOfWords++;
		}
		return line;
	}

	private String removeLastWordFromLine(String line) {
		String[] array = line.split(" ");
		String newLine = "";
		for(int i = 0; i < array.length; i++) {
			if(i < array.length - 1) {
				newLine += (newLine.length() != 0) ? " " + array[i] : array[i];
			}
		}
		
		String removedWord = array[array.length - 1];
		int value = removedWord.length();
		if(value > 0) {
			ArrayList<String> arrayList = (bookMap.get(value) == null) ? new ArrayList<String>() : bookMap.get(value);
			arrayList.add(removedWord);
			bookMap.put(value, arrayList);
		}
		return newLine;
	}

	/**
	 * A recursion to find combination of words which total length is same as total length of wordLengths
	 * @param wordLengths - gap of wordLengths to fill
	 * @return combination of words that fill the gap
	 */
	private String[] recursion(int[] wordLengths) {		
		String[] foundWords = getWordsForWordLengths(wordLengths);

		if(foundWords != null) {
			return foundWords;
		}
		int minWordLength = getMinWordLength();
		int maxWordLength = getMaxWordLength();
		for(int k = 0; k < wordLengths.length; k++) {
			boolean wordLengthValid = true;
			int[] clonedWordLengths = wordLengths.clone();

			clonedWordLengths[k] -= 1;
			if(clonedWordLengths[k] < minWordLength) {
				wordLengthValid = false;
			}
			if(k < clonedWordLengths.length-1) {
				clonedWordLengths[k+1] += 1;
				if(clonedWordLengths[k+1] > maxWordLength) wordLengthValid = false;
			} else {
				clonedWordLengths[0] += 1;
				if(clonedWordLengths[0] > maxWordLength) wordLengthValid = false;
			}
			Arrays.sort(clonedWordLengths);

			String visit = "";
			for(int wordLength : clonedWordLengths) {
				visit += (visit.length() == 0) ? "" + wordLength : " " + wordLength;
			}
			
			if(!visited.contains(visit) && wordLengthValid) {
				visited.add(visit);
				String[] newWords = recursion(clonedWordLengths);
				if(newWords != null) {
					return newWords;
				}
			}
		}
		return null;
	}
		
	/**
	 * Returns the longest word available that is removed from bookMap
	 * @param length - length of the word
	 * @return found word or null
	 */
	private String getLongestWord(int length) {
		if(length == 0) return null;

		String longestWord = null;

		int longestLength = (length > bookMap.size()) ? bookMap.size() : length;
		while(longestWord == null) {
			longestWord = getWord(longestLength);
			longestLength--;
			if(longestLength == 0) {
				break;
			}
		}
		return longestWord;
	}
	
	/**
	 * Returns a word by its length
	 * @param length - length of the word
	 * @return found word or null
	 */
	private String getWord(int length) {
		if(length == 0) return null;

		ArrayList<String> arrayList = bookMap.get(length);
		if(arrayList == null || arrayList.size() == 0) {
			return null;
		}
		String word = (arrayList.get(0) != null) ? arrayList.get(0) : null;
		return word;
	}

	/**
	 * Returns array of words that have the lengths as in wordLengths array
	 * @param wordLengths lengths of words to find
	 * @return found words
	 */
	private String[] getWordsForWordLengths(int[] wordLengths) {
		String[] foundWords = new String[wordLengths.length];
		int foundWordsIndex = 0;

		for(int k = 0; k < wordLengths.length; k++) {
			int currentLengths = 1;
			int current = wordLengths[k];
			
			int tempIndex = k+1;
			while(tempIndex < wordLengths.length) {
				if(wordLengths[tempIndex] == current) {
					currentLengths++;
					k++;
				}
				tempIndex++;
			}
			// now we have the amount of current in variable currentLengths
			ArrayList<String> arrayList = bookMap.get(current);
			if(arrayList == null || arrayList.size() == 0) {
				return null;
			} else if(arrayList.size() < currentLengths) {
				return null;
			}
			for(int m = 0; m < currentLengths; m++) {
				foundWords[foundWordsIndex] = arrayList.get(m);
				foundWordsIndex++;
			}
		}

		return foundWords;
	}
	
	/**
	 * Removes a word from bookMap
	 * @param word - word to be removed
	 */
	private void removeWord(String word) {
		ArrayList<String> arrayList = bookMap.get(word.length());
		if(arrayList == null) return;
		
		for(int k = 0; k < arrayList.size(); k++) {
			if(arrayList.get(k).equals(word)) {
				arrayList.remove(k);
				break;
			}
		}
	}
	
	
	/**
	 * @return the longest word found in bookMap
	 */
	private int getMaxWordLength() {
		int maxWordLength = 0;

		for (int key : bookMap.keySet()) {
		   ArrayList<String> arrayList = bookMap.get(key);
		   if(arrayList != null && arrayList.size() > 0) {
			   int wordLength = arrayList.get(0).length();
		       maxWordLength = (wordLength > maxWordLength) ? wordLength : maxWordLength;
		   }
		}
		return maxWordLength;
	}

	/**
	 * @return the shortest word found in bookMap
	 */
	private int getMinWordLength() {
		int minWordLength = Integer.MAX_VALUE;

		for (int key : bookMap.keySet()) {
		   ArrayList<String> arrayList = bookMap.get(key);
		   if(arrayList != null && arrayList.size() > 0) {
			   int wordLength = arrayList.get(0).length();
			   minWordLength = (wordLength < minWordLength) ? wordLength : minWordLength;
		   }
		}
		return minWordLength;
	}
	
	public ArrayList<String> getBook() {
		return book;
	}
}
