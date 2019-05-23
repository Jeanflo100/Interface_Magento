package ecofish.interface_magento.daos;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceFactory {

	private static MysqlDataSource dataSource;

	public static DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = new MysqlDataSource();
			
			/*dataSource.setURL("jdbc:mysql://127.0.0.1:3306/ecofish products");
			dataSource.setUser("root");
			dataSource.setPassword("");*/
			
			dataSource.setURL("jdbc:mysql://???/");
			dataSource.setUser("???");
			dataSource.setPassword("???");
			
		}
		return dataSource;
	}
}
