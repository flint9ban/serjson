package org.nban.serjson;


import org.junit.Assert;
import org.junit.Test;

public class FieldNameContextTest {

    @Test
    public void test(){
        FieldNameContext fieldNameContext = new FieldNameContext();
        fieldNameContext.addFieldNameDefine("a","$.a")
                .addFieldNameDefine("b","b")
                .addDateFieldNameDefine("c","c","abc");
        FieldNameDefine fieldDefine = fieldNameContext.getFieldDefine("a");
        Assert.assertEquals(fieldDefine.getJsonPath(),"$.a");
        fieldDefine = fieldNameContext.getFieldDefine("c");
        Assert.assertEquals(fieldDefine.getClass(),DateFieldNameDefine.class);
        Assert.assertEquals(((DateFieldNameDefine)fieldDefine).getDateFormat(),"abc");
    }

    @Test
    public void testIsSelfDefine(){
        FieldNameContext fieldNameContext = new FieldNameContext();
        fieldNameContext.addFieldNameDefine("a","$.a")
                .addFieldNameDefine("a.b","b")
                .addDateFieldNameDefine("a.c","c","abc");
        fieldNameContext.isSelfDefine("a");
    }

}