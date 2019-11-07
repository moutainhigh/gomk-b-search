package io.gomk.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;

public enum FixedTagRuleEnum implements IEnum<Integer> {
	F11(11, "", "招标项目管理范围"),
	F12(12,"industry_name", "专业版块"),
	F13(13, "prj_type", "项目类型"),
	F14(14, "if_cent_purchas", "是否集采"),
	F15(15, "prj_nature", "项目阶段"),
	F16(16, "capital_source", "资金来源");
	
    
	FixedTagRuleEnum(int value, String field, String fieldCN) {
        this.value = value;
        this.field = field;
        this.fieldCN = fieldCN;
    }
	@EnumValue
    private int value;
    private String field;
    private String fieldCN;
	@Override
	public Integer getValue() {
		return this.value;
	}
	public String getField() {
		return this.field;
	}
	public String getFieldCN() {
		return this.fieldCN;
	}
	public static FixedTagRuleEnum fromValue(Integer value) throws UnknownEnumException {
		for (FixedTagRuleEnum tEnum : FixedTagRuleEnum.values()) {
            if (tEnum.getValue() == value) {
                return tEnum;
            }
        }
        throw new UnknownEnumException("Error: Invalid scope Enum type value: " + value);
	}

}
