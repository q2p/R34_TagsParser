package q2p.tagsparser.global;

import java.io.FileOutputStream;
import java.io.IOException;

public class HardAcess {
	private final Object LOCK = new Object();
	private FileOutputStream fos = null;
	
	public HardAcess(final String path) throws IOException {
		fos = new FileOutputStream(path);
	}
	
	public final void flush(final Flushable flushabe) {
		synchronized(LOCK) {
			try {
				fos.write(flushabe.flush());
				fos.flush();
			} catch(final IOException e) {
				Assist.abort(e.getMessage());
			}
		}
	}
	
	public final HardAcess kill() {
		try {
			fos.close();
		} catch(final IOException e) {
		} finally {
			fos = null;
		}
		return null;
	}
	
	public final boolean wasKilled() {
		synchronized(LOCK) {
			return fos == null;
		}
	}
	
	public static interface Flushable {
		public byte[] flush();
	}
}