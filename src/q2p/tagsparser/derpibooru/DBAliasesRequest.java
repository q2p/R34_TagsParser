package q2p.tagsparser.derpibooru;

import java.io.IOException;
import java.util.LinkedList;
import q2p.tagsparser.global.Assist;
import q2p.tagsparser.global.IntegerPoolRequest;
import q2p.tagsparser.global.Networker;

public final class DBAliasesRequest extends IntegerPoolRequest {
	DBAliasesRequest() {
		super("derpibooru_aliases.txt", "derpibooru aliases request: ");
	}
	
	protected final void receiveBody(final int pick) throws IOException {
		String body = Networker.receive("https://derpibooru.org/tags/aliases?page="+pick, true, DerpiBooru.headers);
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
			final String alias = collumns.removeFirst();
			final String tag;
			try {
				tag = Assist.innerHTML(collumns.removeFirst());
			} catch (final IndexOutOfBoundsException e) {
				continue;
			}
			
			flush(new DBAlias(alias, tag));
		}
		answer(pick, true);
	}
}