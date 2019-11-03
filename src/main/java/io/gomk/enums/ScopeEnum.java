package io.gomk.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;

public enum ScopeEnum implements IEnum<Integer> {
	ZBWJ(1, "招标文件库"),
    ZGYQ(2, "投标人资格要求库"),
    PBBF(3, "评标办法库"),
	JSYQ(4, "技术要求库"),
	ZJCG(5, "造价成果文件库"),
	ZCFG(6, "政策法规库"),
	ZBFB(7, "招标范本库"),
	TBWJ(8, "投标文件库"),
	TBR(9, "投标人库"),
	ZBXM(10, "招标项目库"),
	CPJG(11, "产品价格库"),
	CGSCTC(12, "采购市场探查"),
	KH(13, "客户库"),
	ZJK(14, "专家库");
    
	ScopeEnum(int value, String desc) {
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
	public static ScopeEnum fromValue(Integer value) throws UnknownEnumException {
		for (ScopeEnum tEnum : ScopeEnum.values()) {
            if (tEnum.getValue() == value) {
                return tEnum;
            }
        }
        throw new UnknownEnumException("Error: Invalid scope Enum type value: " + value);
	}

}
