package com.shuen.splan;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class StopLANCommand {
   public static void register(CommandDispatcher<CommandSourceStack> cd) {
      cd.register(Commands.literal("stopLAN").requires((css) -> {
             return css.hasPermission(4) && css.getServer().isPublished();
      }).executes((cc)->{
          if(splan.instance.stop_LAN()) {
              cc.getSource().sendSuccess(() -> { return Component.literal("Success."); }, false);
              return 0;
          }
          cc.getSource().sendFailure(Component.literal("Failed."));
          return 1;
      }));
   }
}