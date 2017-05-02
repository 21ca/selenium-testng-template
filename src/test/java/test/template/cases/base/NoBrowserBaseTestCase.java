package test.template.cases.base;

public abstract class NoBrowserBaseTestCase extends BaseTestCase {
	protected boolean isBrowserUsed() {
		return false;
	}
}
