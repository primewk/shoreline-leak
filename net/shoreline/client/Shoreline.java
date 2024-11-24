package net.shoreline.client;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import net.shoreline.client.api.Identifiable;
import net.shoreline.client.api.event.handler.EventBus;
import net.shoreline.client.api.event.handler.EventHandler;
import net.shoreline.client.api.file.ClientConfiguration;
import net.shoreline.client.init.Managers;
import net.shoreline.client.init.Modules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Shoreline {
   public static Logger LOGGER;
   public static EventHandler EVENT_HANDLER;
   public static ClientConfiguration CONFIG;
   public static ShutdownHook SHUTDOWN;
   public static Executor EXECUTOR;

   public static void init() {
      LOGGER = LogManager.getLogger("Shoreline");
      info("This build of Shoreline is on Git hash {} and was compiled on {}", "72d9e9a", "05/12/2024 02:51");
      info("Starting preInit ...");
      EXECUTOR = Executors.newFixedThreadPool(1);
      EVENT_HANDLER = new EventBus();
      info("Starting init ...");
      Managers.init();
      Modules.init();
      info("Starting postInit ...");
      CONFIG = new ClientConfiguration();
      Managers.postInit();
      SHUTDOWN = new ShutdownHook();
      Runtime.getRuntime().addShutdownHook(SHUTDOWN);
      CONFIG.loadClient();
   }

   public static void info(String message) {
      LOGGER.info(String.format("[Shoreline] %s", message));
   }

   public static void info(String message, Object... params) {
      LOGGER.info(String.format("[Shoreline] %s", message), params);
   }

   public static void info(Identifiable feature, String message) {
      LOGGER.info(String.format("[%s] %s", feature.getId(), message));
   }

   public static void info(Identifiable feature, String message, Object... params) {
      LOGGER.info(String.format("[%s] %s", feature.getId(), message), params);
   }

   public static void error(String message) {
      LOGGER.error(message);
   }

   public static void error(String message, Object... params) {
      LOGGER.error(message, params);
   }

   public static void error(Identifiable feature, String message) {
      LOGGER.error(String.format("[%s] %s", feature.getId(), message));
   }

   public static void error(Identifiable feature, String message, Object... params) {
      LOGGER.error(String.format("[%s] %s", feature.getId(), message), params);
   }
}
