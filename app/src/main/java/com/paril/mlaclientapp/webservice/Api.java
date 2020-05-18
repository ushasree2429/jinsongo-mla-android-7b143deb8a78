package com.paril.mlaclientapp.webservice;

import com.paril.mlaclientapp.util.CommonUtils;

import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Api {


    private static Retrofit retrofit = null;

    public static APIInterface getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        OkHttpClient client= UnsafeOkHttpClient.getUnsafeOkHttpClient();
        if (retrofit == null ) {

            Retrofit.Builder builderHTTPS= new Retrofit.Builder()
                    .baseUrl(CommonUtils.MlaBaseURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create());
                    retrofit = builderHTTPS.build();
        }
        return retrofit.create(APIInterface.class);
    }

    //Code taken from "https://futurestud.io/tutorials/retrofit-2-how-to-trust-unsafe-ssl-certificates-self-signed-expired"
    public static class UnsafeOkHttpClient {
        public static OkHttpClient getUnsafeOkHttpClient() {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }
                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }
                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });  OkHttpClient okHttpClient = builder.build();
                return okHttpClient;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}