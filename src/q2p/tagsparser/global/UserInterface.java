package q2p.tagsparser.global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import q2p.tagsparser.converter.Converter;
import q2p.tagsparser.derpibooru.DerpiBooru;
import q2p.tagsparser.e621.E621;
import q2p.tagsparser.r34.Rule34;

final class UserInterface {
	private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
	public static final String readLine() {
		try {
			return READER.readLine();
		} catch(final IOException e) {
			Assist.abort("Консоль сказала пока.");
			return null;
		}
	}
	public static final int readNumber(final int maxNumber, final String onWrongInput) {
		while(true) {
			try {
				int option = Integer.parseInt(readLine());
				if(option > 0 && option <= maxNumber)
					return option;
			} catch(NumberFormatException e) {}
			System.out.println(onWrongInput);
		}
	}

	static final void entry() {
		System.out.println("Tags Parser.");
		while(true) {
			System.out.println("Type in operation:\n1) Collect Tags.\n2) Convert Tags.");
			final int operation = readNumber(2, "Invalid operation.");
			if(operation == 1) {
				inputForCollect();
			} else {
				try {
					Converter.convert();
				} catch(final Exception e) {
					e.printStackTrace();
					Assist.abort(e.getMessage());
				}
			}
		}
	}
	
	public static final boolean yesNo(final String message) {
		while(true) {
			System.out.println(message);
			System.out.println("y/n");
			final String str = readLine();
			if(str.equalsIgnoreCase("y"))
				return true;
			if(str.equalsIgnoreCase("n"))
				return false;
		}
	}
	
	private static void inputForCollect() {
		Rule34.collectInit(yesNo("Collect from rule34.xxx?"));
		E621.collectInit(yesNo("Collect from e621.net?"));
		DerpiBooru.collectInit(yesNo("Collect from derpibooru.org?"));
		Threader.collect();
	}

	static final void abort(final String reason) {
		System.out.println(reason);
		System.exit(1);
	}
}