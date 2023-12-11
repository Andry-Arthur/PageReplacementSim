import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * FIFO, LRU, and Optimal page-replacement algorithms implementation assuming demand paging
 * HW# 7
 * @author Andry Arthur
 *
 * Justification for method overloads: it made things easier to simulate everything 
 * 	(Frames 1-7) in SimReplacement.java
 */
public class PageReplacement {
	int nFrames;
	int nRequest;
	int[] request;

	public PageReplacement(int numFrames, int numReq) {
		nFrames = numFrames;
		nRequest = numReq;
		request = new int[nRequest];

		//initialize requests
		for(int i = 0; i < nRequest; ++i) {
			request[i] = (int) (Math.random() * (10));
		}
	}


	public void runFIFO() {
		int nFaults = 0;
		Queue<Integer> frames = new LinkedList<>();

		for(int i = 0; i < nRequest; ++i) {
			int curr = request[i];
			if(!frames.contains(curr)) {
				if(frames.size() < nFrames) {
					frames.add(curr);
				}
				else {
					frames.poll();
					frames.add(curr);
				}
				nFaults++;
			}
		}
		System.out.println("FIFOReplacement: frames: " + nFrames + " faults: " + nFaults);
	}

	public void runFIFO(int frame) {
		nFrames = frame;
		runFIFO();
	}

	public void runLRU() {
		int nFaults = 0;
		ArrayList<Integer> frames = new ArrayList<>();
		Queue<Integer> lru = new LinkedList<>(); //keeps track of order of used items

		for(int i = 0; i < nRequest; ++i) {
			int curr = request[i];
			if(!frames.contains(curr)) {
				if(frames.size() < nFrames) {
					frames.add(curr);
					lru.add(curr);
					nFaults++;

				}
				else {
					int leastRecent = lru.poll();
					for(int j = 0; j < frames.size(); ++j) {
						if(frames.get(j) == leastRecent) {
							frames.set(j, curr);
							nFaults++;
							lru.add(curr);
						}
					}
				}
			}
			else {
				//if curr is in frames, update LRU queue to reflect that curr was recently encountered
				lru.remove(curr);
				lru.add(curr);
			}
		}
		System.out.println("LRUReplacement: frames: " + nFrames + " faults: " + nFaults);
	}

	public void runLRU(int frame) {
		nFrames = frame;
		runLRU();
	}

	public void runOptimal() {
		int nFaults = 0;
		HashMap<Integer, Integer> framesMap = new HashMap<>();
		outer: for(int i = 0; i < nRequest; ++i) {
			int curr = request[i];
			if(!framesMap.containsValue(curr)) {
				//memory not full: fill in the space 
				if(framesMap.size() < nFrames) {
					framesMap.put(framesMap.size(), curr);
					nFaults++;
				}
				else {
					int[] ind = new int[nFrames]; //keeps track of max index of item per page frame 
					for(int j = 0; j < nFrames; ++j) {
						int currFrameItem = framesMap.get(j);
						
						//Make sure curr item in given frame is present among following requests
						if(arrContains(request, currFrameItem, i)==false) {
							framesMap.put(j, curr);
							nFaults++;
							continue outer; //directly jump back to next curr as frames have been updated
						}
						else {
							for(int k = i+1; k < nRequest; ++k) {
								if(currFrameItem==request[k]) {
									ind[j] = k;
									break;
								}
							}
						}
					}
					
					//getting the index of frame to be replaced
					int farInd = -1;
					int frameInd = -1;
					for(int j = 0; j < nFrames; ++j) {
						if(ind[j] > farInd) {
							farInd = ind[j];
							frameInd = j;
						}
					}
					framesMap.put(frameInd, curr);
					nFaults++;
				}
			}
		}
		System.out.println("OptimalReplacement: frames: " + nFrames + " faults: " + nFaults);
	}

	public void runOptimal(int frame) {
		nFrames = frame;
		runOptimal();
	}


	public boolean arrContains(int[] arr, int val, int startIndex) {
		for(int i = startIndex; i < arr.length; ++i) {
			if(arr[i]==val) return true;
		}
		return false;
	}

	public String toString() {
		return Arrays.toString(request);
	}
}
