package io.gomk.service.impl;

import io.gomk.model.entity.FactoryOrg;
import io.gomk.enums.StatusEnum;
import io.gomk.mapper.FactoryOrgMapper;
import io.gomk.service.IFactoryOrgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 机构部门表 服务实现类
 * </p>
 *
 * @author Robinxiao
 * @since 2019-06-20
 */
@Service
public class FactoryOrgServiceImpl extends ServiceImpl<FactoryOrgMapper, FactoryOrg> implements IFactoryOrgService {

	
	@Override
	@Transactional
	public void insertOrg(String orgName, FactoryOrg parentOrg) throws Exception {
		Integer newChildAmount = parentOrg.getChildAmount() + 1;
		int length = newChildAmount.toString().length() < 2 ? 2 : newChildAmount.toString().length();
		if (length > 2) {
			throw new Exception("orgCode lenth more than 2");
		}
		String orgCode = parentOrg.getOrgCode() + String.format("%0" + length + "d", newChildAmount);
		parentOrg.setChildAmount(newChildAmount);
		
		FactoryOrg org = new FactoryOrg();
		org.setOrgName(orgName);
		org.setChildAmount(0);
		org.setLevel(parentOrg.getLevel() + 1);
		org.setOrgCode(orgCode);
		org.setParentId(parentOrg.getId());
		org.setStatus(StatusEnum.NORMAL);
		org.setFactoryId(parentOrg.getFactoryId());
		baseMapper.insert(org);
		baseMapper.updateById(parentOrg);
		
	}

}
