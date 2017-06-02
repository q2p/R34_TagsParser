package q2p.tagsparser.converter;

public final class DBTag {
	public final String name;
	public final int amount;
	public final String category;
	public final String description;

	public DBTag() {
		name = RawReader.next();
		amount = RawReader.nextInt();
		category = RawReader.next();
		description = RawReader.nextStrings();
	}
}