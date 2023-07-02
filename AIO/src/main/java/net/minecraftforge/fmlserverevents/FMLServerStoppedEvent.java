package net.minecraftforge.fmlserverevents;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.event.server.ServerLifecycleEvent;

public class FMLServerStoppedEvent extends ServerLifecycleEvent{

	public FMLServerStoppedEvent(MinecraftServer server) {
		super(server);
		// TODO Auto-generated constructor stub
	}

}
