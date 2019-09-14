package io.gomk.framework.utils.parse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text {

	// 读数据，返回字符串行的集合
	public List<String> read(String filePath) throws IOException {

		List<String> list = new ArrayList<String>();
		File f = new File(filePath);
		if (f.isFile() && f.exists()) {
			InputStream file = new FileInputStream(f);
			InputStreamReader read = new InputStreamReader(file, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(read);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				list.add(line);
			}
			bufferedReader.close();
		}
		return list;
	}
}
