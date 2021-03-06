package q2p.tagsparser.e621;

import java.io.IOException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import q2p.tagsparser.global.IntegerPoolRequest;
import q2p.tagsparser.global.Networker;

public final class E621AliasesRequest extends IntegerPoolRequest {
	E621AliasesRequest() {
		super("e621_aliases.txt", "e621 aliases request: ");
	}
	
	protected final void receiveBody(final int pick) throws IOException {
		final JSONArray arr = JSONArray.parseArray(Networker.receive("https://e621.net/tag_alias/index.json?approved=all&order=tag&page="+pick, true, E621.headers));
		if(arr.size() == 0) {
			answer(pick, false);
			return;
		}
		for(int i = arr.size()-1; i != -1; i--) {
			final JSONObject o = arr.getJSONObject(i);
			
			flush(new E621Alias(o.getIntValue("id"), o.getString("name"), o.getIntValue("alias_id"), o.getBooleanValue("pending")));
		}
	}
}
