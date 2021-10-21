package org.nban.serjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class JSONParseTest {


    @Test
    public void fromJson() throws InvocationTargetException, IntrospectionException, InstantiationException, IllegalAccessException {
        FieldNameContext fieldNameContext = new FieldNameContext();
        fieldNameContext.addFieldNameDefine("a","$.a")
                .addFieldNameDefine("BList","$.bList")
                .addFieldNameDefine("testInnerClass","$.inner")
                .addFieldNameDefine("testInnerClass.b","b")
                .addFieldNameDefine("testInnerClass.c","c")
                .addFieldNameDefine("innerClassList","$.innerClassList")
                .addFieldNameDefine("innerClassList.jsonValue","$")
                .addFieldNameDefine("innerClassList.b","bb")
                .addFieldNameDefine("innerClassList.c","cc");
        FieldNamePolicy fieldNamePolicy = new FieldNamePolicy(fieldNameContext);
        JSONParse jsonParse = new JSONParse(fieldNamePolicy);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a","valueA");
        JSONObject innerClass = new JSONObject();
        innerClass.put("b","valueB");
        innerClass.put("c","valueC");
        jsonObject.put("inner",innerClass);
        JSONArray jsonArray = new JSONArray();
        JSONObject item1 = new JSONObject();
        item1.put("bb","bb1");
        item1.put("cc","cc1");
        jsonArray.add(item1);
        JSONObject item2 = new JSONObject();
        item2.put("bb","bb2");
        item2.put("cc","cc2");
        jsonArray.add(item2);
        jsonObject.put("innerClassList",jsonArray);

        JSONArray bList = new JSONArray();
        bList.add("bl1");
        bList.add("bl2");
        bList.add("bl3");
        jsonObject.put("bList",bList);

        TestClass testClass = new TestClass();
        jsonParse.fromJson(jsonObject.toJSONString(),testClass);
        System.out.println(JSONObject.toJSONString(testClass));

    }
}