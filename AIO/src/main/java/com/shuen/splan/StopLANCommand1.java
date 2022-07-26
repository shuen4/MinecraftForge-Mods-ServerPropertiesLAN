package com.shuen.splan;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextComponentString;

public class StopLANCommand1 {
   public static void register(CommandDispatcher<CommandSource> cd) {
      cd.register(Commands.literal("stopLAN").requires((css) -> {
             return css.hasPermissionLevel(4) && css.getServer().getPublic();
      }).executes((cc)->{
          if(splan.instance.stop_LAN()) {
              cc.getSource().sendFeedback(new TextComponentString("Success."),false);
              return 0;
          }
          cc.getSource().sendErrorMessage(new TextComponentString("Failed."));
          return 1;
      }));
   }
}