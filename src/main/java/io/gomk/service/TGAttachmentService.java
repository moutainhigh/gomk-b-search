package io.gomk.service;

import io.gomk.model.entity.TGAttachment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nick
 * @since 2019-11-10
 */
public interface TGAttachmentService extends IService<TGAttachment> {
    /**
     * 根据附件主建下载附件
     * @param id
     * @return
     */
    String downloadById( String id, HttpServletResponse response, HttpServletRequest request);

    /**
     * 上传
     * @param attaName
     * @param attaDecs
     * @param attaType
     * @param attafile
     * @throws IOException 
     * @throws IllegalStateException 
     */
    void uploadAtta(String attaName, String attaDecs, int attaType,  MultipartFile attafile) throws IllegalStateException, IOException;
}
