package io.gomk.common.utils;

import java.lang.reflect.Field;

public class ReflectUtil {
    public static Object getValueFormObject(Object object, String fieldName) {
        if (object==null){
//            LOG.error("the fields is wrong,object is null,fieldName is "+fieldName);
            return null;
        }
        if(fieldName==null||fieldName=="") {
//            LOG.error("the fields is wrong,object is null,object is  "+object.toString());
            return null;
        }
        Field field;
        try {
            field = object.getClass().getDeclaredField(fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(object);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
//            LOG.error("Get Value Form Object Wrong");
        }

        return null;
    }

}
