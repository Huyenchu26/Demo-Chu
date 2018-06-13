package mq.com.chuohapps.utils.data;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.utils.AppLogger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nguyen.dang.tho on 9/25/2017.
 */

public final class FileUtils {
    public static final String IMAGE_FOLDER = "image_folder";

    private FileUtils() {
    }

    public static File getFileInternal(Context context, String fileName, String folder) {
        try {
            File mFile = new File(context.getFilesDir() + "/" + folder + "/", fileName);
            if (mFile.exists()) return mFile;
        } catch (Exception e) {
            AppLogger.error(e.getMessage());
        }
        return null;
    }

    public static boolean deleteFile(Context context, String fileName, String folder) {
        try {
            File file = getFileInternal(context, fileName, folder);
            if (file != null) {
                if (file.exists())
                    return file.delete();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static void saveFileInternalAsync(Context context, final byte[] data, final String fileName, String folder, final SaveFileCallBack callBack) {
        new AsyncTask<Object, Void, File>() {
            @Override
            protected File doInBackground(Object... input) {
                Context mContext = ((Context) input[0]);
                if (mContext == null) return null;
                String mFileName = ((String) input[1]);
                if (mFileName == null) return null;
                String mFolder = ((String) input[2]);
                if (mFolder == null) return null;
                byte[] mData = (byte[]) input[3];
                if (mData == null) return null;

                File mFile = createFile(mContext, mFileName, mFolder);
                if (mFile == null) return null;

                if (saveFileInternal(mData, mFile)) return mFile;
                else return null;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                if (file != null)
                    if (callBack != null) callBack.onSuccess(file);
                    else {
                        if (callBack != null) callBack.onError();
                    }
            }
        }.execute(context, fileName, folder, data);
    }

    private static boolean saveFileInternal(byte[] mData, File mFile) {
        //write data to internal storage
        boolean success;
        FileOutputStream outputStream = null;
        try {
//                    outputStream = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            outputStream = new FileOutputStream(mFile, false);
            outputStream.write(mData);
            success = true;
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception er) {
                er.printStackTrace();
            }
        }
        return success;
    }

    public static void downloadFileInternalAsync(final Context context, final String urlString, final String fileName, final String folder, final SaveFileCallBack callBack) {
        //downloadFileNormal(context, urlString, fileName, folder, callBack);
        downloadFileWithOkHttp(urlString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                saveFileInternalAsync(context, response.body().bytes(), fileName, folder, callBack);
            }
        });

    }

    private static void downloadFileNormal(Context context, String urlString, String fileName, String folder, final SaveFileCallBack callBack) {
        new AsyncTask<Object, Void, File>() {
            @Override
            protected File doInBackground(Object... input) {
                Context mContext = ((Context) input[0]);
                if (mContext == null) return null;
                String mFileName = ((String) input[1]);
                if (mFileName == null) return null;
                String mFolder = ((String) input[2]);
                if (mFolder == null) return null;
                String mUrl = (String) input[3];
                if (mUrl == null) return null;
                File mFile = createFile(mContext, mFileName, mFolder);
                if (mFile == null) return null;
                downloadFileInternal(mUrl, mFile);
                return mFile;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                if (file != null)
                    if (callBack != null) callBack.onSuccess(file);
                    else {
                        if (callBack != null) callBack.onError();
                    }
            }
        }.execute(context, fileName, folder, urlString);
    }

    private static boolean downloadFileInternal(String mUrl, File mFile) {
        boolean success = false;
        OutputStream output = null;
        InputStream inputStream = null;
        int count;
        try {
            URL url = new URL(mUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lenghtOfFile = connection.getContentLength();

            // input stream to read file - with 8k buffer
            inputStream = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file
            output = new FileOutputStream(mFile, false);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = inputStream.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                // writing data to file
                output.write(data, 0, count);
            }
            // flushing output
            output.flush();
            success = true;
            // closing streams


        } catch (Exception e) {
            success = false;
            AppLogger.error("Error: " + e.getMessage());
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    private static void downloadFileWithOkHttp(String mUrl, Callback callback) {
        if (mUrl == null || mUrl.equals(AppConfigs.EMPTY)) {
            callback.onFailure(null, null);
            return;
        }
        try {
            Request request = new Request.Builder()
                    .url(mUrl)
                    .build();
            new OkHttpClient().newCall(request).enqueue(callback);
        } catch (Exception e) {
            callback.onFailure(null, null);
        }

    }

    private static File createFile(Context mContext, String mFileName, String mFolder) {
        createFolderIfNeeded(mContext, mFolder);
        File mFile = new File(mContext.getFilesDir() + "/" + mFolder + "/", mFileName);
        deleteFileIfExist(mFile);
        return mFile;
    }

    private static void deleteFileIfExist(File mFile) {
        if (mFile.exists()) {
            mFile.delete();
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createFolderIfNeeded(Context context, String folder) {
        File directory = new File(context.getFilesDir() + "/" + folder + "/");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public static String getFileUrlWithName(Context context, String fileName) {
        File file = new File(context.getFilesDir() + "/" + IMAGE_FOLDER + "/", fileName);
        return file.getAbsolutePath();
    }

    public static void getFileLocalDataAsync(String fileUrl, final GetFileCallBack callBack) {
        new AsyncTask<String, Void, byte[]>() {
            @Override
            protected byte[] doInBackground(String... params) {
                return getFileLocalData(params[0]);
            }

            @Override
            protected void onPostExecute(byte[] bytes) {
                super.onPostExecute(bytes);
                if (callBack != null) callBack.onDone(bytes);
            }
        }.execute(fileUrl);
    }

    public static byte[] getFileLocalData(String fileUrl) {
        File file = new File(fileUrl);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

    public interface GetFileCallBack {
        void onDone(byte[] data);
    }

    public interface SaveFileCallBack {
        void onSuccess(File file);

        void onError();
    }
}
