package com.example.infolinetestapplication.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.infolinetestapplication.entity.Login;
import com.example.infolinetestapplication.entity.Person;
import com.example.infolinetestapplication.entity.Record;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by suleyman on 31.3.2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // database name
    private static final String DATABASE_NAME = "test.db";
    // database version
    private static final int DATABASE_VERSION = 1;

    //  Person tablosuna ulaşmayı sağlayan Data Access Object
    private Dao<Person, Integer> personDao = null;

    //  Login tablosuna ulaşmayı sağlayan Data Access Object
    private Dao<Login, Integer> loginDao = null;

    //  Record tablosuna ulaşmayı sağlayan Data Access Object
    private Dao<Record, Integer> recordDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Person.class); // Eğer yok ise person,login ve record tablolarını yaratıyoruz
            TableUtils.createTableIfNotExists(connectionSource, Login.class);
            TableUtils.createTableIfNotExists(connectionSource, Record.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }

    public Dao<Person, Integer> getPersonDao() throws SQLException {
        if (personDao == null) {
            personDao = getDao(Person.class);
        }
        return personDao;
    }

    public Dao<Login, Integer> getLoginDao() throws SQLException {
        if (loginDao == null) {
            loginDao = getDao(Login.class);
        }
        return loginDao;
    }

    public Dao<Record, Integer> getRecordDao() throws SQLException {
        if (recordDao == null) {
            recordDao = getDao(Record.class);
        }
        return recordDao;
    }


    // database i initialize eden method
    // db de kullanıcı adı admin ve şifresi 14562335 olan tek kullanıcıyı insert ediyor.
    public void initializeDb() {
        try {

            personDao = getPersonDao();
            loginDao  = getLoginDao();
            recordDao = getRecordDao();
            List<Person> personList = personDao.queryForAll();
            if(personList.size() == 0) { // eğer yoksa kayıt ekler
                Person person = new Person();
                person.setUsername(Variables.USERNAME);
                person.setPassword(Encoder.encrypt(Variables.PASSWORD));
                personDao.create(person);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //loginde girilen kullanıcı adı ve şifreli kullanıcı olup
    // olmadığını kontrol eder
    public Person queryPerson(String username, String password) {
        try {

            List<Person> result = personDao.queryBuilder().where().eq(Person.USERNAME_TEXT_FIELD, username).and().eq(Person.PASSWORD_TEXT_FIELD, Encoder.encrypt(password)).query();
            if(result == null || result.size() == 0) {
                return null;
            } else {
                return result.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Person queryPerson(int personId) {
        try {

            Person result = personDao.queryForId(personId);
            if(result == null) {
                return null;
            } else {
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // son success loginin tarihi setlenir
    public void updateLoginTime(Long loginTime, Person person) {
        try {
            List<Login> result = loginDao.queryBuilder().where().eq(Login.PERSON_ID_TEXT_FIELD, person.getId()).query();
            if(result == null || result.size() == 0) { // first login record
                Login firstLogin = new Login();
                firstLogin.setPersonId(person.getId());
                firstLogin.setLoginTime(loginTime);
                loginDao.create(firstLogin);
            } else { // login time will be updated
                Login login = result.get(0);
                login.setLoginTime(loginTime);
                loginDao.update(login);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Login queryLogin(Long personId) {
        try {

            List<Login> result = loginDao.queryBuilder().where().eq(Login.PERSON_ID_TEXT_FIELD, personId).query();
            if(result == null || result.size() == 0) {
                return null;
            } else {
                return result.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Record createRecord(String snapshotPath, String videoPath) {
        try {
            Record record = new Record();
            record.setVideoPath(videoPath);
            record.setSnapshotPath(snapshotPath);
            recordDao.create(record);
            return record;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
