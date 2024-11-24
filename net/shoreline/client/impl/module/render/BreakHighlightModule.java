package net.shoreline.client.impl.module.render;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_259;
import net.minecraft.class_2596;
import net.minecraft.class_2620;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3191;
import net.minecraft.class_3532;
import net.shoreline.client.api.config.Config;
import net.shoreline.client.api.config.setting.ColorConfig;
import net.shoreline.client.api.config.setting.EnumConfig;
import net.shoreline.client.api.config.setting.NumberConfig;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.api.module.ModuleCategory;
import net.shoreline.client.api.module.ToggleModule;
import net.shoreline.client.api.render.RenderManager;
import net.shoreline.client.impl.event.network.PacketEvent;
import net.shoreline.client.impl.event.render.RenderWorldEvent;
import net.shoreline.client.init.Modules;
import net.shoreline.client.mixin.accessor.AccessorWorldRenderer;
import net.shoreline.client.util.world.BlastResistantBlocks;

public class BreakHighlightModule extends ToggleModule {
   Config<BreakHighlightModule.HighlightMode> modeConfig;
   Config<Float> rangeConfig;
   Config<Color> colorConfig;
   private final Map<class_2620, Long> breakingProgress;

   public BreakHighlightModule() {
      super("BreakHighlight", "Highlights blocks that are being broken", ModuleCategory.RENDER);
      this.modeConfig = new EnumConfig("Mode", "The mode for highlighting blocks", BreakHighlightModule.HighlightMode.PACKET, BreakHighlightModule.HighlightMode.values());
      this.rangeConfig = new NumberConfig("Range", "The range to render breaking blocks", 5.0F, 10.0F, 50.0F);
      this.colorConfig = new ColorConfig("Color", "The break highlight color", new Color(255, 0, 0), false, true);
      this.breakingProgress = new ConcurrentHashMap();
   }

   @EventListener
   public void onPacketInbound(PacketEvent.Inbound event) {
      class_2596 var3 = event.getPacket();
      if (var3 instanceof class_2620) {
         class_2620 packet = (class_2620)var3;
         if (!this.contains(packet.method_11277()) && !BlastResistantBlocks.isUnbreakable(packet.method_11277())) {
            this.breakingProgress.put(packet, System.currentTimeMillis());
         }
      }

   }

   @EventListener
   public void onRenderWorld(RenderWorldEvent event) {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         double dx;
         double dy;
         double dz;
         if (this.modeConfig.getValue() == BreakHighlightModule.HighlightMode.NORMAL) {
            Int2ObjectMap<class_3191> blockBreakProgressions = ((AccessorWorldRenderer)mc.field_1769).getBlockBreakingProgressions();
            ObjectIterator var3 = Int2ObjectMaps.fastIterable(blockBreakProgressions).iterator();

            while(var3.hasNext()) {
               Entry<class_3191> info = (Entry)var3.next();
               class_2338 pos = ((class_3191)info.getValue()).method_13991();
               double dist = mc.field_1724.method_5707(pos.method_46558());
               if (!(dist > ((NumberConfig)this.rangeConfig).getValueSq())) {
                  int damage = ((class_3191)info.getValue()).method_13988();
                  class_2680 state = mc.field_1687.method_8320(pos);
                  class_265 outlineShape = state.method_26218(mc.field_1687, pos);
                  if (!outlineShape.method_1110()) {
                     class_238 bb = outlineShape.method_1107();
                     bb = new class_238((double)pos.method_10263() + bb.field_1323, (double)pos.method_10264() + bb.field_1322, (double)pos.method_10260() + bb.field_1321, (double)pos.method_10263() + bb.field_1320, (double)pos.method_10264() + bb.field_1325, (double)pos.method_10260() + bb.field_1324);
                     double x = bb.field_1323 + (bb.field_1320 - bb.field_1323) / 2.0D;
                     double y = bb.field_1322 + (bb.field_1325 - bb.field_1322) / 2.0D;
                     dx = bb.field_1321 + (bb.field_1324 - bb.field_1321) / 2.0D;
                     dy = (double)damage * ((bb.field_1320 - x) / 9.0D);
                     dz = (double)damage * ((bb.field_1325 - y) / 9.0D);
                     double sizeZ = (double)damage * ((bb.field_1324 - dx) / 9.0D);
                     RenderManager.renderBox(event.getMatrices(), new class_238(x - dy, y - dz, dx - sizeZ, x + dy, y + dz, dx + sizeZ), Modules.COLORS.getRGB(60));
                     RenderManager.renderBoundingBox(event.getMatrices(), new class_238(x - dy, y - dz, dx - sizeZ, x + dy, y + dz, dx + sizeZ), 1.5F, Modules.COLORS.getRGB(125));
                  }
               }
            }

         } else {
            Iterator var25 = this.breakingProgress.entrySet().iterator();

            while(true) {
               while(var25.hasNext()) {
                  java.util.Map.Entry<class_2620, Long> mine = (java.util.Map.Entry)var25.next();
                  class_2338 mining = ((class_2620)mine.getKey()).method_11277();
                  long elapsedTime = System.currentTimeMillis() - (Long)mine.getValue();

                  for(long count = this.breakingProgress.keySet().stream().filter((p) -> {
                     return p.method_11280() == ((class_2620)mine.getKey()).method_11280();
                  }).count(); count > 2L; --count) {
                     this.breakingProgress.entrySet().stream().filter((p) -> {
                        return ((class_2620)p.getKey()).method_11280() == ((class_2620)mine.getKey()).method_11280();
                     }).min(Comparator.comparingLong(java.util.Map.Entry::getValue)).ifPresent((min) -> {
                        this.breakingProgress.remove(min.getKey(), min.getValue());
                     });
                  }

                  if (!mc.field_1687.method_22347(mining) && elapsedTime <= 2500L) {
                     double dist = mc.field_1724.method_5707(mining.method_46558());
                     if (!(dist > ((NumberConfig)this.rangeConfig).getValueSq())) {
                        class_265 outlineShape = mc.field_1687.method_8320(mining).method_26218(mc.field_1687, mining);
                        outlineShape = outlineShape.method_1110() ? class_259.method_1077() : outlineShape;
                        class_238 render1 = outlineShape.method_1107();
                        class_238 render = new class_238((double)mining.method_10263() + render1.field_1323, (double)mining.method_10264() + render1.field_1322, (double)mining.method_10260() + render1.field_1321, (double)mining.method_10263() + render1.field_1320, (double)mining.method_10264() + render1.field_1325, (double)mining.method_10260() + render1.field_1324);
                        class_243 center = render.method_1005();
                        float scale = class_3532.method_15363((float)elapsedTime / 2500.0F, 0.0F, 1.0F);
                        dx = (render1.field_1320 - render1.field_1323) / 2.0D;
                        dy = (render1.field_1325 - render1.field_1322) / 2.0D;
                        dz = (render1.field_1324 - render1.field_1321) / 2.0D;
                        class_238 scaled = (new class_238(center, center)).method_1009(dx * (double)scale, dy * (double)scale, dz * (double)scale);
                        RenderManager.renderBox(event.getMatrices(), scaled, ((ColorConfig)this.colorConfig).getValue(60).getRGB());
                        RenderManager.renderBoundingBox(event.getMatrices(), scaled, 1.5F, ((ColorConfig)this.colorConfig).getValue(125).getRGB());
                     }
                  } else {
                     this.breakingProgress.remove(mine.getKey(), mine.getValue());
                  }
               }

               return;
            }
         }
      }
   }

   private boolean contains(class_2338 pos) {
      return this.breakingProgress.keySet().stream().anyMatch((p) -> {
         return p.method_11277().equals(pos);
      });
   }

   private static enum HighlightMode {
      NORMAL,
      PACKET;

      // $FF: synthetic method
      private static BreakHighlightModule.HighlightMode[] $values() {
         return new BreakHighlightModule.HighlightMode[]{NORMAL, PACKET};
      }
   }
}
