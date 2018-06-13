package mq.com.chuohapps.utils.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import mq.com.chuohapps.AppConfigs;
import mq.com.chuohapps.R;


/**
 * Created by nguyen.dang.tho on 9/1/2017.
 */

public final class ImageUtils {
    private ImageUtils() {
    }

    public static <S> void loadImage(@NonNull ImageView imageView, S source, int defaultResource) {
        if (source instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) source);
            return;
        }
        Glide.with(imageView.getContext()).
                load((source == null) ? defaultResource
                        : (source instanceof String) ? (String) source
                        : (source instanceof Integer) ? (Integer) source
                        : (source instanceof Uri) ? (Uri) source
                        : (source instanceof File) ? (File) source
                        : (source instanceof byte[]) ? (byte[]) source
                        : source)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.color.colorPlaceHole)
                .error(defaultResource)
                .centerCrop()
                .fitCenter()
                .into(imageView);
    }

    public static <S> void loadImage(@NonNull ImageView imageView, S source) {
        loadImage(imageView, source, R.color.colorPlaceHole);
    }

    public static <OS, LS> void loadImageNoCache(@NonNull final ImageView imageView, LS localSource, final OS onlineSource) {
        loadImageNoCache(imageView, localSource, onlineSource, R.color.colorPlaceHole);
    }

    public static <OS, LS> void loadImageNoCache(@NonNull final ImageView imageView, LS localSource, final OS onlineSource, final int defaultResource) {
        if (localSource == null && onlineSource != null && (onlineSource instanceof Bitmap)) {
            imageView.setImageBitmap((Bitmap) onlineSource);
            return;
        }
        if (localSource instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) localSource);
            return;
        }
        Glide.with(imageView.getContext()).
                load((localSource == null) ?
                        (onlineSource == null) ? defaultResource
                                : (onlineSource instanceof String) ? (String) onlineSource
                                : (onlineSource instanceof Integer) ? (Integer) onlineSource
                                : (onlineSource instanceof Uri) ? (Uri) onlineSource
                                : (onlineSource instanceof File) ? (File) onlineSource
                                : (onlineSource instanceof byte[]) ? (byte[]) onlineSource
                                : onlineSource
                        : (localSource instanceof String) ? (String) localSource
                        : (localSource instanceof Integer) ? (Integer) localSource
                        : (localSource instanceof Uri) ? (Uri) localSource
                        : (localSource instanceof File) ? (File) localSource
                        : (localSource instanceof byte[]) ? (byte[]) localSource
                        : localSource)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.color.colorPlaceHole)
                .listener(new RequestListener<Object, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
                        loadImageNoCache(imageView, onlineSource, defaultResource);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .error(defaultResource)
                .fitCenter()
                .into(imageView);
    }

    public static <OS, LS> void loadImageNoCacheCircle(@NonNull final ImageView imageView, LS localSource, final OS onlineSource, int defaultResource) {
        if (localSource == null && onlineSource != null && (onlineSource instanceof Bitmap)) {
            imageView.setImageBitmap((Bitmap) onlineSource);
            return;
        }
        if (localSource instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) localSource);
            return;
        }
        Glide.with(imageView.getContext()).
                load((localSource == null) ?
                        (onlineSource == null) ? R.color.colorPlaceHole
                                : (onlineSource instanceof String) ? (String) onlineSource
                                : (onlineSource instanceof Integer) ? (Integer) onlineSource
                                : (onlineSource instanceof Uri) ? (Uri) onlineSource
                                : (onlineSource instanceof File) ? (File) onlineSource
                                : (onlineSource instanceof byte[]) ? (byte[]) onlineSource
                                : onlineSource
                        : (localSource instanceof String) ? (String) localSource
                        : (localSource instanceof Integer) ? (Integer) localSource
                        : (localSource instanceof Uri) ? (Uri) localSource
                        : (localSource instanceof File) ? (File) localSource
                        : (localSource instanceof byte[]) ? (byte[]) localSource
                        : localSource)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(defaultResource)
                .fitCenter()
                .into(imageView);
    }

    public static <OS, LS> void loadImageWithTag(@NonNull final ImageView imageView, LS localSource, final OS onlineSource, String tag) {

        Glide.with(imageView.getContext()).
                load((localSource == null || localSource.equals(AppConfigs.EMPTY)) ?
                        (onlineSource == null) ? R.color.colorPlaceHole
                                : (onlineSource instanceof String) ? (String) onlineSource
                                : (onlineSource instanceof Integer) ? (Integer) onlineSource
                                : (onlineSource instanceof Uri) ? (Uri) onlineSource
                                : (onlineSource instanceof File) ? (File) onlineSource
                                : (onlineSource instanceof byte[]) ? (byte[]) onlineSource
                                : onlineSource
                        : (localSource instanceof String) ? (String) localSource
                        : (localSource instanceof Integer) ? (Integer) localSource
                        : (localSource instanceof Uri) ? (Uri) localSource
                        : (localSource instanceof File) ? (File) localSource
                        : (localSource instanceof byte[]) ? (byte[]) localSource
                        : localSource)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(tag))
                .error(R.color.colorPlaceHole)
                .fitCenter()
                .into(imageView);
    }

    public static <S> void loadImageNoCache(@NonNull ImageView imageView, S source) {
        if (source instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) source);
            return;
        }
        Glide.with(imageView.getContext())
                .load((source == null) ? R.color.colorPlaceHole
                        : (source instanceof String) ? (String) source
                        : (source instanceof Integer) ? (Integer) source
                        : (source instanceof Uri) ? (Uri) source
                        : (source instanceof File) ? (File) source
                        : (source instanceof byte[]) ? (byte[]) source
                        : source)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.color.colorPlaceHole)
                .error(R.color.colorPlaceHole)
                .into(imageView);
    }

    public static <S> void loadImageCircle(@NonNull ImageView imageView, S source) {
        loadImageCircle(imageView, source, R.color.colorPlaceHole);
    }

    public static <S> void loadImageCircle(@NonNull ImageView imageView, S source, int defaultResource) {
        if (source instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) source);
            return;
        }
        Glide.with(imageView.getContext()).
                load((source == null) ? defaultResource
                        : (source instanceof String) ? (String) source
                        : (source instanceof Integer) ? (Integer) source
                        : (source instanceof Uri) ? (Uri) source
                        : (source instanceof File) ? (File) source
                        : (source instanceof byte[]) ? (byte[]) source
                        : source)
                .asBitmap()
                .dontAnimate()
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.color.colorPlaceHole)
                .error(defaultResource)
                .into(imageView);
    }

    public static <S> void loadImageLocalCircle(@NonNull ImageView imageView, S source) {
        loadImageLocalCircle(imageView, source, R.drawable.error_image);
    }

    public static <S> void loadImageLocalCircle(@NonNull ImageView imageView, S source, int defaultResource) {
        if (source instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) source);
            return;
        }
        Glide.with(imageView.getContext()).
                load((source == null) ? defaultResource
                        : (source instanceof String) ? (String) source
                        : (source instanceof Integer) ? (Integer) source
                        : (source instanceof Uri) ? (Uri) source
                        : (source instanceof File) ? (File) source
                        : (source instanceof byte[]) ? (byte[]) source
                        : source)
                .asBitmap()
                .centerCrop()
                .placeholder(R.color.colorPlaceHole)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(defaultResource)
                .into(imageView);
    }


    public static Bitmap imageToBitmap(ImageView image) {
        if (image.getDrawable() instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
            return bitmapDrawable.getBitmap();
        } else {
            if (image.getDrawingCache() == null) image.buildDrawingCache();
            return image.getDrawingCache();
        }
    }

    public static byte[] imageToBytes(ImageView image, int maxWidthPx) {
        return bitmapToBytes(imageToBitmap(image), maxWidthPx);
    }

    public static byte[] bitmapToBytes(Bitmap bitmap, int maxWidthPx) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resizeBitmap(bitmap, maxWidthPx).compress(Bitmap.CompressFormat.JPEG, 95, stream);
        byte[] data = stream.toByteArray();
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int maxWidthPx) {
        int sampleWidth = maxWidthPx;
        int currentWidth = bitmap.getWidth();
        float ratio = currentWidth > sampleWidth ? currentWidth / sampleWidth : 1f;
        int resultWidth = ratio == 1 ? currentWidth : Math.round(currentWidth / ratio);
        int resultHeight = ratio == 1 ? bitmap.getHeight() : Math.round(bitmap.getHeight());
        return Bitmap.createScaledBitmap(bitmap, resultWidth, resultHeight, true);
    }

    public static void clear(Context context) {
        Glide.get(context).clearMemory();
    }
}
