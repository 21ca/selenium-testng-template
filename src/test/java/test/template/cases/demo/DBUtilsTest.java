package test.template.cases.demo;
import java.sql.Connection;

import org.testng.annotations.Test;

import test.template.cases.base.BaseTestCase;
import test.template.utils.DBRestoreUtils;
import test.template.utils.DBUtils;

public class DBUtilsTest extends BaseTestCase {

	@Test(enabled = false)
	public void test() throws Exception {
		Connection cn = DBUtils.getConnection("oracle_");
//		System.out.println(DBUtils.update(cn, "INSERT INTO mytable1(id) VALUES(9)"));
//		System.out.println(DBUtils.query(cn, "SELECT * FROM mytable1"));
		
		System.out.println(DBUtils.query(cn, "SELECT * FROM TABLE2 WHERE cdate=to_date('2017-06-01', 'YYYY-MM-DD')"));
		System.out.println(DBUtils.query(cn, "SELECT * FROM TABLE2 WHERE cdate=TIMESTAMP '2017-06-01 00:00:00.0'"));
		System.out.println(DBUtils.query(cn, "SELECT * FROM TABLE2 WHERE cdate=DATE '2017-06-01'"));

		DBUtils.update(cn, "UPDATE TABLE2 SET cdate=TIMESTAMP '2017-06-02 00:00:00.0', ctime=TIMESTAMP '2017-06-02 00:00:00.0' WHERE id=1");
//		DBUtils.update(cn, "INSERT INTO TABLE2(ID, CDATE, CTIME) VALUES(2, to_date('2017-06-01', 'YYYY-MM-DD'), to_date('2017-06-01', 'YYYY-MM-DD'))");
		
		String backupId = DBRestoreUtils.backup(cn, "SELECT * FROM TABLE2 WHERE ID=1");
		DBUtils.update(cn, "UPDATE TABLE2 SET cdate=TIMESTAMP '2017-06-02 00:00:00.0', ctime=TIMESTAMP '2017-06-08 00:00:00.0' WHERE id=1");

		DBRestoreUtils.restore(cn, backupId);

		cn.close();
	}

}
