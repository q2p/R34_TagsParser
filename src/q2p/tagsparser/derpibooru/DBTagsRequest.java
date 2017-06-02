package q2p.tagsparser.derpibooru;

import java.io.IOException;
import java.util.LinkedList;
import q2p.tagsparser.global.Assist;
import q2p.tagsparser.global.IntegerPoolRequest;
import q2p.tagsparser.global.Networker;

public final class DBTagsRequest extends IntegerPoolRequest {
	DBTagsRequest() {
		super("derpibooru_tags.txt", "derpibooru tags request: ");
	}
	
	protected final void receiveBody(final int pick) throws IOException {
		String body = Networker.receive("https://derpibooru.org/tags?page="+pick, true, DerpiBooru.headers);
		int idx = Assist.endIndexTrain(body, 0, "Popular Tags", "tag-list");
		final LinkedList<String> spans = new LinkedList<String>();
		Assist.split(body.substring(idx, body.indexOf("clearfix", idx)), "tag dropdown", spans);
		spans.removeFirst();
		if(spans.isEmpty()) {
			answer(pick, false);
			return;
		}
		while(!spans.isEmpty()) {
			final String span = spans.removeFirst();
			
			idx = Assist.endIndex(span, "data-tag-category=\"", 0);
			final String category = idx==-1?"":span.substring(idx, span.indexOf("\"", idx));
			
			String name = Assist.devideBy(span, "<a ", "</a>");
			final String shortDescription = Assist.devideBy(name, "title=\"", "\"");
			name = name.substring(name.indexOf(">")+1).trim();
			String tagCount = Assist.devideBy(span, "class=\"tag__count\"", "</span>");
			tagCount = tagCount.substring(tagCount.indexOf(">")+1).trim();
			final int count = Integer.parseInt(tagCount.substring(1, tagCount.length()-1));
			
			flush(new DBTag(name, count, category, shortDescription));
		}
		answer(pick, true);
	}
}