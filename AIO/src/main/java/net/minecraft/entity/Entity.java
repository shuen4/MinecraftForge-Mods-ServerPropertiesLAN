package net.minecraft.entity;

import java.util.UUID;

import net.minecraft.command.ICommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;

public abstract class Entity extends net.minecraftforge.common.capabilities.CapabilityProvider<Entity> implements INameable, ICommandSource, net.minecraftforge.common.extensions.IForgeEntity {

	protected Entity(Class<Entity> baseClass) {
		super(baseClass);
		// TODO Auto-generated constructor stub
	}

	public UUID getUniqueID() {
		// TODO Auto-generated method stub
		return null;
	}

	public void func_145747_a(ITextComponent iTextComponent, UUID uniqueID) {
		// TODO Auto-generated method stub
		
	}

	public void m_6352_(Component component, UUID uniqueID) {
		// TODO Auto-generated method stub
		
	}

	public UUID m_142081_() {
		// TODO Auto-generated method stub
		return null;
	}
}