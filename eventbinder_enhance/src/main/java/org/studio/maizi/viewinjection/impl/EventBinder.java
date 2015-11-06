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

import org.studio.maizi.viewinjection.IEventBinder;
import org.studio.maizi.viewinjection.anno.EventTarget;
import org.studio.maizi.viewinjection.anno.RegistListener;
import org.studio.maizi.viewinjection.dto.ActionParams;
import org.studio.maizi.viewinjection.exception.VIRuntimeException;
import org.studio.maizi.viewinjection.util.SimpleIntrospect;
import org.studio.maizi.viewinjection.util.StringFormatter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * an implementation of IEventBinder.<br />
 * attention: see--->{@link SimpleIntrospect}<br />
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-3.
 */
@SuppressWarnings("all")
public class EventBinder implements IEventBinder {

    private static final String NO_MATCH_LISTENER = "%s have no matching listener on class %s";
    private static final String INSTACE_ERROR = "Field : %s's RegistListener annotation param %s.class have no empty-params constructor or it is a illegal parameter, please check your code...";
    private static final String NO_MATCH_SET_METHOD = "class : %s have no matching 'set-method' either setXxx or addYyy, read comment in class->org.studio.maizi.viewinjection.util.SimpleIntrospect for detail...";

    /**
     * mark your listener's class match current view or not, if you pass the wrong class, we'll throw runtime-exception to prompt you
     */
    private boolean isFind;

    /**
     * the caching of mapping resId with all the interface it's listener implements on the view this resId represent
     */
    private Map<Integer, List<Class<?>>> ID_INTERFACES_MAPPING = new HashMap<>();
    /**
     * the caching of mapping all the listener object pass from user manually with it's class object
     */
    private Map<Class<?>, Object> CLASS_OBJECT_MAPPING = new HashMap<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void action(ActionParams params) {
        RegistListener annoRegist = null;
        if (params.getResId() != 0 && (annoRegist = params.getField().getAnnotation(RegistListener.class)) != null /*&& eventBinder != null*/) {
            bindEvent(params.getField(), params.getResId(), params.getObj(), annoRegist, params.getListeners());
        }
    }

    @Override
    public void bindEvent(Field field, int resId, Object obj, RegistListener anno, Object... listeners) {
        if (field == null) return;
        scanParams(listeners);//scan the listeners.
        Class<?>[] clazzs = anno.listeners();
        for (Class<?> type : clazzs) {
            isFind = false;
            Class<?> impl = type;
            do {
                Class<?>[] intrs = type.getInterfaces();
                scanIntr(field, resId, obj, impl, type, intrs, listeners);
            } while ((type = type.getSuperclass()) != null);
            if (!isFind)
                throw new VIRuntimeException(StringFormatter.format(NO_MATCH_LISTENER, field.getName(), impl.getSimpleName()));
        }
    }

    /**
     * scan all the object pass from user manually.
     *
     * @param listeners additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     */
    private void scanParams(Object... listeners) {
        for (Object obj : listeners) {
            CLASS_OBJECT_MAPPING.put(obj.getClass(), obj);
        }
    }

    /**
     * scanning each interface.
     *
     * @param field      the field which is an instance of view current scanning.
     * @param resId      the resId of this view.
     * @param obj        may be it is current context, or it is a fragment of an activity.
     * @param impl       the type which add by user in annotation @RegistListener
     * @param type       the current superClass type of type 'impl'
     * @param interfaces the interfaces which 'type' have implemented
     * @param listeners  additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     */
    private void scanIntr(Field field, int resId, Object obj, Class<?> impl, Class<?> type, Class<?>[] interfaces, Object... listeners) {
        for (Class<?> cls : interfaces) {
            if (ID_INTERFACES_MAPPING.get(resId) == null || (ID_INTERFACES_MAPPING.get(resId) != null && !ID_INTERFACES_MAPPING.get(resId).contains(cls))) {//represent it have been scanned...
                Method[] methods = cls.getMethods();
                for (Method method : methods) {
                    try {
                        Method _method = impl.getMethod(method.getName(), method.getParameterTypes());
                        _method.setAccessible(true);
                        EventTarget _anno = _method.getAnnotation(EventTarget.class);
                        if (_anno != null) {
                            int[] targets = _anno.targets();
                            for (int _resId : targets) {
                                List<Class<?>> list = null;
                                if ((list = ID_INTERFACES_MAPPING.get(_resId)) == null)
                                    list = new LinkedList<>();
                                list.add(cls);
                                ID_INTERFACES_MAPPING.put(_resId, list);
                            }
                            if (ID_INTERFACES_MAPPING.containsKey(resId)) {
                                isFind = true;
                                bind(cls, field, resId, obj, impl, type, listeners);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        //can't reach.
                        e.printStackTrace();
                    }
                }
            } else {
                isFind = true;
                bind(cls, field, resId, obj, impl, type, listeners);
            }
        }
    }

    /**
     * bind the listener with the matching-case.
     *
     * @param cls       the interface which current scanning.
     * @param field     the field which is an instance of view current scanning.
     * @param resId     the resId of this view.
     * @param obj       may be it is current context, or it is a fragment of an activity.
     * @param impl      the type which add by user in annotation @RegistListener
     * @param type      the current superClass type of type 'impl'
     * @param listeners additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     */
    private void bind(Class<?> cls, Field field, int resId, Object obj, Class<?> impl, Class<?> type, Object... listeners) {
        Object object = null;
        if (type == obj.getClass()) {
            //attempt to inject with the obj of current context or fragment.
            inject(cls, field, obj, null);
            return;
        } else if ((object = CLASS_OBJECT_MAPPING.get(type)) != null) {
            //attempt to inject with the obj which have been pass by user manually.
            inject(cls, field, object, obj);
            return;
        } else {
            //attempt to inject with automatic object creating.
            inject(cls, field, impl, obj);
        }
    }

    /**
     * inject with the current object of activity of fragment automatic or the object which have been passed by user manually.
     *
     * @param cls   the interface which current scanning.
     * @param field the field which is an instance of view current scanning.
     * @param impl  the type which add by user in annotation @RegistListener
     * @param obj   may be it is current context, or it is a fragment of an activity.
     * @return inject successful or not.
     */
    private boolean inject(Class<?> cls, Field field, Object impl, Object obj) {
        Constructor<?> constructor = null;
        int paramsLen = 0;
        if (impl instanceof Class) {
            Class<?> clazz = Class.class.cast(impl);
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
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
                throw new VIRuntimeException(StringFormatter.format(INSTACE_ERROR, field.toString(), clazz.getSimpleName()));
            try {
                if (paramsLen == 0)
                    impl = constructor.newInstance();
                else if (paramsLen == 1)
                    impl = constructor.newInstance(obj);
            } catch (InstantiationException e) {
                //can't reach
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                //can't reach
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                //can't reach
                e.printStackTrace();
            }
        }
        if (attemptSetInject(cls, field, impl, constructor == null && obj == null ? null : obj))
            return true;
        else if (attemptAddInject(cls, field, impl, constructor == null && obj == null ? null : obj))
            return true;
        else
            throw new VIRuntimeException(StringFormatter.format(NO_MATCH_SET_METHOD, field.getType().getName()));
    }

    /**
     * attempt to use set-method 'setXxx' to inject listener.
     *
     * @param cls   the interface which current scanning.
     * @param field the field which is an instance of view current scanning.
     * @param impl  the type which add by user in annotation @RegistListener
     * @param obj   may be it is current context, or it is a fragment of an activity, or null.
     * @return inject successful or not.
     */
    private boolean attemptSetInject(Class<?> cls, Field field, Object impl, Object obj) {
        //attempt set method injection
        String methodName = SimpleIntrospect.getSetMethodName(cls);
        return invoke(cls, field, impl, obj, methodName);
    }

    /**
     * attempt to use set-method 'addYyy' to inject listener.
     *
     * @param cls   the interface which current scanning.
     * @param field the field which is an instance of view current scanning.
     * @param impl  the type which add by user in annotation @RegistListener
     * @param obj   may be it is current context, or it is a fragment of an activity, or null.
     * @return inject successful or not.
     */
    private boolean attemptAddInject(Class<?> cls, Field field, Object impl, Object obj) {
        String methodName = SimpleIntrospect.getAddMethodName(cls);
        return invoke(cls, field, impl, obj, methodName);
    }

    /**
     * real action of injection.
     *
     * @param cls        the interface which current scanning.
     * @param field      the field which is an instance of view current scanning.
     * @param impl       the type which add by user in annotation @RegistListener
     * @param obj        may be it is current context, or it is a fragment of an activity, or null.
     * @param methodName inject successful or not.
     * @return inject successful or not.
     */
    private boolean invoke(Class<?> cls, Field field, Object impl, Object obj, String methodName) {
        Method method = null;
        try {
            method = field.getType().getMethod(methodName, cls);
            method.setAccessible(true);
            if (obj == null)
                method.invoke(field.get(impl), impl);
            else
                method.invoke(field.get(obj), impl);
            return true;
        } catch (NoSuchMethodException e) {
            //can't reach...
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }
}
