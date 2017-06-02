package q2p.tagsparser.derpibooru;

import java.util.LinkedList;

public final class DerpiBooru {
	protected static final LinkedList<String> headers = new LinkedList<String>();
	
	static {
		headers.addLast("Cookie");
		headers.addLast("__cfduid=d06a2da1faf763ed3f9bfb62e0f35fa191481192112; cf_clearance=1c599e568c81b25b7d8c2e8ba1fba1836c2a6b05-1481083049-2592000");		
		headers.addLast("Connection");
		headers.addLast("keep-alive");
	}
	
	private static DBTagsRequest tagsRequest = new DBTagsRequest();
	private static DBAliasesRequest aliasesRequest = new DBAliasesRequest();
	private static DBImplicationsRequest implicationsRequest = new DBImplicationsRequest();
		
	public static final void collectInit(final boolean haveWork) {
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