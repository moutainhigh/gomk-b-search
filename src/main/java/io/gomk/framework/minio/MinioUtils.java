package io.gomk.framework.minio;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import io.minio.MinioClient;

/**
 * 上传文件
 *
 */
@Component
public class MinioUtils {

	/**
	 * 端点URL对象
	 */
	@Value("${minio.url}")
	private String url;

	/**
	 * 同用户ID,可以唯一标识帐户
	 */
	@Value("${minio.accessKey}")
	private String accessKey;

	/**
	 * secretKey帐户的密码
	 */
	@Value("${minio.secretKey}")
	private String secretKey;

	final static String BUCKET = "system-file";

	/**
	 * 上传文件
	 * 
	 * @param file
	 *            文件流
	 * @return 文件路径
	 */
	public String upload(MultipartFile file) {

		if (file == null) {
			return "";
		}
		// 创建人
		String fname = file.getOriginalFilename();
		fname = IdWorker.get32UUID() + fname.substring(fname.lastIndexOf("."));
		// 上传到minio
		try {
			MinioClient minioClient = new MinioClient(url, accessKey, secretKey);
			// 检查存储桶是否已经存在
			boolean isExist = minioClient.bucketExists(BUCKET);
			if (!isExist) {
				minioClient.makeBucket(BUCKET);
			}
			String path = DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + fname;
			minioClient.putObject(BUCKET, path, file.getInputStream(), file.getContentType());
			
			return path;

		} catch (Exception e) {

			e.printStackTrace();
			System.out.println("minio出现异常");
			return null;
		}
	}

	/**
	 * 文件下载
	 * @param path
	 *            路径
	 * @param out
	 *            文件输出流
	 * @return 下载结果
	 */
	public void download(String path, OutputStream out) throws Exception {

		// 使用给定的URL对象，访问密钥和密钥创建Minio客户端对象
		MinioClient minioClient = new MinioClient(url, accessKey, secretKey);

		// 获取minio下载的输入流
		InputStream stream = minioClient.getObject(BUCKET, path);

		// 设置每次读取值
		byte[] buf = new byte[2048];
		int len = -1;

		// 循环读取输入流
		while ((len = stream.read(buf)) != -1) {
			out.write(buf, 0, len);
		}

		// 资源关闭
		out.flush();
		out.close();
		stream.close();

	}

}
