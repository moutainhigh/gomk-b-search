package io.gomk.service;

import io.gomk.model.entity.DZbPrj;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 招投标项目维度表 服务类
 * </p>
 *
 * @author chen
 * @since 2019-10-28
 */
public interface IDZbPrjService extends IService<DZbPrj> {

    DZbPrj getPrjByPrjCode(String prjCode);

}
