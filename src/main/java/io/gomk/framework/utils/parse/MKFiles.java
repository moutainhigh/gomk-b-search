package io.gomk.framework.utils.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

public class MKFiles {

	// 读取的数据再创建一个目录
	public static String readString(String m_RootPath) {

		try {

			File m_RootPathFile0 = new File(m_RootPath);
			File m_RootPathFile = new File(m_RootPathFile0.getPath() + "Test");
			if (m_RootPathFile.exists()) { // 先删除再创建
				FileUtils.deleteDirectory(m_RootPathFile);
			}
			m_RootPathFile.mkdirs(); // 创建目录

			List<String> list = ForPathDoc(m_RootPath);
			for (String path : list) {
				System.out.println("doc filename:" + path);

				File pathFile = new File(path);
				String m_Suffix = getSuffix(path.toLowerCase());
				if (m_Suffix.equals("doc")) {

					io.gomk.framework.utils.parse.Word2003 word = new io.gomk.framework.utils.parse.Word2003();
					String str = word.read(path);
					System.out.println("doc filename:" + str);
				} else if (m_Suffix.equals("docx")) {

					io.gomk.framework.utils.parse.Word2007 word = new io.gomk.framework.utils.parse.Word2007();
					String str = word.read(path);
					System.out.println("doc filename:" + str);
				} else if (m_Suffix.equals("pdf")) {

					io.gomk.framework.utils.parse.PDF word = new io.gomk.framework.utils.parse.PDF();
					String str = word.read(path);
					System.out.println("doc filename:" + str);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// 下载，解压，返回根目录
	// http://tms1.dtw.com.cn/Files/123/, 忻州供电局段预算-上报.zip, d:/
	public static String downloadUnZipRar(String m_url, String m_FileName, String m_DescPath) {

		String m_zipFileName = "";
		String m_rootPath = "";
		try {

			// 下载文件
			String url = m_url + java.net.URLEncoder.encode(m_FileName, "utf-8");
			System.out.println("下载文件开始...");
			downloadFromUrl(url, m_DescPath);
			System.out.println("下载文件结束");

			// 创建根目录
			String m_Suffix = getSuffix(m_FileName);
			String m_NameNonSuffix = getNameNonSuffix(m_FileName);
			m_zipFileName = m_DescPath + m_FileName;
			m_rootPath = m_DescPath + m_NameNonSuffix + "/";
			File m_rootPathFile = new File(m_rootPath);
			if (m_rootPathFile.exists()) { // 先删除再创建
				FileUtils.deleteDirectory(m_rootPathFile);
			}
			m_rootPathFile.mkdirs(); // 创建目录
			System.out.println("根文件(m_zipFileName):" + m_zipFileName);
			System.out.println("根目录(m_rootPath):" + m_rootPath);

			// 解压文件到根目录
			File m_ZipFile = new File(m_zipFileName);
			unZip(m_ZipFile, m_rootPath);

			// 递归解压文件
			List<String> list = ForPath(m_rootPath);
			for (String str : list) {
				unZipOrRar(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return m_rootPath;
	}

	// 解压文件，递归解压文件的文件
	public static void unZipOrRar(String zipFileName) throws IOException {

		File zipFile = new File(zipFileName);
		String m_FileName = zipFile.getName();
		String m_ParentPath = zipFile.getParent();
		String m_Suffix = getSuffix(m_FileName);
		String m_NameNonSuffix = getNameNonSuffix(m_FileName);

		// 解压文件目录的设置
		String zipPath = m_ParentPath + "/" + m_NameNonSuffix + "/";
		File zipPathFile = new File(zipPath);
		if (zipPathFile.exists()) {
			FileUtils.deleteDirectory(zipPathFile);
		}
		zipPathFile.mkdirs();

		// 解压文件
		File m_unZipFile = new File(zipFileName);
		if (m_Suffix.toLowerCase().equals("zip")) {

			try {
				unZip(m_unZipFile, zipPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (m_Suffix.toLowerCase().equals("rar")) {

			try {
				unRar(m_unZipFile, zipPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 解压rar,只解压一个文件。
	public static void unRar(File rarFile, String outDir) throws FileNotFoundException, RarException, IOException {

		// 创建目录
		File outFileDir = new File(outDir);
		if (!outFileDir.exists()) {
			outFileDir.mkdirs();
		}

		Archive archive = new Archive(new FileInputStream(rarFile));
		FileHeader fileHeader = archive.nextFileHeader();
		while (fileHeader != null) {

			if (fileHeader.isDirectory()) {
				fileHeader = archive.nextFileHeader();
				continue;
			}
			String fileName = outDir + fileHeader.getFileNameW();

			File out = new File(fileName);
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

	// 解压zip,只解压一个文件。
	public static void unZip(File zipFile, String outDir) throws IOException {

		File outFileDir = new File(outDir);
		if (!outFileDir.exists()) {
			outFileDir.mkdirs();
		}

		ZipFile zip = new ZipFile(zipFile, "GBK"); // 中文
		for (Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements();) {

			ZipEntry entry = (ZipEntry) enumeration.nextElement();
			String zipEntryName = java.net.URLDecoder.decode(entry.getName(), "utf-8");
			InputStream in = zip.getInputStream(entry);
			String fileName = outDir + zipEntryName;

			// 处理压缩文件包含文件夹的情况
			if (entry.isDirectory()) {
				File fileDir = new File(fileName);
				fileDir.mkdir();
				continue;
			}
			File file = new File(fileName);

			// 创建文件之前，测试是否有目录
			String sparentPath = file.getParent();
			File fparentPath = new File(sparentPath);
			if (!fparentPath.exists()) {
				fparentPath.mkdirs();
			}

			file.createNewFile();
			OutputStream out = new FileOutputStream(file);
			byte[] buff = new byte[1024];
			int len;

			while ((len = in.read(buff)) > 0) {
				out.write(buff, 0, len);
			}
			in.close();
			out.close();
		}
	}

	// 下载文件
	public static String downloadFromUrl(String url, String dir) {

		try {

			URL httpurl = new URL(url);
			String fileName = getFileNameFromUrl(url);
			fileName = java.net.URLDecoder.decode(fileName, "utf-8"); // base64转化为中文
			File f = new File(dir + fileName);
			FileUtils.copyURLToFile(httpurl, f);

		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "";
	}

	// 循环调用文件目录下的所有的文件
	public static List<String> ForPath(String zipPath) {

		System.out.println("递归循环调用文件目录:" + zipPath);
		List<String> list = new ArrayList<String>();
		File forFileOrDirectory = new File(zipPath); // 要遍历的路径
		File[] fs = forFileOrDirectory.listFiles(); // 遍历path下的文件和目录，放在File数组中

		// 遍历File[]数组
		for (File f : fs) {
			if (f.isDirectory()) {
				List<String> list0 = ForPath(zipPath + f.getName() + "/");
				for (String str0 : list0) {
					list.add(str0);
				}
			} else {
				list.add(zipPath + f.getName());
			}
		}
		return list;
	}

	// 循环调用文件目录下的所有的文件,只获取doc,pdf文件
	public static List<String> ForPathDoc(String m_Path) {

		System.out.println("递归循环调用文件目录(doc,pdf):" + m_Path);
		List<String> list = new ArrayList<String>();
		File forFileOrDirectory = new File(m_Path); // 要遍历的路径
		File[] fs = forFileOrDirectory.listFiles(); // 遍历path下的文件和目录，放在File数组中

		// 遍历File[]数组
		for (File f : fs) {
			if (f.isDirectory()) {
				List<String> list0 = ForPathDoc(m_Path + f.getName() + "/");
				for (String str0 : list0) {
					list.add(str0);
				}
			} else {
				String name = getSuffix(f.getName().toLowerCase());
				if (name.equals("doc") || name.equals("docx") || name.equals("pdf")) {
					if (f.getName().indexOf("~", 0) < 0) {
						list.add(m_Path + f.getName());
					}
				}
			}
		}
		return list;

	}

	// 获取URL地址的文件名称
	public static String getFileNameFromUrl(String url) {
		String name = new Long(System.currentTimeMillis()).toString() + ".X";
		int index = url.lastIndexOf("/");
		if (index > 0) {
			name = url.substring(index + 1);
			if (name.trim().length() > 0) {
				return name;
			}
		}
		return name;
	}

	// 获取扩展名称
	public static String getSuffix(String fileName) {
		File file = new File(fileName);
		String name = file.getName();
		String suffix = fileName.substring(name.lastIndexOf(".") + 1);
		return suffix;
	}

	// 获取文件名称，不带扩展名
	public static String getNameNonSuffix(String fileName) {
		String name = fileName.substring(0, fileName.lastIndexOf("."));
		return name;
	}

}
