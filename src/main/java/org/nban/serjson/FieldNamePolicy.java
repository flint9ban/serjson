package org.nban.serjson;

import java.beans.PropertyDescriptor;

/**
 * @author liyi
 * @date 2021/10/16
 */
public class FieldNamePolicy {

    private FieldNameContext fieldNameContext;

    public FieldNamePolicy(FieldNameContext fieldNameContext) {
        this.fieldNameContext = fieldNameContext;
    }

    public FieldNameDefine getFieldDefine(String parentPath, PropertyDescriptor propertyDescriptor){
        String propertyPath = getPropertyPath(parentPath, propertyDescriptor);
        return fieldNameContext.getFieldDefine(propertyPath);
    }

    public boolean isSelfDefine(String parentPath,PropertyDescriptor propertyDescriptor){
        String propertyPath = getPropertyPath(parentPath, propertyDescriptor);
        return fieldNameContext.isSelfDefine(propertyPath);
    }

    public String getPropertyPath(String path,PropertyDescriptor propertyDescriptor){
        if (path == null) {
            return propertyDescriptor.getName();
        }else {
            return path+"."+propertyDescriptor.getName();
        }
    }

}
