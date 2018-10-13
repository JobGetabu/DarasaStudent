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

    private ClassScanDao classScanDao;

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

    public LiveData<List<ClassScan>> getTodayScannedClasses(final Date today){
       return classScanDao.getTodayScannedClasses(today);
    }

    public LiveData<List<ClassScan>> getTodayScannedClasses(String today){
        return classScanDao.getTodayScannedClasses(today);
    }
}
