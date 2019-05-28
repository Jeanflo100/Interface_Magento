package ecofish.interface_magento.daos;

import java.io.File;
import java.io.IOException;
import org.ini4j.Wini;

import javax.sql.DataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceFactory {

	private static MysqlDataSource dataSource;
	private static String server;
	private static String port;
	private static String username;
	private static String password;
	private static String name;
	
	public static DataSource getDataSource() {
		if (dataSource == null) {
			initDatabaseConnectionData();
			dataSource = new MysqlDataSource();
			dataSource.setURL("jdbc:mysql://" + server + ":" + port + "/" + name);
			dataSource.setUser(username);
			dataSource.setPassword(password);
		}
		return dataSource;
	}
	
	private static void initDatabaseConnectionData() {
		try {
			Wini ini = new Wini(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "config.ini"));
			server = ini.get("database", "server");
			port = ini.get("database", "port");
			username = ini.get("database", "username");
			password = ini.get("database", "password");
			name = ini.get("database", "name");
		}
		catch (IOException e){
			System.out.println("Error when recovering database connection data :");
			System.out.println(e.getMessage());
		}
	}
	
}
