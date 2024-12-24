package com.iocl.Dispatch_Portal_Application.Security;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.type.StringType;

public class PostgreSQL94CustomDialect extends PostgreSQL94Dialect {

    public PostgreSQL94CustomDialect() {
        super();
        // Registering PostgreSQL array type for String array
        this.registerHibernateType(2003, StringType.INSTANCE.getName());
    }
}