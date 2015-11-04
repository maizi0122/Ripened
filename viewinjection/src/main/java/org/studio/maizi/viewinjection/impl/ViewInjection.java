/*
 *  $HomePage: https://github.com/maizi0122/ $
 *  $Revision: 000001 $
 *  $Date: 2015-10-18 09:05:31 -0000 (Sun, 18 Oct 2015) $
 *
 *  ====================================================================
 *  Copyright (C) 2015 The Maizi-Studio Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  ====================================================================
 *
 *  This project powered by Maizi-Studio, but works with the
 *  license of apache,so you should abide by this license.
 *  Any question contacting with email below:
 *  maizi0122@gmail.com
 */

package org.studio.maizi.viewinjection.impl;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;

import org.studio.maizi.viewinjection.IEventBinder;
import org.studio.maizi.viewinjection.IViewInjection;
import org.studio.maizi.viewinjection.anno.ResId;
import org.studio.maizi.viewinjection.exception.VIRuntimeException;
import org.studio.maizi.viewinjection.util.Type;

import java.lang.reflect.Field;
import java.util.Formatter;

/**
 * an implementation of IViewInjection.
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-10-18.
 */
@SuppressWarnings("all")
public class ViewInjection implements IViewInjection {

    private static final String NULL_CTX = "param context can not be null, please check your code...";
    private static final String NULL_FRAG_AND_ROOT = "params fragment and root can not be null, please check your code...";
    private static final String ILLEGAL_ID = "can not find the view by id: %s annotation(ResId):#0x %s .please check your code...";
    private static final String ILLEGAL_FIELD = "Field: %s was not a subclass of view,please check your code...";
    private static final String CAST_EXCEPTION = "Field: %s : %s";

    /** the object which can bind listener smartly. */
    private IEventBinder eventBinder;

    public ViewInjection() {
        this(null);
    }

    public ViewInjection(IEventBinder eventBinder) {
        this.eventBinder = eventBinder;
    }


    @Override
    public void initView(Activity context, Object... listeners) {
        if (context == null)
            throw new RuntimeException(NULL_CTX);
        Class<? extends Activity> clazz = context.getClass();
        analysis(context, null, clazz, listeners);
    }

    @Override
    public void initView(Fragment fragment, View root, Object... listeners) {
        if (fragment == null || root == null) {
            throw new RuntimeException(NULL_FRAG_AND_ROOT);
        }
        Class<? extends Fragment> clazz = fragment.getClass();
        analysis(fragment, root, clazz, listeners);
    }

    /**
     * analysis the object of activity of fragment<br />
     * if you setup the IEventBinder, we can also help you bind the listener smartly.<br />
     * In fact, we are not force you to use auto-event binding, you can bind the listener all by yourself.<br />
     *
     * @param obj           may be it is current context, or it is a fragment of an activity.
     * @param root          if 'obj' is fragment, it is the root view of your fragment.
     * @param clazz         the class object of 'obj'.
     * @param listeners     additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     */
    private void analysis(Object obj, View root, Class<?> clazz, Object[] listeners) {
        Type type = null;
        Activity context = null;
        if (obj instanceof Activity) {
            context = Activity.class.cast(obj);
            type = Type.Activity;
        } else if (obj instanceof Fragment) {
            type = Type.Fragment;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            ResId anno = field.getAnnotation(ResId.class);
            int resId = 0;
            if (anno != null) {
                resId = anno.id();
                View viewById = null;
                if (type.equals(Type.Activity))
                    viewById = context.findViewById(resId);
                else if (type.equals(Type.Fragment))
                    viewById = root.findViewById(resId);
                if (viewById == null) {
                    throw new VIRuntimeException(new Formatter().format(ILLEGAL_ID, field.toString(), Integer.toHexString(resId)).toString());
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
                    throw new VIRuntimeException(new Formatter().format(ILLEGAL_FIELD, field.toString()).toString());
                try {
                    field.set(obj, field.getType().cast(viewById));
                    Object o = field.get(obj);
                    System.out.println();
                } catch (RuntimeException e) {
                    throw new VIRuntimeException(new Formatter().format(CAST_EXCEPTION, field.toString(), e.getMessage()).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (resId != 0 && eventBinder != null) {
                eventBinder.bindEvent(field, resId, obj, listeners);
            }
        }
    }
}
