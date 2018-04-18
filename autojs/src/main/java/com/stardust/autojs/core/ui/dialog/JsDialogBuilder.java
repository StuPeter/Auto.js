package com.stardust.autojs.core.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.afollestad.materialdialogs.Theme;
import com.stardust.autojs.core.eventloop.EventEmitter;
import com.stardust.autojs.runtime.ScriptBridges;
import com.stardust.util.ArrayUtils;
import com.stardust.util.UiHandler;

import java.text.NumberFormat;
import java.util.Collection;

/**
 * Created by Stardust on 2018/4/17.
 */

public class JsDialogBuilder extends MaterialDialog.Builder {

    private final EventEmitter mEmitter;
    private final UiHandler mUiHandler;
    private JsDialog mDialog;

    public JsDialogBuilder(@NonNull Context context, ScriptBridges bridges, UiHandler uiHandler) {
        super(context);
        mEmitter = new EventEmitter(bridges);
        this.mUiHandler = uiHandler;
        setUpEvents();
    }

    private void setUpEvents() {
        showListener(dialog -> emit("show", dialog));
        onAny((dialog, which) -> {
            switch (which) {
                case NEUTRAL:
                    emit("neutral", dialog);
                    emit("any", "neutral", dialog);
                    break;
                case NEGATIVE:
                    emit("negative", dialog);
                    emit("any", "negative", dialog);
                    break;
                case POSITIVE:
                    EditText editText = dialog.getInputEditText();
                    if (editText != null) {
                        emit("input", editText.getText().toString());
                    }
                    emit("positive", dialog);
                    emit("any", "positive", dialog);
                    break;
            }
        });
        dismissListener(dialog -> emit("dismiss", dialog));
        cancelListener(dialog -> emit("cancel", dialog));
    }

    public JsDialog getDialog() {
        return mDialog;
    }

    public JsDialog buildDialog() {
        mDialog = new JsDialog(super.build(), mEmitter, mUiHandler);
        return mDialog;
    }

    public JsDialogBuilder once(String eventName, Object listener) {
        mEmitter.once(eventName, listener);
        return this;
    }

    public JsDialogBuilder on(String eventName, Object listener) {
        mEmitter.on(eventName, listener);
        return this;
    }

    public JsDialogBuilder addListener(String eventName, Object listener) {
        mEmitter.addListener(eventName, listener);
        return this;
    }

    public boolean emit(String eventName, Object... args) {
        return mEmitter.emit(eventName, args);
    }

    public String[] eventNames() {
        return mEmitter.eventNames();
    }

    public int listenerCount(String eventName) {
        return mEmitter.listenerCount(eventName);
    }

    public Object[] listeners(String eventName) {
        return mEmitter.listeners(eventName);
    }

    public JsDialogBuilder prependListener(String eventName, Object listener) {
        mEmitter.prependListener(eventName, listener);
        return this;
    }

    public JsDialogBuilder prependOnceListener(String eventName, Object listener) {
        mEmitter.prependOnceListener(eventName, listener);
        return this;
    }

    public JsDialogBuilder removeAllListeners() {
        mEmitter.removeAllListeners();
        return this;
    }

    public JsDialogBuilder removeAllListeners(String eventName) {
        mEmitter.removeAllListeners(eventName);
        return this;
    }

    public JsDialogBuilder removeListener(String eventName, Object listener) {
        mEmitter.removeListener(eventName, listener);
        return this;
    }

    public JsDialogBuilder setMaxListeners(int n) {
        mEmitter.setMaxListeners(n);
        return this;
    }

    public int getMaxListeners() {
        return mEmitter.getMaxListeners();
    }

    public static int defaultMaxListeners() {
        return EventEmitter.defaultMaxListeners();
    }

}
