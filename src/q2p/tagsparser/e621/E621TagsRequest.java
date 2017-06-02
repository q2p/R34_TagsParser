package q2p.tagsparser.e621;

import java.io.IOException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import q2p.tagsparser.global.IntegerPoolRequest;
import q2p.tagsparser.global.Networker;

public final class E621TagsRequest extends IntegerPoolRequest {
	E621TagsRequest() {
		super("e621_tags.txt", "e621 tags request: ");
	}
	
	protected final void receiveBody(final int pick) throws IOException {
		final JSONArray arr = JSONArray.parseArray(Networker.receive("https://e621.net/tag/index.json?limit=400&order=name&page="+pick, true, E621.headers));
		if(arr.size() == 0) {
			answer(pick, false);
			return;
		}
		for(int i = arr.size()-1; i != -1; i--) {
			final JSONObject o = arr.getJSONObject(i);
			
			flush(new E621Tag(o.getIntValue("id"), o.getString("name"), o.getIntValue("count"), o.getIntValue("type")));
		}
	}
}
