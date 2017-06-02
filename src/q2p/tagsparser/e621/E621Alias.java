package q2p.tagsparser.e621;

import java.nio.charset.StandardCharsets;
import q2p.tagsparser.global.HardAcess.Flushable;

public final class E621Alias implements Flushable {
	public final int id;
	public final String name;
	public final int alias_id;
	public final boolean pending;

	public E621Alias(final int id, final String name, final int alias_id, final boolean pending) {
		this.id = id;
		this.name = name;
		this.alias_id = alias_id;
		this.pending = pending;
	}

	public final byte[] flush() {
		return (id+"\n"+name+"\n"+alias_id+"\n"+pending+"\n").getBytes(StandardCharsets.UTF_8);
	}
}