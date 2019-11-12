package io.gomk.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.gomk.common.code.StatusCode;
import io.gomk.common.exception.BusinessException;
import io.gomk.es6.EsUtil;
import io.gomk.framework.hdfs.HdfsOperator;
import io.gomk.framework.utils.RandomUtil;
import io.gomk.mapper.TGAttachmentMapper;
import io.gomk.model.entity.TGAttachment;
import io.gomk.service.TGAttachmentService;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nick
 * @since 2019-11-10
 */
@Slf4j
@Service
@DS("oneself")
public class TGAttachmentServiceImpl extends ServiceImpl<TGAttachmentMapper, TGAttachment> implements TGAttachmentService {

	@Autowired
	EsUtil esUtil;
	private final static String STORE_HDFS = "HDFS";

	@Value("${spring.data.hdfs.server}")
	protected String hdfsServer;
	@Value("${spring.data.hdfs.zbfbDst}")
	protected String zbfbDst;
	
	@Override
    public String downloadById(String id,HttpServletResponse response,HttpServletRequest request) {
        TGAttachment atta = baseMapper.selectById(id);
        if (atta != null) {
        	InputStream is = esUtil.getInputStreams(atta.getFtpType(), atta.getFtpPath(), atta.getFileExt());
            return downloadAttachment(is, atta.getAttaName(),response,request);
        } else {
            throw new BusinessException(StatusCode.DOWNLOAD_ERROR);
        }
    }

    @Override
    public void uploadAtta(String attaName, String attaDecs, int attaType, MultipartFile attafile) throws IllegalStateException, IOException {
        //TODO 需要加入上传文件实现
        TGAttachment attachment  = new TGAttachment();
        String originalFilename = attafile.getOriginalFilename();
        attachment.setFileExt(originalFilename.substring(originalFilename.lastIndexOf(".") + 1));

        attachment.setFtpType(STORE_HDFS);
        
        System.setProperty("HADOOP_USER_NAME","hdfs");
		Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsServer);
        String src= "/temp/" + RandomUtil.getGlobalUniqueId() + "." + attachment.getFileExt();
        File tempFile = new File(src);
        attafile.transferTo(tempFile);
        String dst = zbfbDst + tempFile.getName();
        HdfsOperator.putToHDFS(tempFile.getAbsolutePath(), dst, configuration);
        tempFile.delete();
        
        attachment.setFtpPath(dst);

        attachment.setAttaName(attaName);
        attachment.setAttaDecs(attaDecs);
        attachment.setAttaType(attaType);
        attachment.setCreatedTime(LocalDateTime.now());
        baseMapper.insert(attachment);

    }

    /**
     * 下载附件
     * @param downloadurl
     * @param fileName
     * @param response
     * @param request
     * @return
     */
    private String downloadAttachment(InputStream is, String fileName, HttpServletResponse response,HttpServletRequest request) {
        // TODO path需要根据实际情况定义,
        String path ="";
        BufferedOutputStream out = null;

        String downloadMode = "attachment";

        try {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            String agent = request.getHeader("User-Agent");
            boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
            if (isMSIE) {
                response.setHeader("Content-Disposition",
                        downloadMode + ";filename=" + URLEncoder.encode(fileName, "UTF-8"));
            } else {
                response.setHeader("Content-Disposition",
                        downloadMode + ";filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
            }
            out = new BufferedOutputStream(response.getOutputStream());
           // is = new BufferedInputStream(new FileInputStream(new File(filepath)));
            byte[] content = new byte[1024];
            int len = 0;
            while ((len = is.read(content)) > 0) {
                out.write(content, 0, len);
            }
            out.flush();
            return "下载成功";

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return "下载失败";
    }
}
