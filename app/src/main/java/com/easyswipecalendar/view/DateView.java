package com.easyswipecalendar.view;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

import com.easyswipecalendar.R;

/**
 * Created by sreedhu on 2/2/16.
 */
public class DateView extends CheckedTextView {

    public static final float NORMAL = 0;
    private float mLetterSpacing = NORMAL;
    private CharSequence mOriginalText = "";

    public DateView(Context context) {
        super(context);
    }

    public DateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        parseAttributes(context, attrs);
    }

    public DateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseAttributes(context, attrs);
    }

    /**
     * Parse the attributes.
     *
     * @param context The Context the widget is running in, through which it can access the current theme, resources,
     *                etc.
     * @param attrs   The attributes of the XML tag that is inflating the widget.
     */
    private void parseAttributes(Context context, AttributeSet attrs) {
        // Typeface.createFromAsset doesn't work in the layout editor, so skipping.
        if (isInEditMode()) {
            return;
        }

        mOriginalText = getText();
    }

    private void applyLetterSpacing() {
        if (getLetterSpacing() == NORMAL) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        int length = mOriginalText.length();
        for (int i = 0; i < length; i++) {
            builder.append(mOriginalText.charAt(i));
            if (i + 1 < length) {
                builder.append("\u00A0");
            }
        }
        String s = builder.toString();
        SpannableString finalText = new SpannableString(s);
        if (s.length() > 1) {
            for (int i = 1; i < s.length(); i += 2) {
                finalText.setSpan(new ScaleXSpan((getLetterSpacing() + 1) / 10), i, i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(finalText, BufferType.SPANNABLE);
    }

    public float getLetterSpacing() {
        return mLetterSpacing;
    }

    public void setLetterSpacing(float letterSpacing) {
        mLetterSpacing = letterSpacing;
        applyLetterSpacing();
    }

    /**
     * Measuring the textview to draw circle for selected dates,
     * Since textview is wrap_content the bg wont be proper square so we are
     * comparing height and width if height is greater than width the we are setting that too width
     * and vice versa
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthDesc = MeasureSpec.getMode(widthMeasureSpec);
        int heightDesc = MeasureSpec.getMode(heightMeasureSpec);
        int size = 0;
        if (widthDesc == MeasureSpec.UNSPECIFIED
                && heightDesc == MeasureSpec.UNSPECIFIED) {
            size = getContext().getResources().getDimensionPixelSize(R.dimen.text_padding_dense);
        } else if ((widthDesc == MeasureSpec.UNSPECIFIED || heightDesc == MeasureSpec.UNSPECIFIED)
                && !(widthDesc == MeasureSpec.UNSPECIFIED && heightDesc == MeasureSpec.UNSPECIFIED)) {
            size = width > height ? width : height;
        } else {
            size = width > height ? height : width;
        }
        setMeasuredDimension(size, size);
    }
}
