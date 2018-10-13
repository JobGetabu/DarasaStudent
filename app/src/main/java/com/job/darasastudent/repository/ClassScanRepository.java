package com.job.darasastudent.repository;

import android.app.Application;
import android.util.Log;

import com.job.darasastudent.appexecutor.DefaultExecutorSupplier;
import com.job.darasastudent.datasource.ClassRoomDatabase;
import com.job.darasastudent.model.ClassScan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Job on Saturday : 10/13/2018.
 */
public class ClassScanRepository {

    public static final String TAG = "Repository";

    private ClassScanDao classScanDao;
    private  List<ClassScan> mScannedClasses = new ArrayList<>();
    private  List<ClassScan> mDateScannedClasses = new ArrayList<>();
    private  List<ClassScan> mTodayScannedClasses = new ArrayList<>();

    public ClassScanRepository(Application application) {
        ClassRoomDatabase db = ClassRoomDatabase.getDatabase(application);
        classScanDao = db.classScanDao();

    }

    public void insert (final ClassScan classScan){
        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .submit(new Runnable() {
                    @Override
                    public void run() {
                        classScanDao.insert(classScan);
                    }
                });
    }

    public List<ClassScan> getScannedClasses(){
        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .submit(new Runnable() {
                    @Override
                    public void run() {
                        mScannedClasses = classScanDao.getAllScannedClasses();
                        Log.d(TAG, "mScannedClasses: "+mScannedClasses.size());
                    }
                });
        return mScannedClasses;
    }
    public List<ClassScan> getDateScannedClasses(final Date today){

        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
                .submit(new Runnable() {
                    @Override
                    public void run() {
                        mDateScannedClasses = classScanDao.getDateScannedClasses(today);
                        Log.d(TAG, "mDateScannedClasses: "+mDateScannedClasses.size());
                    }
                });
        return mDateScannedClasses;
    }

    public List<ClassScan> getTodayScannedClasses(final String today){
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
