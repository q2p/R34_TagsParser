package q2p.tagsparser.global;

import java.util.LinkedList;

public final class StringPool {
	private final LinkedList<String> pickable = new LinkedList<String>();
	private final char[] chars;
	private int gaveOut;
	private boolean finalized = false;
	
	public StringPool(final String start, final char[] characters) {
		chars = characters;
		pickable.addLast(start);
	}
	
	public final String next() {
		synchronized(pickable) {
			while(pickable.isEmpty()) {
				if(gaveOut == 0)
					return null;
				
				try {
					pickable.wait();
				} catch(final InterruptedException e) {}
			}
			
			gaveOut++;
			
			return pickable.removeFirst();
		}
	}
	public final boolean needFinalization() {
		synchronized(pickable) {
			if(!finalized) {
				finalized = pickable.isEmpty() && gaveOut == 0;
				
				return finalized;
			}
			return false;
		}
	}
	public final void answer(final String value) {
		synchronized(pickable) {
			gaveOut--;
			
			if(value != null) {
				for(final char c : chars)
					pickable.addFirst(value+c);
			}
			pickable.notifyAll();
		}
	}
}