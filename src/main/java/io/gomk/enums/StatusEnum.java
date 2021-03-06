/*
 * Copyright (c) 2018-2022 Caratacus, (caratacus@qq.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.gomk.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <p>
 * 状态枚举
 * </p>
 *
 * @author Caratacus
 */
public enum StatusEnum {

    NORMAL(0, "正常"), DISABLE(1, "禁用"), DELETED(2, "已删除");

    @EnumValue
    private final int value;
    private final String desc;

    StatusEnum(final int value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

	public String getDesc() {
		return desc;
	}
	 @JsonCreator
    public static StatusEnum getEnum(int value) {
        for (StatusEnum enums : StatusEnum.values()) {
            if (enums.getValue() == value) {
                return enums;
            }
        }
        throw new UnknownEnumException("Error: Invalid StatusEnum type value: " + value);
    }
}
