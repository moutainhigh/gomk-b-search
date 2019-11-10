package io.gomk.service;

import io.gomk.model.entity.TGAttachment;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
