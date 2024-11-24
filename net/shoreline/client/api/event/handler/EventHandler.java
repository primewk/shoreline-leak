package net.shoreline.client.api.event.handler;

import net.shoreline.client.api.event.Event;

public interface EventHandler {
   void subscribe(Object var1);

   void unsubscribe(Object var1);

   boolean dispatch(Event var1);
}
