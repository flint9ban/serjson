package org.nban.serjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.util.TypeUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author liyi
 * @date 2021/10/16
 */
public class JSONParse {

    private final FieldNamePolicy fieldNamePolicy;

    public JSONParse(FieldNamePolicy fieldNamePolicy) {
        this.fieldNamePolicy = fieldNamePolicy;
    }

    /**
     *  根据fieldNamePolicy中的配置对象属性和json的对应关系，填充对象属性
     * @param json
     * @param result
     * @param <T>
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public <T> void fromJson(String json,T result) throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
        fillProperties(json,result,null);
    }

    private <T> void fillProperties(String jsonObject,T value,String parent) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
        BeanInfo beanInfo = Introspector.getBeanInfo(value.getClass());
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null) {
                //根据路径找不到对应的json值
                Object jsonValue = readJSONValue(jsonObject,parent,propertyDescriptor);
                if (jsonValue == null) {
                    continue;
                }
                Object realValue = wrapValue(jsonValue,parent,propertyDescriptor);
                if (realValue != null) {
                    writeMethod.invoke(value,realValue);
                }
            }
        }
    }

    /**
     * 根据属性获得属性对应的jsonpath，获取对应的值
     * @param jsonObject
     * @param parentPath
     * @param propertyDescriptor
     * @return
     */
    private Object readJSONValue(String jsonObject,String parentPath,PropertyDescriptor propertyDescriptor){
        FieldNameDefine fieldDefine = fieldNamePolicy.getFieldDefine(parentPath, propertyDescriptor);
        if (fieldDefine == null) {
            return null;
        }
        return JSONPath.read(jsonObject, fieldDefine.getJsonPath());
    }

    /**
     * 将json装换成目标对象
     * @param jsonValue
     * @param parentPath
     * @param propertyDescriptor
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     */
    private Object wrapValue(Object jsonValue,String parentPath,PropertyDescriptor propertyDescriptor) throws InstantiationException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        if (fieldNamePolicy.isSelfDefine(parentPath,propertyDescriptor)) {
            Type propertyType = getPropertyType(propertyDescriptor);
            String currentPath = fieldNamePolicy.getPropertyPath(parentPath,propertyDescriptor);
            if (isCollection(propertyType)) {
                Collection collection = TypeUtils.createCollection(propertyType);
                JSONArray jsonArray = JSONObject.parseArray(jsonValue.toString());
                for (Object item : jsonArray) {
                    Object o = newSelfDefineValue(item, TypeUtils.getCollectionItemClass(propertyType), currentPath);
                    collection.add(o);
                }
                return collection;
            }else {
                Object o = newSelfDefineValue(jsonValue,TypeUtils.getClass(propertyType),currentPath);
                return o;
            }
        }else {
            Type propertyType = getPropertyType(propertyDescriptor);
            return TypeUtils.castToJavaBean(jsonValue,TypeUtils.getClass(propertyType));
        }

    }

    private Object newSelfDefineValue(Object jsonValue,Class cls,String parentPath) throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Object o = cls.newInstance();
        fillProperties(jsonValue.toString(), o,parentPath);
        return o;
    }


    private boolean isCollection(Type type){
        Class<?> cls = TypeUtils.getClass(type);
        if (Collection.class.isAssignableFrom(cls)) {
            return true;
        }
        return false;
    }

    private Type getPropertyType(PropertyDescriptor propertyDescriptor){
        Type[] genericParameterTypes = propertyDescriptor.getWriteMethod().getGenericParameterTypes();
        if (genericParameterTypes.length==1) {
            return genericParameterTypes[0];
        }
        return null;
    }
}
