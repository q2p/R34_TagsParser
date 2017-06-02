package q2p.tagsparser.converter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Storage {
	public static final String readString(final DataInputStream dis) throws Exception {
		final byte[] buff = new byte[dis.readInt()];
		dis.read(buff);
		return new String(buff, StandardCharsets.UTF_8);
	}
	public static final List<String> readStrings(final DataInputStream dis, final List<String> container) throws Exception {
		for(int i = dis.readInt(); i != 0; i--)
			container.add(readString(dis));
		
		return container;
	}
	public static final void writeString(final DataOutputStream dos, final String string) throws IOException {
		final byte[] buff = string.getBytes(StandardCharsets.UTF_8);
		dos.writeInt(buff.length);
		dos.write(buff);
	}
	public static final void writeStrings(final DataOutputStream dos, final List<String> strings) throws IOException {
		dos.writeInt(strings.size());
		for(final String s : strings)
			writeString(dos, s);
	}
	public static final String getFileContents(final String fileName) throws IOException {
		final FileInputStream fis = new FileInputStream(fileName);
		byte[] buff = new byte[fis.available()];
		fis.read(buff);
		fis.close();
		
		return new String(buff, StandardCharsets.UTF_8);
	}
}