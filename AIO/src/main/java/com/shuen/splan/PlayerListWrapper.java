package com.shuen.splan;

import java.lang.reflect.Field;

public class PlayerListWrapper {
    private Object obj;
    public PlayerListWrapper(Object obj){
        this.obj=obj;
    }
    public void setMaxPlayers(int intProperty) {
        try {//1.13 - 1.16
            Field field = splan.getField(net.minecraft.server.management.PlayerList.class,"field_72405_c");
            field.set((net.minecraft.server.management.PlayerList)obj,intProperty);
        } catch (Error|Exception E1) {
            try {//1.17 - 1.18
                Field field = splan.getField(net.minecraft.server.players.PlayerList.class,"f_11193_","maxPlayers");
                field.setAccessible(true);
                field.set((net.minecraft.server.players.PlayerList)obj,intProperty);
            } catch (Error|Exception E2) {
                splan.LOGGER.error("Error Setting max players:");
                splan.LOGGER.error("Error 1:",E1);
                splan.LOGGER.error("Error 2:",E2);
            }
        }
    }
    public int getMaxPlayers() {
        try {
            return ((net.minecraft.server.management.PlayerList)obj).getMaxPlayers();
        } catch (Error E1) {
            try {
                return ((net.minecraft.server.players.PlayerList)obj).m_11310_();
            } catch (Error E2) {
                splan.LOGGER.error("Error Getting max players:");
                splan.LOGGER.error("Error 1:",E1);
                splan.LOGGER.error("Error 2:",E2);
            }
        }
        return 20;
    }
}
