package net.shoreline.client.api.event;

public class StageEvent extends Event {
   private EventStage stage;

   public EventStage getStage() {
      return this.stage;
   }

   public void setStage(EventStage stage) {
      this.stage = stage;
   }
}
