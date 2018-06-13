package mq.com.chuohapps.utils.views;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by nguyen.dang.tho on 9/20/2017.
 */

public final class WidgetsUtils {
    private WidgetsUtils() {
    }

    public static void setTextUnderLine(TextView textView, String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textView.setText(content);
    }

    public static void setCLickText(TextView textView, String notClick, String click, final View.OnClickListener onClickListener) {
        String all = notClick + " " + click;
        SpannableString spannableString = new SpannableString(all);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                onClickListener.onClick(textView);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        spannableString.setSpan(clickableSpan, notClick.length() + 1, all.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }

    private static List<FuriganaKey> getListRenderFurigana(List<FuriganaKey> keyList, String rawText) {
        ArrayList<FuriganaKey> result = new ArrayList<>();
        ArrayList<FuriganaKey> tempHiraList = new ArrayList<>(keyList);
        if (keyList.size() == 0) {
            result.add(new FuriganaKey(rawText, ""));
            return result;
        }
        if (rawText.equals(tempHiraList.get(0).key)) {
            result.add(tempHiraList.get(0));
            return result;
        }

        if (rawText.contains(tempHiraList.get(0).key)) {
            String[] list = rawText.split(tempHiraList.get(0).key);
            String hiraText = tempHiraList.get(0).key;
            String hiraTextValue = tempHiraList.get(0).value;
            tempHiraList.remove(0);


            if (list.length > 1) {
                if (!list[0].equals("")) {
                    if (tempHiraList.size() != 0)
                        result.addAll(getListRenderFurigana(tempHiraList, list[0]));
                    else result.add(new FuriganaKey(list[0], ""));
                }
                result.add(new FuriganaKey(hiraText, hiraTextValue));
                for (int i = 1; i < list.length - 1; i++) {
                    if (tempHiraList.size() != 0)
                        result.addAll(getListRenderFurigana(tempHiraList, list[i]));
                    else result.add(new FuriganaKey(list[i], ""));
                    result.add(new FuriganaKey(hiraText, hiraTextValue));
                }
                if (!list[list.length - 1].equals("")) {
                    if (tempHiraList.size() != 0)
                        result.addAll(getListRenderFurigana(tempHiraList, list[list.length - 1]));
                    else result.add(new FuriganaKey(list[list.length - 1], ""));
                }

            } else if (list.length == 1) {
                int pos = rawText.indexOf(hiraText);
                if (pos == 0) {
                    result.add(new FuriganaKey(hiraText, hiraTextValue));
                    if (!list[0].equals("")) {
                        if (tempHiraList.size() != 0)
                            result.addAll(getListRenderFurigana(tempHiraList, list[0]));
                        else result.add(new FuriganaKey(list[0], ""));
                    }
                } else {
                    if (!list[0].equals("")) {
                        if (tempHiraList.size() != 0)
                            result.addAll(getListRenderFurigana(tempHiraList, list[0]));
                        else result.add(new FuriganaKey(list[0], ""));
                        result.add(new FuriganaKey(hiraText, hiraTextValue));
                    }
                }
            }
            if (result.size() == 0) {
                result.add(new FuriganaKey(rawText, ""));
            }
        } else {
            tempHiraList.remove(0);
            return getListRenderFurigana(tempHiraList, rawText);
        }
        return result;
    }

    public static String makeTextForFuriganaView(List<FuriganaKey> keyList, String rawText) {
        StringBuilder furiganaBuilder = new StringBuilder();
        List<FuriganaKey> furiganaKeys = getListRenderFurigana(keyList, rawText);
        for (FuriganaKey furiganaKey : furiganaKeys) {
            if (furiganaKey.value.length() == 0) {
                furiganaBuilder.append(furiganaKey.key);
            } else {
                furiganaBuilder.append("{").append(furiganaKey.key).append(";").append(furiganaKey.value).append("}");
            }
        }
        return furiganaBuilder.toString();
    }

    public static class FuriganaKey {
        public String key;
        public String value;
        public FuriganaKey(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
