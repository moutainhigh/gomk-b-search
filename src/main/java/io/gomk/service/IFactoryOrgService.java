package io.gomk.service;

import io.gomk.model.entity.FactoryOrg;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 机构部门表 服务类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-06-20
 */
public interface IFactoryOrgService extends IService<FactoryOrg> {

	void insertOrg(String orgName, FactoryOrg parentOrg) throws Exception;

}
