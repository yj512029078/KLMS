package com.neekle.kunlunandroid.webservices.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class UnitTestManager {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(GeneralUnitTest.class);

		return suite;
	}
}
