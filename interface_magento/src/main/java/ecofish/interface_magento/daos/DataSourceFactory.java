package ecofish.interface_magento.daos;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceFactory {

	private static MysqlDataSource dataSource;

	public static DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = new MysqlDataSource();
			dataSource.setURL("jdbc:mysql://77.72.0.118:3308/nvwebcou_Ecofish_dump");
			dataSource.setUser("nvwebcou_Eco_dump");
			dataSource.setPassword("qYF8d0nsqgC(");
		}
		return dataSource;
	}
}
