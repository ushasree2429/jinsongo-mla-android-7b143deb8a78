



package com.paril.mlaclientapp.ui.activity;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.Global;
import com.paril.mlaclientapp.model.MLAGroupDetails;
import com.paril.mlaclientapp.model.MlAAddPostDetails;
import com.paril.mlaclientapp.ui.fragment.MLAPostViewFragment;
import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.webservice.Api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Response;


public class PostActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
    private MlAAddPostDetails userDetails;
    boolean isToAdd = false, enabledEditMode = false;
    EditText txtPostMessage;
    Integer[] Groupid;
    int gid;
    int loggedid;
    Spinner spn;
    String mode,mResult;
    List<MLAGroupDetails> groupDetails = new ArrayList<>();
    List<MLAGroupDetails> groupid = new ArrayList<>();
    ArrayList<String> userNames;
    String[] GroupName;
    String[] listItems;
    String GroupKey,userName;
    String sessionKey,EncryptedSession,EncryptedMessage;
    private static final String ALPHA_NUMERIC_STRING = "huhijnowmABVGDILWMKJF";
    String alias,AES="AES";

    KeyStore keyStore;





    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mla_post);
        isToAdd = getIntent().getBooleanExtra(CommonUtils.EXTRA_IS_TO_ADD, false);
        txtPostMessage = (EditText) findViewById(R.id.textView2);
        spn=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.Group_arrays,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        spn.setOnItemSelectedListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MLAGetAllGroupsAPI mlagetalluserapi = new MLAGetAllGroupsAPI(PostActivity.this);
        mlagetalluserapi.execute();
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            // decryptString(iil);
        } catch (Exception e) {
            e.printStackTrace();

        }

        if (!isToAdd) {
            userDetails = (MlAAddPostDetails) getIntent().getSerializableExtra(CommonUtils.EXTRA_USER_ADMIN_DATA);
            setToolbarTitle("Adding post");
            setUpData();
            enabledEditMode = getIntent().getBooleanExtra(CommonUtils.EXTRA_EDIT_MODE, false);
            enableFields(enabledEditMode);

        } else {
            setToolbarTitle("Add Post");

            txtPostMessage.setEnabled(true);

        }

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         mode = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void setUpData() {
        txtPostMessage.setText(userDetails.getPost());

    }

    private void enableFields(boolean makeEditable) {
        txtPostMessage.setEnabled(makeEditable);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!isToAdd) {
            if (enabledEditMode) {
                menu.findItem(R.id.menu_edit).setVisible(false);
                menu.findItem(R.id.menu_cancel).setVisible(true);
                menu.findItem(R.id.menu_save).setVisible(true);
            } else {
                menu.findItem(R.id.menu_edit).setVisible(true);
                menu.findItem(R.id.menu_cancel).setVisible(false);
                menu.findItem(R.id.menu_save).setVisible(false);
            }
        } else {
            menu.findItem(R.id.menu_edit).setVisible(false);
            menu.findItem(R.id.menu_cancel).setVisible(false);
            menu.findItem(R.id.menu_save).setVisible(true);
        }

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_edit) {
            enabledEditMode = true;
            invalidateOptionsMenu();
            enableFields(enabledEditMode);
        } else if (item.getItemId() == R.id.menu_cancel) {
            enabledEditMode = false;
            invalidateOptionsMenu();
            enableFields(enabledEditMode);
            setUpData();
        } else if (item.getItemId() == R.id.menu_save) {

            hideKeyboard();
            if (isToAdd) {
                if (TextUtils.isEmpty(txtPostMessage.getText().toString())) {
                    showSnackBar(getString(R.string.enter_all_fields), findViewById(R.id.activity_view_post_coordinatorLayout));

                }
                 else {
                    if (CommonUtils.checkInternetConnection(PostActivity.this)) {
                        if(mode.equals("Group")) {
                            listItems = GroupName;
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(PostActivity.this);
                            mBuilder.setTitle("Choose an item");
                            mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mResult = listItems[i];
                                    gid=Groupid[i];
                                    dialogInterface.dismiss();
                                    System.out.println(mResult+""+gid);

                                    MLAAddPostAPI addPostTask = new MLAAddPostAPI(PostActivity.this);
                                    addPostTask.execute();

                                }
                            });
                            AlertDialog mDialog = mBuilder.create();
                            mDialog.show();
                        }else if(mode.equals("Friends"))
                        {
                            MLAGetSelfGroupsAPI auto =new MLAGetSelfGroupsAPI(PostActivity.this);
                            auto.execute();

                            System.out.println("Got default gid  "+gid);


                            MLAAddPostAPI addPostTask = new MLAAddPostAPI(PostActivity.this);
                            addPostTask.execute();
                        }
                        else {
                            gid=0;

                            MLAAddPublicPostAPI addPostTask = new MLAAddPublicPostAPI(PostActivity.this);
                            addPostTask.execute();
                        }


                    } else {
                        showSnackBar(getString(R.string.check_connection), findViewById(R.id.activity_view_post_coordinatorLayout));
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

  public static String DefaultKey(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    class MLAAddPostAPI extends AsyncTask<Void, Void, String> {
        Context context;
        MlAAddPostDetails userDetails = new MlAAddPostDetails();

        public MLAAddPostAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {

            userDetails.setPost(txtPostMessage.getText().toString());
            //showProgressDialog("Adding Post ...");
        }

        @Override
        protected void onPostExecute(String statusCode) {
            hideProgressDialog();
            if (statusCode.equals("201")) //the item is created
            {
                finish();
            } else {
                showSnackBar("Post Added", findViewById(R.id.activity_view_post_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            Global g = (Global) getApplication();
            int data = g.getData();
            String userName = g.getUsername();
            System.out.print("testing" + data);

            Call<List<MLAGroupDetails>> getgrpkey = Api.getClient().getGroupKey(gid,data);
            try {
                Response<List<MLAGroupDetails>> result = getgrpkey.execute();
                System.out.println(result);
                GroupKey = result.body().get(0).groupkey;
                System.out.println("gkey"+GroupKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
            alias=userName;
            sessionKey = DefaultKey(12);
            try {
                EncryptedMessage = encryptString(userDetails.getPost(), sessionKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String dGroupKey=CommonUtils.decryptString(GroupKey,alias,keyStore);//commonutils
            System.out.println("decrypt:"+dGroupKey);
            try {
                EncryptedSession = encryptString(sessionKey, dGroupKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("encryptrd sess:  " + EncryptedSession);
            System.out.println("encryptrd mess:  " + EncryptedMessage);
            String digitalsignature=getDigitalSig(userDetails.getPost(),alias);

            Call<List<MlAAddPostDetails>> callAddPost = Api.getClient().callAddPost(EncryptedMessage, gid, data, mode, EncryptedSession, userName,digitalsignature);//posttype; encrypted session key
            try {
                Response<List<MlAAddPostDetails>> respCallAdmin = callAddPost.execute();
                System.out.println(respCallAdmin);
                return "" + respCallAdmin.code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }



    class MLAAddPublicPostAPI extends AsyncTask<Void, Void, String> {
        Context context;
        MlAAddPostDetails userDetails = new MlAAddPostDetails();

        public MLAAddPublicPostAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {

            userDetails.setPost(txtPostMessage.getText().toString());
            
        }

        @Override
        protected void onPostExecute(String statusCode) {
            hideProgressDialog();
            if (statusCode.equals("201")) //the item is created
            {
                finish();
            } else {
                showSnackBar("Post Added", findViewById(R.id.activity_view_post_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            Global g = (Global) getApplication();
            int data = g.getData();
            String userName = g.getUsername();
            System.out.print("testing" + data);
            sessionKey="N/A";
            String digsig="N/A";


            Call<List<MlAAddPostDetails>> callAddPost = Api.getClient().callAddPost(userDetails.getPost(), gid, data, mode,sessionKey , userName,digsig);//posttype; encrypted session key
            try {
                Response<List<MlAAddPostDetails>> respCallAdmin = callAddPost.execute();
                System.out.println(respCallAdmin);
                return "" + respCallAdmin.code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }










    class MLAGetAllGroupsAPI extends AsyncTask<Void, Void, List<MLAGroupDetails>> {
        Context context;
        ArrayList<String> userNameData;

        public MLAGetAllGroupsAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            userNameData = userNames;
            showProgressDialog("Getting Posts...");
        }

        @Override
        protected void onPostExecute(List<MLAGroupDetails> apiResponse) {

            hideProgressDialog();

            if (apiResponse != null) {
            } else {
                showSnackBar(getString(R.string.server_error), findViewById(R.id.fragment_user_coordinatorLayout));
            }
        }

        protected List<MLAGroupDetails> doInBackground(Void... params) {

            try {
                Global g = (Global) getApplication();
                int loggedid = g.getData();
                userName=g.getUsername();

                Call<List<MLAGroupDetails>> callPostData = Api.getClient().getgroups(loggedid);//lloggedid
                Response<List<MLAGroupDetails>> responseAdminUser = callPostData.execute();
                System.out.println("response" + responseAdminUser);
                if (responseAdminUser.isSuccessful() && responseAdminUser.body() != null) {
                    System.out.println("response body:" + responseAdminUser.body());
                    groupDetails= responseAdminUser.body();
                    GroupName = new String[groupDetails.size()];
                    Groupid=new Integer[groupDetails.size()];
                    for (int i = 0; i < groupDetails.size(); i++) {

                        GroupName[i] = groupDetails.get(i).group_name;
                        Groupid[i]=groupDetails.get(i).group_id;



                    }
                    List<MLAGroupDetails> listUserDisplayCheckb = new ArrayList<>();
                    for (int i = 0; i < groupDetails.size(); i++) {
                        final MLAGroupDetails usersDisplayProvider = new MLAGroupDetails(GroupName[i], false);
                        listUserDisplayCheckb.add(usersDisplayProvider);
                    }
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


    class MLAGetSelfGroupsAPI extends AsyncTask<Void, Void, List<MLAGroupDetails>> {
        Context context;

        public MLAGetSelfGroupsAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            //userNameData = userNames;
            showProgressDialog("Getting details...");
        }

        @Override
        protected void onPostExecute(List<MLAGroupDetails> apiResponse) {

            hideProgressDialog();

            if (apiResponse != null) {
            } else {
                showSnackBar(getString(R.string.server_error), findViewById(R.id.fragment_user_coordinatorLayout));
            }
        }

        protected List<MLAGroupDetails> doInBackground(Void... params) {

            try {
                Global g = (Global) getApplication();
                 loggedid = g.getData();
                Call<List<MLAGroupDetails>> callPostData = Api.getClient().getselfgroups(loggedid);//lloggedid
                Response<List<MLAGroupDetails>> responseAdminUser = callPostData.execute();
                System.out.println("response" + responseAdminUser);
                if (responseAdminUser.isSuccessful() && responseAdminUser.body() != null) {
                    System.out.println("response body:" + responseAdminUser.body().toString());

                    groupid=responseAdminUser.body();
                    gid=groupid.get(0).group_id;

                    return  responseAdminUser.body();
                }else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }


        }
    }

    public String encryptString(String text,String Key) throws  Exception {
      SecretKeySpec input=generateKey(Key);
      Cipher c= Cipher.getInstance(AES);
      c.init(Cipher.ENCRYPT_MODE,input);
      byte[] enval=c.doFinal(text.getBytes());
      String encryptedValue=Base64.encodeToString(enval,Base64.DEFAULT);
      System.out.println("[[[[[[[[[["+encryptedValue);
     // decryptString(encryptedValue,Key);
      return encryptedValue;

    }

    private SecretKeySpec generateKey(String key) throws Exception {
        final MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] bytes= key.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] text=digest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(text,"AES");
        return secretKeySpec;
    }

    public String getDigitalSig(String message,String al) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(al, null);
            Signature sign = null;

            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");

            keyPairGen.initialize(2048);

            KeyPair pair = keyPairGen.generateKeyPair();

            PrivateKey pv = privateKeyEntry.getPrivateKey();
            sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(pv);
            byte[] bytes = message.getBytes();
            //Adding data to the signature
            sign.update(bytes);
            //Calculating the signature
            byte[] signature = sign.sign();
            return signature.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
