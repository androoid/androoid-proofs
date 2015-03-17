package io.androoid.libraryproof.utils;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Juan Carlos García on 17/3/15.
 */
public class DatabaseConfigUtils extends OrmLiteConfigUtil{

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt");
    }
}
