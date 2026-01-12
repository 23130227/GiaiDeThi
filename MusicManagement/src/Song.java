import java.io.IOException;
import java.io.RandomAccessFile;

public class Song {
	private byte genre;
	private String name;
	private float size;

	public Song() {
	}

	public Song(byte genre, String name, float size) {
		this.genre = genre;
		this.name = name;
		this.size = size;
	}

	public byte getGenre() {
		return genre;
	}

	public void setGenre(byte genre) {
		this.genre = genre;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Song [genre=" + genre + ", name=" + name + ", size=" + size + "]";
	}

	public void write(RandomAccessFile raf, int nameSize) throws IOException {
		// TODO Auto-generated method stub
		raf.writeBoolean(false);
		raf.writeByte(genre);
		for (int i = 0; i < nameSize; i++) {
			if (i < name.length()) {
				raf.writeChar(name.charAt(i));
			} else {
				raf.writeChar(0);
			}
		}
		raf.writeFloat(size);
	}

	public void read(RandomAccessFile raf, int NAME_SIZE) throws IOException {
		genre = raf.readByte();
		char[] nameArr = new char[NAME_SIZE];
		for (int i = 0; i < nameArr.length; i++) {
			nameArr[i] = raf.readChar();
		}
		name = new String(nameArr).trim();
		size = raf.readFloat();
	}
}