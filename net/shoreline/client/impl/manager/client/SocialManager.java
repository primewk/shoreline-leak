package net.shoreline.client.impl.manager.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.minecraft.class_2561;
import net.shoreline.client.api.social.SocialRelation;
import net.shoreline.client.util.Globals;

public class SocialManager implements Globals {
   private final ConcurrentMap<String, SocialRelation> relationships = new ConcurrentHashMap();

   public boolean isRelation(String name, SocialRelation relation) {
      return this.relationships.get(name) == relation;
   }

   public boolean isFriend(String name) {
      return this.isRelation(name, SocialRelation.FRIEND);
   }

   public boolean isFriend(class_2561 name) {
      return name != null && this.isRelation(name.getString(), SocialRelation.FRIEND);
   }

   public void addRelation(String name, SocialRelation relation) {
      if (mc.field_1724 == null || !name.equals(mc.field_1724.method_5476().getString())) {
         SocialRelation relationship = (SocialRelation)this.relationships.get(name);
         if (relationship != null) {
            this.relationships.replace(name, relation);
         } else {
            this.relationships.put(name, relation);
         }
      }
   }

   public void addFriend(String name) {
      this.addRelation(name, SocialRelation.FRIEND);
   }

   public void addFriend(class_2561 name) {
      this.addRelation(name.getString(), SocialRelation.FRIEND);
   }

   public SocialRelation remove(String playerName) {
      return (SocialRelation)this.relationships.remove(playerName);
   }

   public SocialRelation remove(class_2561 playerName) {
      return (SocialRelation)this.relationships.remove(playerName.getString());
   }

   public Collection<String> getRelations(SocialRelation relation) {
      List<String> friends = new ArrayList();
      Iterator var3 = this.relationships.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, SocialRelation> relationship = (Entry)var3.next();
         if (relationship.getValue() == relation) {
            friends.add((String)relationship.getKey());
         }
      }

      return friends;
   }

   public Collection<String> getFriends() {
      return this.getRelations(SocialRelation.FRIEND);
   }
}
