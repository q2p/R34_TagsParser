package q2p.tagsparser.derpibooru;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import q2p.tagsparser.global.HardAcess.Flushable;

public final class DBImplication implements Flushable {
	public final String tag;
	public final LinkedList<String> implies;

	public DBImplication(final String tag, final LinkedList<String> implies) {
		this.tag = tag;
		this.implies = implies;
	}

	public final byte[] flush() {
		StringBuilder sb = new StringBuilder(tag);
		sb.append("\n");
		sb.append(implies.size());
		sb.append("\n");
		while(!implies.isEmpty()) {
			sb.append(implies.removeFirst());
			sb.append("\n");
		}
		return (sb.toString()).getBytes(StandardCharsets.UTF_8);
	}
}