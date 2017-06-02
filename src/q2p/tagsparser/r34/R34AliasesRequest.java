package q2p.tagsparser.r34;

import java.io.IOException;
import java.util.LinkedList;
import q2p.tagsparser.global.Assist;
import q2p.tagsparser.global.IntegerPoolRequest;
import q2p.tagsparser.global.Networker;

public final class R34AliasesRequest extends IntegerPoolRequest {
	R34AliasesRequest() {
		super("r34_aliases.txt", "r34 aliases request: ");
	}
	
	protected final void receiveBody(final int pick) throws IOException {
		String body = Networker.receive("http://rule34.xxx/index.php?page=alias&s=list&pid="+pick, true);
		final int idx = Assist.endIndexTrain(body, 0, "id=\"aliases\"", "<table", "<thead", "Reason", "</tr>");
		final LinkedList<String> rows = new LinkedList<String>();
		Assist.devideBy(body.substring(idx, body.indexOf("</table>", idx)), "<tr", "</tr>", rows);
		if(rows.isEmpty()) {
			answer(pick, false);
			return;
		}
		while(!rows.isEmpty()) {
			final LinkedList<String> colls = new LinkedList<String>();
			Assist.devideBy(rows.removeFirst(), "<td>", "</td>", colls);
			colls.removeFirst();

			final String[] first = separateAliasInfo(colls.removeFirst());
			final String[] secound = separateAliasInfo(colls.removeFirst());
			final String reason = Rule34.escapeXML(colls.removeFirst());

			flush(new Rule34Alias(first[0], Integer.parseInt(first[1]), secound[0], Integer.parseInt(secound[1]), reason));
		}
		answer(pick, true);
	}
	
	private static final String[] separateAliasInfo(String original) {
		original = original.substring(original.indexOf('>')+1);
		
		String amount = original.substring(original.indexOf('>')+1).trim();
		amount = amount.substring(1, amount.length()-1);
		
		original = original.substring(0, original.indexOf('<'));
		
		return new String[] {Rule34.escapeXML(original), Rule34.escapeXML(amount)};
	}
}