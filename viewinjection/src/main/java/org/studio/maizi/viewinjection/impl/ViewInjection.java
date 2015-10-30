package org.studio.maizi.viewinjection.impl;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import org.studio.maizi.viewinjection.IViewInjection;
import org.studio.maizi.viewinjection.R;
import org.studio.maizi.viewinjection.anno.ResId;

import java.lang.reflect.Field;
import java.util.Formatter;

/**
 * Created by maizi on 13-10-8.<br />
 * <p>auto view injection
 */
public class ViewInjection implements IViewInjection {

    @Override
    public void initView(Activity context) {
        if (context == null)
            throw new RuntimeException("context can not be null,please check your code...");
        Class<? extends Context> clazz = context.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            ResId anno = field.getAnnotation(ResId.class);
            if (anno != null) {
                int resId = anno.id();
                View viewById = context.findViewById(resId);
                if (viewById == null) {
                    throw new RuntimeException(new Formatter().format(context.getResources().getString(R.string.illegal_id), field.toString(), Integer.toHexString(resId)).toString());
                }
                Class superClass = field.getType();
                boolean isFind = false;
                while ((superClass = superClass.getSuperclass()) != null) {
                    if (superClass == View.class) {
                        isFind = true;
                        break;
                    }
                }
                if (!isFind)
                    throw new RuntimeException(new Formatter().format(context.getResources().getString(R.string.illegal_filed), field.toString()).toString());
                try {
                    field.set(context, field.getType().cast(viewById));
                } catch (RuntimeException e) {
                    throw new RuntimeException(new Formatter().format(context.getResources().getString(R.string.cast_exception), field.toString(), e.getMessage()).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
