import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

//Driver program for skip list implementation.

public class SkipListTreeMapCompare {
    public static void main(String[] args) throws FileNotFoundException {
	Scanner sc;
	File file = null;
	if (args.length > 0) {
	    file = new File(args[0]);
	    sc = new Scanner(file);
	} else {
	    sc = new Scanner(System.in);
	}
	String operation = "";
	long operand = 0;
	int modValue = 999983;
	long result = 0;
	Long returnValue = null;
	SkipListImpl<Long> skipList = new SkipListImpl<>();
	TreeMap<Long, Long> treeMap = new TreeMap<>();
	// Initialize the timer
	Timer timer = new Timer();

	while (!((operation = sc.next()).equals("End"))) {
	    switch (operation) {
	    case "Add": {
		operand = sc.nextLong();
		if(skipList.add(operand)) {
		    result = (result + 1) % modValue;
		}
		break;
	    }
	    case "Remove": {
		operand = sc.nextLong();
		if (skipList.remove(operand) != null) {
		    result = (result + 1) % modValue;
		}
		break;
	    }
	    case "Contains":{
		operand = sc.nextLong();
		if (skipList.contains(operand)) {
		    result = (result + 1) % modValue;
		}
		break;
	    }
	    default:
            break;
	    }
	}

	// End Time
	timer.end();

	System.out.println("Result given by SkipList is " + result);
	System.out.println("Time taken by SkipList is " + timer);
	System.out.println();
	
	result = 0;
	sc = new Scanner(file);
	timer.start();
	while (!((operation = sc.next()).equals("End"))) {
	    switch (operation) {
	    case "Add": {
		operand = sc.nextLong();
		if(treeMap.put(operand , Long.valueOf(0)) == null) {
		    result = (result + 1) % modValue;
		}
		break;
	    }
	    case "Remove": {
		operand = sc.nextLong();
		if (treeMap.remove(operand) != null) {
		    result = (result + 1) % modValue;
		}
		break;
	    }
	    case "Contains":{
		operand = sc.nextLong();
		if (treeMap.containsKey(operand)) {
		    result = (result + 1) % modValue;
		}
		break;
	    }
	    default:
            break;
	    }
	}
	timer.end();
	System.out.println("Result given by TreeMap is " + result);
	System.out.println("Time taken by TreeMap is " + timer);
    }
}