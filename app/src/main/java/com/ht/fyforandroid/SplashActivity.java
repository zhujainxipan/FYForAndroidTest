package com.ht.fyforandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ht.fyforandroid.base.BaseActivity;
import com.ht.fyforandroid.net.asynctasknet.Request;
import com.ht.fyforandroid.net.asynctasknet.callback.JsonCallBack;
import com.ht.fyforandroid.net.asynctasknet.callback.StringCallBack;
import com.ht.fyforandroid.net.asynctasknet.http.UrlHelper;
import com.ht.fyforandroid.net.downdemo.DownloadProgressListener;
import com.ht.fyforandroid.net.downdemo.FileDownloader;
import com.ht.fyforandroid.net.simplenet.core.RequestQueue;
import com.ht.fyforandroid.net.simplenet.core.SimpleNet;
import com.ht.fyforandroid.net.simplenet.requests.StringRequest;
import com.ht.fyforandroid.net.threadpoolnet.RequestManager;
import com.ht.fyforandroid.net.threadpoolnet.request.RequestCallback;
import com.ht.fyforandroid.util.DoubleClickExitHelper;
import com.ht.fyforandroid.widget.filter.FilterTestActivity;

import java.io.File;

import butterknife.InjectView;

public class SplashActivity extends BaseActivity {
    @InjectView(R.id.btn_test_httpclient)
    Button mBtnTestHttpclient;
    @InjectView(R.id.tv_result)
    TextView mTvResult;
    @InjectView(R.id.btn_test_json)
    Button mBtnTestJson;
    @InjectView(R.id.tv_progress)
    TextView mTvProgress;
    @InjectView(R.id.btn_test_simplenet)
    Button mSimpleButton;
    @InjectView(R.id.btn_test_threadpool)
    Button mThreadPoolButton;
    @InjectView(R.id.downloadbar)
    ProgressBar mProgressBar;
    @InjectView(R.id.resultView)
    TextView mResultView;
    @InjectView(R.id.button)
    Button mButton;
    @InjectView(R.id.path)
    EditText mEditText;
    @InjectView(R.id.btn_bigimg)
    Button mBtnBigImg;
    @InjectView(R.id.btn_recycleview)
    Button mBtnrv;
    @InjectView(R.id.my_image_view)
    SimpleDraweeView testIv;
    @InjectView(R.id.btn_filter)
    Button mBtnFilter;
    private DoubleClickExitHelper mDoubleClickExit;
    // 1、构建请求队列
    RequestQueue mQueue = SimpleNet.newRequestQueue();

    String downStr = "http://dlsw.baidu.com/sw-search-sp/soft/7b/33461/freeime.1406862029.exe";


    /**
     * 当Handler被创建会关联到创建它的当前线程的消息队列，该类用于往消息队列发送消息
     * 消息队列中的消息由当前线程内部进行处理
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mProgressBar.setProgress(msg.getData().getInt("size"));
                    float num = (float) mProgressBar.getProgress() / (float) mProgressBar.getMax();
                    int result = (int) (num * 100);
                    mResultView.setText(result + "%");

                    if (mProgressBar.getProgress() == mProgressBar.getMax()) {
                        Toast.makeText(SplashActivity.this, "下载完成", Toast.LENGTH_LONG).show();
                    }
                    break;
                case -1:
                    Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mDoubleClickExit = new DoubleClickExitHelper(this);
    }

    @Override
    protected void initView() {

        super.mLoadingDialog.hideLoading();

        threadPoolTest();

        sysNetTest();

        simpleNetTest();

        downTest();

        doBigImgTest();

        doRecycleViewTest();

        doFrescoTest();

        doFilter();
    }

    private void doFilter() {
        mBtnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, FilterTestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doFrescoTest() {
        testIv.setImageURI("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png");
    }

    private void doRecycleViewTest() {
        mBtnrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, RecycleViewTestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doBigImgTest() {
        mBtnBigImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, BigImgTestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void downTest() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = mEditText.getText().toString();
                System.out.println(Environment.getExternalStorageState() + "------" + Environment.MEDIA_MOUNTED);

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    download(path, Environment.getExternalStorageDirectory());
                } else {
                    Toast.makeText(SplashActivity.this, "sd卡不存在", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 主线程(UI线程)
     * 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
     *
     * @param path
     * @param savedir
     */
    private void download(final String path, final File savedir) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileDownloader loader = new FileDownloader(SplashActivity.this, path, savedir, 3);
                mProgressBar.setMax(loader.getFileSize());//设置进度条的最大刻度为文件的长度
                try {
                    loader.download(new DownloadProgressListener() {
                        @Override
                        public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
                            Message msg = new Message();
                            msg.what = 1;
                            msg.getData().putInt("size", size);
                            handler.sendMessage(msg);//发送消息
                        }
                    });
                } catch (Exception e) {
                    handler.obtainMessage(-1).sendToTarget();
                }
            }
        }).start();
    }


    /**
     * simplenet实现方案
     */
    private void simpleNetTest() {
        mSimpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStringRequest();
            }
        });
    }


    /**
     * 异步任务网络框架实现方案
     */
    private void sysNetTest() {
        mBtnTestHttpclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestString();
            }
        });

        mBtnTestJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestJson();
            }
        });
    }


    /**
     * 线程池实现方案
     */
    private void threadPoolTest() {
        mThreadPoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestCallback callback = new RequestCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        mTvResult.setText(response.toString());
                    }

                    @Override
                    public void onFail(String errorMessage) {

                    }
                };
                com.ht.fyforandroid.net.threadpoolnet.request.StringRequest request = new com.ht.fyforandroid.net.threadpoolnet.request.StringRequest(
                        "http://www.baidu.com",
                        com.ht.fyforandroid.net.threadpoolnet.request.Request.RequestMethod.GET,
                        callback,
                        null
                );
                RequestManager.getInstance().executeRequest(request);
            }
        });
    }

    /**
     * 发送GET请求,返回的是String类型的数据, 同理还有{@see JsonRequest}、{@see MultipartRequest}
     */
    private void sendStringRequest() {
        StringRequest request = new StringRequest(com.ht.fyforandroid.net.simplenet.base.Request.HttpMethod.GET, "http://www.baidu.com",
                new com.ht.fyforandroid.net.simplenet.base.Request.RequestListener<String>() {

                    @Override
                    public void onComplete(int stCode, String response, String errMsg) {
                        mTvResult.setText(Html.fromHtml(response));
                    }
                });

        mQueue.addRequest(request);
    }


    private void requestJson() {
        final Request request = new Request(UrlHelper.TEST_JSON, Request.RequestMethod.GET);
        request.setCallback(new JsonCallBack() {
                                @Override
                                public void onFailure(Exception result) {
                                    result.printStackTrace();
                                }

                                @Override
                                public void onSuccess(Object result) {
                                    mTvResult.setText((String) result);
                                }

                                @Override
                                public void onProgressUpdate(int curPos, int contentLength) {

                                }

                                @Override
                                public Object onPreHandle(Object object) {
                                    return object;
                                }

                                @Override
                                public Object onPresRequest() {
                                    return null;
                                }
                            }


        );
        request.execute();
    }

    private void requestString() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ht_training_http.txt";
        final Request request = new Request(UrlHelper.TEST_STRING, Request.RequestMethod.GET);
        request.setCallback(new StringCallBack() {
            @Override
            public void onFailure(Exception result) {
                result.printStackTrace();
            }

            @Override
            public void onSuccess(Object result) {
                mTvResult.setText((String) result);
            }

            @Override
            public void onProgressUpdate(int curPos, int contentLength) {
                mTvProgress.setText(curPos + "xxxxx" + contentLength);
            }

            @Override
            public Object onPreHandle(Object object) {
                // 比如需要把数据顺序再重新排列一下，或者插入到数据库中等等
                return object;
            }

            @Override
            public Object onPresRequest() {
                return null;
            }
        }.setPath(path));
        request.execute();
    }

    @Override
    protected void initData() {

    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            return mDoubleClickExit.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
