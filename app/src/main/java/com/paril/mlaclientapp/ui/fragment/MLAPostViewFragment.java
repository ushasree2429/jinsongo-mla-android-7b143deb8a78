

package com.paril.mlaclientapp.ui.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.security.KeyPairGeneratorSpec;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.paril.mlaclientapp.R;

import com.paril.mlaclientapp.model.Global;
import com.paril.mlaclientapp.model.MLAPostDetails;
import com.paril.mlaclientapp.model.MLARegisterUsers;
import com.paril.mlaclientapp.ui.activity.MLAHomeActivity;


import com.paril.mlaclientapp.ui.activity.PostActivity;
import com.paril.mlaclientapp.ui.adapter.MLAInstructorAdapter;
import com.paril.mlaclientapp.ui.adapter.MLAPostAdapter;
import com.paril.mlaclientapp.ui.adapter.OnItemClickListener;
import com.paril.mlaclientapp.ui.view.EmptyRecyclerView;
import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.webservice.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by paril on 7/14/2017.
 */
public class MLAPostViewFragment extends Fragment {
    List<MLAPostDetails> postdetails = new ArrayList<>();
    ArrayList<String> userNames;
    String[] UserName;
    String[] Message,sessionkey,groupKey,dsessionkey,dgroupkey,dmessage;
    ListView PostList;
    EmptyRecyclerView recyclerViewUsers;
    ArrayAdapter<String> adapter;
    MLAPostAdapter mlaUserDisplayAdapter;
    View view;
    MLARegisterUsers register;
    int loggedid;
    String loggedname,decryptedText;
    String alias,AES="AES" ;

    KeyStore keyStore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mla_postlist, container, false);
        PostList = (ListView) view.findViewById(R.id.mla_user_display_listView);
        PostList.setEmptyView(view.findViewById(R.id.empty_text_view));

        MLAGetUserInformationAPI getUserDetails = new MLAGetUserInformationAPI();
        Bundle bundle = getActivity().getIntent().getExtras();
        getUserDetails.execute(bundle.get("userName").toString(), bundle.get("userType").toString());



        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

        } catch (Exception e) {
            e.printStackTrace();

        }

         MLAGetAllPostsAPI mlagetalluserapi= new MLAGetAllPostsAPI(this.getActivity());
          mlagetalluserapi.execute();
        view.findViewById(R.id.mla_user_btnEnroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



               final Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtra(CommonUtils.EXTRA_IS_TO_ADD, true);
                intent.putExtra(CommonUtils.EXTRA_EDIT_MODE, false);
                startActivity(intent);
            }
        });

        return view;
    }




    class MLAGetAllPostsAPI extends AsyncTask<Void, Void, List<MLAPostDetails>> {
        Context context;
        ArrayList<String> userNameData;

        public MLAGetAllPostsAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            userNameData = userNames;
            ((MLAHomeActivity) getActivity()).showProgressDialog("Getting Posts...");
        }

        @Override
        protected void onPostExecute(List<MLAPostDetails> apiResponse) {

            ((MLAHomeActivity) getActivity()).hideProgressDialog();
            if (apiResponse != null) {
                postdetails = apiResponse;
                UserName = new String[postdetails.size()];
                Message = new String[postdetails.size()];
                sessionkey=new String[postdetails.size()];
                groupKey=new String[postdetails.size()];
                dgroupkey=new String[postdetails.size()];
                dsessionkey=new String[postdetails.size()];
                dmessage=new String[postdetails.size()];

                for (int i = 0; i < postdetails.size(); i++) {
                   if(postdetails.get(i).username.equals(loggedname))
                   {
                       UserName[i] ="You to "+postdetails.get(i).postType;
                   }
                   else{
                       UserName[i] = postdetails.get(i).username +" to "+postdetails.get(i).postType;
                   }
                    Message[i] = postdetails.get(i).post;
                    sessionkey[i]=postdetails.get(i).sessionKey;
                    groupKey[i]=postdetails.get(i).groupKey;

                    try {
                        if(groupKey[i]!="") {
                            System.out.println("entered" + " " + groupKey[i]);
                            alias = loggedname;
                            dgroupkey[i] = CommonUtils.decryptString(groupKey[i], alias, keyStore);
                            System.out.println("entered" + " " + dgroupkey[i]);
                            dsessionkey[i] = decryptString(sessionkey[i], dgroupkey[i]);
                            System.out.println("decrypted serssion" + dsessionkey[i]);
                            dmessage[i] = decryptString(Message[i], dsessionkey[i]);
                        }
                        else
                        {
                            dmessage[i]=Message[i];
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                List<MLAPostDetails> listUserDisplayCheckb = new ArrayList<>();
                for (int i = 0; i < postdetails.size(); i++) {
                    final MLAPostDetails usersDisplayProvider = new MLAPostDetails( dmessage[i],UserName[i],2);
                    listUserDisplayCheckb.add(usersDisplayProvider);
                }


                mlaUserDisplayAdapter = new MLAPostAdapter(context, listUserDisplayCheckb);
                PostList = (ListView) view.findViewById(R.id.mla_user_display_listView);
                PostList.setAdapter(mlaUserDisplayAdapter);
            } else {
                ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.server_error), getView().findViewById(R.id.fragment_user_coordinatorLayout));
            }
        }

        @Override
        protected List<MLAPostDetails> doInBackground(Void... params) {

            try {

                Call<List<MLAPostDetails>> callPostData = Api.getClient().getPost(loggedid);//lloggedid
                Response<List<MLAPostDetails>> responseAdminUser = callPostData.execute();
                System.out.println("response" +responseAdminUser);
                if (responseAdminUser.isSuccessful() && responseAdminUser.body() != null) {
                   // System.out.println("response body:" +responseAdminUser.body());
                    return responseAdminUser.body();
                } else {
                    return null;
                }

            } catch (MalformedURLException e) {
                return null;

            } catch (IOException e) {
                return null;
            }
        }
    }


    class MLAGetUserInformationAPI extends AsyncTask<String, Void, Void> {
        MLARegisterUsers UserDetails = new MLARegisterUsers();


        String userType;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Void result) {
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Call<List<MLARegisterUsers>> callAdminData = Api.getClient().getuser(params[0]);
                Response<List<MLARegisterUsers>> responseAdminData = callAdminData.execute();
                if (responseAdminData.isSuccessful() && responseAdminData.body() != null) {
                    register = responseAdminData.body().get(0);
                    loggedid = Integer.parseInt(register.getUserId());
                    loggedname=register.getUserName();
                    System.out.println(register.getUserId());
                }

            } catch (MalformedURLException e) {
                return null;

            } catch (IOException e) {
                return null;
            }
            return null;
        }
    }

    private SecretKeySpec generateKey(String key) throws Exception {
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes= key.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] text=digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(text,"AES");
        return secretKeySpec;
    }

    public String decryptString(String text,String Key)throws Exception
    {
        SecretKeySpec input=generateKey(Key);
        Cipher c= Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE,input);
        byte[] decodeval=Base64.decode(text,Base64.DEFAULT);
        byte[] deval=c.doFinal(decodeval);
        String Decrypted=new String(deval);
        System.out.println("]]]]]]]"+Decrypted);
        return Decrypted;
    }

}


