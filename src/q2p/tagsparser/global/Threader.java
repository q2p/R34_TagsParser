package q2p.tagsparser.global;

import q2p.tagsparser.derpibooru.DerpiBooru;
import q2p.tagsparser.e621.E621;
import q2p.tagsparser.r34.Rule34;

public final class Threader {
	public static final int availableProcessors = Math.max(1, Runtime.getRuntime().availableProcessors());
	
	public static final void collect() {
		final Thread[] threads = new Thread[availableProcessors];
		
		for(int i = 0; i != threads.length; i++)
			threads[i] = new Thread(new CollectThread());

		for(final Thread thread : threads)
			thread.start();
		
		try {
			for(final Thread thread : threads)
				thread.join();
		} catch(final InterruptedException e) {}
	}
	
	private static final class CollectThread implements Runnable {
		public void run() {
			Rule34.collect();
			E621.collect();
			DerpiBooru.collect();
		}
	}
}