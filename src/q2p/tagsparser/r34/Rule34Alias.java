package q2p.tagsparser.r34;

import java.nio.charset.StandardCharsets;
import q2p.tagsparser.global.HardAcess.Flushable;

public final class Rule34Alias implements Flushable {
	public final String alias;
	public final int aliasCount;
	public final String to;
	public final int toCount;
	public final String reason;

	public Rule34Alias(final String alias, final int aliasCount, final String to, final int toCount, final String reason) {
		this.alias = alias;
		this.aliasCount = aliasCount;
		this.to = to;
		this.toCount = toCount;
		this.reason = reason;
	}
	
	public final byte[] flush() {
		return (alias+"\n"+aliasCount+"\n"+to+"\n"+toCount+"\n"+reason+"\n").getBytes(StandardCharsets.UTF_8);
	}
}