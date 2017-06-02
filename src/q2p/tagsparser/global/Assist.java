package q2p.tagsparser.global;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public final class Assist {
	public static final void abort(final String reason) {
		System.out.println("Error:\n"+reason);
		System.exit(1);
	}
	public static final int endIndex(final String string, final String pattern, final int fromIndex) {
		int i = string.indexOf(pattern, fromIndex);
		if(i == -1)
			return -1;
		return i+pattern.length();
	}
	public static final int endIndexTrain(final String string, int fromIndex, final String ... patterns) {
		for(final String pattern : patterns) {
			final int i = string.indexOf(pattern, fromIndex);
			if(i == -1)
				return -1;
			fromIndex = i + pattern.length();
		}
		return fromIndex;
	}

	public static final List<String> devideBy(final String string, final String beg, final String end, final List<String> container) {
		int b;
		int e = 0;
		while(true) {
			b = string.indexOf(beg, e);
			if(b == -1)
				break;
			b += beg.length();
			e = string.indexOf(end, b);
			if(e == -1)
				break;
			container.add(string.substring(b, e));
			e += end.length();
		}
		return container;
	}
	public static final String devideBy(final String string, final String beg, final String end) {
		int idx = string.indexOf(beg)+beg.length();
		return string.substring(idx, string.indexOf(end, idx));
	}
	
	public static final String innerHTML(final String string) {
		final int idx = string.indexOf('>')+1;
		return string.substring(idx, string.indexOf('<', idx)).trim();
	}
	public static final List<String> split(final String string, final String pattern, final List<String> container) {
		int pidx = 0;
		int idx;
		while(true) {
			idx = string.indexOf(pattern, pidx);
			if(idx == -1) {
				container.add(string.substring(pidx));
				break;
			}
			container.add(string.substring(pidx, idx));
			pidx = idx+pattern.length();
		}
		
		return container;
	}
	
	public static final void writePartialyAndFlush(final OutputStream outputStream, final int bufferSize, final byte[] data) throws IOException {
		int offset = 0;
		int left = data.length;
		while(left != 0) {
			final int length = Math.min(bufferSize, left);
			outputStream.write(data, offset, length);
			outputStream.flush();
			offset += length;
			left -= length;
		}
	}
	public static final void writePartialyAndFlush(final InputStream inputStream, final OutputStream outputStream, final int bufferSize) throws IOException {
		writePartialyAndFlush(inputStream, outputStream, new byte[bufferSize]);
	}
	public static final void writePartialyAndFlush(final InputStream inputStream, final OutputStream outputStream, final byte[] buffer) throws IOException {
		while(inputStream.available() > 0) {
			final int length = inputStream.read(buffer);
			outputStream.write(buffer, 0, length);
			outputStream.flush();
		}
	}
	public static final long progressTime(final long lastTime, final long delay, final String out) {
		final long time = System.currentTimeMillis();
		if(time - lastTime >= delay) {
			System.out.println(out);
			return time;
		}
		return lastTime;
	}
}