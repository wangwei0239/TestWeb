package com.example.wangwei.testweb;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String URL_PREFIX = "http://mail.emotibot.com.cn:8009/Emotibot/api/APP/";
    private ImageView iv;

    private Notification mNotification;
    private NotificationManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.Ext.init(this.getApplication());
        x.Ext.setDebug(true);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyyHHss");//年时秒
        String prefix = sdf.format(date);
        sdf.applyPattern("MMddmm");//月日分钟
        String surfix = sdf.format(date);
        String userID = "1";
        String rawResult = prefix + userID + surfix;
        String rawResultMd5 = MD5.md5(rawResult);
        char[] array = {rawResultMd5.charAt(2), rawResultMd5.charAt(9), rawResultMd5.charAt(15), rawResultMd5.charAt(17)};
        String result = String.valueOf(array);
        sdf.applyPattern("yyyyMMddHHmmss");
        String timeString = sdf.format(date);
        System.out.println("md5:" + result + " time:" + timeString);
//        testGetMsg(result, timeString);
        //createDb();

        setAM();
//        imageProcess();
        //onTestBaidu2Click(result,timeString);
    }

    public void setAM() {
        //创建Intent对象，action为ELITOR_CLOCK，附加信息为字符串“你该打酱油了”
        Intent intent = new Intent("ELITOR_CLOCK");
        intent.putExtra("msg", "你该打酱油了");

        //定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        //也就是发送了action 为"ELITOR_CLOCK"的intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);

        //AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);

        //设置闹钟从当前时间开始，每隔5s执行一次PendingIntent对象pi，注意第一个参数与第二个参数的关系
        // 5秒后通过PendingIntent pi对象发送广播
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 5 * 1000, pi);
    }

    private void testGetMsg(String result, String timeString) {
        RequestParams rp = new RequestParams(URL_PREFIX + "getMessage.php?UserID=1&cmd=ASK&chk=" + result + "&time=" + timeString);
        x.http().get(rp, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("sucess------------" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("cuowu------------");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                System.out.println("wancheng------------");
            }
        });
    }

    private void imageProcess() {
//        iv = (ImageView) findViewById(R.id.iv);
        String path = Environment.getExternalStorageDirectory() + "/finger/27.jpg";
        File file = new File(path);
        if (file.exists()) {
            System.out.println("exist");
        } else {
            System.out.println("not exist");
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        bitmap = toRoundBitmap(bitmap);
        bitmap = bitmap.createScaledBitmap(bitmap, 144, 144, false);
        iv.setImageBitmap(bitmap);
    }

    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// …Ë÷√ª≠± Œﬁæ‚≥›

        canvas.drawARGB(0, 0, 0, 0); // ÃÓ≥‰’˚∏ˆCanvas
        paint.setColor(color);

        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// …Ë÷√¡Ω’≈Õº∆¨œ‡Ωª ±µƒƒ£ Ω,≤Œøºhttp://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); //“‘Mode.SRC_INƒ£ Ω∫œ≤¢bitmap∫Õ“—æ≠draw¡ÀµƒCircle

        return output;
    }

    private void createDb() {
        DbManager db = x.getDb(((AppApplication) getApplicationContext()).getDaoConfig());
        List<LYJPerson> lyjPersons = getOldMessageWithPaging(0, 3, 3);
        for (int i = 0; i < lyjPersons.size(); i++) {
//                Log.i("liyuanjinglyj","LYJPerson"+i+".name="+lyjPersons.get(i).getName());
            Log.i("liyuanjinglyj", "LYJPerson" + i + ".age=" + lyjPersons.get(i).getAge());
        }
    }

    public List<LYJPerson> getOldMessageWithPaging(int uid, int limit, int reverseOffset) {
        DbManager db = x.getDb(((AppApplication) getApplicationContext()).getDaoConfig());
        ArrayList<LYJPerson> tmp = new ArrayList<LYJPerson>();
        try {
            long msgAmount = db.selector(LYJPerson.class).count();
            int offset = (int) (msgAmount - reverseOffset - limit);
            if (offset < 0)
                offset = 0;
            tmp.addAll(db.selector(LYJPerson.class).offset(offset).limit(limit).findAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }


    private void onTestBaidu2Click(String result, String time) {
        Log.i("Test", "get inside");
//        RequestParams params = new RequestParams("http://tsa-china.takeda.com.cn/HR/APP/getAbout.php?cmd=ASK&UserID=1&time="+time+"&chk="+result);
        RequestParams params = new RequestParams(URL_PREFIX + "uploadImage.php");
//        RequestParams params = new RequestParams("http://tsa-china.takeda.com.cn/HR/APP/chat.php?UserID=1&chk="+result+"&time="+time+"&text=北京天气");
        params.setMultipart(true);
        File fileFolder = new File(Environment.getExternalStorageDirectory() + "/finger/");
        File file = new File(fileFolder, "2.jpg");//UserID=xxx&chk=num&time=yyyymmddHHMMSS&face=file
        params.addBodyParameter("UserID", "1");
        params.addBodyParameter("chk", result);
        params.addBodyParameter("time", time);
        params.addBodyParameter("face", file);

        if (file.exists()) {
            System.out.println("exist");
        } else {
            System.out.println("not exist");
        }

        x.http().post(params, progressCallback);
    }


    Callback.ProgressCallback<String> progressCallback = new Callback.ProgressCallback<String>() {
        @Override
        public void onWaiting() {
            System.out.println("等待-------------------");
        }

        @Override
        public void onStarted() {
            System.out.println("开始-------------------");
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            System.out.println("loading-------------------total:" + total + " current:" + current + " isDow" + isDownloading);
        }

        @Override
        public void onSuccess(String result) {
            Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
            System.out.println("成功-------------------");
            System.out.println(result);
            int resultCode = analyzeFaceScanResult(result);
            handleFaceScanResult(resultCode);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println("错误-------------------");
        }

        @Override
        public void onCancelled(CancelledException cex) {
            Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            System.out.println("取消-------------------");
        }

        @Override
        public void onFinished() {
            System.out.println("完成-------------------");
        }
    };

    org.xutils.common.Callback.CommonCallback<String> commonCallback = new org.xutils.common.Callback.CommonCallback<String>() {
        @Override
        public void onSuccess(String result) {
            Toast.makeText(x.app(), result, Toast.LENGTH_LONG).show();
            System.out.println("成功-------------------");
            System.out.println(result);
            int resultCode = analyzeFaceScanResult(result);
            handleFaceScanResult(resultCode);
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println("错误-------------------");
        }

        @Override
        public void onCancelled(CancelledException cex) {
            Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            System.out.println("取消-------------------");
        }

        @Override
        public void onFinished() {
            System.out.println("完成-------------------");
        }
    };

    private void handleFaceScanResult(int resultCode) {
        switch (resultCode) {
            case FACE_SCAN_DEFAULT:
                Log.i(LOG_TAG, "default not handled");
                break;
            case FACE_SCAN_NO_HUMAN:
                Log.i(LOG_TAG, "no huam");
                break;
            case FACE_SCAN_JSON_PARSE_ERROR:
                Log.i(LOG_TAG, "code error, json parse");
                break;
            case FACE_SCAN_UPLOAD_ERROR:
                Log.i(LOG_TAG, "upload error");
                break;
            case FACE_SCAN_SUCCESS:
                Log.i(LOG_TAG, "success");
                break;
            case FACE_SCAN_PARA_ERROR:
                Log.i(LOG_TAG, "handleFaceScanResult: code error para error");

        }
    }


    private int analyzeFaceScanResult(String input) {
        int result = FACE_SCAN_DEFAULT;
        try {
            JSONObject jsonObject = new JSONObject(input);
            int resultCode = jsonObject.getInt("return");
            switch (resultCode) {
                case 0:
                    JSONObject picResult = jsonObject.getJSONObject("data");
                    boolean hasHuman = (picResult.getInt("human") > 0);
                    if (hasHuman) {
                        result = FACE_SCAN_SUCCESS;
                    } else {
                        result = FACE_SCAN_NO_HUMAN;
                    }
                    break;
                case -1:
                    Log.i(LOG_TAG, "Parameter error --------------------");
                    result = FACE_SCAN_PARA_ERROR;
                    break;
                case -2:
                    result = FACE_SCAN_UPLOAD_ERROR;
                    break;
            }
        } catch (JSONException e) {
            result = FACE_SCAN_JSON_PARSE_ERROR;
            e.printStackTrace();
        }
        return result;
    }


    private static String LOG_TAG = "FaceScanningActivity";

    private static final int FACE_SCAN_SUCCESS = 1;
    private static final int FACE_SCAN_DEFAULT = 10;
    private static final int FACE_SCAN_NO_HUMAN = 0;
    private static final int FACE_SCAN_PARA_ERROR = -1;
    private static final int FACE_SCAN_UPLOAD_ERROR = -2;
    private static final int FACE_SCAN_JSON_PARSE_ERROR = -3;
}
