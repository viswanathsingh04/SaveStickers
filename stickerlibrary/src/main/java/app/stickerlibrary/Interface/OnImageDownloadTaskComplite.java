package app.stickerlibrary.Interface;

import java.io.File;

public interface OnImageDownloadTaskComplite {
    public void onFinish(String filePath, File saveImgFolder);
}