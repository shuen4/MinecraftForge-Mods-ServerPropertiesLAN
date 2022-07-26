package com.shuen.splan;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class StopLANCommand {
   public static void register(CommandDispatcher<CommandSource> cd) {
      cd.register(Commands.literal("stopLAN").requires((css) -> {
             return css.hasPermission(4) && css.getServer().isPublished();
      }).executes((cc)->{
          if(splan.instance.stop_LAN()) {
              cc.getSource().sendSuccess(new StringTextComponent("Success."),false);
              return 0;
          }
          cc.getSource().sendFailure(new StringTextComponent("Failed."));
          return 1;
      }));
   }
}