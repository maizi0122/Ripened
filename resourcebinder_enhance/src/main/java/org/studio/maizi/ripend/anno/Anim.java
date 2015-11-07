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

package org.studio.maizi.ripend.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mark the animation configuration of this view field.<br />
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-6.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Anim {

    /**
     * the resId of this animation.
     *
     * @return ...
     */
    int animResId() default android.R.anim.slide_in_left;

    /**
     * the duration of this animation
     *
     * @return ...
     */
    int duration() default 250;

    /**
     * the start offset of this animation
     *
     * @return ...
     */
    int startOffset() default 0;

    /**
     * the interpolator of this animation
     *
     * @return ...
     */
    int interpolator() default android.R.interpolator.linear;

    /**
     * the repeat count of this animation
     *
     * @return ...
     */
    int repeatCount() default 0;

    /**
     * is fillAfter or not.
     *
     * @return ...
     */
    boolean fillAfter() default false;

}
