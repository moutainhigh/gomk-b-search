package io.gomk.framework.utils.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class AppMain {

	public static void main(String[] args) {
		System.out.println("hello world");

		// 多条数据
		List<String[]> urllist = new ArrayList<String[]>();
		String[] urlarray = new String[5];
		urlarray[0] = "http://tms1.dtw.com.cn/Files/123/";
		urlarray[1] = "忻州供电局段预算-上报.zip";
		urlarray[2] = "d:/zip/";
		urllist.add(urlarray);

		// 下载文件并且进行解压缩。返回解压之后的目录。
		for (String[] urlarr1 : urllist) {

			String m_url = urlarr1[0];
			String m_FileName = urlarr1[1];
			String m_DescPath = urlarr1[2];
			String m_RootPath = MKFiles.downloadUnZipRar(m_url, m_FileName, m_DescPath);
			urlarr1[3] = m_RootPath;
			System.out.println("m_rootPath:" + m_RootPath);
		}

		// 读取word保存到text中
		for (String[] urlarr2 : urllist) {

			String m_RootPathText = MKFiles.saveString(urlarr2[3]);
			System.out.println("m_RootPathText:" + m_RootPathText);
			urlarr2[4] = m_RootPathText;
		}

		// 结束就删除2个文件, zip文件，解压之后的目录 1234678944
//		try {
//			File m_zipFile = new File(m_DescPath + m_FileName);
//			if (m_zipFile.exists()) {
//				m_zipFile.delete();
//				System.out.println("删除zip文件。");
//			}
//			File m_rootPathFile = new File(m_RootPath);
//			if (m_rootPathFile.exists()) {
//				FileUtils.deleteDirectory(m_rootPathFile);
//				System.out.println("删除解压目录。");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		// 读取TEXT的文件
		for (String[] urlarr3 : urllist) {

			List<String[]> list = MKFiles.readString(urlarr3[4]);
			String[] arr = list.get(0);
			for (int i = 1; i < arr.length; i++) {
				System.out.println(arr[i]);
			}
		}

		// 删除TEXT的目录
//		try {
//			System.out.println("删除目录：" + m_RootPathText);
//			File m_rootPathFile = new File(m_RootPathText);
//			if (m_rootPathFile.exists()) {
//				FileUtils.deleteDirectory(m_rootPathFile);
//				System.out.println("已经删除目录。");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
