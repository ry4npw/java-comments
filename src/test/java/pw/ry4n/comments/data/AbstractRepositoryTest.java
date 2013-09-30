package pw.ry4n.comments.data;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.DefaultOperationListener;
import org.dbunit.IDatabaseTester;
import org.dbunit.IOperationListener;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.dbunit.dataset.datatype.CustomDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pw.ry4n.comments.webapp.config.AppConfig;
import pw.ry4n.comments.webapp.config.DatabaseConfig;

/**
 * A base DBUnit unit test that provides support for loading CSV files into the
 * database before each test. To successfully use this base class, you need to
 * have the following files in the same folder as your repository unit tests:
 * 
 * <ul>
 * <li>a table-ordering.txt file listing one table name per line in the order
 * the tables are to be loaded</li>
 * <li>a number of CSV files named after the table. the first line of the CSV
 * file should be the column names followed by 1 line per database row</li>
 * </ul>
 * 
 * @author Ryan Powell
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, DatabaseConfig.class })
public abstract class AbstractRepositoryTest {
	private IDatabaseTester databaseTester;
	private IOperationListener operationListener;

	@Inject
	private DataSource dataSource;

	@Before
	public void setUpDatabase() throws Exception {
		try {
			databaseTester = new DataSourceDatabaseTester(dataSource);
			databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
			databaseTester.setOperationListener(getOperationListener());

			IDataSet dataSet = getDataSet();
			databaseTester.setDataSet(dataSet);

			databaseTester.onSetup();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * <p>
	 * Creates an operational listener that listens for connection requests.
	 * </p>
	 * 
	 * <p>
	 * When a new connection has been created, invoke the
	 * {@link #setUpDatabaseConfig(org.dbunit.database.DatabaseConfig)} method
	 * so that user defined parameters can be set.
	 * </p>
	 */
	protected IOperationListener getOperationListener() {
		if (this.operationListener == null) {
			this.operationListener = new DefaultOperationListener() {
				public void connectionRetrieved(IDatabaseConnection connection) {
					super.connectionRetrieved(connection);
					setUpDatabaseConfig(connection.getConfig());
				}
			};
		}
		return this.operationListener;
	}

	/**
	 * You can override to change the DatabaseConfig. This method currently
	 * overrides the Timestamp data type to accept multiple date formats.
	 * 
	 * @param config
	 */
	protected void setUpDatabaseConfig(org.dbunit.database.DatabaseConfig config) {
		config.setProperty(
				org.dbunit.database.DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
				new CustomDataTypeFactory());
	}

	/**
	 * You can override this method if your CSV and table-ordering.txt are in a
	 * different location than the unit test.
	 * 
	 * @return the IDataSet to load
	 * @throws DataSetException
	 */
	protected IDataSet getDataSet() throws DataSetException {
		// returns CSV files in the current directory
		return new CsvURLDataSet(getClass().getResource(""));
	}

	@After
	public void tearDownDatabase() throws Exception {
		if (databaseTester != null) {
			databaseTester.onTearDown();
		}
	}
}
