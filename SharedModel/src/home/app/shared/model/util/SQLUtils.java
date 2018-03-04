package home.app.shared.model.util;

import java.sql.CallableStatement;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

import oracle.jbo.JboException;
import oracle.jbo.server.DBTransaction;

public class SQLUtils {

    public SQLUtils() {
        super();
    }

    public static void callStoredProcedure(DBTransaction dbt, final String fullyQualifiedSql, final Object[] params) {
        CallableStatement stmt = null;
        try {
            stmt = dbt.createCallableStatement(fullyQualifiedSql, dbt.DEFAULT);
            for (int index = 1; index <= params.length; index++) {
                stmt.setObject(index, params[index - 1]);
            }
            stmt.execute();
        } catch (SQLException e) {
            throw new JboException(e);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se) {
                throw new JboException(se);
            }
        }
    }

    public static Map<Integer, Object> callStoredProcedure(DBTransaction dbt, final String fullyQualifiedSql,
                                                           final Map<Integer, Object> inputParams,
                                                           final Map<Integer, Integer> outputParams) {
        Map<Integer, Object> returnMap = new HashMap<Integer, Object>();
        CallableStatement stmt = null;
        try {
            stmt = dbt.createCallableStatement(fullyQualifiedSql, dbt.DEFAULT);
            int pos = 0;
            if (inputParams != null) {
                for (Map.Entry input : inputParams.entrySet()) {
                    pos = (Integer) input.getKey();
                    stmt.setObject(pos, input.getValue());
                }
            }
            if (outputParams != null) {
                for (Map.Entry output : outputParams.entrySet()) {
                    pos = (Integer) output.getKey();
                    stmt.registerOutParameter(pos, (Integer) output.getValue());
                }
            }
            stmt.execute();
            if (outputParams != null) {
                for (Map.Entry output : outputParams.entrySet()) {
                    pos = (Integer) output.getKey();
                    returnMap.put(pos, stmt.getObject(pos));
                }
            }
        } catch (SQLException e) {
            throw new JboException(e);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se) {
                throw new JboException(se);
            }
        }
        return returnMap;
    }
}

