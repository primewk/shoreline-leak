package net.shoreline.client.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Supplier;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.Identifiable;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.impl.event.config.ConfigUpdateEvent;
import net.shoreline.client.util.render.animation.Animation;
import net.shoreline.client.util.render.animation.Easing;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class Config<T> implements Identifiable, Serializable<T> {
   private final String name;
   private final String desc;
   protected T value;
   private final T defaultValue;
   private ConfigContainer container;
   private Supplier<Boolean> visible;
   protected final Animation configAnimation;

   public Config(String name, String desc, T value) {
      this.configAnimation = new Animation(false, 200.0F, Easing.CUBIC_IN_OUT);
      if (value == null) {
         throw new NullPointerException("Null values not supported");
      } else {
         this.name = name;
         this.desc = desc;
         this.value = value;
         this.defaultValue = value;
      }
   }

   public Config(String name, String desc, T value, Supplier<Boolean> visible) {
      this(name, desc, value);
      this.visible = visible;
   }

   @Internal
   public Config(String name, String desc) {
      this.configAnimation = new Animation(false, 200.0F, Easing.CUBIC_IN_OUT);
      this.name = name;
      this.desc = desc;
      this.defaultValue = null;
   }

   public JsonObject toJson() {
      JsonObject obj = new JsonObject();
      obj.addProperty("name", this.getName());
      obj.addProperty("id", this.getId());
      return obj;
   }

   public T fromJson(JsonObject obj) {
      if (obj.has("value")) {
         JsonElement element = obj.get("value");
         return element.getAsByte();
      } else {
         return null;
      }
   }

   public String getName() {
      return this.name;
   }

   public String getId() {
      return String.format("%s-%s-config", this.container.getName().toLowerCase(), this.name.toLowerCase());
   }

   public String getDescription() {
      return this.desc;
   }

   public T getValue() {
      return this.value;
   }

   public void setValue(T val) {
      if (val == null) {
         throw new NullPointerException("Null values not supported!");
      } else {
         ConfigUpdateEvent event = new ConfigUpdateEvent(this);
         event.setStage(EventStage.PRE);
         Shoreline.EVENT_HANDLER.dispatch(event);
         this.value = val;
         event.setStage(EventStage.POST);
         Shoreline.EVENT_HANDLER.dispatch(event);
      }
   }

   public ConfigContainer getContainer() {
      return this.container;
   }

   public void setContainer(ConfigContainer cont) {
      this.container = cont;
   }

   public Animation getAnimation() {
      return this.configAnimation;
   }

   public boolean isVisible() {
      return this.visible != null ? (Boolean)this.visible.get() : true;
   }

   public void resetValue() {
      this.setValue(this.defaultValue);
   }
}
