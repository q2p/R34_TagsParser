package q2p.tagsparser.converter;

import q2p.tagsparser.r34.Rule34Type;

public final class Rule34Tag {
	public final String name;
	public final int amount;
	public final Rule34Type type;

	public Rule34Tag() {
		amount = Integer.parseInt(RawReader.next());
		name = RawReader.next();
		type = Rule34Type.getByNameMatch(RawReader.next());
		if(type == null)
			throw new RuntimeException("Не правильный тип: "+type);
	}
	public Rule34Tag(final String name, final int amount, final Rule34Type type) {
		this.name = name;
		this.amount = amount;
		this.type = type;
	}
}