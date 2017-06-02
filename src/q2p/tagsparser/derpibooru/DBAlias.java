package q2p.tagsparser.derpibooru;

import java.nio.charset.StandardCharsets;
import q2p.tagsparser.global.HardAcess.Flushable;

public final class DBAlias implements Flushable {
	public final String tag;
	public final String alias;

	public DBAlias(final String alias, final String tag) {
		this.alias = alias;
		this.tag = tag;
	}

	public final byte[] flush() {
		return (alias+"\n"+tag+"\n").getBytes(StandardCharsets.UTF_8);
	}
}