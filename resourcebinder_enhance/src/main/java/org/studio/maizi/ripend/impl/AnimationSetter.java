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

package org.studio.maizi.ripend.impl;

import android.content.res.Resources;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import org.studio.maizi.ripend.IAnimation;
import org.studio.maizi.ripend.anno.Anim;
import org.studio.maizi.ripend.dto.ActionParams;
import org.studio.maizi.ripend.exception.VIRuntimeException;
import org.studio.maizi.ripend.util.StringFormatter;

import java.lang.reflect.Field;

/**
 * an implementation of IAnimation.<br />
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-6.
 */
public class AnimationSetter implements IAnimation {

    private static final String NOT_VIEW_INSTANCE = "Field: %s is not an instance of View, please check your code...";
    private static final String WRONG_RES_ID = "Field: %s's annotation of Anim with the wrong ResId value: %d, please check your code...";
    private static final String WRONG_ANIM_DURATION = "Field: %s's annotation of Anim with the wrong duration value: %d, it should be >0 & <3000, please check your code...";
    private static final String WRONG_ANIM_START_OFFSET = "Field: %s's annotation of Anim with the wrong startOffset value: %d, it should be >0 & <3000, please check your code...";
    private static final String WRONG_ANIM_REPEAT_COUNT = "Field: %s's annotation of Anim with the wrong repeatCount value: %d, it should be >0, please check your code...";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void action(ActionParams params) {
        Anim anno;
        if ((anno = params.getField().getAnnotation(Anim.class)) != null) {
            View view = null;
            try {
                view = View.class.cast(params.getField().get(params.getObj()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                throw new VIRuntimeException(StringFormatter.format(NOT_VIEW_INSTANCE, params.getField().toGenericString()), e);
            }
            setAnimation(view, anno.animResId(), anno.duration(), anno.startOffset(), anno.interpolator(), anno.repeatCount(), anno.fillAfter(), params.getField());
        }
    }

    @Override
    public void setAnimation(View view, int animResId, int duration, int startOffset, int interpolator, int repeatCount, boolean fillAfter, Field field) {
        //check all Anim annotation params.
        int tempResId = 0;
        Resources resources = view.getContext().getResources();
        try {
            if ((tempResId = animResId) <= 0 || !resources.getResourceTypeName(animResId).equals(RES_ANIM_NAME))
                throw new VIRuntimeException(StringFormatter.format(WRONG_RES_ID, field.toString(), tempResId));
            if ((tempResId = interpolator) <= 0 || !resources.getResourceTypeName(interpolator).equals(RES_INTERPOLATOR_NAME))
                throw new VIRuntimeException(StringFormatter.format(WRONG_RES_ID, field.toString(), tempResId));
        } catch (Resources.NotFoundException e) {
            throw new VIRuntimeException(StringFormatter.format(WRONG_RES_ID, field.toString(), tempResId));
        }
        if (duration < 0)
            throw new VIRuntimeException(StringFormatter.format(WRONG_ANIM_DURATION, field.toGenericString(), duration));
        if (startOffset < 0)
            throw new VIRuntimeException(StringFormatter.format(WRONG_ANIM_START_OFFSET, field.toGenericString(), startOffset));
        if (repeatCount < 0)
            throw new VIRuntimeException(StringFormatter.format(WRONG_ANIM_REPEAT_COUNT, field.toGenericString(), repeatCount));
        try {
            duration = resources.getInteger(duration);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        Animation anim = AnimationUtils.loadAnimation(view.getContext(), animResId);
        anim.setDuration(duration);
        anim.setStartOffset(startOffset);
        anim.setInterpolator(view.getContext(), interpolator);
        anim.setRepeatCount(repeatCount);
        anim.setFillAfter(fillAfter);
        view.setAnimation(anim);
        anim.start();
    }

}
