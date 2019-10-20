package io.gomk.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * 标签规则
 * @author vko
 *
 */
public enum TagRuleTypeEnum implements IEnum<Integer> {
	KEYWORD(1, "关键字规则"),
    FORMULA(2, "公式规则");
   
	TagRuleTypeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    private int value;
    private String desc;
	@Override
	public Integer getValue() {
		return this.value;
	}
	public String getDesc() {
		return this.desc;
	}
	public static TagRuleTypeEnum fromValue(Integer value) throws Exception {
		for (TagRuleTypeEnum tEnum : TagRuleTypeEnum.values()) {
            if (tEnum.getValue() == value) {
                return tEnum;
            }
        }
        throw new Exception("Error: Invalid Enum type value: " + value);
	}

}
