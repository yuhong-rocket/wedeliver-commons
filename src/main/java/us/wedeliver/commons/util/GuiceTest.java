package us.wedeliver.commons.util;

import java.util.Properties;
import java.util.concurrent.Callable;

import com.google.inject.Injector;

public class GuiceTest {
  public static Injector injector;

  static {
    injector = ExceptionUtil.unchecked(new Callable<Injector>() {

      @Override
      public Injector call() throws Exception {
        Properties properties = PropertiesUtil.load("/junit.properties");
        String moduleNames = properties.getProperty("junit.guice.modules");
        String overrideModuleNames = properties.getProperty("junit.guice.override_modules");
        return GuiceUtil.createInjector(moduleNames, overrideModuleNames);
      }
    });

    Runtime.getRuntime().addShutdownHook(new Thread() {

      @Override
      public void run() {
        ShutdownSupport shutdownSupport = injector.getInstance(ShutdownSupport.class);
        shutdownSupport.shutdown();
      }
    });
  }

  public <T> T find(Class<T> type) {
    return injector.getInstance(type);
  }

}