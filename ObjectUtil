package com.zat.oms.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 對象格式化工具類
 * @author sam.liang
 */
public class ObjectUtil {

    /**
     * 將對象轉json字符串
     * <description>
     *     1.不支持map轉json字符串
     *     2.不支持對象A裡面的map屬性轉換
     * </description>
     * @param object
     * @return
     * @throws Exception
     */
    public static String Object2JsonStr(Object object) throws Exception{
        //包括public、private和proteced，但是不包括父类的申明字段
        Field[] declaredFields = object.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder("{");
        for (Field field : declaredFields) {
            if(!field.isAccessible()){
                field.setAccessible(true);
            }
            //無意義的標誌屬性
            if("operatorType".equals(field.getName()) || "serialVersionUID".equals(field.getName())){
                continue;
            }
            //獲取當前屬性的參數值
            Object tempFieldValue  = field.get(object);
            //判斷是否是list
            if(List.class.isAssignableFrom(field.getType())) {
                sb.append("\""+field.getName()+"\":[");
                if(tempFieldValue !=null) {
                    //获取对象list属性的class
                    Class<?> clazz = field.get(object).getClass();
                    //获取list属性的size方法
                    Method sizeMethod = clazz.getDeclaredMethod("size");
                    //調用size方法返回list裡面的記錄數
                    Integer size = (Integer) sizeMethod.invoke(field.get(object));
                    for(int i=0;i<size;i++) {
                        //獲取list的get方法
                        Method getMethod = clazz.getDeclaredMethod("get", int.class);
                        //調用list的get方法
                        Object innerObj = getMethod.invoke(field.get(object), i);
                        //遞歸調用自己本身
                        sb.append(Object2JsonStr(innerObj));
                        sb.append(",");
                    }
                    //20191216：如果list.size==0，不用刪除最後一個逗號
                    if(size>0) {
                        sb.deleteCharAt(sb.length()-1);
                    }
                }
                sb.append("],");
            //如果object中有class類型的屬性則遞歸遍歷
            }else if(field.getType().getName().contains("com.zat.oms")){
                sb.append("\""+field.getName()+"\":");
                sb.append(Object2JsonStr(tempFieldValue));
                sb.append(",");
            }else{
                sb.append("\""+field.getName()+"\":\""+(tempFieldValue==null?"":tempFieldValue)+"\",");
            }
        }
        //刪除最後一個逗號
        sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Object to map
     * @param obj
     * @return
     */
    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }
}
