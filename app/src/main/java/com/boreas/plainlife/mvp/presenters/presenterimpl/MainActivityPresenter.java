package com.boreas.plainlife.mvp.presenters.presenterimpl;

import android.content.Context;

import com.boreas.plainlife.App;
import com.boreas.plainlife.Constant;
import com.boreas.plainlife.ObjectPool;
import com.boreas.plainlife.websocket.PlainMessage;
import com.boreas.plainlife.websocket.WebSocketManger;
import com.boreas.plainlife.api.ApiService;
import com.boreas.plainlife.base.BaseRequest;
import com.boreas.plainlife.mq.RabbitMQConfiguration;
import com.boreas.plainlife.mvp.presenters.ipresenter.IMainActivityPresenter;
import com.boreas.plainlife.mvp.views.viewinterfaces.MainActivityInterface;
import com.boreas.plainlife.utils.RxTimer;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivityPresenter extends BaseRequest implements IMainActivityPresenter {
    private ApiService apiService;
    private MainActivityInterface mainActivityInterface;
    private Context context;
    private App app;
    private WebSocketManger webSocketManger;
    private PlainMessage plainMessage;
    private ObjectPool objectPool;

    private Disposable subscribe;

    @Inject
    public MainActivityPresenter(ApiService apiService, MainActivityInterface mainActivityInterface, Context context, App app, WebSocketManger webSocketManger, PlainMessage plainMessage, ObjectPool objectPool) {
        this.apiService = apiService;
        this.mainActivityInterface = mainActivityInterface;
        this.context = context;
        this.app = app;
        this.webSocketManger = webSocketManger;
        this.plainMessage = plainMessage;
        this.objectPool = objectPool;
    }


    @Override
    public void requestUserInfo() {
        if (isNetWorkEnable()) {
            mainActivityInterface.onShowLoadingDialog();
            this.apiService.appGetInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userInfo -> {
                        mainActivityInterface.onDisLoadingDialog();
                        if (userInfo.getCode() == 200) {
                            this.objectPool.setUserInfo(userInfo);
                            return;
                        }
                        mainActivityInterface.onFailed(userInfo.getMsg());
                    }, throwable -> {
                        mainActivityInterface.onDisLoadingDialog();
                        mainActivityInterface.onFailed("请求数据发生错误");
                    });
        } else {
            mainActivityInterface.noNetWork();
        }
    }

    @Override
    public void onInit() {
        this.webSocketManger.init();
        this.sendHBMessage();
    }

    public void sendHBMessage() {
        RxTimer hbRxTimer = new RxTimer();
        hbRxTimer.interval(Constant.HB_TIME, number -> {
            this.webSocketManger.sendMessage(this.plainMessage.hbService(String.valueOf(this.objectPool.getUserInfo().getUser().getUserId())));
        });
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestory() {
        if (subscribe != null && !subscribe.isDisposed()) {
            subscribe.dispose();
        }
        this.desotyMQ();
        this.webSocketManger.destroy();
    }

    private void desotyMQ() {
        Observable.create(emitter -> {
            RabbitMQConfiguration.getInstance().onDestory();
        }).subscribeOn(Schedulers.newThread())
                .subscribe();
    }
}
