package com.hhq.mediaplayerdemo;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ┏┓　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * iBoosJie.　 ┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 * 创建人：杰
 * 创建日期： 2017/5/12
 * 说明：
 * 修改：
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "Util_CrashHandler";

    private Context mContext;
    private static CrashHandler INSTANCE = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context ctx) {
        mContext = ctx.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            Throwable cause = e.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();

            //File file = new File(getDiskCacheDir(mContext, "crash"), "crash.log");
            File file  = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"crash.log");

            if (file.exists() && file.length() > 5 * 1024 * 1024) {     // <5M
                boolean delete = file.delete();
            }

            //File folder = new File(getDiskCacheDir(mContext, "crash"));
            File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (folder.exists() && folder.isDirectory()) {
                if (!file.exists()) {
                    boolean newFile = file.createNewFile();
                    if(!newFile){
                        return;
                    }
                }
            } else {
                boolean mkDir = folder.mkdir();
                if (mkDir) {
                    boolean newFile = file.createNewFile();
                    if(!newFile){
                        return;
                    }
                }
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            String format = sdf.format(new Date());
            byte[] bytes = (format +        //追加Crash时间
                    "\n" + result +   //Crash内容
                    "\n" + "######## ~~~ T_T ~~~ ########" +    //分割线
                    "\n").getBytes();
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        mDefaultHandler.uncaughtException(t, e);    //该代码不执行的话程序无法终止
    }

    /**
     * @param context context
     * @param dirName dirName
     * @return dir
     */
    private String getDiskCacheDir(Context context, String dirName) {
        String cachePath = null;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            }
        }
        if (cachePath == null) {
            File cacheDir = context.getCacheDir();
            if ((cacheDir != null) && (cacheDir.exists())) {
                cachePath = cacheDir.getPath();
            }
        }
        //0/emulate/Android/data/data/com.***.***/crash/crash.log
        return cachePath + File.separator + dirName;
    }

}
