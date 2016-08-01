package com.ht.fyforandroid;

import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ht.fyforandroid.base.BaseActivity;
import com.ht.fyforandroid.net.MultiThreadDownloader.bizs.DLManager;
import com.ht.fyforandroid.net.MultiThreadDownloader.interfaces.SimpleDListener;
import com.ht.fyforandroid.net.asynctasknet.Request;
import com.ht.fyforandroid.net.asynctasknet.callback.JsonCallBack;
import com.ht.fyforandroid.net.asynctasknet.callback.StringCallBack;
import com.ht.fyforandroid.net.asynctasknet.http.UrlHelper;
import com.ht.fyforandroid.net.simplenet.core.RequestQueue;
import com.ht.fyforandroid.net.simplenet.core.SimpleNet;
import com.ht.fyforandroid.net.simplenet.requests.StringRequest;
import com.ht.fyforandroid.net.threadpoolnet.RequestManager;
import com.ht.fyforandroid.net.threadpoolnet.request.RequestCallback;
import com.ht.fyforandroid.util.DoubleClickExitHelper;

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
    @InjectView(R.id.ll_container)
    LinearLayout mLinearLayout;
    @InjectView(R.id.btn_down_test)
    Button mBtnDownTest;
    @InjectView(R.id.tv_progress_1)
    TextView mTvProgress1;
    private DoubleClickExitHelper mDoubleClickExit;
    // 1、构建请求队列
    RequestQueue mQueue = SimpleNet.newRequestQueue();

    String downStr = "http://dlsw.baidu.com/sw-search-sp/soft/7b/33461/freeime.1406862029.exe";
    private String saveDir = Environment.getExternalStorageDirectory() + "/AigeStudio/";


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

        DLManager.getInstance(SplashActivity.this).setMaxTask(2);
        downTest();

    }

    private void downTest() {
        mBtnDownTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DLManager.getInstance(SplashActivity.this).dlStart(downStr, saveDir,
                        null, null, new SimpleDListener(){
                            @Override
                            public void onStart(String fileName, String realUrl, int fileLength) {
//                                mTvProgress1.setText(fileLength + "");
                                Log.d("down...", fileName);
                            }

                            @Override
                            public void onProgress(int progress) {
//                                mTvProgress1.setText(progress + "");
                                Log.d("down...", progress + "");
                            }

                            @Override
                            public void onError(int status, String error) {
//                                mTvProgress1.setText(error);
                                Log.d("down...", error);
                            }
                        });
            }
        });
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
