package q2p.tagsparser.e621;

import java.util.LinkedList;

public class E621 {
	static final LinkedList<String> headers = new LinkedList<String>();
	
	static {
		headers.addLast("User-Agent");
		headers.addLast("Mozilla/5.0 (Windows NT 6.1; rv:45.0) Gecko/20100101 Firefox/45.0");
		headers.addLast("Cookie");
		headers.addLast("__cfduid=d0d6e02de4bf58b471c8c08d5601009611481082878; cf_clearance=1c599e568c81b25b7d8c2e8ba1fba1836c2a6b05-1481083049-2592000; blacklist_avatars=true; blacklist_users=false; e621=BAh7BjoPc2Vzc2lvbl9pZCIlMjQyNGYyYmEyMTA2ODYzNGQ4ZjExMWZiMWM5ZjY3ZTk%3D--680a8cc5af0a331423829615ffb148bc6f8b156e");		
		headers.addLast("Connection");
		headers.addLast("keep-alive");
	}

	private static E621TagsRequest tagsRequest = new E621TagsRequest();
	private static E621AliasesRequest aliasesRequest = new E621AliasesRequest();
	private static E621ImplicationsRequest implicationsRequest = new E621ImplicationsRequest();
		
	public static void collectInit(final boolean haveWork) {
		tagsRequest.collectInit(haveWork);
		aliasesRequest.collectInit(haveWork);
		implicationsRequest.collectInit(haveWork);
	}

	public static final void collect() {
		tagsRequest.collect();
		aliasesRequest.collect();
		implicationsRequest.collect();
	}
}