package q2p.tagsparser.global;

import java.util.Iterator;
import java.util.LinkedList;

public final class IntegerPool {
	private final Object LOCK = new Object();
	private int nextInt;
	private int limit = -1;
	private final int step;
	private final LinkedList<Integer> waitFor = new LinkedList<Integer>();
	private boolean finalized = false;
	
	public IntegerPool(final int start, final int step) {
		this.nextInt = start;
		this.step = step;
	}
	
	public final int next() {
		synchronized(LOCK) {
			if(limit != -1 && nextInt > limit)
				return -1;
						
			final int ret = nextInt;
			waitFor.addFirst(new Integer(ret));
			
			nextInt += step;
						
			return ret;
		}
	}
	public final boolean answer(final int value, final boolean haveMore) {
		synchronized(LOCK) {
			if(!haveMore) {
				if(limit == -1)
					limit = value;
				else
					limit = Math.min(limit, value);
			}
			
			final Iterator<Integer> iter = waitFor.iterator();
			while(iter.next().intValue() != value) {}
			iter.remove();
			
			if(!finalized) {
				if(limit != -1 && value > limit && waitFor.isEmpty())
					finalized = true;
				
				return finalized;
			}
			return false;
		}
	}
}