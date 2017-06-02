package q2p.tagsparser.converter;

public final class Rule34Alias {
	public final String alias;
	public final int aliasCount;
	public final String to;
	public final int toCount;
	public final String reason;

	public Rule34Alias() {
		alias = RawReader.next();
		aliasCount = RawReader.nextInt();
		to = RawReader.next();
		toCount = RawReader.nextInt();
		reason = RawReader.next();
	}
}