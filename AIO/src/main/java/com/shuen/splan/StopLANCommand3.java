package com.shuen.splan;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class StopLANCommand3 {
   public static void register(CommandDispatcher<CommandSourceStack> cd) {
      cd.register(Commands.m_82127_("stopLAN").requires((css) -> {
             return css.m_6761_(4) && css.m_81377_().m_6992_();
      }).executes((cc)->{
          if(splan.instance.stop_LAN()) {
              cc.getSource().m_81354_((Component)new TextComponent("Success."),false);
              return 0;
          }
          cc.getSource().m_81352_((Component)new TextComponent("Failed."));
          return 1;
      }));
   }
}