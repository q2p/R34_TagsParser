package q2p.tagsparser.converter;

import java.util.LinkedList;

public final class DBImplication {
	public final String tag;
	public final LinkedList<String> implies = new LinkedList<String>();

	public DBImplication() {
		tag = RawReader.next();
		RawReader.nextStrings(implies);
	}
}