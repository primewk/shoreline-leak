package net.shoreline.client.mixin.gui.screen;

import net.minecraft.class_419;
import net.shoreline.client.util.Globals;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({class_419.class})
public abstract class MixinDisconnectedScreen extends MixinScreen implements Globals {
}
