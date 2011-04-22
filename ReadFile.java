import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ReadFile{
	
	private static String title;
	private static String author;
	private static String language;

	
    public static void main(String[] args){
    	
        try {
			
			//Sets up a file reader to read the file passed
			FileReader input = new FileReader("wealthofnations.txt");
            
			//Filter FileReader through a Buffered read to read a line at a time
			BufferedReader bufRead = new BufferedReader(input);
			
            String line; 	// String that holds current file line
            int count = 0;	// Line number of count 
            Integer startCountingAtLine=500;
            Integer endCountingAtLine=999999999;
        	char exclamationMark ='!';
        	Integer exclamationMarkCount = 0;
        	char questionMark = '?';
        	Integer questionMarkCount = 0;
        	char periodMark ='.';
        	Integer periodMarkCount = 0;
        	char commaMark =',';
        	Integer commaMarkCount = 0;
        	char quoteMark ='"';
        	Integer quoteMarkCount = 0;
        	char colonMark =':';
        	Integer colonMarkCount = 0;
        	char semicolonMark =';';
        	Integer semicolonMarkCount = 0;
        	char dashMark ='-';
        	Integer dashMarkCount = 0;

            
            // Read first line
            line = bufRead.readLine();
            count++;
            
            //Create HashMap to hold the word (key) and frequency
            HashMap<String, Integer> hm = new HashMap<String, Integer>();
            
			// Read through file one line at time until there are no new lines
            while (line != null){
                String []splits = line.split(" ");
                
                
                //Get Title
                if((count<30) && (splits[0].equals("Title:"))){
                	for(int k = 1; k < splits.length; k++){
                		if(k==1){
                			title = splits[k];
                		} else {
                			title = title + "_" + splits[k];
                		}
                	}
                }
                
                
                //Get Author
                if((count<30) && (splits[0].equals("Author:"))){
                	for(int k = 1; k < splits.length; k++){
                		if(k==1){
                			author = splits[k];
                		} else {
                			author = author + "_" + splits[k];
                		}
                	}
                }
                
                
                //Get Language
                if((count<30) && (splits[0].equals("Language:"))){
                	for(int k = 1; k < splits.length; k++){
                		if(k==1){
                			language = splits[k];
                		} else {
                			language = language + "_" + splits[k];
                		}
                	}
                }
        		

                //Only start counting words after the line starting with ***
                if ((count<30) && (splits[0].equals("***"))){
                	startCountingAtLine = count+1;
                }
                
                //Stop counting words after the line starting with: End of the Project Gutenberg
                if ((splits[0].equals("End")) && (splits[1].equals("of")) && (splits[2].equals("the")) && (splits[3].equals("Project")) && (splits[4].equals("Gutenberg"))){
                	endCountingAtLine = count;
                	//System.out.println("Stop counting at: " + endCountingAtLine);
                }
                
                
                if ((count >= startCountingAtLine) && (count < endCountingAtLine)) {
                	//Count the !, ?, etc marks
                	for(int j = 0; j < line.length(); j++){
                		if(line.charAt(j) == questionMark){
                			questionMarkCount++;
                		}
                		if(line.charAt(j) == exclamationMark){
                			exclamationMarkCount++;
                		}
                		if(line.charAt(j) == periodMark){
                			periodMarkCount++;
                		}
                		if(line.charAt(j) == commaMark){
                			commaMarkCount++;
                		}
                		if(line.charAt(j) == quoteMark){
                			quoteMarkCount++;
                		}
                		if(line.charAt(j) == colonMark){
                			colonMarkCount++;
                		}
                		if(line.charAt(j) == semicolonMark){
                			semicolonMarkCount++;
                		}
                		if(line.charAt(j) == dashMark){
                			dashMarkCount++;
                		}
                			
                	}
                	
                	for(int k = 0; k < splits.length; k++){
                		
                		String cleanedString = splits[k].toLowerCase(); //change all words to lower case

                		//Add the cleaned word to HashMap. If it already exists, just increment value in the HashMap
                		if(hm.containsKey(cleanedString)){
                			int oldValue = (Integer) hm.get(cleanedString);
                			hm.put(cleanedString, oldValue+1);
                		}else{
                			hm.put(cleanedString, new Integer(1));
                		}
                	}
                }
                line = bufRead.readLine();
                count++;
            } 
            bufRead.close();

            hm.put("!", exclamationMarkCount);
            hm.put("?", questionMarkCount);
            hm.put(".", periodMarkCount);
            hm.put(",", commaMarkCount);
            hm.put("\"", quoteMarkCount);
            hm.put(":", colonMarkCount);
            hm.put(";", semicolonMarkCount);
            hm.put("-", dashMarkCount);
            
            
            System.out.println("Title :" + title);
            System.out.println("Author :" + author);
            System.out.println("Language :" + language);
            
            System.out.println("questionMarkCount: " + questionMarkCount);
            System.out.println("exclamationMarkCount: " + exclamationMarkCount);
            
            //Get initial HashMap size before removing first and last characters
            Integer initialHashMapSize = hm.size();
            
            
            //Get the total # of words in the book and assign it to sumNumbers
            Object[] frequency = hm.values().toArray(); 
            //System.out.println("Frequency length: "+ frequency.length);
            int sumNumbers=0;
            for (int f = 0; f<frequency.length; f++){
            	sumNumbers = sumNumbers + ((Integer) frequency[(Integer) f]);
            }
          
            
            //Go through all the keys (words) in the ArrayList and remove common punctuation
            System.out.println("Initial number of unique words: " + initialHashMapSize);
            
            HashMap<String, Integer> cleanedNewLinesHashMap = new LinkedHashMap<String, Integer>();
            cleanedNewLinesHashMap = removeEmptyLines(hm);            
            System.out.println("After cleaning New Lines: " + cleanedNewLinesHashMap.size());
            
            HashMap<String, Integer> cleanedFirstCharsHashMap = new LinkedHashMap<String, Integer>();
            cleanedFirstCharsHashMap = removeBadFirstChars(hm);
            System.out.println("After removing Bad First characters: " + cleanedFirstCharsHashMap.size());
            
            HashMap<String, Integer> cleanedLastCharsHashMap = new LinkedHashMap<String, Integer>();
            cleanedLastCharsHashMap = removeBadLastChars(cleanedFirstCharsHashMap);
            System.out.println("After removing Bad Last characters 1x: " + cleanedLastCharsHashMap.size());
    
            HashMap<String, Integer> cleanedLastCharsHashMap2 = new LinkedHashMap<String, Integer>();
            cleanedLastCharsHashMap2 = removeBadLastChars(cleanedLastCharsHashMap);
            System.out.println("After removing Bad Last characters 2x: " + cleanedLastCharsHashMap2.size());
            
            
            //Sort HashMap
            HashMap<String, Integer> hms = new LinkedHashMap<String, Integer>();
            hms = sortHashMap(cleanedLastCharsHashMap2);
            
            
            //Retrieve all values from sorted HashMap and print to a file
            Iterator<?> iteratorAllValues = hms.entrySet().iterator();
            try {
        		FileWriter writer = new FileWriter(author + "-" + title + ".txt"); //replace this with author-title
            while(iteratorAllValues.hasNext()){
            	String currentString = String.valueOf(iteratorAllValues.next());
            	//System.out.println(iteratorAllValues.next());
                  		writer.write("\n" + currentString);
            }
            writer.close();
            } catch(IOException ex) {
        		ex.printStackTrace();
        	} 	
            
            
            System.out.println("Filtered number of unique words (hms size): " + hms.size());
            System.out.println("Total word count (sumNumbers): " + sumNumbers);
            
        }catch (ArrayIndexOutOfBoundsException e){
            /* If no file was passed on the command line, this exception is
			generated. A message indicating how to the class should be
			called is displayed */
			System.out.println("ArrayIndexOutOfBounds Exception thrown.\n");			

		}catch (IOException e){
			// If another exception is generated, print a stack trace
            e.printStackTrace();
        }
        
    }// end main
    
    
    
    
    
    private static HashMap<String, Integer> sortHashMap(HashMap<String, Integer> input){
    	
    	Map<String, Integer> tempMap = new HashMap<String, Integer>();
    	for (String wsState : input.keySet()){
    		tempMap.put(wsState,input.get(wsState));
    	}
    	List mapKeys = new ArrayList(tempMap.keySet());
    	List unsortedMapValues = new ArrayList(tempMap.values());
    	List sortedMapValues = new ArrayList(unsortedMapValues);
    	//Collections.sort(sortedMapValues); //to see smallest values first
    	Collections.sort(sortedMapValues, Collections.reverseOrder()); //to see largest values first
    	HashMap sortedMap = new LinkedHashMap();
    	Object[] sortedArray = sortedMapValues.toArray();
    	int size = sortedArray.length;
    	for (int i=0; i < size ; i++){
    		Integer value = (Integer)sortedArray[i];
    		int mapValueIndex = unsortedMapValues.indexOf(value);
    		sortedMap.put(mapKeys.get(mapValueIndex),(Integer)sortedArray[i]);
    		mapKeys.remove(mapValueIndex);
    		unsortedMapValues.remove(mapValueIndex);
    	}
    	
    	return sortedMap;
    	}
    





    private static HashMap<String, Integer> removeEmptyLines(HashMap<String, Integer> input){

    	if(input.containsKey("")){
    		//System.out.println("Empty present ");
    		input.remove("");
    	}

    	return input;
    }





    private static HashMap<String, Integer> removeBadFirstChars(HashMap<String, Integer> input){

    	ArrayList<String> wordsWithBadFirstChar = new ArrayList<String> ();
    	Iterator<?> iteratorAllKeys = input.keySet().iterator();

    	while(iteratorAllKeys.hasNext()){        
    		String currentString = String.valueOf(iteratorAllKeys.next());

    		//Add all words with invalid first char to ArrayList wordsWithBadFirstChar
    		if (((currentString.charAt(0) > 0) && (currentString.charAt(0) < 48)) || ((currentString.charAt(0) > 57) && (currentString.charAt(0) < 65)) || (currentString.charAt(0) > 122) || ((currentString.charAt(0) > 90) && (currentString.charAt(0) < 97))){
    			//if the string is just one character like -, don't add it to bad words array list
    			if (currentString.length() > 1){
    				wordsWithBadFirstChar.add(currentString);
    			}
    		}

    	}

    	//Remove all words with bad first char
    	//try {
    		//FileWriter writer = new FileWriter("badfirstchar.txt");
    		//System.out.println("Number of words with bad first char: " + wordsWithBadFirstChar.size());
    		//writer.write("Number of words with bad first char: " + wordsWithBadFirstChar.size());
    		Integer counter=0;

    		while(wordsWithBadFirstChar.isEmpty() == false) {
    			//System.out.println("Word to delete: " + wordsWithBadFirstChar.get(counter));
    			//writer.write("\nWord to delete: " + wordsWithBadFirstChar.get(counter));
    			String wordWithoutBadFirstChar = wordsWithBadFirstChar.get(counter).substring(1, wordsWithBadFirstChar.get(counter).length());

    			//if the passed in HashMap already contains the word without bad first char, just append. If not add it to HashMap and set value=1
    			if(input.containsKey(wordWithoutBadFirstChar)){
    				Integer valueWithoutBadFirstChar = (Integer) input.get(wordWithoutBadFirstChar);
    				Integer valueWithBadFirstChar = (Integer) input.get(wordsWithBadFirstChar.get(counter));
    				input.put(wordWithoutBadFirstChar, new Integer(valueWithoutBadFirstChar+valueWithBadFirstChar));
    			}else{
    				input.put(wordWithoutBadFirstChar, new Integer(1));
    			}

    			input.remove(wordsWithBadFirstChar.get(counter));
    			wordsWithBadFirstChar.remove(wordsWithBadFirstChar.get(counter));
    			//System.out.println("New number of words with bad first char: " + wordsWithBadFirstChar.size());
    		}

    	//	writer.close();
    	//} catch(IOException ex) {
    	//	ex.printStackTrace();
    	//}

    	return input;
    }


    
    
    

    private static HashMap<String, Integer> removeBadLastChars(HashMap<String, Integer> input){

    	ArrayList<String> wordsWithBadLastChar = new ArrayList<String> ();
    	Iterator<?> iteratorAllKeys2 = input.keySet().iterator();

    	while(iteratorAllKeys2.hasNext()){        
    		String currentString = String.valueOf(iteratorAllKeys2.next());

    		//Add all words with invalid last char to ArrayList wordsWithBadLastChar
    		if (((currentString.charAt(currentString.length()-1) > 0) && (currentString.charAt(currentString.length()-1) < 48)) || ((currentString.charAt(currentString.length()-1) > 57) && (currentString.charAt(currentString.length()-1) < 65)) || (currentString.charAt(currentString.length()-1) > 122) || ((currentString.charAt(currentString.length()-1) > 90) && (currentString.charAt(currentString.length()-1) < 97))) {
    			//if the string is just one character like -, don't add it to bad words array list
    			if (currentString.length() > 1){
    				wordsWithBadLastChar.add(currentString);
    				//System.out.println(currentString);
    			}
    		}

    	}

    	//Remove all words with bad last char
    	//try {
    		//FileWriter writer = new FileWriter("badlastchar.txt");
    		//System.out.println("Number of words with bad last char: " + wordsWithBadLastChar.size());
    		//writer.write("Number of words with bad last char: " + wordsWithBadLastChar.size());
    		Integer counter=0;

    		while(wordsWithBadLastChar.isEmpty() == false) {
    			//writer.write("\nWord to delete: " + wordsWithBadLastChar.get(counter));
    			//System.out.println("Word to delete: " + wordsWithBadLastChar.get(counter2));
    			String wordWithoutBadLastChar = wordsWithBadLastChar.get(counter).substring(0, wordsWithBadLastChar.get(counter).length()-1);

    			//if the passed in HashMap already contains the word without bad last char, just append. If not add it to HashMap and set value=1
    			if(input.containsKey(wordWithoutBadLastChar)){
    				Integer valueWithoutBadLastChar = (Integer) input.get(wordWithoutBadLastChar);
    				Integer valueWithBadLastChar = (Integer) input.get(wordsWithBadLastChar.get(counter));
    				input.put(wordWithoutBadLastChar, new Integer(valueWithoutBadLastChar+valueWithBadLastChar));
    			}else{
    				input.put(wordWithoutBadLastChar, new Integer(1));
    			}

    			input.remove(wordsWithBadLastChar.get(counter));
    			wordsWithBadLastChar.remove(wordsWithBadLastChar.get(counter));
    			//System.out.println("New number of words with bad last char: " + wordsWithBadLastChar.size());
    		}

    	//	writer.close();
    	//} catch(IOException ex) {
    	//	ex.printStackTrace();
    	//}

    	return input;
    }

    
    
}

