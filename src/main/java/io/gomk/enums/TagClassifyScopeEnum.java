package io.gomk.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;

public enum TagClassifyScopeEnum implements IEnum<Integer> {
	ZBWJ(1, "招标文件库"),
    ZGYQ(2, "资格要求库"),
    PBBF(3, "评标办法库"),
	JSYQ(4, "技术要求库"),
	ZJCG(5, "造价成果库"),
	ZCFG(6, "政策法规库"),
	ZBFB(7, "招标范本库"),
	TBWJ(8, "投标文件库");
    
	TagClassifyScopeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
	@EnumValue
    private int value;
    private String desc;
	@Override
	public Integer getValue() {
		return this.value;
	}
	public String getDesc() {
		return this.desc;
	}
	public static TagClassifyScopeEnum fromValue(Integer value) throws UnknownEnumException {
		for (TagClassifyScopeEnum tEnum : TagClassifyScopeEnum.values()) {
            if (tEnum.getValue() == value) {
                return tEnum;
            }
        }
        throw new UnknownEnumException("Error: Invalid scope Enum type value: " + value);
	}

}
