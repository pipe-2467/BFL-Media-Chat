package com.bflmedia.chat;

import android.app.PictureInPictureParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Rational;
import android.webkit.JavascriptInterface;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ผูก Javascript Interface เข้ากับ WebView ให้ index.html เรียกใช้ได้
        this.bridge.getWebView().addJavascriptInterface(new WebAppInterface(), "AndroidNative");
    }

    public class WebAppInterface {
        // คำสั่งสั่งเปิดหน้าตั้งค่า "แสดงผลเหนือแอปอื่น" (Overlay Permission)
        @JavascriptInterface
        public void openOverlaySettings() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }
        }

        // คำสั่งสั่งให้ออกจากแอป แล้วกลายเป็นหน้าต่างลอยนอกแอป (Picture-in-Picture)
        @JavascriptInterface
        public void enterPipMode() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Rational aspectRatio = new Rational(9, 16); // อัตราส่วนหน้าต่างลอย
                PictureInPictureParams.Builder pipBuilder = new PictureInPictureParams.Builder();
                pipBuilder.setAspectRatio(aspectRatio);
                enterPictureInPictureMode(pipBuilder.build());
            }
        }
    }
}
