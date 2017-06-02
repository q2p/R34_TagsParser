package q2p.tagsparser.e621;

import java.nio.charset.StandardCharsets;
import q2p.tagsparser.global.HardAcess.Flushable;

public final class E621Implication implements Flushable {
	public final int id;
	public final int consequent_id;
	public final int predicate_id;
	public final boolean pending;

	public E621Implication(final int id, final int consequent_id, final int predicate_id, final boolean pending) {
		this.id = id;
		this.consequent_id = consequent_id;
		this.predicate_id = predicate_id;
		this.pending = pending;
	}

	public final byte[] flush() {
		return (id+"\n"+consequent_id+"\n"+predicate_id+"\n"+pending+"\n").getBytes(StandardCharsets.UTF_8);
	}
}