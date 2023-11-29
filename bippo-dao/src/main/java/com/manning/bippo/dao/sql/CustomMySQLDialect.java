package com.manning.bippo.dao.sql;

import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.DoubleType;

public class CustomMySQLDialect extends MySQL5Dialect {

    public CustomMySQLDialect() {
        super();

        // Fulltext queries, MATCH ? AGAINST (?)
        super.registerFunction("match", new SQLFunctionTemplate(DoubleType.INSTANCE, "match(?1) against (?2 in boolean mode)"));
    }
}
