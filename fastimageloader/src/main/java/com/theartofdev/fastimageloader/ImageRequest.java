// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.theartofdev.fastimageloader;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Encapsulate
 */
class ImageRequest {

    //region: Fields and Consts

    /**
     * the URL of the requested image as given
     */
    private final String mUri;

    /**
     * the spec to load the image by
     */
    private final ImageLoadSpec mSpec;

    /**
     * the file of the image in the disk
     */
    private final File mFile;

    /**
     * Is the request is prefetch request
     */
    private boolean mPrefetch;

    /**
     * the size of the file in the disk cache
     */
    private long mFileSize = -1;

    /**
     * the loaded image bitmap
     */
    private ReusableBitmapImpl mBitmap;

    /**
     * the target to load the image into
     */
    private List<Target> mTargets = new ArrayList<>(3);
    //endregion

    /**
     * @param uri the URL of the requested image as given
     * @param spec the dimension key used to load the image in specific size
     * @param file the path of the image in the disk
     */
    ImageRequest(String uri, ImageLoadSpec spec, File file) {
        mUri = uri;
        mSpec = spec;
        mFile = file;
        mPrefetch = true;
    }

    /**
     * @param target the target to load the image into
     * @param uri the URL of the requested image as given
     * @param spec the dimension key used to load the image in specific size
     * @param file the path of the image in the disk
     */
    ImageRequest(Target target, String uri, ImageLoadSpec spec, File file) {
        mTargets.add(target);
        mUri = uri;
        mSpec = spec;
        mFile = file;
        mPrefetch = false;
    }

    /**
     * The unique key of the image request.
     */
    String getUniqueKey() {
        return mSpec.getUriUniqueKey(mUri);
    }

    /**
     * the URL of the requested image as given
     */
    public String getUri() {
        return mUri;
    }

    /**
     * the URL of the requested image with thumbor parameters
     */
    public String getEnhancedUri() {
        return mSpec.getUriEnhancer().enhance(mUri, mSpec);
    }

    /**
     * the spec to load the image by
     */
    public ImageLoadSpec getSpec() {
        return mSpec;
    }

    /**
     * the file of the image in the disk
     */
    public File getFile() {
        return mFile;
    }

    /**
     * the size of the file in the disk
     */
    public long getFileSize() {
        return mFileSize;
    }

    /**
     * the size of the file in the disk
     */
    public void setFileSize(long fileSize) {
        mFileSize = fileSize;
    }

    /**
     * the loaded image bitmap
     */
    public ReusableBitmapImpl getBitmap() {
        return mBitmap;
    }

    /**
     * the loaded image bitmap
     */
    public void setBitmap(ReusableBitmapImpl bitmap) {
        if (mBitmap != null)
            mBitmap.setInLoadUse(false);
        mBitmap = bitmap;
        if (mBitmap != null)
            mBitmap.setInLoadUse(true);
    }

    /**
     * the target to load the image into
     */
    public Collection<Target> getValidTargets() {
        filterValidTargets();
        return mTargets;
    }

    /**
     * Is the loading of the requested image is still valid or was it canceled.<br/>
     */
    public boolean isValid() {
        filterValidTargets();
        return mPrefetch || mTargets.size() > 0;
    }

    /**
     * Is the request is for prefetch and not real target
     */
    public boolean isPrefetch() {
        return mPrefetch;
    }

    /**
     * Add another target to the request.
     *
     * @return Was the request prefetch before the target was added
     */
    public boolean addTarget(Target target) {
        boolean wasPrefetch = mPrefetch;
        mPrefetch = false;
        mTargets.add(target);
        return wasPrefetch;
    }

    private void filterValidTargets() {
        for (int i = mTargets.size() - 1; i >= 0; i--) {
            boolean isValid = TextUtils.equals(mTargets.get(i).getUrl(), mUri);
            if (!isValid) {
                mTargets.remove(i);
            }
        }
    }

    @Override
    public String toString() {
        return "ImageRequest{" +
                "mUri='" + mUri + '\'' +
                ", mSpec='" + mSpec + '\'' +
                ", mFile='" + mFile + '\'' +
                ", mFileSize=" + mFileSize +
                ", mBitmap=" + mBitmap +
                ", mTargets=" + mTargets.size() +
                ", mPrefetch=" + mPrefetch +
                ", isValid=" + isValid() +
                '}';
    }
}

