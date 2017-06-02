package q2p.tagsparser.derpibooru;

import java.io.IOException;
import java.util.LinkedList;
import q2p.tagsparser.global.Assist;
import q2p.tagsparser.global.IntegerPoolRequest;
import q2p.tagsparser.global.Networker;

public final class DBImplicationsRequest extends IntegerPoolRequest {
	DBImplicationsRequest() {
		super("derpibooru_implications.txt", "derpibooru implications request: ");
	}
	
	protected final void receiveBody(final int pick) throws IOException {
		String body = Networker.receive("https://derpibooru.org/tags/implied?page="+pick, true, DerpiBooru.headers);
		body = body.substring(Assist.endIndexTrain(body, 0, "block__content", "<tbody>"));
		final LinkedList<String> rows = new LinkedList<String>();
		Assist.devideBy(body, "<tr>", "</tr>", rows);
		if(rows.isEmpty()) {
			answer(pick, false);
			return;
		}
		while(!rows.isEmpty()) {
			final LinkedList<String> collumns = new LinkedList<String>();
			Assist.devideBy(rows.removeFirst(), "<td>", "</td>", collumns);
			String tag = Assist.innerHTML(collumns.removeFirst());
			Assist.split(collumns.removeFirst(), ",", collumns);
			for(int i = collumns.size(); i != 0; i--)
				collumns.addLast(Assist.innerHTML(collumns.removeFirst()));
			
			flush(new DBImplication(tag, collumns));
		}
		answer(pick, true);
	}
}