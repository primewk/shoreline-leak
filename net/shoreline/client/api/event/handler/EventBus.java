package net.shoreline.client.api.event.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.shoreline.client.api.event.Event;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.event.listener.Listener;

public class EventBus implements EventHandler {
   private final Set<Object> subscribers = Collections.synchronizedSet(new HashSet());
   private final Map<Object, PriorityQueue<Listener>> listeners = new ConcurrentHashMap();

   public void subscribe(Object obj) {
      if (!this.subscribers.contains(obj)) {
         this.subscribers.add(obj);
         Method[] var2 = obj.getClass().getMethods();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Method method = var2[var4];
            method.trySetAccessible();
            if (method.isAnnotationPresent(EventListener.class)) {
               EventListener listener = (EventListener)method.getAnnotation(EventListener.class);
               if (method.getReturnType() == Void.TYPE) {
                  Class<?>[] params = method.getParameterTypes();
                  if (params.length == 1) {
                     PriorityQueue<Listener> active = (PriorityQueue)this.listeners.computeIfAbsent(params[0], (v) -> {
                        return new PriorityQueue();
                     });
                     active.add(new Listener(method, obj, listener.receiveCanceled(), listener.priority()));
                  }
               }
            }
         }

      }
   }

   public void unsubscribe(Object obj) {
      if (this.subscribers.remove(obj)) {
         this.listeners.values().forEach((set) -> {
            set.removeIf((l) -> {
               return l.getSubscriber() == obj;
            });
         });
         this.listeners.entrySet().removeIf((e) -> {
            return ((PriorityQueue)e.getValue()).isEmpty();
         });
      }

   }

   public boolean dispatch(Event event) {
      if (event == null) {
         return false;
      } else {
         PriorityQueue<Listener> active = (PriorityQueue)this.listeners.get(event.getClass());
         if (active != null && !active.isEmpty()) {
            Iterator var3 = (new ArrayList(active)).iterator();

            while(true) {
               Listener listener;
               do {
                  if (!var3.hasNext()) {
                     return event.isCanceled();
                  }

                  listener = (Listener)var3.next();
               } while(event.isCanceled() && !listener.isReceiveCanceled());

               listener.invokeSubscriber(event);
            }
         } else {
            return false;
         }
      }
   }
}
