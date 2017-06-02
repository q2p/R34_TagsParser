package q2p.tagsparser.converter;

import java.util.LinkedList;
import q2p.tagsparser.r34.Rule34Type;

public final class Rule34Mod {
	public final String name;
	public int amount;
	public Rule34Type type;
	public final LinkedList<Rule34Mod> aliases = new LinkedList<Rule34Mod>();
	public final LinkedList<String> aliasReasons = new LinkedList<String>();

	public Rule34Mod(final String name, final int amount, final Rule34Type type) {
		this.name = name;
		this.amount = amount;
		this.type = type;
	}
	
	public final void merge(final int amount, final Rule34Type type) {
		this.amount = Math.max(this.amount, amount);
		if(type.id > this.type.id)
			this.type = type;
	}

	public static final void alias(final Rule34Mod tag1, final Rule34Mod tag2, final String reason) {
		tag1.aliases.addLast(tag2);
		tag2.aliases.addLast(tag1);
		tag1.aliasReasons.addLast(reason);
		tag2.aliasReasons.addLast(reason);
	}

	public void unlink(Rule34Mod tag) {
		final int i = aliases.indexOf(tag);
		aliases.remove(i);
		aliasReasons.remove(i);
	}
}