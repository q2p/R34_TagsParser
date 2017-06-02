package q2p.tagsparser.converter;

import java.util.LinkedList;

public final class DBMod {
	public String name;
	public int amount;
	public final String category;
	public final String description;
	public final LinkedList<DBMod> aliases = new LinkedList<DBMod>();
	public final LinkedList<DBMod> implies = new LinkedList<DBMod>();

	public DBMod(final String name, final int amount, final String category, final String description) {
		this.name = name;
		this.amount = amount;
		this.category = category;
		this.description = description;
	}
	
	public final void merge(final int amount) {
		this.amount = Math.max(this.amount, amount);
	}

	public static final void alias(final DBMod tag1, final DBMod tag2) {
		tag1.aliases.addLast(tag2);
		tag2.aliases.addLast(tag1);
	}

	public void implies(final LinkedList<String> implies) {
		while(!implies.isEmpty()) {
			final DBMod tag = Converter.pushDB(implies.removeFirst(), 1, "", "");
			this.implies.addLast(tag);
		}
	}

	public final void updateName() {
		name = name.replace(' ', '_');
	}

	public void unlinkAlias(final DBMod tag) {
		aliases.remove(tag);
	}
}