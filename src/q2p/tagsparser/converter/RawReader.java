package q2p.tagsparser.converter;

import java.io.IOException;
import java.util.LinkedList;

final class RawReader {
	private static String content = null;
	private static int idx = 0;
	static final void open(final String path) throws IOException {
		content = Storage.getFileContents(path);
		idx = 0;
	}
	
	static final String next() {
		if(content == null)
			return null;
		
		int idx2 = content.indexOf("\n", idx);
		if(idx2 == -1) {
			content = null;
			idx = 0;
			return null;
		}
		
		final String ret = content.substring(idx, idx2);
		
		idx = idx2+1;
		
		return ret;
	}
	static final int nextInt() {
		return Integer.parseInt(next());
	}
	static final boolean nextBoolean() {
		return next().equals("true");
	}
	static final String nextStrings() {
		final StringBuilder builder = new StringBuilder();
		for(int i = nextInt(); i != 0; i--)
			builder.append(next());
		return builder.toString();
	}
	static final LinkedList<String> nextStrings(LinkedList<String> container) {
		for(int i = nextInt(); i != 0; i--)
			container.addLast(next());
		return container;
	}
	
	static boolean hasNext() {
		if(content == null)
			return false;
		
		return content.indexOf("\n", idx) != -1;
	}

}