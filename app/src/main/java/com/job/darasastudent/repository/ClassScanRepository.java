package com.job.darasastudent.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.job.darasastudent.appexecutor.DefaultExecutorSupplier;
import com.job.darasastudent.datasource.ClassRoomDatabase;
import com.job.darasastudent.model.ClassScan;

import java.util.Date;
import java.util.List;

/**
 * Created by Job on Saturday : 10/13/2018.
 */
public class ClassScanRepository {

    public static final String TAG = "Repository";

    private ClassScanDao classScanDao;
    private LiveData<List<ClassScan>> mScannedClasses = new LiveData<List<ClassScan>>() {};
    private LiveData<List<ClassScan>> mDateScannedClasses = new LiveData<List<ClassScan>>() {};
    private LiveData<List<ClassScan>> mTodayScannedClasses = new LiveData<List<ClassScan>>() {};

    public ClassScanRepository(Application application) {
        ClassRoomDatabase db = ClassRoomDatabase.getDatabase(application);
        classScanDao = db.classScanDao();

    }

    public void insert(final ClassScan classScan) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .submit(new Runnable() {
                    @Override
                    public void run() {
                        classScanDao.insert(classScan);
                    }
                });
    }

    public LiveData<List<ClassScan>> getScannedClasses() {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .submit(new Runnable() {
                    @Override
                    public void run() {
                        mScannedClasses = classScanDao.getAllScannedClasses();

                    }
                });
        return mScannedClasses;
    }

    public LiveData<List<ClassScan>> getDateScannedClasses(final Date today) {


        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .submit(new Runnable() {
                    @Override
                    public void run() {
                        mDateScannedClasses = classScanDao.getDateScannedClasses(today);

                    }
                });
        return mDateScannedClasses;
    }

    public LiveData<List<ClassScan>> getTodayScannedClasses(final String today) {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .submit(new Runnable() {
                    @Override
                    public void run() {
                        mTodayScannedClasses = classScanDao.getTodayScannedClasses(today);
                    }
                });
        return mTodayScannedClasses;
    }
}
