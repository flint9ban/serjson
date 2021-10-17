package org.nban.serjson;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liyi 日期属性和jsonpath 映射关系
 * @date 2021/10/16
 */
@Getter
@Setter
public class DateFieldNameDefine extends FieldNameDefine {

    /**
     * 日期格式ßß
     */
    private String dateFormat;

    public DateFieldNameDefine(String pojoAttrName, String jsonPath,String dateFormat) {
        super(pojoAttrName, jsonPath);
        this.dateFormat = dateFormat;
    }
}
