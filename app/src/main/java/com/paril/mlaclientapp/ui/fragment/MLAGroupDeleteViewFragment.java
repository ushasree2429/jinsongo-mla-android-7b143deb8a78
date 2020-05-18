package com.paril.mlaclientapp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLAGroupDetails;
import com.paril.mlaclientapp.model.MLARegisterUsers;
import com.paril.mlaclientapp.ui.activity.MLAHomeActivity;
import com.paril.mlaclientapp.ui.adapter.MLADeleteGroupAdapter;
import com.paril.mlaclientapp.webservice.Api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MLAGroupDeleteViewFragment extends Fragment {
    View view;
    ListView GroupList;
    MLARegisterUsers register;
    int loggedid;
    String loggedusername;
    ArrayList<String> userNames;
    ArrayList<Integer> gid;
    String[] GroupName;
    Integer[] GroupId;
    List<MLAGroupDetails> groupDetails = new ArrayList<>();
    MLADeleteGroupAdapter mlaUserDisplayAdapter;
    Button btnAdd;
    int exitid;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mla_deletegroup, container, false);
        GroupList = (ListView) view.findViewById(R.id.mla_user_display_listView);
        GroupList.setEmptyView(view.findViewById(R.id.empty_text_view));
        btnAdd = (Button) view.findViewById(R.id.mla_user_btnEnroll);

        MLAGetUserInformationAPI getUserDetails = new MLAGetUserInformationAPI();
        Bundle bundle = getActivity().getIntent().getExtras();
        getUserDetails.execute(bundle.get("userName").toString(), bundle.get("userType").toString());


        MLAGetAllGroupsAPI mlagetalluserapi = new MLAGetAllGroupsAPI(this.getActivity());
        mlagetalluserapi.execute();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (mlaUserDisplayAdapter != null && mlaUserDisplayAdapter.getCount() > 0) {
                    // the userDisplayadapter include the boolean values and the userName of the student.
                    int listSize = mlaUserDisplayAdapter.getCount();//this is the list size
                    MLAGroupDetails userDisplayCheckbxProvider;
                    gid = new ArrayList<>();

                    for (int i = 0; i < listSize; i++) {
                        userDisplayCheckbxProvider = (MLAGroupDetails) mlaUserDisplayAdapter.getItem(i);
                        if (userDisplayCheckbxProvider.getCheck()) {
                            gid.add(userDisplayCheckbxProvider.getGroup_id());

                        }
                    }

                    // if the students are selected from the list then call the API.
                    if(gid.size()>0){
                        MLAExitGroup enrollStudent = new MLAExitGroup(MLAGroupDeleteViewFragment.this.getActivity());
                        enrollStudent.execute();
                    } else{
                        ((MLAHomeActivity) getActivity()).showSnackBar("Select atleast one group.", getView().findViewById(R.id.fragment_user_coordinatorLayout));
                    }


                }
            }
        });

        return view;
    }



    class MLAGetUserInformationAPI extends AsyncTask<String, Void, Void> {

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
                    loggedusername=register.getUserName();
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



    class MLAGetAllGroupsAPI extends AsyncTask<Void, Void, List<MLAGroupDetails>> {
        Context context;
        ArrayList<String> userNameData;

        public MLAGetAllGroupsAPI(Context ctx) {
            context = ctx;
        }



        @Override
        protected void onPreExecute() {
            userNameData = userNames;

        }

        @Override
        protected void onPostExecute(List<MLAGroupDetails> apiResponse) {
    if (apiResponse != null) {
        groupDetails= apiResponse;
        GroupName = new String[groupDetails.size()];
        GroupId=new Integer[groupDetails.size()];
        for (int i = 0; i < groupDetails.size(); i++) {

            GroupName[i] = groupDetails.get(i).group_name;
            GroupId[i]=groupDetails.get(i).group_id;

        }
        List<MLAGroupDetails> listUserDisplayCheckb = new ArrayList<>();
        for (int i = 0; i < groupDetails.size(); i++) {
            final MLAGroupDetails usersDisplayProvider = new MLAGroupDetails(GroupName[i], GroupId[i],false);
            listUserDisplayCheckb.add(usersDisplayProvider);
        }

        mlaUserDisplayAdapter = new MLADeleteGroupAdapter(context, listUserDisplayCheckb);
        GroupList = (ListView) view.findViewById(R.id.mla_user_display_listView);
        GroupList.setAdapter(mlaUserDisplayAdapter);
            } else { ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.server_error), getView().findViewById(R.id.fragment_user_coordinatorLayout));
    }
        }

        protected List<MLAGroupDetails> doInBackground(Void... params) {

            try {
                int userId=loggedid;
                Call<List<MLAGroupDetails>> callPostData = Api.getClient().getdeletegroups(userId);//lloggedid
                Response<List<MLAGroupDetails>> responseAdminUser = callPostData.execute();
                System.out.println("response" + responseAdminUser);
                if (responseAdminUser.isSuccessful() && responseAdminUser.body() != null) {
                    System.out.println("response body:" + responseAdminUser.body());


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

    class MLAExitGroup extends  AsyncTask<Void, Void, String>
    {
        Context context;
        ArrayList<Integer> userNameData;

        public MLAExitGroup(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            userNameData = gid;
        }

        protected void onPostExecute(String statusCode) {

            if (statusCode.equals("Success")) //the item is created
            {

                MLAGetAllGroupsAPI mlaAPI = new MLAGetAllGroupsAPI(MLAGroupDeleteViewFragment.this.getActivity());
                mlaAPI.execute();
                ((MLAHomeActivity) getActivity()).showSnackBar("Left Group.", view.findViewById(R.id.fragment_user_coordinatorLayout));
            } else {
                ((MLAHomeActivity) getActivity()).showSnackBar("Error while exiting group.", view.findViewById(R.id.fragment_user_coordinatorLayout));
            }
        }

        @Override
        protected String doInBackground(Void... params) {
           // Gkeys= new ArrayList<>();
            for (int i = 0; i < userNameData.size(); i++) {
                System.out.println(userNameData.get(i));
                exitid=  userNameData.get(i);

            }
            try {
                Call <String> callEnrollSubjectData = Api.getClient().ExitGroup(loggedid,exitid);
                Response <String> responseSubjectData = callEnrollSubjectData.execute();
                System.out.println("response is:" + responseSubjectData);

            } catch (MalformedURLException e) {
                return null;

            } catch (IOException e) {
                return null;
            }

            return "Success";
        }


    }

}
