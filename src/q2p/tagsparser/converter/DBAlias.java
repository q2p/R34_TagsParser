package q2p.tagsparser.converter;

public final class DBAlias {
	public final String tag;
	public final String alias;

	public DBAlias() {
		alias = RawReader.next();
		tag = RawReader.next();
	}
}