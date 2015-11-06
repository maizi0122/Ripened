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

package org.studio.maizi.rapefield.util;

import org.studio.maizi.rapefield.exception.VIRuntimeException;

/**
 * if you abide by the rule of naming the set-method for one field of your class, which follows with the rule of android-source project.<br />
 * when your field's type is an interface which have only one method, your set-method should be named like setXxxYyy<br />
 * when your field's type is an interface which have more than one method, your set-method should be named like addXxxYyy<br />
 * <pre>such as:
 * public class A{
 *     private Xxx abc;
 *
 *     public void setAbc(Xxx abc){
 *         this.abc = abc;
 *     }
 *     //the interface which has only one function(),it's "set-method" start with "set"
 *     public interface Xxx{
 *         return-type fun(params...);
 *     }
 *
 *     private Yyy def;
 *
 *     public void addDef(Yyy def){
 *         this.def = def;
 *     }
 *     //the interface which has more than one function(),it's "set-method" start with "add"
 *     public interface Yyy{
 *         return-type fun1(params...);
 *         return-type fun2(params...);
 *         .
 *         .
 *     }
 * }</pre>
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-3.
 */
@SuppressWarnings("all")
public class SimpleIntrospect {

    private static final String SET_PREFIX = "set";
    private static final String ADD_PREFIX = "add";
    private static final String NULL_CONTEXT = "params cls can not be null...";

    /**
     * get the set method's name of this property.<br />
     * runtime of this module,it will get the set method's name of an interface which has only one method.
     *
     * @param cls the class object.
     * @return the method name of "setXxx"
     */
    public static String getSetMethodName(Class<?> cls) {
        return getMethodName(cls, SET_PREFIX);
    }

    /**
     * get the add method's name of this property.<br />
     * runtime of this module,it will get the set method's name of an interface which has more than one method.
     *
     * @param cls the class object.
     * @return the method name of "addYyy"
     */
    public static String getAddMethodName(Class<?> cls) {
        return getMethodName(cls, ADD_PREFIX);
    }

    /**
     * @param cls    the class object.
     * @param prefix the prefix of 'set-method'
     * @return the method name of 'set-method'
     */
    private static String getMethodName(Class<?> cls, String prefix) {
        if (cls == null)
            throw new VIRuntimeException(NULL_CONTEXT);
        String propertyName = cls.getSimpleName();
        char[] suffix = propertyName.toCharArray();
        if (suffix[0] >= 97 & suffix[0] < 122) {
            suffix[0] = Character.toUpperCase(suffix[0]);
        }
        return new StringBuilder(prefix).append(suffix).toString();
    }
}
