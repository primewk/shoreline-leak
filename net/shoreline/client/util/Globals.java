package net.shoreline.client.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_310;

public interface Globals {
   class_310 mc = class_310.method_1551();
   Random RANDOM = ThreadLocalRandom.current();
}
