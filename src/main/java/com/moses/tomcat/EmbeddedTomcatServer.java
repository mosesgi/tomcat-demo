package com.moses.tomcat;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 * 嵌入式Tomcat服务器
 */
public class EmbeddedTomcatServer {
    public static void main(String[] args) throws Exception {
        //classes目录 绝对路径
        // idea_workapce
        String classesPath = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes";
        System.out.println(classesPath);

        Tomcat tomcat = new Tomcat();
        //设置端口
        tomcat.setPort(9090);
        //设置Host
        Host host = tomcat.getHost();
        host.setName("localhost");
        host.setAppBase("webapps");

        //设置Context
        //tomcat/src/main/webapp
        String webapp = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "webapp";
        String contextPath = "/";
        //设置WebApp绝对路径到Context, 作为它的docBase
        Context context = tomcat.addWebapp(contextPath, webapp);
        if(context instanceof StandardContext){
            StandardContext standardContext = (StandardContext) context;
            //设置默认的Web.xml文件到Context
            standardContext.setDefaultWebXml(classesPath + File.separator + "conf/web.xml");    //Default servlet

            //设置Classpath到Context
            //添加DemoServlet到Tomcat容器
            Wrapper wrapper = tomcat.addServlet(contextPath, "DemoServlet", new DemoServlet());
            wrapper.addMapping("/demo");
        }

        //设置Service
        Service service = tomcat.getService();
        //设置Connector
        tomcat.getConnector().setURIEncoding("UTF-8");

//        Connector  connector = new Connector();
//        connector.setPort(9090);
//        connector.setURIEncoding("UTF-8");
//        connector.setProtocol("HTTP/1.1");
//        service.addConnector(connector);

        //启动Tomcat服务器
        tomcat.start();
        //强制Tomcat Server等待，避免main线程执行结束关闭
        tomcat.getServer().await();
    }
}
