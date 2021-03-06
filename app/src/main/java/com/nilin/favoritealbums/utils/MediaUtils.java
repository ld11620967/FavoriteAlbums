package com.nilin.favoritealbums.utils;


/**
 * Created by nilin on 17/2/23.
 */
public class MediaUtils {

    /**
     * 格式化时间,将毫秒转换为分:秒格式
     */
    public static String formatTime(long time){
        String min = time / (1000*60) + "";
        String sec = time % (1000*60) + "";
        if (min.length()<2){
            min = "0" + time / (1000*60) + "";
        }else {
            min = time / (1000*60) + "";
        }
        if (sec.length() == 4){
            sec = "0" + time % (1000*60) + "";
        }else if (sec.length() == 3){
            sec = "00" + time % (1000*60) + "";
        }else if (sec.length() == 2){
            sec = "000" + time % (1000*60) + "";
        }else if (sec.length() == 1) {
            sec = "0000" + time % (1000 * 60) + "";
        }
        return min + ":" + sec.trim().substring(0,2);
    }

}