package q2p.tagsparser.r34;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import q2p.tagsparser.global.Assist;
import q2p.tagsparser.global.Completition;
import q2p.tagsparser.global.HardAcess;
import q2p.tagsparser.global.Networker;
import q2p.tagsparser.global.StringPool;

public class Rule34 {
	private static final char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789_()/%$#&^<>:\"'=`".toCharArray();
	private static final String[][] replace = new String[][] {
		{"lt","<"},
		{"gt",">"},
		{"amp","&"},
		{"apos","'"},
		{"quot","\""}
	};
	
	private static void processTagList(final LinkedList<String> parts) {
		while(!parts.isEmpty()) {
			final LinkedList<String> tagParts = new LinkedList<String>();
			Assist.devideBy(parts.removeFirst(), "<td>", "</td>", tagParts);
			int amount = Integer.parseInt(tagParts.removeFirst().trim());
			String name = tagParts.removeFirst();
			name = escapeXML(name.substring(name.indexOf('>', name.indexOf("<a"))+1, name.indexOf("</a>")).trim()).replace(" ", "_").replace("\n", "").replace("\r", "");
			// TODO: при обработке названия тэгов могут повторятся
			String type = tagParts.removeFirst().trim();
			type = type.substring(0, type.indexOf(' '));
			hardTags.flush(new Rule34Tag(name, amount, type));
		}
	}
	
	static String escapeXML(final String xml) {
		StringBuilder ret = new StringBuilder();
		
		int index;
		int endIndex;
		
		for(int i = 0; i < xml.length();) {
			index = xml.indexOf('&', i);
			if(index != -1) {
				ret.append(xml.substring(i, index));
				endIndex = xml.indexOf(';', index);
				if(endIndex != -1) {
					if(xml.charAt(index+1) == '#') {
						ret.append((char)Integer.parseInt(xml.substring(index+2, endIndex)));
					} else {
						for(int j = 0; j < replace.length; j++) {
							if(xml.substring(index+1, endIndex).equals(replace[j][0])) {
								ret.append(replace[j][1]);
								break;
							}
						}
					}
					i = endIndex+1;
				} else {
					i++;
				}
			} else {
				ret.append(xml.substring(i));
				break;
			}
		}
		
		return ret.toString();
	}
	
	private static final R34AliasesRequest aliasesRequest = new R34AliasesRequest();
	public static void collectInit(final boolean haveWork) {
		tagsCompletition = Completition.haveWork(haveWork);
		aliasesRequest.collectInit(haveWork);
	}

	public static void collect() {
		collectTags();
		aliasesRequest.collect();
	}

	private static void finalizeTags() {
		synchronized(tagsCompletition) {
			tagsCompletition = Completition.ENDED;
			hardTags = hardTags.kill();
		}
	}

	private static void collectTags() {
		synchronized(tagsCompletition) {
			if(tagsCompletition == Completition.ENDED)
				return;
			if(tagsCompletition == Completition.READY)
				tagsInit();
		}
		while(true) {
			final String pick = tags.next();
			if(pick == null) {
				if(tags.needFinalization())
					finalizeTags();
				return;
			}
			
			System.out.println("r34 request: "+pick);
			
			final String body;
			try {
				body = Networker.receive("http://rule34.xxx/index.php?page=tags&s=list&tags="+URLEncoder.encode(pick, "UTF-8")+"*&sort=asc&order_by=tag", false);
			} catch (final Exception e) {
				Assist.abort(e.getMessage());
				return;
			}

			final LinkedList<String> wr = new LinkedList<String>();
			Assist.devideBy(body.substring(Assist.endIndex(body,">", Assist.endIndex(body,"Type", Assist.endIndex(body,"tableheader", 0)))), "<tr>", "</tr>", wr);
			if(wr.size() == 1 && wr.getFirst().contains("results found, refine your search.")) {
				if(wr.getFirst().contains("No"))
					tags.answer(null);
				else
					tags.answer(pick);
			} else {
				processTagList(wr);
				tags.answer(null);
			}
		}
	}

	private static Completition tagsCompletition = Completition.ENDED;
	private static HardAcess hardTags = null;
	private static void tagsInit() {
		try {
			hardTags = new HardAcess("r34_tags.txt");
		} catch (final IOException e) {
			Assist.abort(e.getMessage());
		}
		tags = new StringPool("", chars);
		tagsCompletition = Completition.IN_PROGRESS;
	}

	private static StringPool tags;
}