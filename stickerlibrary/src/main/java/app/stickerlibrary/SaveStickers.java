package app.stickerlibrary;

import androidx.appcompat.app.AppCompatActivity;
import app.stickerlibrary.Base.BaseActivity;
import app.stickerlibrary.Interface.OnImageDownloadTaskComplite;
import app.stickerlibrary.Model.DataItem;
import app.stickerlibrary.Model.StickerPack;
import app.stickerlibrary.Utils.DownloadStickerPackImage;
import app.stickerlibrary.Utils.GlobalFun;
import app.stickerlibrary.Utils.StickerBook;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.whatsapp.stickerlib.BuildConfig;
import com.whatsapp.stickerlib.SaveActivity;

import java.io.File;

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

    public void startDownloadStickerImages(DataItem downloadStickerPack) {
        if (GlobalFun.isInternetConnected(this)) {
            showDialog(getString(R.string.labal_downloading_sticker));
            new DownloadStickerPackImage(this, downloadStickerPack.getCatImg(), new OnImageDownloadTaskComplite() {
                @Override
                public void onFinish(String trayImageFilePath, File saveImgFolder) {
                    try {
                        if (!TextUtils.isEmpty(trayImageFilePath)) {
                            File stickerPackTrayIconFile = new File(trayImageFilePath);
                            StickerPack sp = new StickerPack(downloadStickerPack.getIdentifier(), downloadStickerPack.getName(), downloadStickerPack.getName(), Uri.fromFile(stickerPackTrayIconFile), "", "", "", "", SaveStickers.this, true);
                            StickerBook.addStickerPackExisting(sp);

                            for (int i = 0; i < downloadStickerPack.getStickers().size(); i++) {
                                int pos = i;
                                //Sticker downloadSticker = downloadStickerPack.getSticker(pos);
                                String downloadSticker = downloadStickerPack.getStickers().get(pos);
                                new DownloadStickerPackImage(SaveStickers.this, downloadSticker, new OnImageDownloadTaskComplite() {
                                    @Override
                                    public void onFinish(String stickerImageFilePath, File saveImgFolder) {
                                        try {
                                            //show progress
                                            int per = (100 * (pos + 1)) / (downloadStickerPack.getStickers().size() - 1);
                                            updateProgressDialogMessage(getString(R.string.labal_downloading_sticker) + " : " + per + "%");

                                            if (!TextUtils.isEmpty(stickerImageFilePath)) {
                                                File stickerFile = new File(stickerImageFilePath);
                                                sp.addSticker(Uri.fromFile(stickerFile), SaveStickers.this, pos);
                                                if (pos == downloadStickerPack.getStickers().size() - 1) {
                                                    GlobalFun.deleteRecursive(saveImgFolder);
                                                    /*stickerPack = StickerBook.getStickerPackById(downloadStickerPack.identifier);
                                                    stickerPreviewAdapter = new StickerPreviewAdapter(getLayoutInflater(), R.drawable.sticker_error, getResources().getDimensionPixelSize(R.dimen.sticker_pack_details_image_size), getResources().getDimensionPixelSize(R.dimen.sticker_pack_details_image_padding), stickerPack, clickListener);
                                                    recyclerView.setAdapter(stickerPreviewAdapter);*/
                                                    hideDialog();
                                                    //downloadButton.setVisibility(View.GONE);
                                                    addStickerPackToWhatsApp(downloadStickerPack);
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).execute();
                            }
                            invalidateOptionsMenu();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        } else {
            GlobalFun.internetFailedDialog(this);
        }
    }

    private void addStickerPackToWhatsApp(DataItem sp) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra(EXTRA_STICKER_PACK_ID, sp.getIdentifier());
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra(EXTRA_STICKER_PACK_NAME, sp.getName());
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "R.string.error_adding_sticker_pack", Toast.LENGTH_LONG).show();
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
                        MessageDialogFragment.newInstance(R.string.title_validation_error, validationError).show(getSupportFragmentManager(), "validation error");
                    }
                    Log.e(TAG, "Validation failed:" + validationError);
                }
            } else {
                Toast.makeText(this, "added Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}