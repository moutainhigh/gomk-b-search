package io.gomk.framework.utils.parsefile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestRegular {
	public static void main(String[] args) {
		String s = "3.7  投标文件的编制";
		String s1 = "（1）投标人或投标人3243243主要负责人的近亲属；";
		String s2 = "（1）投标函；";
		String s3 = "6.1.3 评标过程中，评标委员会成员有";
		String s4 = "3.6.3 投标人提供两个或两个以上投标报价，或者在投标";
		//String str = s3.replaceFirst("^+\\s*\\d+.\\d*", "");
		String str = s1.replaceFirst("[^\\u4e00-\\u9fa5a-zA-Z]*", "");
		log.info("==:" + str);
	}
	
}
