package net.shoreline.client.impl.event.render.block;

import net.shoreline.client.api.event.Cancelable;
import net.shoreline.client.api.event.Event;

public class RenderTileEntityEvent extends Event {
   @Cancelable
   public static class EnchantingTableBook extends RenderTileEntityEvent {
   }
}
