package q2p.tagsparser.converter;

public final class E621Implication {
	public final int id;
	public final int consequent_id;
	public final int predicate_id;
	public final boolean pending;

	public E621Implication() {
		id = RawReader.nextInt();
		consequent_id = RawReader.nextInt();
		predicate_id = RawReader.nextInt();
		pending = RawReader.nextBoolean();
	}
}