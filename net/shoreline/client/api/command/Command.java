package net.shoreline.client.api.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.class_2172;
import net.shoreline.client.init.Managers;
import net.shoreline.client.util.Globals;

public abstract class Command implements Globals {
   private final String name;
   private final String desc;
   private final LiteralArgumentBuilder<class_2172> builder;

   public Command(String name, String desc, LiteralArgumentBuilder<class_2172> builder) {
      this.name = name;
      this.desc = desc;
      this.builder = builder;
   }

   public abstract void buildCommand(LiteralArgumentBuilder<class_2172> var1);

   protected static LiteralArgumentBuilder<class_2172> literal(String name) {
      return LiteralArgumentBuilder.literal(name);
   }

   protected static <T> RequiredArgumentBuilder<class_2172, T> argument(String name, ArgumentType<T> type) {
      return RequiredArgumentBuilder.argument(name, type);
   }

   protected static SuggestionProvider<class_2172> suggest(String... suggestions) {
      return (context, builder) -> {
         return class_2172.method_9265(Lists.newArrayList(suggestions), builder);
      };
   }

   public LiteralArgumentBuilder<class_2172> getCommandBuilder() {
      return this.builder;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.desc;
   }

   public String getUsage() {
      return Managers.COMMAND.getDispatcher().getAllUsage(this.builder.build(), Managers.COMMAND.getSource(), false)[0];
   }
}
