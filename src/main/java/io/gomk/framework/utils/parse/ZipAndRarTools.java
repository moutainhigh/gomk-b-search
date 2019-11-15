package io.gomk.framework.utils.parse;

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

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZipAndRarTools {
	public static void unRar(InputStream in, String outDir) throws Exception {
        File outFileDir = new File(outDir);
        if (!outFileDir.exists()) {
            boolean isMakDir = outFileDir.mkdirs();
            if (isMakDir) {
                System.out.println("创建压缩目录成功");
            }
        }
        Archive archive = new Archive(in);
        FileHeader fileHeader = archive.nextFileHeader();
        while (fileHeader != null) {
            if (fileHeader.isDirectory()) {
                fileHeader = archive.nextFileHeader();
                continue;
            }
            File out = new File(outDir + fileHeader.getFileNameString());
            if (!out.exists()) {
                if (!out.getParentFile().exists()) {
                    out.getParentFile().mkdirs();
                }
                out.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(out);
            archive.extractFile(fileHeader, os);

            os.close();

            fileHeader = archive.nextFileHeader();
        }
        archive.close();
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
			try {
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
			}catch(IllegalArgumentException e) {
				log.error("error:" +e.getMessage());
				
			}
		}
		log.info("文件:{}. 解压路径:{}. 解压完成. 耗时:{}ms. ", zipPath, descDir, System.currentTimeMillis() - start);

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
