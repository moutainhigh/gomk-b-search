package io.gomk.framework.utils.parse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import org.apache.tools.ant.util.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

public class AppMain {

	public static void main(String[] args) {
		System.out.println("hello world 888");

		// 下载文件并且进行解压缩。返回解压之后的目录。
		String m_RootPath = MKFiles.downloadUnZipRar("http://tms1.dtw.com.cn/Files/123/", "忻州供电局段预算-上报.zip", "d:/");
		System.out.println("m_rootPath:" + m_RootPath);

		// 读取word保存到text中
		String m_RootPathTest = MKFiles.saveString(m_RootPath);
		System.out.println("m_RootPathTest:" + m_RootPathTest);

		// 读取text的文件
		List<String[]> list = MKFiles.readString(m_RootPathTest);
		String[] arr = list.get(0);
		for (int i = 1; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	}

}
