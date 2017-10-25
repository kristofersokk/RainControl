package com.timotheteus.raincontrol.packets;

import com.timotheteus.raincontrol.config.Config;
import com.timotheteus.raincontrol.handlers.PacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketConfig implements IMessage {

    private int generation = Config.generatorProduce;

    public PacketConfig() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        generation = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(generation);
    }

    public void sendToAll() {
        PacketHandler.INSTANCE.sendToAll(this);
    }

    public void sendTo(EntityPlayerMP player) {
        PacketHandler.INSTANCE.sendTo(this, player);
    }

    public static class Handler implements IMessageHandler<com.timotheteus.raincontrol.packets.PacketConfig, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(com.timotheteus.raincontrol.packets.PacketConfig message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handle(com.timotheteus.raincontrol.packets.PacketConfig message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Config.generatorProduceNew = message.generation;
            });
        }
    }
}
