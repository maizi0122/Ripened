package org.studio.maizi.viewinjection;

import android.app.Activity;

/**
 * Created by maizi on 13-10-8.<br />
 * <p>the interface of auto view injection
 */
public interface IViewInjection {
    /**
     * init views automatic
     * @param context your activity context which relate to an instance of window
     */
    void initView(Activity context);
}
