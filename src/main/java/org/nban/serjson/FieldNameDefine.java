package org.nban.serjson;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liyi 定义属性和jsonpath的映射关系
 * @date 2021/10/16
 */
@Getter
@Setter
@AllArgsConstructor
public class FieldNameDefine {

    /**
     * 属性名称
     */
    private String pojoAttrName;

    /**
     * json path
     */
    private String jsonPath;
}
