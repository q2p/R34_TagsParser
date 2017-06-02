package q2p.tagsparser.converter;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import q2p.tagsparser.r34.Rule34Type;

public final class Converter {
	public static ArrayList<ProcessedTag> processedTags;
	
	public static final LinkedList<Rule34Tag> r34tags = new LinkedList<Rule34Tag>();
	public static final LinkedList<Rule34Alias> r34aliases = new LinkedList<Rule34Alias>();
	
	public static final LinkedList<Rule34Mod> r34mods = new LinkedList<Rule34Mod>();
	
	public static final LinkedList<E621Tag> e621tags = new LinkedList<E621Tag>();
	public static final LinkedList<E621Alias> e621aliases = new LinkedList<E621Alias>();
	public static final LinkedList<E621Implication> e621implications= new LinkedList<E621Implication>();
	
	public static final LinkedList<E621Mod> e621mods = new LinkedList<E621Mod>();
	
	public static final LinkedList<DBTag> dbTags = new LinkedList<DBTag>();
	public static final LinkedList<DBAlias> dbAliases = new LinkedList<DBAlias>();
	public static final LinkedList<DBImplication> dbImplications = new LinkedList<DBImplication>();
	
	public static final LinkedList<DBMod> dbMods = new LinkedList<DBMod>();
	
	public static void convert() throws IOException {
		read();
		process();
		write();
	}

	private static void process() {
		unifyR34();
		unifyE621();
		unifyDB();
		
		mergeMods();
		
		linkMods();
		
		numerize();
	}

	private static void numerize() {
		for(int i = processedTags.size()-1; i != -1; i--)
			processedTags.get(i).id = i;
	}

	private static void linkMods() {
		while(!r34mods.isEmpty()) {
			if(r34mods.size() % 500 == 0)
				System.out.println(r34mods.size() +" r34 mods left.");
			link(r34mods.removeFirst());
		}
		
		while(!e621mods.isEmpty()) {
			if(e621mods.size() % 500 == 0)
				System.out.println(e621mods.size() +" e621 mods left.");
			link(e621mods.removeFirst());
		}
		
		while(!dbMods.isEmpty()) {
			if(dbMods.size() % 500 == 0)
				System.out.println(dbMods.size() +" db mods left.");
			link(dbMods.removeFirst());
		}
	}

	private static void link(final DBMod tag) {
		ProcessedTag po = null;
		for(final ProcessedTag p : processedTags) {
			if(p.name.equals(tag.name)) {
				po = p;
				break;
			}
		}
		while(!tag.aliases.isEmpty()) {
			final DBMod alias = tag.aliases.removeFirst();
			final String aliasName = alias.name;
			alias.unlinkAlias(tag);
			
			for(final ProcessedTag p : processedTags) {
				if(p.name.equals(aliasName)) {
					ProcessedTag.alias(po, p);
					break;
				}
			}
		}
		while(!tag.implies.isEmpty()) {
			final String implicationName = tag.implies.removeFirst().name;
			
			for(final ProcessedTag p : processedTags) {
				if(p.name.equals(implicationName)) {
					po.implicate(p);
					break;
				}
			}
		}
	}

	private static void link(final E621Mod tag) {
		ProcessedTag po = null;
		for(final ProcessedTag p : processedTags) {
			if(p.name.equals(tag.name)) {
				po = p;
				break;
			}
		}
		while(!tag.aliases.isEmpty()) {
			final E621Mod alias = tag.aliases.removeFirst();
			final String aliasName = alias.name;
			final boolean approved = tag.aliasApproved.removeFirst();
			alias.unlinkAlias(tag);
			
			for(final ProcessedTag p : processedTags) {
				if(p.name.equals(aliasName)) {
					ProcessedTag.alias(po, p, approved);
					break;
				}
			}
		}
		while(!tag.implies.isEmpty()) {
			final String implicationName = tag.implies.removeFirst().name;
			final boolean approved = tag.impliesApproved.removeFirst();
			
			for(final ProcessedTag p : processedTags) {
				if(p.name.equals(implicationName)) {
					po.implicate(p, approved);
					break;
				}
			}
		}
	}

	private static void link(final Rule34Mod tag) {
		ProcessedTag po = null;
		for(final ProcessedTag p : processedTags) {
			if(p.name.equals(tag.name)) {
				po = p;
				break;
			}
		}
		while(!tag.aliases.isEmpty()) {
			final Rule34Mod alias = tag.aliases.removeFirst();
			final String aliasName = alias.name;
			final String reason = tag.aliasReasons.removeFirst();
			alias.unlink(tag);
			
			for(final ProcessedTag p : processedTags) {
				if(p.name.equals(aliasName)) {
					ProcessedTag.alias(po, p, reason);
					break;
				}
			}
		}
	}

	private static void mergeMods() {
		processedTags = new ArrayList<ProcessedTag>(r34tags.size());
		
		int i = r34mods.size();
		for(final Rule34Mod tag : r34mods) {
			if(i % 500 == 0)
				System.out.println(i +" r34 mods left.");
			i--;
			processedTags.add(new ProcessedTag(tag));
		}

		i = e621mods.size();
		for(final E621Mod tag : e621mods) {
			if(i % 500 == 0)
				System.out.println(i +" e621 mods left.");
			i--;
			push(tag);
		}

		i = dbMods.size();
		for(final DBMod tag : dbMods) {
			if(i % 500 == 0)
				System.out.println(i +" db mods left.");
			i--;
			push(tag);
		}
	}

	private static void push(final E621Mod tag) {
		for(final ProcessedTag p : processedTags) {
			if(p.name.equals(tag.name)) {
				p.merge(tag);
				return;
			}
		}
		processedTags.add(new ProcessedTag(tag));
	}

	private static void push(final DBMod tag) {
		for(final ProcessedTag p : processedTags) {
			if(p.name.equals(tag.name)) {
				p.merge(tag);
				return;
			}
		}
		processedTags.add(new ProcessedTag(tag));
	}

	private static void unifyR34() {
		while(!r34tags.isEmpty()) {
			if(r34tags.size() % 500 == 0)
				System.out.println(r34tags.size()+" r34 tags left.");
			final Rule34Tag tag = r34tags.removeFirst();
			pushR34(tag.name, tag.amount, tag.type);
		}
		while(!r34aliases.isEmpty()) {
			if(r34aliases.size() % 500 == 0)
				System.out.println(r34aliases.size()+" r34 aliases left.");
			final Rule34Alias alias = r34aliases.removeFirst();
			aliasR34(alias.alias, alias.aliasCount, alias.to, alias.toCount, alias.reason);
		}
	}
	
	private static void unifyDB() {
		while(!dbTags.isEmpty()) {
			if(dbTags.size()%500 == 0)
				System.out.println(dbTags.size()+" db tags left.");
			final DBTag tag = dbTags.removeFirst();
			pushDB(tag.name, tag.amount, tag.category, tag.description);
		}
		while(!dbAliases.isEmpty()) {
			if(dbAliases.size()%500 == 0)
				System.out.println(dbAliases.size()+" db aliases left.");
			final DBAlias alias = dbAliases.removeFirst();
			aliasDB(alias.tag, alias.alias);
		}
		while(!dbImplications.isEmpty()) {
			if(dbImplications.size()%500 == 0)
				System.out.println(dbImplications.size()+" db implications left.");
			final DBImplication implication = dbImplications.removeFirst();
			implyDB(implication.tag, implication.implies);
		}

		int i = dbMods.size();
		for(final DBMod tag : dbMods) {
			if(i % 500 == 0)
				System.out.println(i +" db names left.");
			i--;
			tag.updateName();
		}
	}

	private static void implyDB(final String tagName, final LinkedList<String> implies) {
		final DBMod tag = pushDB(tagName, 1, "", "");
		tag.implies(implies);
	}

	private static void aliasDB(final String tagName, final String aliasName) {
		DBMod tag1 = pushDB(tagName, 1, "", "");
		DBMod tag2 = pushDB(aliasName, 1, "", "");
				
		DBMod.alias(tag1, tag2);
	}

	static DBMod pushDB(final String name, final int amount, final String category, final String description) {
		for(final DBMod mod : dbMods) {
			if(mod.name.equals(name)) {
				mod.merge(amount);
				return mod;
			}
		}
		DBMod ret = new DBMod(name, amount, category, description);
		dbMods.add(ret);
		return ret;
	}
	
	private static void unifyE621() {
		while(!e621tags.isEmpty()) {
			if(e621tags.size()%500 == 0)
				System.out.println(e621tags.size()+" e621 tags left.");
			final E621Tag tag = e621tags.removeFirst();
			pushE621(tag.name, tag.count, tag.type, tag.id);
		}
		while(!e621aliases.isEmpty()) {
			if(e621aliases.size()%500 == 0)
				System.out.println(e621aliases.size()+" e621 aliases left.");
			final E621Alias alias = e621aliases.removeFirst();
			aliasE621(alias.name, alias.alias_id, !alias.pending);
		}
		while(!e621implications.isEmpty()) {
			if(e621implications.size()%500 == 0)
				System.out.println(e621implications.size()+" e621 implictaions left.");
			final E621Implication implication = e621implications.removeFirst();
			implyE621(implication.consequent_id, implication.predicate_id, !implication.pending);
		}
	}

	private static void implyE621(final int tagId, final int impliesId, final boolean approved) {
		final E621Mod tag = idE621(tagId);
		final E621Mod implies = idE621(impliesId);
		if(tag == null || implies == null)
			return;
		tag.imply(implies, approved);
	}

	private static void aliasE621(final String tag1name, final int tag2id, final boolean approved) {
		final E621Mod tag1 = nameE621(tag1name);
		final E621Mod tag2 = idE621(tag2id);
		if(tag1 == null || tag2 == null)
			return;
		E621Mod.alias(tag1, tag2, approved);
	}

	private static E621Mod pushE621(final String name, final int amount, final int type, final int id) {
		for(final E621Mod mod : e621mods) {
			if(mod.name.equals(name)) {
				mod.merge(amount);
				return mod;
			}
		}
		E621Mod ret = new E621Mod(name, amount, type, id);
		e621mods.add(ret);
		return ret;
	}

	private static E621Mod nameE621(final String name) {
		for(final E621Mod mod : e621mods)
			if(mod.name.equals(name))
				return mod;
		
		return null;
	}

	private static E621Mod idE621(final int id) {
		for(final E621Mod mod : e621mods)
			if(mod.id == id)
				return mod;
		
		return null;
	}

	private static void aliasR34(final String alias, final int aliasAmount, final String to, final int toAmount, final String reason) {
		Rule34Mod mod1 = pushR34(alias, aliasAmount, Rule34Type.general);
		Rule34Mod mod2 = pushR34(to, toAmount, Rule34Type.general);
				
		Rule34Mod.alias(mod1, mod2, reason);
	}

	private static Rule34Mod pushR34(final String name, final int amount, final Rule34Type type) {
		for(final Rule34Mod mod : r34mods) {
			if(mod.name.equals(name)) {
				mod.merge(amount, type);
				return mod;
			}
		}
		Rule34Mod ret = new Rule34Mod(name, amount, type);
		r34mods.add(ret);
		return ret;
	}

	private static final void read() throws IOException {
		RawReader.open("r34_tags.txt");
		while(RawReader.hasNext()) {
			r34tags.addLast(new Rule34Tag());
		}
		RawReader.open("r34_aliases.txt");
		while(RawReader.hasNext()) {
			r34aliases.addLast(new Rule34Alias());
		}
		
		RawReader.open("e621_tags.txt");
		while(RawReader.hasNext()) {
			e621tags.addLast(new E621Tag());
		}
		RawReader.open("e621_aliases.txt");
		while(RawReader.hasNext()) {
			e621aliases.addLast(new E621Alias());
		}
		RawReader.open("e621_implications.txt");
		while(RawReader.hasNext()) {
			e621implications.addLast(new E621Implication());
		}
		
		RawReader.open("derpibooru_tags.txt");
		while(RawReader.hasNext()) {
			dbTags.addLast(new DBTag());
		}
		RawReader.open("derpibooru_aliases.txt");
		while(RawReader.hasNext()) {
			dbAliases.addLast(new DBAlias());
		}
		RawReader.open("derpibooru_implications.txt");
		while(RawReader.hasNext()) {
			dbImplications.addLast(new DBImplication());
		}
	}
	
	private static final void write() throws IOException {
		final DataOutputStream dos = new DataOutputStream(new FileOutputStream("tags.dat"));
		
		final int to = processedTags.size();
		for(int i = 0; i != to; i++) {
			if((to-i) % 500 == 0)
				System.out.println((to-i) +" tags left.");
			processedTags.get(i).write(dos);
		}

		dos.flush();
		
		dos.close();
		
		processedTags = null;
	}
}