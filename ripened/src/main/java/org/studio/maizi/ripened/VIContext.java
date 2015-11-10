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

package org.studio.maizi.ripened;

import org.studio.maizi.ripened.exception.VIRuntimeException;
import org.studio.maizi.ripened.util.StringFormatter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * the ViewInjection's context
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-5.
 */
@SuppressWarnings("all")
public class VIContext {

    private static final String CLS_NOT_FOUND = "can not find class: %s, please check your code...";
    private static final String NO_EMPTY_CONS = "class: %s have no empty-param constructor, please check your code...";
    private static final String NONE_PUBLIC = "class: %s's empty-param constructor with non-public modifier, please check your code...";

    private static final Map<String, String> PLUGIN_CENTER = new HashMap<String, String>() {
        {
            this.put("Adapter", "org.studio.maizi.ripened.impl.AdapterSetter");
            this.put("Content", "org.studio.maizi.ripened.impl.ContentSetter");
            this.put("Event", "org.studio.maizi.ripened.impl.EventBinder");
            this.put("Animation", "org.studio.maizi.ripened.impl.AnimationSetter");
        }
    };

    private static final Map<String, String> ANNO_PLUGIN_MAPPING = new HashMap<String, String>() {
        {
            this.put("Adapter", "Adapter");
            this.put("ContentView", "Content");
            this.put("EventTarget", "Event");
            this.put("RegistListener", "Event");
            this.put("Anim", "Animation");
        }
    };

    /**
     * the container of IPlugin
     */
    private Set<IPlugin> plugins = new HashSet<>();

    /**
     * add plugin
     *
     * @param plugins the object of plugin.
     * @return current obj.
     */
    public VIContext addPlugin(IPlugin... plugins) {
        for (IPlugin plugin : plugins)
            this.plugins.add(plugin);
        return this;
    }

    /**
     * add plugin aware from annotation.
     *
     * @param classes the classes of annotation.
     * @return current obj.
     */
    public VIContext addPlugin(Annotation... annos) {
        for (Annotation anno : annos) {
            String name = anno.annotationType().getSimpleName();
            String pluginName;
            if ((pluginName = ANNO_PLUGIN_MAPPING.get(name)) != null) {
                String fullClsName = PLUGIN_CENTER.get(pluginName);
                try {
                    Class<?> clazz = Class.forName(fullClsName);
                    IPlugin plugin = IPlugin.class.cast(clazz.newInstance());
                    addPlugin(plugin);
                } catch (ClassNotFoundException e) {
                    throw new VIRuntimeException(StringFormatter.format(CLS_NOT_FOUND, fullClsName), e);
                } catch (InstantiationException e) {
                    throw new VIRuntimeException(StringFormatter.format(NO_EMPTY_CONS, fullClsName), e);
                } catch (IllegalAccessException e) {
                    throw new VIRuntimeException(StringFormatter.format(NONE_PUBLIC, fullClsName), e);
                }
            }
        }
        return this;
    }

    /**
     * remove a plugin from the current VIContext.
     *
     * @param plugin the object of plugin
     * @return
     */
    public boolean removePlugin(IPlugin plugin) {
        return plugins.remove(plugin);
    }

    /**
     * remove all the plugins from the current VIContext.
     *
     * @param plugin
     */
    public void removeAllPlugin(IPlugin plugin) {
        plugins.clear();
    }

    /**
     * get all the plugins from the current VIContext.
     *
     * @return
     */
    public Set<IPlugin> getPlugins() {
        return plugins;
    }

    /**
     * get the plugin of IContent if there we have.
     *
     * @return
     */
    public IPlugin getContentPlugin() {
        for (IPlugin plugin : plugins) {
            if (plugin.getName().equals("Content"))
                return plugin;
        }
        return null;
    }
}
