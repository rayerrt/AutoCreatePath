/**
 * 
 */

package com.android.autocreatfilepath;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author songrui
 */
public class CreatePathTask extends AsyncTask<Integer, String, Integer> {

    private final static String rootDir = Environment.getExternalStorageDirectory()
            .getAbsolutePath();
    private final static String TAG = "AutoCreateFilePath";
    private final static String readDirName = "Undefined.txt";
    private final static String AssetParentDir = "templates";

    private ArrayList<String> dirNameList = new ArrayList<String>();

    private String tempDirPath = "";
    private String tempFilePath = "";
    private boolean isCreateFile = false;

    private String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private String base_hex = "abcdef0123456789";
    private String base_dec = "1234567890";
    private String templateFile = "test.txt";

    private static final String[] RootPrefix = {
            "/storage/sdcard0/",
            "/storage/sdcard1/",
            "/storage/emulated/0/",
            "/storage/emulated/1/"
    };
    private Context mContext;
    private Button mButton;

    public CreatePathTask(Context context, Button button) {
        // TODO Auto-generated constructor stub
        mContext = context;
        mButton = button;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        if (readDirNameFromTxt()) {
            createAllDirOrFile();
        }
        return 0;
    }

    protected void onPreExecute() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.udisk_mount_error),
                    Toast.LENGTH_LONG);
        }
    }

    protected void onPostExecute(Integer result) {
        mButton.setEnabled(true);
    }

    protected void onProgressUpdate(String progress) {

    }

    public boolean readDirNameFromTxt() {
        InputStreamReader inputStreamReader = null;
        InputStream instream = null;
        BufferedReader mReader = null;
        try {
            File txtFile = new File(rootDir + File.separator + readDirName);
            instream = new FileInputStream(txtFile);
            inputStreamReader = new InputStreamReader(instream, "utf-8");
            String line = null;
            int maxLength = 50000;
            mReader = new BufferedReader(inputStreamReader);
            while (((line = mReader.readLine()) != null) && maxLength > 0) {
                if (line.contains("*..")) {
                    continue;
                }
                for (String prefix : RootPrefix) {
                    if (line.startsWith(prefix)) {
                        line = line.substring(prefix.length());
                        break;
                    }
                }
                dirNameList.add(line);
                maxLength--;
            }
            mReader.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void createAllDirOrFile() {
        for (int i = 0; i < dirNameList.size(); ++i) {
            String randomPath = dirNameList.get(i);
            isFilePath(randomPath);
            if (randomPath.contains("*")) {
                randomPath = randomPath.replace("*", getRandomString(base, 16));
            }
            if (isCreateFile) {
                createSDCardDirOrFile(rootDir + File.separator + tempDirPath, false);
            }
            createSDCardDirOrFile(rootDir + File.separator + randomPath,
                    isCreateFile);
        }
    }

    private void createSDCardDirOrFile(String filePath, boolean createFile) {
        File path = null;
        try {
            path = new File(filePath);
            if (!path.exists()) {
                if (createFile) {
                    copyFileToPath(filePath);
                } else {
                    path.mkdirs();
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void isFilePath(String path) {
        isCreateFile = false;
        if (path.contains("*.")) {
            isCreateFile = true;
        }
        int lastIndex = path.lastIndexOf(File.separator);
        if (-1 != lastIndex) {
            tempDirPath = path.substring(0, lastIndex);
            tempFilePath = path.substring(lastIndex + 1);
        }
    }

    private String getRandomString(String input, int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(input.length());
            sb.append(input.charAt(number));
        }
        return sb.toString();
    }

    private void copyFileToPath(String strOutFileName) throws IOException {
        String strOutFileExt = strOutFileName.substring(strOutFileName.lastIndexOf(".") + 1);
        InputStream inputStream;
        String[] templateList = mContext.getAssets().list(AssetParentDir);
        for (String template : templateList) {
            String templateExt = template.substring(template.lastIndexOf(".") + 1);
            if (templateExt.equals(strOutFileExt)) {
                Log.d(TAG, "create file randomPath is " + strOutFileName + ", source file is "
                        + template);
                OutputStream outputStream = new FileOutputStream(strOutFileName);
                inputStream = mContext.getAssets().open(AssetParentDir + File.separator + template);
                byte[] buffer = new byte[1024];
                int length = inputStream.read(buffer);
                while (length > 0) {
                    outputStream.write(buffer, 0, length);
                    length = inputStream.read(buffer);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
                return;
            }
        }
        Log.d(TAG, "create file randomPath is " + strOutFileName);
        try {
            File file = new File(strOutFileName);
            file.createNewFile();
            FileWriter mFileWriter = new FileWriter(file);
            mFileWriter.write("test");
            mFileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
