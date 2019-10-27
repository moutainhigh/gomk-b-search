package io.gomk.framework.utils.parse;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件压缩工具类
 */
public class ZipUtil {
	private static final int BUFFER_SIZE = 2 * 1024;
	/**
	 * 是否保留原来的目录结构 true: 保留目录结构; false: 所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 */
	private static final boolean KeepDirStructure = true;
	private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);

	public static void main(String[] args) {
//		try {
//			toZip("E:/app1", "E:/app.zip",true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		String path = "/Users/vko/Documents/my-code/temp/1.zip";
		String targetDir = "/Users/vko/Documents/my-code/temp/2";
		try {
			unZipFiles(path, targetDir);
			// decompression(path, targetDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//	/**
//	 * 压缩成ZIP
//	 * @param srcDir         压缩 文件/文件夹 路径
//	 * @param outPathFile    压缩 文件/文件夹 输出路径+文件名 D:/xx.zip
//	 * @param isDelSrcFile   是否删除原文件: 压缩前文件
//	 */
//	public static void toZip(String srcDir, String outPathFile,boolean isDelSrcFile) throws Exception {
//		long start = System.currentTimeMillis();
//		FileOutputStream out = null; 
//		ZipOutputStream zos = null;
//		try {
//			out = new FileOutputStream(new File(outPathFile));
//			zos = new ZipOutputStream(out);
//			File sourceFile = new File(srcDir);
//			if(!sourceFile.exists()){
//				throw new Exception("需压缩文件或者文件夹不存在");
//			}
//			compress(sourceFile, zos, sourceFile.getName());
//			if(isDelSrcFile){
//				FileUtil.delDir(srcDir);
//			}
//			log.info("原文件:{}. 压缩到:{}完成. 是否删除原文件:{}. 耗时:{}ms. ",srcDir,outPathFile,isDelSrcFile,System.currentTimeMillis()-start);
//		} catch (Exception e) {
//			log.error("zip error from ZipUtils: {}. ",e.getMessage());
//			throw new Exception("zip error from ZipUtils");
//		} finally {
//			try {
//				if (zos != null) {zos.close();}
//				if (out != null) {out.close();}
//			} catch (Exception e) {}
//		}
//	}

	/**
	 * 递归压缩方法
	 * 
	 * @param sourceFile 源文件
	 * @param zos        zip输出流
	 * @param name       压缩后的名称
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name) throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			zos.putNextEntry(new ZipEntry(name));
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				if (KeepDirStructure) {
					zos.putNextEntry(new ZipEntry(name + "/"));
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					if (KeepDirStructure) {
						compress(file, zos, name + "/" + file.getName());
					} else {
						compress(file, zos, file.getName());
					}
				}
			}
		}
	}

	/**
	 * 解压文件到指定目录
	 */
	@SuppressWarnings({ "rawtypes", "resource" })
	public static void unZipFiles(String zipPath, String descDir) throws IOException {
		log.info("文件:{}. 解压路径:{}. 解压开始.", zipPath, descDir);
		long start = System.currentTimeMillis();
		File zipFile = new File(zipPath);
		System.err.println(zipFile.getName());
		if (!zipFile.exists()) {
			throw new IOException("需解压文件不存在.");
		}
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		} else {
			delDir(descDir);
		}
		ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			System.err.println(zipEntryName);
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + File.separator + zipEntryName).replaceAll("\\*", "/");
			System.err.println(outPath);
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			// 输出文件路径信息
			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
		log.info("文件:{}. 解压路径:{}. 解压完成. 耗时:{}ms. ", zipPath, descDir, System.currentTimeMillis() - start);

	}

	/**
	 * 解压文件 filePath所代表文件系统不能与targetStr一致
	 * 
	 * @param filePath  压缩文件路径
	 * @param targetStr 解压至所在文件目录
	 */
	public static void decompression(String filePath, String targetStr) {
		File pathFile = new File(targetStr);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		File source = new File(filePath);
		if (source.exists()) {
			ZipInputStream zis = null;
			BufferedOutputStream bos = null;
			try {
				zis = new ZipInputStream(new FileInputStream(source), Charset.forName("GBK"));
				ZipEntry entry = null;
				while ((entry = zis.getNextEntry()) != null && !entry.isDirectory()) {
					File target = new File(targetStr, entry.getName());
					if (!target.getParentFile().exists()) {
						target.getParentFile().mkdirs();// 创建文件父目录
					}
					// 写入文件
					bos = new BufferedOutputStream(new FileOutputStream(target));
					int read = 0;
					byte[] buffer = new byte[1024 * 10];
					while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
						bos.write(buffer, 0, read);
					}
					bos.flush();
				}
				zis.closeEntry();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				closeQuietly(zis, bos);
			}
		}
	}

	/**
	 * 关闭一个或多个流对象
	 * 
	 * @param closeables 可关闭的流对象列表
	 */
	public static void closeQuietly(Closeable... closeables) {
		try {
			if (closeables != null && closeables.length > 0) {
				for (Closeable closeable : closeables) {
					if (closeable != null) {
						closeable.close();
					}
				}
			}
		} catch (IOException e) {
			// do nothing
		}
	}

	// 删除文件或文件夹以及文件夹下所有文件
	public static void delDir(String dirPath) throws IOException {
		log.info("删除文件开始:{}.", dirPath);
		long start = System.currentTimeMillis();
		try {
			File dirFile = new File(dirPath);
			if (!dirFile.exists()) {
				return;
			}
			if (dirFile.isFile()) {
				dirFile.delete();
				return;
			}
			File[] files = dirFile.listFiles();
			if (files == null) {
				return;
			}
			for (int i = 0; i < files.length; i++) {
				delDir(files[i].toString());
			}
			dirFile.delete();
			log.info("删除文件:{}. 耗时:{}ms. ", dirPath, System.currentTimeMillis() - start);
		} catch (Exception e) {
			log.info("删除文件:{}. 异常:{}. 耗时:{}ms. ", dirPath, e, System.currentTimeMillis() - start);
			throw new IOException("删除文件异常.");
		}
	}
}
