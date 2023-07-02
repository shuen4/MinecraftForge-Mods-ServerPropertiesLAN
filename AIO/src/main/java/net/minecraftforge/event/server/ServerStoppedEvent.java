package net.minecraftforge.event.server;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public class ServerStoppedEvent extends ServerLifecycleEvent{

	public ServerStoppedEvent(MinecraftServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

}
