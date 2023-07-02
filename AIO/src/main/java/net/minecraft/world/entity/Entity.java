package net.minecraft.world.entity;

public abstract class Entity extends net.minecraftforge.common.capabilities.CapabilityProvider<Entity> implements net.minecraft.util.INameable, net.minecraft.command.ICommandSource, net.minecraftforge.common.extensions.IForgeEntity {

	protected Entity(Class<Entity> baseClass) {
		super(baseClass);
		// TODO Auto-generated constructor stub
	}

	public java.util.UUID getUniqueID() {
		// TODO Auto-generated method stub
		return null;
	}

	public void func_145747_a(net.minecraft.util.text.ITextComponent iTextComponent, java.util.UUID uniqueID) {
		// TODO Auto-generated method stub
		
	}

	public void m_6352_(net.minecraft.network.chat.Component component, java.util.UUID uniqueID) {
		// TODO Auto-generated method stub
		
	}

	public java.util.UUID m_142081_() {
		// TODO Auto-generated method stub
		return null;
	}

	public void m_213846_(net.minecraft.network.chat.Component m_237113_) {
		// TODO Auto-generated method stub
		
	}
}