package com.xteam.sample.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;

/**
 * Created by Amit on 10/23/15.
 */

/*
CustomSearchView is overriding the widget.SearchView
 */
public class CustomSearchView extends android.support.v7.widget.SearchView {

    private AutoCompleteTextView mSearchAutoComplete;
    private Context mContext;

    public CustomSearchView(Context context) {
        super(context);
        this.mContext = context;
        initialize();
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void initialize() {
        mSearchAutoComplete = (AutoCompleteTextView) findViewById(android.support.v7.appcompat.R.id.search_src_text);

        if (mSearchAutoComplete == null) {
            return;
        }
        mSearchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchAutoComplete.removeTextChangedListener(this);
                setSpans(s, Color.RED);
                mSearchAutoComplete.addTextChangedListener(this);
            }
        });

    }

    private void setSpans(Editable s, @ColorInt int backgroundColor) {

        BackgroundColorSpan[] spans = s.getSpans(0, s.length(), BackgroundColorSpan.class);
        String[] words;
        if (s.toString().endsWith(" ")) {
            words = (s.toString() + "X").split("\\s");
        } else {
            words = s.toString().split("\\s");
        }
        int completedWordsCount = words.length - 1;
        if (spans.length != completedWordsCount) {
            for (BackgroundColorSpan span : spans) {
                s.removeSpan(span);
            }

            int currentIndex = 0;
            for (int i = 0; i < words.length-1; i++) {
                s.setSpan(new CustomDrawble(Color.GRAY, Color.WHITE), currentIndex, currentIndex +
                        words[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                currentIndex += words[i].length() + 1;
            }
        }
    }

}
