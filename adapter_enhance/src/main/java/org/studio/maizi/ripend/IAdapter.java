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

import java.lang.reflect.Field;

/**
 * set plugin used for adapter setting.<br />
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-5.
 */
public interface IAdapter extends IPlugin {

    /**
     * the name of this plugin.
     */
    String NAME = "Adapter";

    /**
     * set the adapter
     *
     * @param obj               may be an instance of activity|fragment|hodler
     * @param adapter           the adapter
     * @param field             current field scanning
     * @param cls               the class of current obj,in case current runtime.
     * @param adapter_listeners additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     */
    void setAdapter(Object obj, Object adapter, Field field, Class<?> cls, Object... adapter_listeners);
}
