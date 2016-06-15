package br.com.amaro.manoel.homeautomation;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by manoel on 14/06/16.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;
}
