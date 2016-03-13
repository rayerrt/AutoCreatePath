
package com.android.autocreatfilepath;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {

    private final static String SDCardRoot = Environment.getExternalStorageDirectory()
            .getAbsolutePath();

    private final static String TAG = "AutoCreateFilePath";
    private final static String readDirName = "Undefined.txt"; // 放在根目录下

    private Button mButton;

    private File txtFile;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.mButton);
        txtFile = new File(SDCardRoot + File.separator + readDirName);
        mContext = this.getApplicationContext();
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFileStat()) {
                    mButton.setEnabled(false);
                    CreatePathTask mCreatePathTask = new CreatePathTask(mContext, mButton);
                    mCreatePathTask.execute(0);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        // TODO 自动生成的方法存根
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkFileStat() {
        if (!txtFile.exists()) {
            Toast.makeText(MainActivity.this, "路径文件读取失败,请将Undefined.txt放在手机根目录",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
