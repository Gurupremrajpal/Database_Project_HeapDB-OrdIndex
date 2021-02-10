
package disk_store;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

//The Java file implements a Binary search operations which adds a new indexes.
/**
 * An ordered index.  Duplicate search key values are allowed,
 * but not duplicate index table entries.  In DB terminology, a
 * search key is not a superkey.
 * 
 * A limitation of this class is that only single integer search
 * keys are supported.
 *
 */
public class OrdIndex implements DBIndex 
{
	                                   
	private List <List <Integer>> index; 
	public OrdIndex() { // Initialization of index
		index = new ArrayList <List <Integer>>(); 
	}

	int insertBinarySearch(int key) //This binary search adds Index
    { 
		int start = 0;
		int end = index.size() - 1;
		int mid = (start+end)/2;
		if(end<start)
		{
			return 0;
		}
		if(index.get(end).get(0) < key)
		{
			return end+1;
		}
		while(start<=end)
		{
			mid = (start+end)/2;
			if(index.get(mid).get(0) == key)
			{
				return mid;
			}
			if(index.get(mid).get(0) > key)
			{
				end = mid - 1;
			}
			if(index.get(mid).get(0) < key )
			{
				start = mid + 1;
			}
		}
        return mid; 
    } 

	int binarySearch(int key) //This binary search does the searching part
    { 
		int start = 0;
		int end = index.size() - 1;
		int mid ;
		while(start<=end)
		{
			mid = (start+end)/2;
			if(index.get(mid).get(0) == key)
			{
				return mid;
			}
			if(index.get(mid).get(0) > key)
			{
				end = mid - 1;
			}
			if(index.get(mid).get(0) < key )
			{
				start = mid + 1;
			}
		}
        return -1; 
    } 
	
	int blockBinarySearch(List <Integer> vals, int blockNum) //This binary searches for blockNum instead of key
    { 
		int start = 0;
		int end = vals.size()-1;
		int mid;
		while(start<=end)
		{
			mid = (start+end)/2;
			if( vals.get(mid) == blockNum)
			{
				return mid;
			}
			if( vals.get(mid) > blockNum)
			{
				end = mid - 1;
			}
			if( vals.get(mid) < blockNum)
			{
				start = mid + 1;
			}
		}
        return -1; 
    } 
	@Override
	public List<Integer> lookup(int key) 
	{ 
		
		int pos = binarySearch(key);
		List <Integer> result = new ArrayList <Integer>();
		if (pos == -1)
		{
			return result;
		}
		
		for(int i=1; i<index.get(pos).size(); i++)
		{
			result.add(index.get(pos).get(i));
		}
		
		Set <Integer> unique = new HashSet <Integer>();
		for (Integer t: result)
		{
			unique.add(t);
		}
		
		result.clear();
		
		for (Integer t : unique) 
		{
			result.add(t);
		}
		return result;
	}

	@Override 
	public void insert(int key, int blockNum) 
	{ 
		//If index is not empty
		if (index.size() > 0)
		{
			int pos = insertBinarySearch(key);
			//Adding the at the end of the index
			if (pos == index.size())
			{
				ArrayList <Integer> ordIndex = new ArrayList <Integer>();// new list
				index.add(ordIndex);
				index.get(pos).add(key);
				index.get(pos).add(blockNum);
			}
			//Adding in between the index
			else
			{	
				//key is not present at the position
				if (index.get(pos).get(0) != key)
				{
					ArrayList <Integer> ordIndex = new ArrayList <Integer>();// new list
					index.add(pos, ordIndex);
					index.get(pos).add(key);
					index.get(pos).add(blockNum);
				}
				//adding the key at the position and then sorting 
				else
				{
					index.get(pos).add(blockNum);
					Collections.sort(index.get(pos).subList(1, index.get(pos).size())); // Does the sorting of  values  in index
				}
			}
		}
		//Index is empty
		else
		{
			ArrayList <Integer> ordIndex = new ArrayList <Integer>();// new list
			index.add(0, ordIndex);
			index.get(0).add(key);
			index.get(0).add(blockNum);
		}
	}
	@Override
	public void delete(int key, int blockNum) {
		int pos = binarySearch(key);
		if (pos != -1)
		{
			List <Integer> temp = new ArrayList<Integer>();
			for(int i=1; i<index.get(pos).size(); i++)
			{
				temp.add(index.get(pos).get(i));
			}
			
			int blockPos = blockBinarySearch(temp, blockNum); 
			if (blockPos != -1) // deleting the block
			{
				index.get(pos).remove(blockPos+1);
			}
			if (index.get(pos).size() == 1)
			{
				index.remove(pos);
			}
		}
	}
	/**
	 * Return the number of entries in the index
	 * @return
	 */
	public int size() {
		int size = 0;
		for (int i = 0; i < index.size(); i++)
		{
			size += index.get(i).size()-1;
		}
		return size;
	}
	@Override
	public String toString() {
		return index.toString(); 
	}
}
