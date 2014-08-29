package com.neekle.kunlunandroid.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

import android.util.Log;

public class FileUtil {

	private static final String TAG = "FileUtil";

	/**
	 * </br><b>title : </b> 把源文件复制到目标文件中。 </br><b>description :</b>把源文件复制到目标文件中。
	 * 
	 * @param source
	 *            源文件
	 * @param dest
	 *            目标文件
	 * @throws IOException
	 *             如果源文件不存在或者目标文件不可写入，抛出IO异常。
	 */
	public static void copy(File source, File dest) throws IOException {
		FileInputStream fileIS = null;
		FileOutputStream fileOS = null;
		try {
			fileIS = new FileInputStream(source);
			fileOS = new FileOutputStream(dest);
		} catch (FileNotFoundException ex) {
			Log.e(TAG, "Source File not exist !");
		}
		FileChannel fic = fileIS.getChannel();
		MappedByteBuffer mbuf = fic.map(FileChannel.MapMode.READ_ONLY, 0,
				source.length());
		fic.close();
		fileIS.close();
		if (!dest.exists()) {
			String destPath = dest.getPath();
			String destDir = destPath.substring(0,
					destPath.lastIndexOf(File.separatorChar));
			File dir = new File(destDir);
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					Log.i(TAG, "Directory created");
				} else {
					Log.e(TAG, "Directory not created");
				}
			}
		}
		FileChannel foc = fileOS.getChannel();
		foc.write(mbuf);
		foc.close();
		fileOS.close();
		mbuf.clear();
	}

	/**
	 * </br><b>title : </b> 复制文件 </br><b>description :</b>复制文件 </br><b>time
	 * :</b> 2012-7-8 下午5:04:23
	 * 
	 * @param source
	 *            源文件路径
	 * @param dest
	 *            目标文件路径
	 * @throws IOException
	 *             如果源文件不存在或者目标文件不可写入，抛出IO异常。
	 */
	public static void copy(String source, String dest) throws IOException {
		copy(new File(source), new File(dest));
	}

	/**
	 * <b>description :</b> 保存一个输入流到指定路径中，保存完成后输入流将被关闭。 </br><b>time :</b>
	 * 2012-9-13 下午4:16:52
	 * 
	 * @param is
	 * @param path
	 * @throws IOException
	 */
	public static void save(InputStream is, String path) throws IOException {
		save(is, path, true);
	}

	/**
	 * <b>description :</b> 保存一个输入流到指定路径中 </br><b>time :</b> 2012-9-13 下午4:20:09
	 * 
	 * @param is
	 *            输入流
	 * @param path
	 *            路径
	 * @param closeInputStream
	 *            是否关闭输入流
	 * @throws IOException
	 */
	public static void save(InputStream is, String path,
			boolean closeInputStream) throws IOException {
		FileOutputStream os = new FileOutputStream(createFile(path));
		byte[] cache = new byte[10 * 1024];
		for (int len = 0; (len = is.read(cache)) != -1;) {
			os.write(cache, 0, len);
		}
		os.close();
		if (closeInputStream)
			is.close();
	}

	public static File createFile(String path) throws IOException {
		File distFile = new File(path);
		if (!distFile.exists()) {
			File dir = distFile.getParentFile();
			if (dir != null && !dir.exists()) {
				dir.mkdirs();
			}
			distFile.createNewFile();
		}
		return distFile;
	}

	/**
	 * <b>description :</b> 保存一个字节数组流到指定路径中 </br><b>time :</b> 2012-9-14
	 * 下午12:24:29
	 * 
	 * @param data
	 * @param path
	 */
	public static void save(byte[] data, String path) throws IOException {
		FileOutputStream os = new FileOutputStream(createFile(path));
		os.write(data, 0, data.length);
		os.close();
	}

	/**
	 * 移动文件
	 * 
	 * @param source
	 * @param dest
	 * @throws IOException
	 * 
	 */
	public static void moveFile(String source, String dest) throws IOException {
		copy(source, dest);
		File src = new File(source);
		if (src.exists() && src.canRead()) {
			if (src.delete()) {
				Log.i(TAG, "Source file was deleted");
			} else {
				src.deleteOnExit();
			}
		} else {
			Log.w(TAG, "Source file could not be accessed for removal");
		}
	}

	/**
	 * 删除文件夹及其下内容
	 * 
	 * @param dirPath
	 * @return
	 * @throws IOException
	 */
	public static boolean deleteDirectory(String dirPath) throws IOException {
		if (dirPath == null)
			return false;
		return deleteDirectory(new File(dirPath));
	}

	/**
	 * 删除文件夹及其下内容
	 * 
	 * @param dirFile
	 * @return
	 */
	public static boolean deleteDirectory(File dirFile) {
		boolean result = false;
		if (dirFile != null && dirFile.isDirectory()) {
			if (dirFile != null) {
				for (File file : dirFile.listFiles()) {
					if (!file.delete()) {
						file.deleteOnExit();
					}
				}
				if (dirFile.delete()) {
					result = true;
				} else {
					dirFile.deleteOnExit();
				}
			}
		}
		return result;
	}

	/**
	 * 删除文件夹及其下内容。如果文件夹被系统锁定或者文件夹不能被清空，将返回false。
	 * 
	 * @param directory
	 * @param useOSNativeDelete
	 *            标识是否使用系统命令进行删除操作。
	 * @return 文件夹删除成功则返回true，文件夹不存在则返回false。
	 * @throws IOException
	 *             如果文件夹不能被删除，则抛出异常。
	 * 
	 */
	public static boolean deleteDirectoryWithOSNative(String directory)
			throws IOException {
		boolean result = false;
		Process process = null;
		Thread std = null;
		try {
			Runtime runTime = Runtime.getRuntime();
			if (File.separatorChar == '\\') {
				process = runTime.exec("CMD /D /C \"RMDIR /Q /S "
						+ directory.replace('/', '\\') + "\"");
			} else {
				process = runTime.exec("rm -rf "
						+ directory.replace('\\', File.separatorChar));
			}
			std = stdOut(process);
			while (std.isAlive()) {
				try {
					Thread.sleep(250);
				} catch (Exception e) {
				}
			}
			result = true;
		} catch (Exception e) {
			Log.e(TAG, "Error running delete script");
		} finally {
			if (null != process) {
				process.destroy();
				process = null;
			}
			std = null;
		}
		return result;
	}

	/**
	 * </br><b>title : </b> 使用本地系统命令重命名一个文件。 </br><b>description
	 * :</b>使用本地系统命令重命名一个文件。 </br><b>time :</b> 2012-7-8 下午5:12:23
	 * 
	 * @param from
	 *            原文件名
	 * @param to
	 *            新文件名
	 */
	public static void rename(String from, String to) {
		Process process = null;
		Thread std = null;
		try {
			Runtime runTime = Runtime.getRuntime();
			if (File.separatorChar == '\\') {
				process = runTime.exec("CMD /D /C \"REN " + from + ' ' + to
						+ "\"");
			} else {
				process = runTime.exec("mv -f " + from + ' ' + to);
			}
			std = stdOut(process);
			while (std.isAlive()) {
				try {
					Thread.sleep(250);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error running delete script");
		} finally {
			if (null != process) {
				process.destroy();
				process = null;
				std = null;
			}
		}
	}

	/**
	 * <b>description :</b> 创建一个文件夹。 </br><b>time :</b> 2012-7-8 下午5:13:25
	 * 
	 * @param directory
	 * @return 创建成功则返回true，否则返回false。
	 * @throws IOException
	 */
	public static boolean makeDirectory(String directory) throws IOException {
		return makeDirectory(directory, false);
	}

	/**
	 * 提取文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String extractName(String path) {
		if (path == null)
			return null;
		boolean hasFileName = path.substring(path.length() - 5, path.length())
				.contains(".");
		if (hasFileName) {
			return path.substring(path.lastIndexOf(File.separator) + 1);
		} else {
			return null;
		}
	}

	/**
	 * 导入文件后缀名
	 * 
	 * @param path
	 * @return
	 */
	public static String extractSuffix(String path) {
		return path.substring(path.lastIndexOf(".") + 1);
	}

	/**
	 * </br><b>title : </b> 创建一个文件夹 </br><b>description
	 * :</b>如果<i>createParents</i> 被标记为true，则父级文件夹不存在将会被自动创建。 </br><b>time :</b>
	 * 2012-7-8 下午5:14:07
	 * 
	 * @param directory
	 *            需要被创建的文件夹
	 * @param createParents
	 *            是否创建父级文件夹标识
	 * @return 如果文件夹创建成功，返回true。如果文件夹已经存在，返回false。
	 * @throws IOException
	 *             如果文件夹不能被创建，则抛出异常
	 */
	public static boolean makeDirectory(String directory, boolean createParents)
			throws IOException {
		boolean created = false;
		File dir = new File(directory);
		if (createParents) {
			created = dir.mkdirs();
		} else {
			created = dir.mkdir();
		}
		return created;
	}

	/**
	 * <b>description :</b> 计算文件夹大小 </br><b>time :</b> 2012-9-13 下午9:02:18
	 * 
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	public static long getSize(File directory) throws IOException {

		File[] files = directory.listFiles();
		long size = 0;
		for (File f : files) {
			if (f.isDirectory())
				size += getSize(f);
			else {
				FileInputStream fis = new FileInputStream(f);
				size += fis.available();
				fis.close();
			}
		}
		return size;
	}

	/**
	 * Special method for capture of StdOut.
	 * 
	 * @return
	 */
	private final static Thread stdOut(final Process p) {
		final byte[] empty = new byte[128];
		for (int b = 0; b < empty.length; b++) {
			empty[b] = (byte) 0;
		}
		Thread std = new Thread() {
			public void run() {
				StringBuilder sb = new StringBuilder(1024);
				byte[] buf = new byte[128];
				BufferedInputStream bis = new BufferedInputStream(
						p.getInputStream());
				try {
					while (bis.read(buf) != -1) {
						sb.append(new String(buf).trim());
						System.arraycopy(empty, 0, buf, 0, buf.length);
					}
					bis.close();
				} catch (Exception e) {
					Log.e(TAG, String.format("%1$s", e));
				}
			}
		};
		std.setDaemon(true);
		std.start();
		return std;
	}

	/**
	 * 生成一个文件名。类似 282818_00023 。这个名字由于当前秒数加随机数组成。
	 * 
	 * @return 生成的文件名
	 */
	public static String generateCustomName() {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		sb.append(System.nanoTime());
		sb.append('_');
		int i = random.nextInt(99999);
		if (i < 10) {
			sb.append("0000");
		} else if (i < 100) {
			sb.append("000");
		} else if (i < 1000) {
			sb.append("00");
		} else if (i < 10000) {
			sb.append("0");
		}
		sb.append(i);
		return sb.toString();
	}

}