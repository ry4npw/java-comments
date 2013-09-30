package org.dbunit.dataset.datatype;

import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomDataTypeFactory extends DefaultDataTypeFactory {
	private static final Logger logger = LoggerFactory
			.getLogger(CustomDataTypeFactory.class);

	@SuppressWarnings("rawtypes")
	@Override
	public Collection getValidDbProducts() {
		 return Arrays.asList(new String[]{"HSQL Database Engine"});
	}

	@Override
	public DataType createDataType(int sqlType, String sqlTypeName)
			throws DataTypeException {
		logger.debug("BEGIN createDataType()");
		switch (sqlType) {
		case Types.TIMESTAMP:
			logger.debug("intercepted Timestamp datatype, returning custom value");
			return new CustomTimestampDataType();
		default:
			return super.createDataType(sqlType, sqlTypeName);
		}
	}
}
