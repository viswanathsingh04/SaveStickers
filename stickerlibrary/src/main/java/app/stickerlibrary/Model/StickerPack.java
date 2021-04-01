package app.stickerlibrary.Model;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import app.stickerlibrary.Utils.ImageManipulation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class StickerPack implements Parcelable {

    public Uri trayImageUri_local;
    public String trayImageUri = "";
    public String identifier = "";
    public String name;
    public String publisher;
    public String tray_image_file;
    public String publisherEmail;
    public String publisherWebsite;
    public String privacyPolicyWebsite;
    public String licenseAgreementWebsite;

    public String iosAppStoreLink;
    private final List<Sticker> stickers;
    private long totalSize_local;
    private String totalSize;
    public String androidPlayStoreLink;
    private boolean isWhitelisted;
    private boolean isDefaultPack = false;

    public StickerPack(String identifier, String name, String publisher, Uri trayImageUri, String publisherEmail, String publisherWebsite, String privacyPolicyWebsite, String licenseAgreementWebsite, Context context, boolean isDefaultPack) {
        this.identifier = identifier;
        this.name = name;
        this.publisher = publisher;
        this.tray_image_file = "trayimage";
        this.trayImageUri_local = ImageManipulation.convertIconTrayToWebP(trayImageUri, this.identifier, "trayImage", context);
        this.publisherEmail = publisherEmail;
        this.publisherWebsite = publisherWebsite;
        this.privacyPolicyWebsite = privacyPolicyWebsite;
        this.licenseAgreementWebsite = licenseAgreementWebsite;
        this.isDefaultPack = isDefaultPack;
        if (isDefaultPack)
            this.stickers = new ArrayList<>();
        else
            this.stickers = returnDefault30SizeArraty();

        //addPlayStoreAndAppStoreLinks();
    }

    public StickerPack(String identifier, String name, String publisher, String tray_image_fileFromAssets, String publisherEmail, String publisherWebsite, String privacyPolicyWebsite, String licenseAgreementWebsite, Context context) {
        this.identifier = identifier;
        this.name = name;
        this.publisher = publisher;
        this.tray_image_file = "trayimage";
        this.trayImageUri_local = ImageManipulation.convertIconTrayToWebP(tray_image_fileFromAssets, this.identifier, "trayImage", context);
        this.publisherEmail = publisherEmail;
        this.publisherWebsite = publisherWebsite;
        this.privacyPolicyWebsite = privacyPolicyWebsite;
        this.licenseAgreementWebsite = licenseAgreementWebsite;
        this.stickers = new ArrayList<>();
        //addPlayStoreAndAppStoreLinks();
    }

    /*public void addPlayStoreAndAppStoreLinks() {
        if (!TextUtils.isEmpty(GlobalFun.AndroidPlayStore_Link)) {
            this.androidPlayStoreLink = GlobalFun.AndroidPlayStore_Link;
        }
        if (!TextUtils.isEmpty(GlobalFun.IosAppStore_Link)) {
            this.iosAppStoreLink = GlobalFun.IosAppStore_Link;
        }
    }*/

    public List<Sticker> returnDefault30SizeArraty() {
        ArrayList<Sticker> arrayList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            arrayList.add(new Sticker());
        }
        return arrayList;
    }

    public void setIsWhitelisted(boolean isWhitelisted) {
        this.isWhitelisted = isWhitelisted;
    }

    public boolean getIsWhitelisted() {
        return isWhitelisted;
    }

    public boolean isDefaultPack() {
        return isDefaultPack;
    }

    public void setDefaultPack(boolean defaultPack) {
        isDefaultPack = defaultPack;
    }

    protected StickerPack(Parcel in) {
        identifier = in.readString();
        name = in.readString();
        publisher = in.readString();
        tray_image_file = in.readString();
        publisherEmail = in.readString();
        publisherWebsite = in.readString();
        privacyPolicyWebsite = in.readString();
        licenseAgreementWebsite = in.readString();
        iosAppStoreLink = in.readString();
        stickers = in.createTypedArrayList(Sticker.CREATOR);
        totalSize_local = in.readLong();
        androidPlayStoreLink = in.readString();
        isWhitelisted = in.readByte() != 0;
        isDefaultPack = in.readByte() != 0;
    }

    public static final Creator<StickerPack> CREATOR = new Creator<StickerPack>() {
        @Override
        public StickerPack createFromParcel(Parcel in) {
            return new StickerPack(in);
        }

        @Override
        public StickerPack[] newArray(int size) {
            return new StickerPack[size];
        }
    };

   /* public void addSticker(Uri uri, Context context) {
        String index = String.valueOf(stickersAddedIndex);
        this.stickers.add(new Sticker(
                index,
                ImageManipulation.convertImageToWebP(uri, this.identifier, index, context),
                new ArrayList<String>()));
        stickersAddedIndex++;
    }*/

    public void addSticker(String stickerPath, Context context, int pos) {
        this.stickers.add(new Sticker(
                "" + pos,
                ImageManipulation.convertImageToWebP(stickerPath, this.identifier, "" + pos, context),
                new ArrayList<String>()));
    }

    public void addSticker(Uri uri, Context context, int pos) {
        String index = String.valueOf(pos);
        this.stickers.add(new Sticker(
                index,
                ImageManipulation.convertImageToWebP(uri, this.identifier, index, context),
                new ArrayList<String>()));
    }

    public void addStickerOnSpecificPos(Uri uri, Context context, int pos) {
        this.stickers.set(pos, new Sticker(
                "" + pos,
                ImageManipulation.convertImageToWebP(uri, this.identifier, "" + pos, context),
                new ArrayList<String>()));
    }

    public void deleteSticker(int pos, Sticker stickers) {
        try {
            new File(stickers.getUriAsUri().getPath()).delete();
            this.stickers.set(pos, new Sticker());
            Fresco.getImagePipeline().evictFromMemoryCache(stickers.getUriAsUri());
        } catch (Exception e) {
        }
    }

    public Sticker getSticker(int index) {
        return this.stickers.get(index);
    }

    public Sticker getStickerById(int index) {
        for (Sticker s : this.stickers) {
            if (!TextUtils.isEmpty(s.getimage_file()) && s.getimage_file().equals(String.valueOf(index))) {
                return s;
            }
        }
        return null;
    }

    public void setAndroidPlayStoreLink(String androidPlayStoreLink) {
        this.androidPlayStoreLink = androidPlayStoreLink;
    }

    public void setIosAppStoreLink(String iosAppStoreLink) {
        this.iosAppStoreLink = iosAppStoreLink;
    }

    public List<Sticker> getStickers() {
        return stickers;
    }

    public List<Sticker> getActualStickers() {
        List<Sticker> arrStickers = new ArrayList<>();
        for (Sticker s : stickers) {
            if (!TextUtils.isEmpty(s.getimage_file()))
                arrStickers.add(s);
        }
        return arrStickers;
    }

    public long getTotalSize_local() {
        return totalSize_local;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identifier);
        dest.writeString(name);
        dest.writeString(publisher);
        dest.writeString(tray_image_file);
        dest.writeString(publisherEmail);
        dest.writeString(publisherWebsite);
        dest.writeString(privacyPolicyWebsite);
        dest.writeString(licenseAgreementWebsite);
        dest.writeString(iosAppStoreLink);
        dest.writeTypedList(stickers);
        dest.writeLong(totalSize_local);
        dest.writeString(androidPlayStoreLink);
        dest.writeByte((byte) (isWhitelisted ? 1 : 0));
        dest.writeByte((byte) (isDefaultPack ? 1 : 0));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Uri getTrayImageUri() {
        return trayImageUri_local == null ? Uri.parse(getTrayImageUrlPath()) : trayImageUri_local;
    }

    public String getTrayImageUrlPath() {
        try {
            return trayImageUri.replaceAll(" ", "%20");
        } catch (Exception e) {
            return trayImageUri;
        }
    }

    public void setTrayImageUri_local(Uri trayImageUri_local) {
        this.trayImageUri_local = trayImageUri_local;
    }

    public void settray_image_file(String tray_image_file) {
        this.tray_image_file = tray_image_file;
    }

    public void setTrayImageUri(String trayImageUri) {
        this.trayImageUri = trayImageUri;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public String tojson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "StickerPack{" +
                "trayImageUri_local=" + trayImageUri_local +
                ", identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", publisher='" + publisher + '\'' +
                ", tray_image_file='" + tray_image_file + '\'' +
                ", publisherEmail='" + publisherEmail + '\'' +
                ", publisherWebsite='" + publisherWebsite + '\'' +
                ", privacyPolicyWebsite='" + privacyPolicyWebsite + '\'' +
                ", licenseAgreementWebsite='" + licenseAgreementWebsite + '\'' +
                ", iosAppStoreLink='" + iosAppStoreLink + '\'' +
                ", stickers=" + stickers +
                ", totalSize_local=" + totalSize_local +
                ", totalSize='" + totalSize + '\'' +
                ", androidPlayStoreLink='" + androidPlayStoreLink + '\'' +
                ", isWhitelisted=" + isWhitelisted +
                ", isDefaultPack=" + isDefaultPack +
                '}';
    }

    public Uri getTrayImageUri_local() {
        return trayImageUri_local;
    }

    public String gettray_image_file() {
        return tray_image_file;
    }

    public String getPublisherEmail() {
        return publisherEmail;
    }

    public void setPublisherEmail(String publisherEmail) {
        this.publisherEmail = publisherEmail;
    }

    public String getPublisherWebsite() {
        return publisherWebsite;
    }

    public void setPublisherWebsite(String publisherWebsite) {
        this.publisherWebsite = publisherWebsite;
    }

    public String getPrivacyPolicyWebsite() {
        return privacyPolicyWebsite;
    }

    public void setPrivacyPolicyWebsite(String privacyPolicyWebsite) {
        this.privacyPolicyWebsite = privacyPolicyWebsite;
    }

    public String getLicenseAgreementWebsite() {
        return licenseAgreementWebsite;
    }

    public void setLicenseAgreementWebsite(String licenseAgreementWebsite) {
        this.licenseAgreementWebsite = licenseAgreementWebsite;
    }

    public String getIosAppStoreLink() {
        return iosAppStoreLink;
    }

    public void setTotalSize_local(long totalSize_local) {
        this.totalSize_local = totalSize_local;
    }

    public String getAndroidPlayStoreLink() {
        return androidPlayStoreLink;
    }

    public boolean isWhitelisted() {
        return isWhitelisted;
    }

    public void setWhitelisted(boolean whitelisted) {
        isWhitelisted = whitelisted;
    }

    public static Creator<StickerPack> getCREATOR() {
        return CREATOR;
    }
}