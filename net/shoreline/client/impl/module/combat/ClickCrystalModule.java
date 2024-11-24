package net.shoreline.client.impl.module.combat;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1774;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2824;
import net.minecraft.class_2885;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.BooleanConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.RotationModule;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.network.PlayerTickEvent;
import net.shoreline.client.impl.event.world.AddEntityEvent;
import net.shoreline.client.impl.event.world.RemoveEntityEvent;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.player.RotationUtil;

public class ClickCrystalModule extends RotationModule {
   Config<Float> breakDelayConfig = new NumberConfig("SpawnDelay", "Speed to break crystals after spawning", 0.0F, 0.0F, 20.0F);
   Config<Float> randomDelayConfig = new NumberConfig("RandomDelay", "Randomized break delay", 0.0F, 0.0F, 5.0F);
   Config<Boolean> rotateConfig = new BooleanConfig("Rotate", "Rotate before breaking", false);
   Config<Boolean> randomRotateConfig = new BooleanConfig("Rotate-Random", "Slightly randomizes rotations", false, () -> {
      return (Boolean)this.rotateConfig.getValue();
   });
   private final Set<class_2338> placedCrystals = new HashSet();
   private final Map<class_1511, Long> spawnedCrystals = new LinkedHashMap();
   private float randomDelay = -1.0F;

   public ClickCrystalModule() {
      super("ClickCrystal", "Automatically breaks placed crystals", ModuleCategory.COMBAT);
   }

   @EventListener
   public void onPlayerTick(PlayerTickEvent event) {
      if (!this.spawnedCrystals.isEmpty()) {
         Entry<class_1511, Long> e = (Entry)this.spawnedCrystals.entrySet().iterator().next();
         class_1511 crystalEntity = (class_1511)e.getKey();
         Long time = (Long)e.getValue();
         if (this.randomDelay == -1.0F) {
            this.randomDelay = (Float)this.randomDelayConfig.getValue() == 0.0F ? 0.0F : RANDOM.nextFloat((Float)this.randomDelayConfig.getValue() * 25.0F);
         }

         float breakDelay = (Float)this.breakDelayConfig.getValue() * 50.0F + this.randomDelay;
         if (mc.field_1724.method_33571().method_1025(crystalEntity.method_19538()) <= 12.25D && (float)(System.currentTimeMillis() - time) >= breakDelay) {
            if ((Boolean)this.rotateConfig.getValue()) {
               class_243 rotatePos = crystalEntity.method_19538();
               if ((Boolean)this.randomRotateConfig.getValue()) {
                  class_238 bb = crystalEntity.method_5829();
                  rotatePos = new class_243(RANDOM.nextDouble(bb.field_1323, bb.field_1320), RANDOM.nextDouble(bb.field_1322, bb.field_1325), RANDOM.nextDouble(bb.field_1321, bb.field_1324));
               }

               float[] rotations = RotationUtil.getRotationsTo(mc.field_1724.method_33571(), rotatePos);
               this.setRotation(rotations[0], rotations[1]);
            }

            Managers.NETWORK.sendPacket(class_2824.method_34206(crystalEntity, mc.field_1724.method_5715()));
            mc.field_1724.method_6104(class_1268.field_5808);
            this.randomDelay = -1.0F;
         }

      }
   }

   @EventListener
   public void onPacketOutbound(PacketEvent.Outbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2885) {
         class_2885 packet = (class_2885)var3;
         if (!event.isClientPacket() && mc.field_1724.method_5998(packet.method_12546()).method_7909() instanceof class_1774) {
            this.placedCrystals.add(packet.method_12543().method_17777());
         }
      }

   }

   @EventListener
   public void onAddEntity(AddEntityEvent event) {
      class_1297 var3 = event.getEntity();
      if (var3 instanceof class_1511) {
         class_1511 crystalEntity = (class_1511)var3;
         class_2338 base = crystalEntity.method_24515().method_10074();
         if (this.placedCrystals.contains(base)) {
            this.spawnedCrystals.put(crystalEntity, System.currentTimeMillis());
            this.placedCrystals.remove(base);
         }
      }

   }

   @EventListener
   public void onRemoveEntity(RemoveEntityEvent event) {
      class_1297 var3 = event.getEntity();
      if (var3 instanceof class_1511) {
         class_1511 crystalEntity = (class_1511)var3;
         this.spawnedCrystals.remove(crystalEntity);
      }

   }
}
