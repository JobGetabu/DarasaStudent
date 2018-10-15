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
    private int scanCount = 1;

    public ScanViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ClassScanRepository(application);

    }

    public void insert (ClassScan classScan){
        mRepository.insert(classScan);
    }

    public LiveData<List<ClassScan>> getScannedClasses(){
        return mRepository.getScannedClasses();
    }

    public LiveData<List<ClassScan>> getDateScannedClasses(final Date today){
        return mRepository.getDateScannedClasses(today);
    }

    public LiveData<List<ClassScan>> getTodayScannedClasses(String today){
        return mRepository.getTodayScannedClasses(today);
    }

    public int getScanCount() {
        return scanCount;
    }

    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }
}
