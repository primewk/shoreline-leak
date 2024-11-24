package net.shoreline.client;

public class ShutdownHook extends Thread {
   public ShutdownHook() {
      this.setName("Shoreline-ShutdownHook");
   }

   public void run() {
      Shoreline.info("Saving configurations and shutting down!");
      Shoreline.CONFIG.saveClient();
   }
}
