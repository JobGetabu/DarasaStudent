package com.job.darasastudent.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.job.darasastudent.appexecutor.DefaultExecutorSupplier;
import com.job.darasastudent.model.CourseYear;
import com.job.darasastudent.model.LecTeachTime;
import com.job.darasastudent.model.StudentDetails;
import com.job.darasastudent.repository.FirebaseDocumentLiveData;
import com.job.darasastudent.repository.FirebaseQueryLiveData;
import com.job.darasastudent.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.job.darasastudent.util.Constants.LECTEACHTIMECOL;
import static com.job.darasastudent.util.Constants.STUDENTDETAILSCOL;

/**
 * Created by Job on Tuesday : 9/25/2018.
 */
public class AccountSetupViewModel extends AndroidViewModel {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public static final String TAG = "AccountSetupVM";

    //few db references
    private DocumentReference userRef;

    //live data
    private FirebaseDocumentLiveData mUserLiveData;
    private MediatorLiveData<List<LecTeachTime>> listLecTeachTimeResult;

    private LiveData<Calendar> calendarLiveData;


    //mediators
    private MediatorLiveData<StudentDetails> studUserMediatorLiveData;
    private MediatorLiveData<List<LecTeachTime>> lecTechTimeLiveData;

    public AccountSetupViewModel(@NonNull Application application, FirebaseAuth mAuth, FirebaseFirestore mFirestore) {
        super(application);
        this.mAuth = mAuth;
        this.mFirestore = mFirestore;


        //init mediators
        studUserMediatorLiveData = new MediatorLiveData<>();
        lecTechTimeLiveData = new MediatorLiveData<>();
        listLecTeachTimeResult = new MediatorLiveData<>();
        listLecTeachTimeResult.setValue(new ArrayList<LecTeachTime>());

        //init db refs 
        userRef = mFirestore.collection(STUDENTDETAILSCOL).document(mAuth.getCurrentUser().getUid());


        //init livedatas
        mUserLiveData = new FirebaseDocumentLiveData(userRef);

        // Set up the MediatorLiveData to convert DataSnapshot objects into POJO objects
        workOnUsersLiveData();
        //fire up the Rocket
        classListQuery(Calendar.getInstance());
    }

    private void workOnUsersLiveData() {

        studUserMediatorLiveData.addSource(mUserLiveData, new Observer<DocumentSnapshot>() {
            @Override
            public void onChanged(@Nullable final DocumentSnapshot documentSnapshot) {

                if (documentSnapshot != null) {

                    DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                            .submit(new Runnable() {
                                @Override
                                public void run() {

                                    studUserMediatorLiveData.postValue(documentSnapshot.toObject(StudentDetails.class));
                                }
                            });
                } else {
                    studUserMediatorLiveData.postValue(null);
                }
            }
        });
    }

    private void classListQuery(Calendar c) {

        //Due to change in structure we, have to incorporate complex query.
        final int day = c.get(Calendar.DAY_OF_WEEK);
        final String sDay = Constants.getDay(day);

        //wire up the Rocket

        mFirestore.collection(LECTEACHTIMECOL)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {

                        String userId = mAuth.getCurrentUser().getUid();

                        mFirestore.collection(STUDENTDETAILSCOL).document(userId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String currentsemester = documentSnapshot.getString("currentsemester");
                                        String currentyear = documentSnapshot.getString("currentyear");
                                        String course = documentSnapshot.getString("course");
                                        String yearofstudy = documentSnapshot.getString("yearofstudy");

                                        // form query

                                        if (documentSnapshot.exists() && course != null) {

                                            Query mQuery = queryDocumentSnapshots
                                                    .getQuery()
                                                    .whereArrayContains("courses.course", course)
                                                    .whereEqualTo("day", sDay)
                                                    .whereEqualTo("semester", currentsemester)
                                                    .whereEqualTo("studyyear", currentyear)
                                                    .orderBy("time", Query.Direction.ASCENDING);

                                            //double ArrayContains not supported
                                            //.whereArrayContains("courses.yearofstudy", yearofstudy)

                                            workOnClassLiveData(mQuery);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                // errors happened
                                listLecTeachTimeResult.postValue(null);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //  errors
                listLecTeachTimeResult.postValue(null);
            }
        });
    }

    private void workOnClassLiveData(final Query query) {

        final FirebaseQueryLiveData mFirebaseQueryLiveData = new FirebaseQueryLiveData(query);
        //wire up the query
        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .execute(new Runnable() {
                    @Override
                    public void run() {


                        lecTechTimeLiveData.addSource(mFirebaseQueryLiveData, new Observer<QuerySnapshot>() {
                            @Override
                            public void onChanged(@Nullable final QuerySnapshot queryDocSnapshots) {

                                if (queryDocSnapshots != null) {
                                    //heavy task
                                    lecTechTimeLiveData.postValue(queryDocSnapshots.toObjects(LecTeachTime.class));
                                    Log.d("TEST", "================>: queryDocSnapshots "+queryDocSnapshots.toObjects(LecTeachTime.class).toString());
                                } else {
                                    lecTechTimeLiveData.postValue(null);
                                    Log.d("TEST", "================>: queryDocSnapshots "+queryDocSnapshots.toObjects(LecTeachTime.class).toString());
                                }
                            }
                        });
                    }
                });
    }

    public MediatorLiveData<List<LecTeachTime>> getLecTechTimeLiveData() {
        return lecTechTimeLiveData;
    }

    public MediatorLiveData<List<LecTeachTime>> getLecTechTimeResult(String yearofstudy) {

        List<LecTeachTime> localList = new ArrayList<>();
        //list used to populate our class list

        //called on observer to check for null
        for (LecTeachTime lecTeachTime : getLecTechTimeLiveData().getValue()) {
            //we get student course
            for (CourseYear c : lecTeachTime.getCourses()) {
                if (c.getYearofstudy() == Integer.valueOf(yearofstudy)) {
                    localList.add(lecTeachTime);
                }
            }
        }

        listLecTeachTimeResult.setValue(localList);

        return listLecTeachTimeResult;
    }

    public MediatorLiveData<StudentDetails> getLecUserMediatorLiveData() {
        return studUserMediatorLiveData;
    }

    /**
     * Factory for instantiating the viewmodel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private FirebaseAuth mAuth;
        private FirebaseFirestore mFirestore;

        public Factory(@NonNull Application mApplication, FirebaseAuth mAuth, FirebaseFirestore mFirestore) {
            this.mApplication = mApplication;
            this.mAuth = mAuth;
            this.mFirestore = mFirestore;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new AccountSetupViewModel(mApplication, mAuth, mFirestore);
        }
    }
}
