package io.gomk.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import io.gomk.common.code.StatusCode;
import io.gomk.common.exception.BusinessException;
import io.gomk.model.entity.TGAttachment;
import io.gomk.mapper.TGAttachmentMapper;
import io.gomk.service.TGAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author nick
 * @since 2019-11-10
 */
@Service
@DS("oneself")
public class TGAttachmentServiceImpl extends ServiceImpl<TGAttachmentMapper, TGAttachment> implements TGAttachmentService {


    @Override
    public String downloadById(String id,HttpServletResponse response,HttpServletRequest request) {
        TGAttachment atta = baseMapper.selectById(id);
        if (atta != null) {

            return downloadAttachment(atta.getFtpPath(), atta.getAttaName(),response,request);
        } else {
            throw new BusinessException(StatusCode.DOWNLOAD_ERROR);
        }
    }

    /**
     * 下载附件
     * @param downloadurl
     * @param fileName
     * @param response
     * @param request
     * @return
     */
    private String downloadAttachment(String downloadurl, String fileName, HttpServletResponse response,HttpServletRequest request) {
        // TODO path需要根据实际情况定义,
        String path ="";
        BufferedOutputStream out = null;
        InputStream is = null;
        String filepath = path + "/" + downloadurl;
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
            is = new BufferedInputStream(new FileInputStream(new File(filepath)));
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
