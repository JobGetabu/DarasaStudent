package com.job.darasastudent.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.job.darasastudent.model.ClassScan;
import com.job.darasastudent.repository.ClassScanRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by Job on Saturday : 10/13/2018.
 */
public class ScanViewModel extends AndroidViewModel {

    private ClassScanRepository mRepository;

    public ScanViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ClassScanRepository(application);

    }

    public void insert (ClassScan classScan){
        mRepository.insert(classScan);
    }

    public LiveData<List<ClassScan>> getTodayScannedClasses(final Date today){
        return mRepository.getTodayScannedClasses(today);
    }

    public LiveData<List<ClassScan>> getTodayScannedClasses(String today){
        return mRepository.getTodayScannedClasses(today);
    }
}
