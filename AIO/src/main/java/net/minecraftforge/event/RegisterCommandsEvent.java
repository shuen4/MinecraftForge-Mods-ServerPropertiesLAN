package net.minecraftforge.event;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public class RegisterCommandsEvent extends ServerLifecycleEvent {

	public RegisterCommandsEvent(MinecraftServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

}
