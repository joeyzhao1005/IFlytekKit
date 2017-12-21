package com.kit.iflytek.entity.apps;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.kit.iflytek.entity.DataWarpper;
import com.kit.utils.ResWrapper;

/**
 * Created by Zhao on 16/8/3.
 */
public class PackageInfoWarpper implements DataWarpper {


    private PackageInfo packageInfo;

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    @Override
    public String getTitle() {
        PackageManager pManager = ResWrapper.getInstance().getApplicationContext().getPackageManager();
        String appname = packageInfo.applicationInfo.loadLabel(pManager).toString();
        return appname;
    }

    @Override
    public String getContent() {
        String packageName = packageInfo.packageName;
        return packageName;
    }


    @Override
    public Drawable getIcon() {
        Context context = ResWrapper.getInstance().getApplicationContext();
        PackageManager pManager = context.getPackageManager();
        Drawable drawable = packageInfo.applicationInfo.loadIcon(pManager);
        return drawable;
    }

    public static PackageInfoWarpper cast(PackageInfo packageInfo) {
        PackageInfoWarpper packageInfoWarpper = new PackageInfoWarpper();
        packageInfoWarpper.setPackageInfo(packageInfo);
        return packageInfoWarpper;
    }
}
