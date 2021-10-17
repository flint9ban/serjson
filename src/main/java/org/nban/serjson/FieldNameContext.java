package org.nban.serjson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author liyi
 * @date 2021/10/16
 */
public class FieldNameContext {

    /**
     * 属性和定义映射关系
     */
    private Map<String,FieldNameDefine> fieldNameDefineMap = new HashMap<String, FieldNameDefine>();

    /**
     * 自定义的属性集合
     * 自定义的属性映射关系是通过FieldNameDefine,其他类型是直接通过字段名和json映射
     */
    private Set<String> selfDefineSet = new HashSet();

    /**
     * 根据字段名称，获得对应的json定义
     * @param fieldName
     * @return
     */
    public FieldNameDefine getFieldDefine(String fieldName){
        return fieldNameDefineMap.get(fieldName);
    }

    /**
     * 判断字段是否是自定义
     * @param filedName
     * @return
     */
    public boolean isSelfDefine(String filedName){
        return selfDefineSet.contains(filedName);
    }


    /**
     * 增加属性映射关系
     * @param fieldName
     * @param jsonPath
     */
    public FieldNameContext addFieldNameDefine(String fieldName,String jsonPath){
        FieldNameDefine fieldNameDefine = new FieldNameDefine(fieldName, jsonPath);
        fieldNameDefineMap.put(fieldName,fieldNameDefine);
        addSelfDefineSet(fieldName);
        return this;
    }

    /**
     * 增加属性映射关系
     * @param fieldName
     * @param jsonPath
     * @param dateFormat
     */
    public FieldNameContext addDateFieldNameDefine(String fieldName,String jsonPath,String dateFormat){
        DateFieldNameDefine fieldNameDefine = new DateFieldNameDefine(fieldName, jsonPath,dateFormat);
        fieldNameDefineMap.put(fieldName,fieldNameDefine);
        addSelfDefineSet(fieldName);
        return this;
    }

    private void addSelfDefineSet(String fieldName){
        for (String key : fieldNameDefineMap.keySet()) {
            if (key.startsWith(fieldName+".")) {
                selfDefineSet.add(fieldName);
            }
        }
        int lastIndex = fieldName.lastIndexOf(".");
        if (lastIndex > 1) {
            String substring = fieldName.substring(0, lastIndex);
            for (String key : fieldNameDefineMap.keySet()) {
                if (substring.equals(key)) {
                    selfDefineSet.add(substring);
                }
            }
        }
    }

}
