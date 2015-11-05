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
import android.widget.Adapter;
import android.widget.AdapterView;

import org.studio.maizi.viewinjection.IEventBinder;
import org.studio.maizi.viewinjection.IViewInjection;
import org.studio.maizi.viewinjection.anno.ContentView;
import org.studio.maizi.viewinjection.anno.RegistListener;
import org.studio.maizi.viewinjection.anno.ResId;
import org.studio.maizi.viewinjection.exception.VIRuntimeException;
import org.studio.maizi.viewinjection.util.StringFormatter;
import org.studio.maizi.viewinjection.util.Type;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * an implementation of IViewInjection.
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-10-18.
 */
@SuppressWarnings("all")
public class ViewInjection implements IViewInjection {

    private static final String LAYOUT_RES_NAME = "layout";
    private static final String NOT_AN_LAYOUT_RES = "the layout resource reference by R.id.%s at class: %s is not an correct layout resource id, please check your code...";
    private static final String NULL_CTX = "param context can not be null, please check your code...";
    private static final String NULL_FRAG_OR_ROOT = "params fragment or root can not be null, please check your code...";
    private static final String NULL_ADAPTER_OR_ROOT_OR_HOLDER = "params adapter or root or holder can not be null, please check your code...";
    private static final String NOT_ADAPTER_VIEW = "Field: %s is not a sub class of AdapterView, please check your code...";
    private static final String NO_MATCH_ADAPTER = "Field: %s's annotation of Adapter which it's param class have no empty-constructor or your forget pass it instance manually...";
    private static final String INSTACE_ERROR = "Field : %s's RegistListener annotation param %s.class have no empty-params constructor or it is a illegal parameter, please check your code...";
    private static final String ILLEGAL_ID = "can not find the view by id: %s annotation(ResId):#0x %s .please check your code...";
    private static final String ILLEGAL_FIELD = "Field: %s was not a subclass of view,please check your code...";
    private static final String CAST_EXCEPTION = "Field: %s : %s";

    /**
     * the object which can bind listener smartly.
     */
    private IEventBinder eventBinder;

    public ViewInjection() {
        this(new EventBinder());
    }

    public ViewInjection(IEventBinder eventBinder) {
        this.eventBinder = eventBinder;
    }

    @Override
    public ViewInjection setEventBinder(IEventBinder eventBinder) {
        this.eventBinder = eventBinder;
        return this;
    }

    @Override
    public IViewInjection initView(Activity context, Object... listeners) {
        if (context == null)
            throw new RuntimeException(NULL_CTX);
        Class<? extends Activity> clazz = context.getClass();
        analysis(context, null, clazz, null, listeners);
        return this;
    }


    @Override
    public IViewInjection initView(Fragment fragment, View root, Object... listeners) {
        if (fragment == null || root == null)
            throw new RuntimeException(NULL_FRAG_OR_ROOT);
        Class<? extends Fragment> clazz = fragment.getClass();
        analysis(fragment, root, clazz, null, listeners);
        return this;
    }

    @Override
    public IViewInjection initView(Adapter adapter, View root, Object holder, Object... listeners) {
        if (adapter == null || root == null || holder == null)
            throw new RuntimeException(NULL_ADAPTER_OR_ROOT_OR_HOLDER);
        Class<?> clazz = holder.getClass();
        analysis(adapter, root, clazz, holder, listeners);
        return this;
    }

    /**
     * analysis the object of activity of fragment<br />
     * if you setup the IEventBinder, we can also help you bind the listener smartly.<br />
     * In fact, we are not force you to use auto-event binding, you can bind the listener all by yourself.<br />
     *
     * @param obj       may be it is current context, or it is a fragment of an activity, or an adapter
     * @param root      if 'obj' is fragment, it is the root view of your fragment.
     * @param clazz     the class object of 'obj'.
     * @param listeners additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     */
    private void analysis(Object obj, View root, Class<?> clazz, Object holder, Object... listeners) {
        Type type = null;
        Activity context = null;
        if (obj instanceof Activity) {
            context = Activity.class.cast(obj);
            type = Type.Activity;
        } else if (obj instanceof Fragment) {
            type = Type.Fragment;
        } else if (obj instanceof Adapter) {
            type = Type.Adapter;
        }
        ContentView annoCV = null;
        if (context != null && (annoCV = obj.getClass().getAnnotation(ContentView.class)) != null) {//inject the content view.
            int resId = annoCV.value();
            if (!context.getResources().getResourceTypeName(resId).equals(LAYOUT_RES_NAME))
                throw new VIRuntimeException(StringFormatter.format(NOT_AN_LAYOUT_RES, context.getResources().getResourceName(resId).split("/")[1], context.getClass().getName()));
            context.setContentView(resId);
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        if (type.equals(Type.Adapter))
            declaredFields = holder.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            ResId anno = field.getAnnotation(ResId.class);
            int resId = 0;
            if (anno != null) {
                resId = anno.value();
                View viewById = null;
                if (type.equals(Type.Activity))
                    viewById = context.findViewById(resId);
                else if (type.equals(Type.Fragment) || type.equals(Type.Adapter))
                    viewById = root.findViewById(resId);
                if (viewById == null) {
                    throw new VIRuntimeException(StringFormatter.format(ILLEGAL_ID, field.toString(), Integer.toHexString(resId)));
                }
                Class superClass = field.getType();
                boolean isFind = false;
                if (superClass != View.class) {
                    while ((superClass = superClass.getSuperclass()) != null) {
                        if (superClass == View.class) {
                            isFind = true;
                            break;
                        }
                    }
                } else
                    isFind = true;
                if (!isFind)
                    throw new VIRuntimeException(StringFormatter.format(ILLEGAL_FIELD, field.toString()));
                try {
                    Object transfer = obj;
                    if (type.equals(Type.Adapter))
                        obj = holder;
                    field.set(obj, field.getType().cast(viewById));
                    org.studio.maizi.viewinjection.anno.Adapter annoAdpt = null;
                    if ((annoAdpt = field.getAnnotation(org.studio.maizi.viewinjection.anno.Adapter.class)) != null) {
                        setAdapter(transfer, null, field, annoAdpt.value(), listeners);
                    }
                } catch (RuntimeException e) {
                    throw new VIRuntimeException(StringFormatter.format(CAST_EXCEPTION, field.toString(), e.getMessage()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            RegistListener annoRegist = null;
            if (resId != 0 && (annoRegist = field.getAnnotation(RegistListener.class)) != null && eventBinder != null) {
                eventBinder.bindEvent(field, resId, obj, annoRegist, listeners);
            }
        }
    }

    /**
     * @param obj               may be it is current context, or it is a fragment of an activity, or an adapter
     * @param adapter           the object of adapter.
     * @param field             the field current scannnig.
     * @param cls               the impl class of your adapter
     * @param adapter_listeners additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     */
    private void setAdapter(Object obj, Object adapter, Field field, Class<?> cls, Object... adapter_listeners) {
        try {
            Constructor<?> constructor = null;
            int paramsLen = 0;
            Object target = null;
            if (!(field.get(obj) instanceof AdapterView))
                throw new VIRuntimeException(StringFormatter.format(NOT_ADAPTER_VIEW, field.toGenericString()));
            Constructor<?>[] declaredConstructors = cls.getDeclaredConstructors();
            for (Constructor<?> _constructor : declaredConstructors) {
                _constructor.setAccessible(true);
                Class<?>[] params = _constructor.getParameterTypes();
                if (params.length == 1) {
                    if (params[0] == obj.getClass()) {
                        constructor = _constructor;
                        paramsLen = 1;
                        break;
                    }
                } else if (params.length == 0) {
                    constructor = _constructor;
                    paramsLen = 0;
                    break;
                }
            }
            if (constructor == null)
                throw new InstantiationException(StringFormatter.format(INSTACE_ERROR, field.toString(), cls.getSimpleName()));
            if (paramsLen == 0)
                target = constructor.newInstance();
            else if (paramsLen == 1)
                target = constructor.newInstance(obj);
            AdapterView.class.cast(field.get(obj)).setAdapter(Adapter.class.cast(target));
        } catch (IllegalAccessException e) {
            //can't reach
            e.printStackTrace();
        } catch (InstantiationException e) {//represent no matching empty-constructor,attempt to find instance obj pass by user manually.
            boolean isFind = false;
            Adapter target = null;
            for (Object object : adapter_listeners) {
                if (object instanceof Adapter) {
                    target = Adapter.class.cast(object);
                    try {
                        AdapterView.class.cast(field.get(obj)).setAdapter(target);
                    } catch (IllegalAccessException e1) {
                        //can't reach
                        e1.printStackTrace();
                    }
                    isFind = true;
                    break;
                }
            }
            if (!isFind)
                throw new VIRuntimeException(StringFormatter.format(NO_MATCH_ADAPTER, field.getType().getName()));
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            //can't reach
            e.printStackTrace();
        }
    }
}
