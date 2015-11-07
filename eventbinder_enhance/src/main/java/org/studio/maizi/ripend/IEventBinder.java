/*
 * $HomePage: https://github.com/maizi0122/ $
 * $Revision: 000001 $
 * $Date: 2015-10-18 09:05:31 -0000 (Sun, 18 Oct 2015) $
 *
 * ====================================================================
 * Copyright (C) 2011 The Maizi-Studio Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This project powered by Maizi-Studio, but works with the
 * license of apache,so you should abide by this license.
 * Any question contacting with email below:
 * maizi0122@gmail.com
 */

package org.studio.maizi.ripend;

import org.studio.maizi.ripend.anno.RegistListener;

import java.lang.reflect.Field;

/**
 * the plugin of event binding.<br />
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-3.
 */
public interface IEventBinder extends IPlugin {

    /**
     * the name of this plugin.
     */
    String NAME = "Event";

    /**
     * auto listener binding.<br />
     * if you setup this object, we can help you bind the listener smartly.<br />
     * In fact, we are not force you to use auto-event binding, you can bind the listener all by yourself.<br />
     *
     * @param field      the field which is an instance of view current scanning.
     * @param resId      the resId of this view.
     * @param obj        may be it is current context, or it is a fragment of an activity.
     * @param annoRegist the object of RegistListener annotation.
     * @param objs       additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     */
    void bindEvent(Field field, int resId, Object obj, RegistListener annoRegist, Object... objs);
}
