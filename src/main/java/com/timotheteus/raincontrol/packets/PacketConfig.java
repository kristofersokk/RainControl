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

import java.nio.charset.Charset;

public class PacketConfig implements IMessage {

    private PacketTypes.CONFIG[] types;
    private int generation;

    /**
     * Required objects are in the PacketTypes descriptions.
     * Put the objects in the order of the packet types
     *
     * @param types   different config packet types
     *                in the right order
     * @param objects objects in the right order for the packets
     */
    public PacketConfig(PacketTypes.CONFIG[] types, Object... objects) {
        this.types = types;
        int index = 0;
        for (PacketTypes.CONFIG type : types) {
            switch (type) {
                case GENERATION:
                    generation = (int) objects[index];
                    index += 1;
                    continue;

            }
        }
    }

    public PacketConfig() {
    }

    private static String intArrayToString(int[] ints) {
        StringBuilder builder = new StringBuilder();
        for (int a : ints) {
            builder.append(a);
        }
        return builder.toString();
    }

    private static int[] stringToIntArray(CharSequence str) {
        int[] result = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            result[i] = ((int) str.charAt(i));
        }
        return result;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int len = buf.readInt();
        this.types = PacketTypes.CONFIG.getTypes(stringToIntArray(
                buf.readCharSequence(len, Charset.defaultCharset())
        ));
        for (PacketTypes.CONFIG type : types) {
            switch (type) {
                case GENERATION:
                    generation = buf.readInt();
                    continue;

            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        CharSequence sequence = intArrayToString(PacketTypes.CONFIG.getIds(types));
        int len = sequence.length();
        buf.writeInt(len);
        buf.writeCharSequence(sequence, Charset.defaultCharset());
        for (PacketTypes.CONFIG type : types) {
            switch (type) {
                case GENERATION:
                    buf.writeInt(generation);
                    continue;
            }

        }

    }

    public void sendToAll() {
        PacketHandler.INSTANCE.sendToAll(this);
    }

    public void sendTo(EntityPlayerMP player) {
        PacketHandler.INSTANCE.sendTo(this, player);
    }

    @SideOnly(Side.CLIENT)
    public static class Handler implements IMessageHandler<com.timotheteus.raincontrol.packets.PacketConfig, IMessage> {

        @Override
        public IMessage onMessage(com.timotheteus.raincontrol.packets.PacketConfig message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(com.timotheteus.raincontrol.packets.PacketConfig message, MessageContext ctx) {
            for (PacketTypes.CONFIG type : message.types) {
                switch (type) {
                    case GENERATION:
                        Minecraft.getMinecraft().addScheduledTask(() -> {
                            Config.generatorProduce = message.generation;
                        });
                        continue;
                }
            }
        }
    }
}
