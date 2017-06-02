package q2p.tagsparser.converter;

public final class E621Tag {
	public final int id;
	public final String name;
	public final int count;
	public final int type;

	public E621Tag() {
		id = RawReader.nextInt();
		name = RawReader.next();
		count = RawReader.nextInt();
		type = RawReader.nextInt();
	}
}