package com.koral.sockservice;

import com.koral.sockservice.service.SocksServiceTest;
import com.koral.sockservice.util.CsvParserTest;
import com.koral.sockservice.util.XlsxParserTest;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("JUnit Platform Suite Demo")
@SelectClasses( {SocksServiceTest.class, CsvParserTest.class, XlsxParserTest.class})
@IncludeClassNamePatterns(".*Test")
@SelectPackages("java.test.com.koral.sockservice.*")
class SockServiceApplicationTests {

    @Test
    void contextLoads() {

    }

}
