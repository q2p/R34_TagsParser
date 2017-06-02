package q2p.tagsparser.derpibooru;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import q2p.tagsparser.global.Assist;
import q2p.tagsparser.global.HardAcess.Flushable;

public final class DBTag implements Flushable {
	public final String name;
	public final int count;
	public final String category;
	public final LinkedList<String> shortDescription = new LinkedList<String>();

	public DBTag(final String name, int count, final String category, final String shortDescription) {
		this.name = name;
		this.count = count;
		this.category = category;
		Assist.split(shortDescription.replace("\r", ""), "\n", this.shortDescription);
	}

	public final byte[] flush() {
		StringBuilder sb = new StringBuilder(name);
		sb.append("\n");
		sb.append(count);
		sb.append("\n");
		sb.append(category);
		sb.append("\n");
		sb.append(shortDescription.size());
		sb.append("\n");
		while(!shortDescription.isEmpty()) {
			sb.append(shortDescription.removeFirst());
			sb.append("\n");
		}
		return (sb.toString()).getBytes(StandardCharsets.UTF_8);
	}
}