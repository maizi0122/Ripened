package org.studio.maizi.rapefield;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.Adapter;

import org.studio.maizi.rapefield.impl.RapeField;

/**
 * Created by maizi on 13-10-8.<br />
 * the interface of auto view injection
 */
public interface IRapeField {

    /**
     * set the context of viewInjection
     *
     * @param viContext the context of viewInjection
     * @return the current obj
     */
    RapeField setVIContext(VIContext viContext);

    /**
     * obtain the context of RapeField.
     * @return the context of RapeField.
     */
    VIContext getVIContext();

    /**
     * init views automatic in an activity.
     *
     * @param context  current context which relate to an instance of window
     * @param listener additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     * @return the current obj
     */
    IRapeField inject(Activity context, Object... listener);

    /**
     * init views automatic in a fragment.
     *
     * @param fragment the fragment object.
     * @param root     the root view in your fragment.
     * @param listener additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     * @return the current obj
     */
    IRapeField inject(Fragment fragment, View root, Object... listener);

    /**
     * init views automatic in a adapter.
     *
     * @param adapter   this impl class of adapter.
     * @param root      the root view of current item.
     * @param holder    the holder of adapter
     * @param listeners additional params, when your class of listener have no empty-parameter constructor, you should pass the listener object manually...
     * @return the current obj
     */
    IRapeField inject(Adapter adapter, View root, Object holder, Object... listeners);
}