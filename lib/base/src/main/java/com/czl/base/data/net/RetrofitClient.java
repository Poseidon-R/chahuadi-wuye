package com.czl.base.data.net;

import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.czl.base.data.net.HttpLoggingInterceptor;
import com.czl.base.data.net.ResponseInterceptor;
import com.czl.base.data.net.TokenInterceptor;
import com.czl.base.data.net.cookie.CookieJarImpl;
import com.czl.base.data.net.cookie.store.PersistentCookieStore;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitClient {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //服务端根路径
//    public static String baseUrl = "http://120.78.177.177/zhsq-api/";
    public static String baseUrl = "http://8.129.20.33:8080/";
    //    public static String baseUrl = "http://192.168.10.24:8080/";//孙怡
    public static String imgUrl = "http://120.78.177.177/";
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static final RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        this(baseUrl, null);
    }

    private RetrofitClient(String url, Map<String, String> headers) {
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor("Retrofit");
        interceptor.setColorLevel(Level.SEVERE);
        interceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        OkHttpClient okHttpClient =
//                new OkHttpClient.Builder()
                RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder())
                        .cookieJar(new CookieJarImpl(new PersistentCookieStore(Utils.getApp().getApplicationContext())))
                        .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                .addInterceptor(new BaseInterceptor(headers))
                        .addInterceptor(new TokenInterceptor(headers))
//                        .addInterceptor(new RequestDataInterceptor())
                        .addInterceptor(new ResponseInterceptor())
                        .addInterceptor(interceptor)
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = chain.request();
                                HttpUrl oldUrl = request.url();
                                Request.Builder builder = request.newBuilder();
                                List<String> headerValues = request.headers("url_name");
                                if (headerValues != null && headerValues.size() > 0) {
                                    builder.removeHeader("url_name");
                                    String header = headerValues.get(0);
                                    HttpUrl newBaseUrl = null;
                                    if ("img".equals(header)) {
                                        newBaseUrl = HttpUrl.parse(imgUrl);
                                    } else {
                                        newBaseUrl = HttpUrl.parse(baseUrl);
                                    }
                                    HttpUrl newUrl = oldUrl.newBuilder().scheme(newBaseUrl.scheme())
                                            .host(newBaseUrl.host())
                                            .port(newBaseUrl.port())
                                            .build();
                                    return chain.proceed(builder.url(newUrl).build());
                                } else {
                                    return chain.proceed(request);
                                }
                            }
                        })

                        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                        // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                        .connectionPool(new ConnectionPool(15, 10, TimeUnit.SECONDS))
                        .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }
}
