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

package org.studio.maizi.rapefield;

import android.view.View;

import java.lang.reflect.Field;

/**
 * the plugin of animation setting.
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-6.
 */
public interface IAnimation extends IPlugin {

    String NAME = "Animation";
    String RES_ANIM_NAME = "anim";
    String RES_INTERPOLATOR_NAME = "interpolator";

    int a = 10;

    /**
     * set the animation to the specify view object.
     *
     * @param view         the object of view which need animation setting
     * @param animResId    the resource id of animation
     * @param duration     the duration of animation
     * @param startOffset  the start offset of the animation
     * @param interpolator the interpolator of the animation
     * @param repeatCount  the repeatCount of the animation
     * @param fillAfter    is fillAfter or not
     */
    void setAnimation(View view, int animResId, int duration, int startOffset, int interpolator, int repeatCount, boolean fillAfter, Field field);
}
