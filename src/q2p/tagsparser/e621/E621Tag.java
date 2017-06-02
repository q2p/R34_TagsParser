package q2p.tagsparser.e621;

import java.nio.charset.StandardCharsets;
import q2p.tagsparser.global.HardAcess.Flushable;

public final class E621Tag implements Flushable{
	public final int id;
	public final String name;
	public final int count;
	public final int type;

	public E621Tag(final int id, final String name, final int count, final int type) {
		this.id = id;
		this.name = name;
		this.count = count;
		this.type = type;
	}

	public final byte[] flush() {
		return (id+"\n"+name+"\n"+count+"\n"+type+"\n").getBytes(StandardCharsets.UTF_8);
	}
}