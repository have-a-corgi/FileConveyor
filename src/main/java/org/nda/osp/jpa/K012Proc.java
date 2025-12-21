package org.nda.osp.jpa;

public interface K012Proc {

    int executeProc(String name);
    int executeFunc(String name);
    int executeFuncUsingMetadata(String name);
    int executeFuncUsingJdbcTemplate(String name);

}
