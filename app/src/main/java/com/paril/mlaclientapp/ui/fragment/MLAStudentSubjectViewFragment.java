package com.paril.mlaclientapp.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paril.mlaclientapp.R;
import com.paril.mlaclientapp.model.MLASubjectDetails;
import com.paril.mlaclientapp.ui.activity.MLAHomeActivity;
import com.paril.mlaclientapp.ui.adapter.MLAAssignedSubjectAdapter;
import com.paril.mlaclientapp.util.CommonUtils;
import com.paril.mlaclientapp.util.PrefsManager;
import com.paril.mlaclientapp.util.VerticalSpaceItemDecoration;
import com.paril.mlaclientapp.webservice.Api;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by paril on 7/14/2017.
 */
public class MLAStudentSubjectViewFragment extends Fragment {

    com.paril.mlaclientapp.ui.view.EmptyRecyclerView recyclerViewSubjects;
    MLAAssignedSubjectAdapter subjectDisplayAdapter;
    PrefsManager prefsManager;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_display_assigned_subject, container, false);
        recyclerViewSubjects = (com.paril.mlaclientapp.ui.view.EmptyRecyclerView) view.findViewById(R.id.fragment_display_assigned_subject_recyclerView);
        recyclerViewSubjects.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSubjects.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.divider_list)));
        prefsManager = new PrefsManager(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonUtils.checkInternetConnection(getActivity())) {
            MLAGetSubjectDetailsWithStudentAPI getSubjectDetails = new MLAGetSubjectDetailsWithStudentAPI(this.getActivity());
            getSubjectDetails.execute();
        } else {
            ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.check_connection), view.findViewById(R.id.fragment_display_assigned_subject_coordinatorLayout));
        }

    }

    class MLAGetSubjectDetailsWithStudentAPI extends AsyncTask<Void, Void, List<MLASubjectDetails>> {
        Context context;

        public MLAGetSubjectDetailsWithStudentAPI(Context ctx) {
            context = ctx;
        }

        @Override
        protected void onPreExecute() {
            ((MLAHomeActivity) getActivity()).showProgressDialog("Getting Subject Details...");
        }

        @Override
        protected void onPostExecute(List<MLASubjectDetails> listSubjectDetail) {

            ((MLAHomeActivity) getActivity()).hideProgressDialog();
            List<MLASubjectDetails> listSubjectDetails = new ArrayList<MLASubjectDetails>();
            if (listSubjectDetail != null) {
                listSubjectDetails = listSubjectDetail;
            } else {
                ((MLAHomeActivity) getActivity()).showSnackBar(getString(R.string.server_error), getView().findViewById(R.id.fragment_display_subject_coordinatorLayout));
            }
            subjectDisplayAdapter = new MLAAssignedSubjectAdapter(context, listSubjectDetails);
            recyclerViewSubjects.setAdapter(subjectDisplayAdapter);
            recyclerViewSubjects.setEmptyView(getView().findViewById(R.id.fragment_display_assigned_subject_relEmptyView));
        }

        @Override
        protected List<MLASubjectDetails> doInBackground(Void... params) {
            try {
                Call<ArrayList<MLASubjectDetails>> callSubjectData = Api.getClient().getSubjForStudent(prefsManager.getStringData("userName"));
                Response<ArrayList<MLASubjectDetails>> responseSubjectData = callSubjectData.execute();
                if (responseSubjectData.isSuccessful() && responseSubjectData.body() != null) {
                    return responseSubjectData.body();
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
}
