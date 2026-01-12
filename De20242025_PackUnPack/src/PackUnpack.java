import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class PackUnpack {
	private static final int FILE_NAME_SIZE = 100;

	public static void pack(String folder, String packedFile, String extension) throws IOException {
		File file = new File(folder);
		List<File> listFile = getListFileByExt(file, extension);
		RandomAccessFile raf = new RandomAccessFile(packedFile, "rw");
		int totalFileNumber = listFile.size();
		raf.writeInt(totalFileNumber);
		raf.writeInt(FILE_NAME_SIZE);
		for (File f : listFile) {
			raf.writeLong(f.length());
			String fileName = f.getName();
			for (int i = 0; i < FILE_NAME_SIZE / 2; i++) {
				if (i < fileName.length()) {
					raf.writeChar(fileName.charAt(i));
				} else {
					raf.writeChar(0);
				}
			}
		}
		for (File f : listFile) {
			byte[] buffer = new byte[8192];
			int count;
			FileInputStream fis = new FileInputStream(f);
			while ((count = fis.read(buffer)) != -1) {
				raf.write(buffer, 0, count);
			}
			fis.close();
		}
		raf.close();
	}

	public static List<File> getListFileByExt(File file, String ext) {
		List<File> result = new ArrayList<File>();
		for (File f : file.listFiles()) {
			if (f.isFile() && f.getName().endsWith(ext)) {
				result.add(f);
			}
		}
		return result;
	}

	public static boolean unPack(String packedFile, String extractFile, String destFile) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(packedFile, "r");
		int totalFileNumber = raf.readInt();
		int fileNameSize = raf.readInt();
		long[] fileSizeArr = new long[totalFileNumber];
		for (int i = 0; i < totalFileNumber; i++) {
			long fileSize = raf.readLong();
			fileSizeArr[i] = fileSize;
			char[] fileNameArr = new char[fileNameSize / 2];
			for (int j = 0; j < fileNameArr.length; j++) {
				fileNameArr[j] = raf.readChar();
			}
			String fileName = new String(fileNameArr).trim();
			if (extractFile.equalsIgnoreCase(fileName)) {
				int headSize = 4 + 4 + totalFileNumber * (8 + fileNameSize);
				long dataPos = headSize;
				for (int z = 0; z < fileSizeArr.length; z++) {
					if (z == i) {
						break;
					}
					dataPos += fileSizeArr[z];
				}
				raf.seek(dataPos);
				FileOutputStream fos = new FileOutputStream(destFile);
				copy(raf, fos, fileSize);
				raf.close();
				fos.close();
				return true;
			}
		}
		return false;
	}

	public static void copy(RandomAccessFile raf, OutputStream fos, long pSize) throws IOException {
		byte[] buff = new byte[8192];
		long remain = pSize;
		while (remain > 0) {
			int byteMustRead = (int) Math.min(buff.length, remain);
			int byteRead = raf.read(buff, 0, byteMustRead);
			fos.write(buff, 0, byteRead);
			if (byteRead == -1) {
				break;
			}
			remain -= byteRead;
		}
	}

	public static void main(String[] args) throws IOException {
//		pack("D:\\LTM", "D:\\abc.zip", ".pdf");
		unPack("D:\\abc.zip", "CH02 - Truy van.pdf", "D:\\cursor.pdf");
	}
}
