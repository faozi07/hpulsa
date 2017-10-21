package android.hpulsa.com.hpulsanew.util;

import android.app.Activity;
import android.content.pm.PackageManager;

/**
 * Created by ozi on 21/10/2017.
 */

public class Permissions {
    public static boolean checkCameraPermission(Activity activity) {
        String permission = "android.permission.CAMERA";
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean checkWriteExStoragePermission(Activity activity) {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean checkGpsPermission_Fine(Activity activity){
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean checkGpsPermission_Coarse(Activity activity){
        String permission = "android.permission.ACCESS_COARSE_LOCATION";
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean checkContactPermission(Activity activity){
        String permission = "android.permission.ACCESS_COARSE_LOCATION";
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
