package net.shoreline.client.impl.manager.combat.hole;

import io.netty.util.internal.ConcurrentSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.minecraft.class_2338;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.event.EventStage;
import net.shoreline.client.api.event.listener.EventListener;
import net.shoreline.client.impl.event.TickEvent;
import net.shoreline.client.init.Modules;
import net.shoreline.client.util.Globals;
import net.shoreline.client.util.world.BlastResistantBlocks;
import net.shoreline.client.util.world.BlockUtil;

public class HoleManager implements Globals {
   private final ExecutorService executor = Executors.newFixedThreadPool(1);
   private Future<Set<Hole>> result;
   private Set<Hole> holes = new ConcurrentSet();

   public HoleManager() {
      Shoreline.EVENT_HANDLER.subscribe(this);
   }

   @EventListener
   public void onTickEvent(TickEvent event) {
      if (event.getStage() == EventStage.PRE) {
         HoleManager.HoleTask runnable = new HoleManager.HoleTask(this.getSphere(mc.field_1724.method_19538()));
         this.result = this.executor.submit(runnable);
      }
   }

   public List<class_2338> getSphere(class_243 start) {
      List<class_2338> sphere = new ArrayList();
      double rad = Math.ceil(Math.max(5.0D, Modules.HOLE_ESP.getRange()));

      for(double x = -rad; x <= rad; ++x) {
         for(double y = -rad; y <= rad; ++y) {
            for(double z = -rad; z <= rad; ++z) {
               class_2382 pos = new class_2382((int)(start.method_10216() + x), (int)(start.method_10214() + y), (int)(start.method_10215() + z));
               class_2338 p = new class_2338(pos);
               sphere.add(p);
            }
         }
      }

      return sphere;
   }

   public Hole checkHole(class_2338 pos) {
      if (pos.method_10264() == mc.field_1687.method_31607() && !BlastResistantBlocks.isUnbreakable(pos)) {
         return new Hole(pos, HoleType.VOID, new class_2338[0]);
      } else {
         int resistant = 0;
         int unbreakable = 0;
         if (BlockUtil.isBlockAccessible(pos)) {
            class_2338 pos1 = pos.method_10069(-1, 0, 0);
            class_2338 pos2 = pos.method_10069(0, 0, -1);
            if (BlastResistantBlocks.isBlastResistant(pos1)) {
               ++resistant;
            } else if (BlastResistantBlocks.isUnbreakable(pos1)) {
               ++unbreakable;
            }

            if (BlastResistantBlocks.isBlastResistant(pos2)) {
               ++resistant;
            } else if (BlastResistantBlocks.isUnbreakable(pos2)) {
               ++unbreakable;
            }

            if (resistant + unbreakable < 2) {
               return null;
            } else {
               class_2338 pos3 = pos.method_10069(0, 0, 1);
               class_2338 pos4 = pos.method_10069(1, 0, 0);
               boolean air3 = mc.field_1687.method_22347(pos3);
               boolean air4 = mc.field_1687.method_22347(pos4);
               class_2338[] quad;
               int var13;
               if (air3 && air4) {
                  class_2338 pos5 = pos.method_10069(1, 0, 1);
                  if (!mc.field_1687.method_22347(pos5)) {
                     return null;
                  } else {
                     quad = new class_2338[]{pos.method_10069(-1, 0, 1), pos.method_10069(0, 0, 2), pos.method_10069(1, 0, 2), pos.method_10069(2, 0, 1), pos.method_10069(2, 0, 0), pos.method_10069(1, 0, -1)};
                     class_2338[] var18 = quad;
                     var13 = quad.length;

                     for(int var20 = 0; var20 < var13; ++var20) {
                        class_2338 p = var18[var20];
                        if (BlastResistantBlocks.isBlastResistant(p)) {
                           ++resistant;
                        } else if (BlastResistantBlocks.isUnbreakable(p)) {
                           ++unbreakable;
                        }
                     }

                     if (resistant != 8 && unbreakable != 8 && resistant + unbreakable != 8) {
                        return null;
                     } else {
                        Hole quadHole = new Hole(pos, resistant == 8 ? HoleType.OBSIDIAN : (unbreakable == 8 ? HoleType.BEDROCK : HoleType.OBSIDIAN_BEDROCK), new class_2338[]{pos1, pos2, pos3, pos4, pos5});
                        quadHole.addHoleOffsets(quad);
                        return quadHole;
                     }
                  }
               } else {
                  class_2338[] doubleX;
                  int var12;
                  class_2338 p;
                  Hole doubleXHole;
                  if (air3 && BlockUtil.isBlockAccessible(pos3)) {
                     doubleX = new class_2338[]{pos.method_10069(-1, 0, 1), pos.method_10069(0, 0, 2), pos.method_10069(1, 0, 1), pos.method_10069(1, 0, 0)};
                     quad = doubleX;
                     var12 = doubleX.length;

                     for(var13 = 0; var13 < var12; ++var13) {
                        p = quad[var13];
                        if (BlastResistantBlocks.isBlastResistant(p)) {
                           ++resistant;
                        } else if (BlastResistantBlocks.isUnbreakable(p)) {
                           ++unbreakable;
                        }
                     }

                     if (resistant != 6 && unbreakable != 6 && resistant + unbreakable != 6) {
                        return null;
                     } else {
                        doubleXHole = new Hole(pos, resistant == 6 ? HoleType.OBSIDIAN : (unbreakable == 6 ? HoleType.BEDROCK : HoleType.OBSIDIAN_BEDROCK), new class_2338[]{pos1, pos2, pos3});
                        doubleXHole.addHoleOffsets(doubleX);
                        return doubleXHole;
                     }
                  } else if (air4 && BlockUtil.isBlockAccessible(pos4)) {
                     doubleX = new class_2338[]{pos.method_10069(0, 0, 1), pos.method_10069(1, 0, 1), pos.method_10069(2, 0, 0), pos.method_10069(1, 0, -1)};
                     quad = doubleX;
                     var12 = doubleX.length;

                     for(var13 = 0; var13 < var12; ++var13) {
                        p = quad[var13];
                        if (BlastResistantBlocks.isBlastResistant(p)) {
                           ++resistant;
                        } else if (BlastResistantBlocks.isUnbreakable(p)) {
                           ++unbreakable;
                        }
                     }

                     if (resistant != 6 && unbreakable != 6 && resistant + unbreakable != 6) {
                        return null;
                     } else {
                        doubleXHole = new Hole(pos, resistant == 6 ? HoleType.OBSIDIAN : (unbreakable == 6 ? HoleType.BEDROCK : HoleType.OBSIDIAN_BEDROCK), new class_2338[]{pos1, pos2, pos4});
                        doubleXHole.addHoleOffsets(doubleX);
                        return doubleXHole;
                     }
                  } else {
                     if (BlastResistantBlocks.isBlastResistant(pos3)) {
                        ++resistant;
                     } else if (BlastResistantBlocks.isUnbreakable(pos3)) {
                        ++unbreakable;
                     }

                     if (BlastResistantBlocks.isBlastResistant(pos4)) {
                        ++resistant;
                     } else if (BlastResistantBlocks.isUnbreakable(pos4)) {
                        ++unbreakable;
                     }

                     return resistant != 4 && unbreakable != 4 && resistant + unbreakable != 4 ? null : new Hole(pos, resistant == 4 ? HoleType.OBSIDIAN : (unbreakable == 4 ? HoleType.BEDROCK : HoleType.OBSIDIAN_BEDROCK), new class_2338[]{pos1, pos2, pos3, pos4});
                  }
               }
            }
         } else {
            return null;
         }
      }
   }

   public Set<Hole> getHoles() {
      if (this.result != null) {
         try {
            this.holes = (Set)this.result.get();
         } catch (InterruptedException | ExecutionException var2) {
         }
      }

      return this.holes;
   }

   public class HoleTask implements Callable<Set<Hole>> {
      private final List<class_2338> blocks;

      public HoleTask(List<class_2338> blocks) {
         this.blocks = blocks;
      }

      public Set<Hole> call() throws Exception {
         Set<Hole> holes1 = new HashSet();
         Iterator var2 = this.blocks.iterator();

         while(var2.hasNext()) {
            class_2338 blockPos = (class_2338)var2.next();
            Hole hole = HoleManager.this.checkHole(blockPos);
            if (hole != null) {
               holes1.add(hole);
            }
         }

         return holes1;
      }
   }
}
