package q2p.tagsparser.converter;

public final class E621Alias {
	public final int id;
	public final String name;
	public final int alias_id;
	public final boolean pending;

	public E621Alias() {
		id = RawReader.nextInt();
		name = RawReader.next();
		alias_id = RawReader.nextInt();
		pending = RawReader.nextBoolean();
	}
}