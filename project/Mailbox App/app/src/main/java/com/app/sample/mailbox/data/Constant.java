package com.app.sample.mailbox.data;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.app.sample.mailbox.R;
import com.app.sample.mailbox.model.Mail;
import com.app.sample.mailbox.model.People;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResourceType")
public class Constant {

    public static Drawable getFilterDrawable(Context ctx, int resId) {
        Resources res = ctx.getResources();
        Drawable icon = res.getDrawable(resId);
        icon.setColorFilter(res.getColor(R.color.icon_drawer_color), PorterDuff.Mode.SRC_IN);
        return icon;
    }
    public static List<People> getPeopleData(Context ctx)  {
        List<People> items = new ArrayList<>();
        String s_arr[] = ctx.getResources().getStringArray(R.array.people_names);
        String s_mail[] = ctx.getResources().getStringArray(R.array.people_mail);
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_photos);
        for (int i = 0; i < s_arr.length; i++) {
            People fr = new People(i, s_arr[i], s_mail[i], drw_arr.getResourceId(i, -1));
            items.add(fr);
        }
        return items;
    }

    public static List<Mail> getInboxData(Context ctx)  {
        List<Mail> items = new ArrayList<>();
        List<People> peoples = getPeopleData(ctx);
        People me = new People(0, ctx.getString(R.string.me_name), ctx.getString(R.string.me_mail), R.drawable.profile);
        String s_date[] = ctx.getResources().getStringArray(R.array.mail_date);
        String str_sub[] = ctx.getResources().getStringArray(R.array.subject_mail);
        String str_content = ctx.getString(R.string.long_lorem_ipsum);
        for (int i = 0; i < 10; i++) {
            Mail m = new Mail(Long.parseLong("00"+i), s_date[i], peoples.get(i), me, str_sub[i], str_content);
            m.setFrom_me(false);
            items.add(m);
        }
        return items;
    }
    public static List<Mail> getOutboxData(Context ctx)  {
        List<Mail> items = new ArrayList<>();
        List<People> peoples = getPeopleData(ctx);
        People me = new People(0, ctx.getString(R.string.me_name), ctx.getString(R.string.me_mail), R.drawable.profile);
        String s_date[] = ctx.getResources().getStringArray(R.array.mail_date);
        String str_sub[] = ctx.getResources().getStringArray(R.array.subject_mail);
        String str_content = ctx.getString(R.string.long_lorem_ipsum);
        for (int i = 10; i < 15; i++) {
            Mail m = new Mail(Long.parseLong("11"+i), s_date[i], me, peoples.get(i), str_sub[i], str_content);
            m.setFrom_me(true);
            items.add(m);
        }
        return items;
    }
}
