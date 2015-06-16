package io.androoid.libraryproof.utils;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Juan Carlos García
 * @since 1.0
 */
public class DatabaseConfigUtils extends OrmLiteConfigUtil{

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt");
    }
}
