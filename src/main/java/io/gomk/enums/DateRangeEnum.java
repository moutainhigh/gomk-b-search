package io.gomk.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.core.enums.IEnum;

public enum DateRangeEnum implements IEnum<Integer> {
	BEFORE_THREE_MONTH(1, "3个月以内"),
    BEFORE_HALF_YEAR(2, "半年内"),
    BEFORE_ONE_YEAR(3, "1年内"),
	BEFORE_THREE_YEAR(4, "3年内"),
	AFTER_THREE_YEAR(5, "3年以上");
    
	DateRangeEnum(int value, String desc) {
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
	public static DateRangeEnum fromValue(Integer value) throws Exception {
		for (DateRangeEnum tEnum : DateRangeEnum.values()) {
            if (tEnum.getValue() == value) {
                return tEnum;
            }
        }
        throw new Exception("Error: Invalid Enum type value: " + value);
	}

}
