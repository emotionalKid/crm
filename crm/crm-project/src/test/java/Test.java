import com.zhh.crm.commons.util.UUIDUtil;
import com.zhh.crm.settings.interceptor.LoginCheckInterceptor;
import com.zhh.crm.workbench.bean.Activity;
import com.zhh.crm.workbench.controller.ActivityController;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

public class Test {
    @org.junit.Test
    public void test() throws Exception {
        String uuid = UUIDUtil.getUUID();
        System.out.println(uuid);

    }
}
