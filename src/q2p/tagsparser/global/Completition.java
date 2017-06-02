package q2p.tagsparser.global;

public enum Completition {
	READY, IN_PROGRESS, ENDED;
	
	public static final Completition haveWork(final boolean haveWork) {
		return haveWork?READY:ENDED;
	}
}