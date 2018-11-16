package common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Ashish.Kumar on 23-05-2018.
 */

public class Bold_TextView extends android.support.v7.widget.AppCompatTextView {

    public Bold_TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public Bold_TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Bold_TextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "bold.ttf");
        setTypeface(tf);
    }
}
