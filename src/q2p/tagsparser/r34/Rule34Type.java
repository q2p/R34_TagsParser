package q2p.tagsparser.r34;

public enum Rule34Type {
	general("general", 0), copyright("copyright", 1), character("character", 2), artist("artist", 3);

	public final String text;
	public final int id;

	private Rule34Type(final String text, final int id) {
		this.text = text;
		this.id = id;
	}
	
	public static final Rule34Type getByName(final String name) {
		for(final Rule34Type t : values())
			if(t.text.equals(name))
				return t;
		
		return null;
	}
	public static final Rule34Type getByNameMatch(final String name) {
		for(final Rule34Type t : values())
			if(name.contains(t.text))
				return t;
		
		return null;
	}
}