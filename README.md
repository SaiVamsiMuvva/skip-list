Instructions to execute the program:
	javac SkipListDriver.java
  java SkipListDriver <input-file-path>

Implementation of SkipLists:
	Each entry in a skiplist has attributes: element- that it stores, nextPointers- An array of references to entries at next indexes, num_levels - Number of levels present in the nextPointers array.
	SkipList has a header, a tail(used to retrieve the last element efficiently), size of the skiplist, maxLevel(maximum number of levels in nextPointers)
	SkipList is initialized with maxLevels as 0 and later used rebuild() method to increase or decrease the maxLevel when new elements are added to or subtracted from the list.
	The findIndex() method has a runtime complexity of O(logn) which is obtained by storing width in each entry that stores the count of number of elements a particular index(in nextPointers) skips.

Sample Output for:
Input: small.txt
Output:
		117
		Time: 9 msec.
		Memory: 1 MB / 61 MB.
Input: large.txt
Output:
		130122
		Time: 2938 msec.
		Memory: 244 MB / 320 MB.

Comparison of SkipLists with TreeMaps:
Instructions to execute the program:
	javac SkipListTreeMapCompare.java
  java SkipListTreeMapCompare <input-file-path>

For small size lists, TreeMap performs much better than SkipLists(Almost 250%) but, as the size of lists grows, the performance of SkipLists also gets better, and almost comparable to TreeMaps.
It demonstrates a difference of only a few milliseconds, although the memory consumption by TreeMap is more.
