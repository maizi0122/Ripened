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

package org.studio.maizi.ripened.impl;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.Adapter;

import org.studio.maizi.ripened.IPlugin;
import org.studio.maizi.ripened.IRipened;
import org.studio.maizi.ripened.VIContext;
import org.studio.maizi.ripened.anno.ResId;
import org.studio.maizi.ripened.dto.ActionParams;
import org.studio.maizi.ripened.exception.VIRuntimeException;
import org.studio.maizi.ripened.util.StringFormatter;
import org.studio.maizi.ripened.util.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * an implementation of IViewInjection.<br />
 * Created by maizi on 13-10-8.<br />
 * <p>auto view injection
 */
@SuppressWarnings("all")
public class Ripened implements IRipened {

    private static final String NULL_CTX = "param context can not be null, please check your code...";
    private static final String NULL_FRAG_OR_ROOT = "params fragment or root can not be null, please check your code...";
    private static final String NULL_ADAPTER_OR_ROOT_OR_HOLDER = "params adapter or root or holder can not be null, please check your code...";
    private static final String ILLEGAL_ID = "can not find the view by id: %s annotation(ResId):#0x %s .please check your code...";
    private static final String ILLEGAL_FIELD = "Field: %s was not a subclass of view,please check your code...";
    private static final String CAST_EXCEPTION = "Field: %s : %s";

    private VIContext viContext;

    public Ripened() {
    }

    public Ripened(VIContext viContext) {
        this.viContext = viContext;
    }

    @Override
    public IRipened setVIContext(VIContext viContext) {
        this.viContext = viContext;
        return this;
    }

    @Override
    public VIContext getVIContext() {
        return viContext;
    }

    @Override
    public IRipened inject(Activity context, Object... listeners) {
        if (context == null)
            throw new RuntimeException(NULL_CTX);
        Class<? extends Activity> clazz = context.getClass();
        analysis(context, null, clazz, null, listeners);
        return this;
    }

    @Override
    public IRipened inject(Fragment fragment, View root, Object... listeners) {
        if (fragment == null || root == null)
            throw new RuntimeException(NULL_FRAG_OR_ROOT);
        Class<? extends Fragment> clazz = fragment.getClass();
        analysis(fragment, root, clazz, null, listeners);
        return this;
    }

    @Override
    public IRipened inject(Adapter adapter, View root, Object holder, Object... listeners) {
        if (adapter == null || root == null || holder == null)
            throw new RuntimeException(NULL_ADAPTER_OR_ROOT_OR_HOLDER);
        Class<?> clazz = holder.getClass();
        analysis(adapter, root, clazz, holder/*, adapter*/);
        return this;
    }

    /**
     * analysis the object of activity of fragment<br />
     * if you setup the IEventBinder, we can also help you bind the listener smartly.<br />
     * In fact, we are not force you to use auto-event binding, you can bind the listener all by yourself.<br />
     *
     * @param obj       may be an instance of activity|fragment|hodler
     * @param root      the root view of current item.
     * @param clazz     the class of current obj,in case current runtime.
     * @param listeners additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     */
    private void analysis(Object obj, View root, Class<?> clazz, Object holder, Object... listeners) {
        Type type = null;
        Activity context = null;
        if (obj instanceof Activity) {
            context = Activity.class.cast(obj);
            type = Type.Activity;
            analysisPlugin(clazz);
        } else if (obj instanceof Fragment) {
            type = Type.Fragment;
        } else if (obj instanceof Adapter) {
            type = Type.Adapter;
        }
        IPlugin contentView = null;
        if (viContext != null && (contentView = viContext.getContentPlugin()) != null)
            contentView.action(new ActionParams(obj, null, null, 0, null, null));
        Field[] declaredFields = clazz.getDeclaredFields();
        if (type.equals(Type.Adapter))
            declaredFields = holder.getClass().getDeclaredFields();
        Object transfer = null;
        for (Field field : declaredFields) {
            field.setAccessible(true);
            analysisPlugin(field);
            ResId anno = field.getAnnotation(ResId.class);
            int resId = 0;
            if (type.equals(Type.Adapter)) {
                if (transfer == null)
                    transfer = obj;
            }
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

                    if (type.equals(Type.Adapter))
                        obj = holder;
                    field.set(obj, field.getType().cast(viewById));
                } catch (RuntimeException e) {
                    throw new VIRuntimeException(StringFormatter.format(CAST_EXCEPTION, field.toString(), e.getMessage()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (viContext != null)
                for (IPlugin plugin : viContext.getPlugins()) {
                    if (!(plugin.getName().equals("Content")))
                        plugin.action(new ActionParams(obj, transfer, field, resId, holder, listeners));
                }
        }
    }

    /**
     * analysis the plugin we need.
     *
     * @param obj the object of Class|Field
     */
    private void analysisPlugin(Object obj) {
        Annotation[] annos = null;
        if (obj instanceof Class)
            annos = Class.class.cast(obj).getDeclaredAnnotations();
        else if (obj instanceof Field)
            annos = Field.class.cast(obj).getDeclaredAnnotations();
        int len = 0;
        if (annos != null && (len = annos.length) != 0) {
            if (viContext == null)
                viContext = new VIContext();
            viContext.addPlugin(annos);
        }
    }
}
