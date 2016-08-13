package com.ht.fyforandroid;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.ht.fyforandroid.R;
import com.ht.fyforandroid.base.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.InjectView;

/**
 * Created by niehongtao on 16/8/12.
 */
public class BigImgTestActivity extends BaseActivity {
    @InjectView(R.id.imageView)
    SubsamplingScaleImageView imageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bigimg;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.mLoadingDialog.hideLoading();
        final String testUrl = "http://n2.hdfimg.com/g9/M00/1E/B4/uoYBAFepon-AKjTRANKobLODE_M932.jpg";
        final File downDir = Environment.getExternalStorageDirectory();
        //使用Glide下载图片,保存到本地
        Glide.with(this)
                .load(testUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        File file = new File(downDir, "m_1385635534691.jpg");
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        FileOutputStream fout = null;
                        try {
                            //保存图片
                            fout = new FileOutputStream(file);
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                            // 将保存的地址给SubsamplingScaleImageView,这里注意设置ImageViewState
                            imageView.setImage(ImageSource.uri(file.getAbsolutePath()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fout != null) fout.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
