package q2p.tagsparser.r34;

import java.nio.charset.StandardCharsets;
import q2p.tagsparser.global.HardAcess.Flushable;

public final class Rule34Tag implements Flushable {
	public final String name;
	public final int amount;
	public final Type type;

	public Rule34Tag(final String name, final int amount, final String type) {
		this.name = name;
		this.amount = amount;
		this.type = Type.getByNameMatch(type);
		if(this.type == null)
			throw new RuntimeException("Не правильный тип: "+type);
	}
	public Rule34Tag(final String name, final int amount, final Type type) {
		this.name = name;
		this.amount = amount;
		this.type = type;
	}
	
	public static enum Type {
		general("general"), artist("artist"), copyright("copyright"), character("character");
		
		public final String text;

		private Type(final String text) {
			this.text = text;
		}
		
		public static final Type getByName(final String name) {
			for(final Type t : values())
				if(t.text.equals(name))
					return t;
			
			return null;
		}
		public static final Type getByNameMatch(final String name) {
			for(final Type t : values())
				if(name.contains(t.text))
					return t;
			
			return null;
		}
	}

	public final byte[] flush() {
		return (amount+"\n"+name+"\n"+type.text+"\n").getBytes(StandardCharsets.UTF_8);
	}
}