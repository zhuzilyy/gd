package com.gd.form.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>类描述：常用方法工具类
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */

public class Util {

    /**
     * 对象判空
     * @param obj 要判空的对象
     * @return
     */
    public static boolean isEmpty(Object obj){
        if(obj==null){
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * 字符串判空
     * @param string 要判空的字符串
     * @return
     */
    public static boolean isEmpty(String string){
        return null == string || "".equals(string.trim());
    }

    public static int screenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int screenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static String appendString(Object str1,String str2){
        return str1 + str2;
    }

    /**
     * 显示键盘
     *
     * @param editText 要获取光标并显示键盘的控件
     */
    public static void showKeyboard(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    /**
     * 获取指定日期
     * @param offset 把日期往后增加一天.整数往后推,负数往前移动
     * @return
     */
    public static String getDayOffToday(int offset) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("M月dd日");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String dateString = "";
        try {
            calendar.add(calendar.DATE, offset);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
            dateString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }


    /**
     * 获取指定日期
     * @param offset 把日期往后增加一天.整数往后推,负数往前移动
     * @return
     */
    public static String getDayOffToday(int offset, SimpleDateFormat format) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String dateString = "";
        try {
            calendar.add(calendar.DATE, offset);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
            dateString = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }
    /**
     * 获取年月日时间（忽略时分秒，即 2018-06-25 00：00：00）
     * @param sdf
     * @param date 要格式化的时间
     * @return
     */
    public static Date getFormatDate(SimpleDateFormat sdf, Date date) {
        Date dateDay = new Date();
        try {
            dateDay = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateDay;
    }

    /**
     * 将分钟转换成小时
     * @param min 分钟
     * @return
     */
    public static String getTimeOfHour(String min) {


        BigDecimal bigDecimal = new BigDecimal("0.0");
        try {
            bigDecimal = new BigDecimal(Double.parseDouble(min) / 60.0f);
        } catch (NumberFormatException e) {
            bigDecimal = new BigDecimal("0.0");
        } finally {
            double result = bigDecimal.doubleValue();
            result = new BigDecimal(result).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            return String.valueOf(result);
        }
    }


    public static boolean isIP(String address) {
        if (address.length() < 7 || address.length() > 15 || "".equals(address)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(address);

        boolean ipAddress = mat.find();

        return ipAddress;
    }

    /**
     * 是否可以修改密码
     * @param password 要判断的密码字符串
     * @return
     */
    public static boolean canChangePwd(String password) {
        if (password.length() < 8 || password.length() > 15 || Util.isEmpty(password)) {
            return false;
        }
        /**
         * 判断密码格式和范围
         */
        String sRex = "^(?!^[0-9]+$)(?!^[a-zA-Z]+$)(?!^[_#@$]+$)[0-9A-Za-z_#@$]{8,15}$";
        Pattern pat = Pattern.compile(sRex);
        Matcher mat = pat.matcher(password);
        return mat.find();
    }
    /**
     * dp转px
     * @param dp
     * @param context
     * @return
     */
    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**

     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp

     */

    public static int pxToDp(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);

    }


    /**
     * 判断字符串是否包含指定字符串
     * @param need 字符串
     * @param have 指定字符串
     * @return
     */
    public static boolean haveStr(String need, String have){
        if(!Util.isEmpty(need)&&!Util.isEmpty(have)){
            return need.contains(have);
        }
        return false;
    }


    /**
     * MD5加密
     * @param pwd
     * @return
     */
    public static String md5Pwd(String pwd){

        //MD5加密的算法，有JDK实现，我们只需要使用
        try {
            //获取加密的对象
            MessageDigest instance = MessageDigest.getInstance("MD5");
            //使用加密对象的方法，完成加密
            byte[] bs = instance.digest(pwd.getBytes());
            //朝着mysql加密结果的方向优化
            /**
             * byte b 1111 1111
             * int b 0000 0000 0000 0000 0000 0000 1111 1111
             * int 255 0000 0000 0000 0000 0000 0000 1111 1111
             * &--------------------------------------------------
             * 0000 0000 0000 0000 0000 0000 1111 1111
             * */
            String str = "";
            //第一，将所有的数据，转换成正数
            for (byte b : bs) {
                int temp = b & 255;
                //第二，将所有的数据，转换成16进制格式
                if(temp >=0 && temp <=15){
                    str = str +"0"+ Integer.toHexString(temp);
                }else{
                    str = str + Integer.toHexString(temp);
                }
            }
            return str;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * GMT(格林威治标准时间)转换当前北京时间
     * @param GMT 秒单部位
     * @return
     */
    public static String stampToDate(long GMT) {
        String res = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            res = simpleDateFormat.format(GMT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public static long getCurTimeLong(){
        return System.currentTimeMillis();
    }
    public static Object[] addDataToArray(Object[] s, Object newData) {
        s= Arrays.copyOf(s, s.length+1);
        s[s.length-1]=newData;
        return s;
    }

    public static String doubleNumber(double d){
        DecimalFormat decimalFormat =new DecimalFormat("#0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(d);
    }

    /**
     *
     * @param temp
     * @return
     */
    public static String doubleOrInt(double temp){
        int tempInt=(int)temp;
        String result;
        if(tempInt-temp==0){
            result = String.valueOf(tempInt);
        }else{
            result = String.valueOf(temp);
        }
        return result;
    }

}
