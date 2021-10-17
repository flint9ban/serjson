package org.nban.serjson;


import org.junit.Assert;
import org.junit.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FieldNamePolicyTest {


    @Test
    public void test() throws IntrospectionException {
        FieldNameContext fieldNameContext = new FieldNameContext();
        fieldNameContext.addFieldNameDefine("a","a")
                .addFieldNameDefine("testInnerClass","s")
                .addFieldNameDefine("testInnerClass.b","b")
                .addFieldNameDefine("testInnerClass.c","c");
        FieldNamePolicy fieldNamePolicy = new FieldNamePolicy(fieldNameContext);
        List<FieldNameDefine> fieldNameDefineList = new ArrayList<>();
        List<FieldNameDefine> innerFieldNameDefineList = new ArrayList<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(TestClass.class);
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            FieldNameDefine fieldDefine = fieldNamePolicy.getFieldDefine(null,propertyDescriptor);
            if (fieldDefine == null) {
                continue;
            }
            fieldNameDefineList.add(fieldDefine);
            if(fieldDefine.getPojoAttrName().equals("testInnerClass")){
                Assert.assertTrue(fieldNamePolicy.isSelfDefine(null,propertyDescriptor));
                BeanInfo innerBean = Introspector.getBeanInfo(TestInnerClass.class);
                for (PropertyDescriptor innerBeanPropertyDescriptor : innerBean.getPropertyDescriptors()) {
                    FieldNameDefine innerField = fieldNamePolicy.getFieldDefine(fieldDefine.getPojoAttrName(), innerBeanPropertyDescriptor);
                    if (innerField == null) {
                        continue;
                    }
                    innerFieldNameDefineList.add(innerField);
                }
            }
        }

        Assert.assertEquals(fieldNameDefineList.size(),2);
        Map<Object, Object> objectMap = fieldNameDefineList.stream().collect(Collectors.toMap(FieldNameDefine::getPojoAttrName, FieldNameDefine::getJsonPath));
        Assert.assertEquals(objectMap.get("a"),"a");
        Assert.assertTrue(objectMap.containsKey("testInnerClass"));
        Assert.assertEquals(innerFieldNameDefineList.size(),2);
        objectMap = innerFieldNameDefineList.stream().collect(Collectors.toMap(FieldNameDefine::getPojoAttrName, FieldNameDefine::getJsonPath));
        Assert.assertEquals(objectMap.get("testInnerClass.b"),"b");
        Assert.assertEquals(objectMap.get("testInnerClass.c"),"c");
    }




}


