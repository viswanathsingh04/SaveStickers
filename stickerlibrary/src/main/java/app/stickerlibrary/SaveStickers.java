package app.stickerlibrary;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.whatsapp.stickerlib.BuildConfig;

import java.io.File;

import app.stickerlibrary.Base.BaseActivity;
import app.stickerlibrary.Interface.OnImageDownloadTaskComplite;
import app.stickerlibrary.Model.DataItem;
import app.stickerlibrary.Model.StickerPack;
import app.stickerlibrary.Utils.DownloadStickerPackImage;
import app.stickerlibrary.Utils.GlobalFun;
import app.stickerlibrary.Utils.StickerBook;

public class SaveStickers extends BaseActivity {

    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
    private static final String TAG = SaveStickers.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_stickers);
    }

    public void startDownloadStickerImages(Context context, DataItem downloadStickerPack) {
        if (GlobalFun.isInternetConnected(context)) {
            //showDialog("downloading");
            new DownloadStickerPackImage(context, downloadStickerPack.getCatImg(), (trayImageFilePath, saveImgFolder) -> {
                try {
                    if (!TextUtils.isEmpty(trayImageFilePath)) {
                        File stickerPackTrayIconFile = new File(trayImageFilePath);
                        StickerPack sp = new StickerPack(downloadStickerPack.getIdentifier(), downloadStickerPack.getName(), downloadStickerPack.getName(), Uri.fromFile(stickerPackTrayIconFile), "", "", "", "", context, true);
                        StickerBook.addStickerPackExisting(sp);

                        for (int i = 0; i < downloadStickerPack.getStickers().size(); i++) {
                            int pos = i;
                            //Sticker downloadSticker = downloadStickerPack.getSticker(pos);
                            String downloadSticker = downloadStickerPack.getStickers().get(pos);
                            new DownloadStickerPackImage(context, downloadSticker, (stickerImageFilePath, saveImgFolder1) -> {
                                try {
                                    //show progress
                                    int per = (100 * (pos + 1)) / (downloadStickerPack.getStickers().size() - 1);
                                    //updateProgressDialogMessage("downloading" + " : " + per + "%");
                                    if (!TextUtils.isEmpty(stickerImageFilePath)) {
                                        File stickerFile = new File(stickerImageFilePath);
                                        sp.addSticker(Uri.fromFile(stickerFile), context, pos);
                                        if (pos == downloadStickerPack.getStickers().size() - 1) {
                                            GlobalFun.deleteRecursive(saveImgFolder1);
                                            //hideDialog();
                                            addStickerPackToWhatsApp(context, downloadStickerPack);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).execute();
                        }
                        invalidateOptionsMenu();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).execute();
        } else {
            GlobalFun.internetFailedDialog(context);
        }
    }

    public void addStickerPackToWhatsApp(Context context, DataItem sp) {
        Intent intent = new Intent();
        Log.e("status", "addstickerpack");
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra(EXTRA_STICKER_PACK_ID, sp.getIdentifier());
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra(EXTRA_STICKER_PACK_NAME, sp.getName());
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "error_adding_sticker_pack", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("requestCode", String.valueOf(requestCode));
        if (requestCode == 2002 && resultCode == RESULT_OK) {
            Toast.makeText(this, "added Successfully", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 200) {
            if (resultCode == Activity.RESULT_CANCELED && data != null) {
                final String validationError = data.getStringExtra("validation_error");
                if (validationError != null) {
                    if (BuildConfig.DEBUG) {
                        MessageDialogFragment.newInstance("Error with the pack", validationError).show(getSupportFragmentManager(), "validation error");
                    }
                    Log.e(TAG, "Validation failed:" + validationError);
                }
            } else {
                Toast.makeText(this, "added Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}