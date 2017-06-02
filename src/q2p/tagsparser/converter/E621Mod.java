package q2p.tagsparser.converter;

import java.util.LinkedList;

public final class E621Mod {
	public final String name;
	public int amount;
	public final int type;
	public final int id;
	public final LinkedList<E621Mod> aliases = new LinkedList<E621Mod>();
	public final LinkedList<Boolean> aliasApproved = new LinkedList<Boolean>();
	public final LinkedList<E621Mod> implies = new LinkedList<E621Mod>();
	public final LinkedList<Boolean> impliesApproved = new LinkedList<Boolean>();

	public E621Mod(final String name, final int amount, final int type, final int id) {
		this.name = name;
		this.amount = amount;
		this.type = type;
		this.id = id;
	}
	
	public final void merge(final int amount) {
		this.amount = Math.max(this.amount, amount);
	}

	public static final void alias(final E621Mod tag1, final E621Mod tag2, final boolean approved) {
		tag1.aliases.addLast(tag2);
		tag2.aliases.addLast(tag1);
		tag1.aliasApproved.addLast(approved);
		tag2.aliasApproved.addLast(approved);
	}

	public final void imply(final E621Mod tag, final boolean approved) {
		implies.addLast(tag);
		impliesApproved.addLast(approved);
	}

	public void unlinkAlias(final E621Mod tag) {
		int i = aliases.indexOf(tag);
		aliases.remove(i);
		aliasApproved.remove(i);
	}
}