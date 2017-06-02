package q2p.tagsparser.converter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import q2p.tagsparser.r34.Rule34Type;

public final class ProcessedTag {
	public final void write(final DataOutputStream dos) throws IOException {
		Storage.writeString(dos, name);
		dos.writeInt(amount);
		dos.writeInt(type.id);
		
		Storage.writeString(dos, description);
		
		dos.writeInt(aliases.size());
		while(!aliases.isEmpty()) {
			dos.writeInt(aliases.removeFirst().id);
			dos.writeBoolean(aliasesConfirmed.removeFirst());
			Storage.writeString(dos, aliasesReasons.removeFirst());
		}

		dos.writeInt(implies.size());
		while(!implies.isEmpty()) {
			dos.writeInt(implies.removeFirst().id);
			dos.writeBoolean(impliesConfirmed.removeFirst());
		}
		
		dos.flush();
	}
	
	public int id = -1;
	public final String name;
	public final LinkedList<ProcessedTag> aliases = new LinkedList<ProcessedTag>();
	public final LinkedList<String> aliasesReasons = new LinkedList<String>();
	public final LinkedList<Boolean> aliasesConfirmed = new LinkedList<Boolean>();
	public final LinkedList<ProcessedTag> implies = new LinkedList<ProcessedTag>();
	public final LinkedList<Boolean> impliesConfirmed = new LinkedList<Boolean>();
	public int amount;
	public Type type;
	public String description;
	
	public ProcessedTag(final Rule34Mod tag) {
		name = tag.name;
		amount = tag.amount;
		type = Type.get(tag.type);
		description = "";
	}
	
	public ProcessedTag(final String name, final int amount) {
		this.name = name;
		this.amount = amount;
		type = Type.general;
		description = "";
	}

	public ProcessedTag(final E621Mod tag) {
		name = tag.name;
		amount = tag.amount;
		type = Type.get(tag.type);
		description = "";
	}

	public void merge(final E621Mod tag) {
		amount += tag.amount;
		if(tag.type == 5)
			type = Type.species;
	}

	public ProcessedTag(final DBMod tag) {
		name = tag.name;
		amount = tag.amount;
		type = Type.get(tag.category);
		description = tag.description;
	}

	public void merge(final DBMod tag) {
		amount += tag.amount;
		description = tag.description;
	}

	public enum Type {
		general(0), franchise(1), character(2), author(3), species(4);

		public final int id;
	
		private Type(final int id) {
			this.id = id;
		}
		
		public static Type get(final String dbCategory) {
			if(dbCategory.equals("spoiler") || dbCategory.equals("episode"))
				return franchise;
			if(dbCategory.equals("character") || dbCategory.equals("oc"))
				return character;
			if(dbCategory.equals("origin"))
				return author;
			
			return general;
		}

		public static final Type get(final int e621type) {
			if(e621type == 0)
				return general;
			if(e621type == 1)
				return author;
			if(e621type == 3)
				return franchise;
			if(e621type == 4)
				return character;
			if(e621type == 5)
				return Type.species;
			
			return null;
		}

		public static final Type get(final Rule34Type type) {
			if(type == Rule34Type.general)
				return general;
			if(type == Rule34Type.copyright)
				return franchise;
			if(type == Rule34Type.character)
				return character;
			if(type == Rule34Type.artist)
				return author;
			return null;
		}
	}
	
	public final void alias(final ProcessedTag tag, final String reason, final boolean approved) {
		final int idx = aliases.indexOf(tag);
		if(idx == -1) {
			aliases.addLast(tag);
			aliasesReasons.addLast(reason);
			aliasesConfirmed.addLast(approved);
		} else {
			if(reason.length() != 0)
				aliasesReasons.set(idx, aliasesReasons.get(idx)+"\n"+reason);
			if(approved)
				aliasesConfirmed.set(idx, true);
		}
	}

	public static final void alias(final ProcessedTag tag1, final ProcessedTag tag2, final String reason) {
		tag1.alias(tag2, reason, false);
		tag2.alias(tag1, reason, false);
	}

	public static final void alias(final ProcessedTag tag1, final ProcessedTag tag2, final boolean approved) {
		tag1.alias(tag2, "", approved);
		tag2.alias(tag1, "", approved);
	}

	public static final void alias(final ProcessedTag tag1, final ProcessedTag tag2) {
		tag1.alias(tag2, "", false);
		tag2.alias(tag1, "", false);
	}

	public void implicate(final ProcessedTag requires, final boolean approved) {
		final int idx = implies.indexOf(requires);
		if(idx == -1) {
			implies.addLast(requires);
			impliesConfirmed.addLast(approved);
		} else {
			if(approved)
				impliesConfirmed.set(idx, true);
		}
	}

	public void implicate(final ProcessedTag requires) {
		if(!implies.contains(requires)) {
			implies.addLast(requires);
			impliesConfirmed.addLast(false);
		}
	}

}