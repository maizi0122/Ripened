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

package org.studio.maizi.viewinjection.dto;

import java.lang.reflect.Field;

/**
 * the object which used to transfer data.
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-5.
 */
public class ActionParams {
    /**
     * an instance of activity or fragment or holder of an adapter.
     */
    private Object obj;
    /**
     * when the init param start with an adapter,this object is adapter for transfer.
     */
    private Object transfer;
    /**
     * the field current scanning.
     */
    private Field field;
    /**
     * the ResId's value of which field current scanning with the annotation of ResId
     */
    private int resId;
    /**
     * the holder of an adapter.
     */
    private Object holder;
    /**
     * all the listeners.
     */
    private Object[] listeners;

    public ActionParams(Object obj, Object transfer, Field field, int resId, Object holder, Object[] listeners) {
        this.obj = obj;
        this.transfer = transfer;
        this.field = field;
        this.resId = resId;
        this.holder = holder;
        this.listeners = listeners;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getTransfer() {
        return transfer;
    }

    public void setTransfer(Object transfer) {
        this.transfer = transfer;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public Object getHolder() {
        return holder;
    }

    public void setHolder(Object holder) {
        this.holder = holder;
    }

    public Object[] getListeners() {
        return listeners;
    }

    public void setListeners(Object[] listeners) {
        this.listeners = listeners;
    }
}
