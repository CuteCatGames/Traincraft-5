package train.common.mtc.packets.handlers;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import train.common.api.Locomotive;
import train.common.mtc.packets.PacketATOSetStopPoint;

public class PacketATOSetStopPointHandler implements IMessageHandler<PacketATOSetStopPoint, IMessage> {

   public IMessage onMessage(PacketATOSetStopPoint message, MessageContext ctx) {
      Entity trainEntity = Minecraft.getMinecraft().thePlayer.getEntityWorld().getEntityByID(message.entity);
      if(trainEntity != null) {
         Locomotive theTrain = (Locomotive)trainEntity;
         theTrain.xFromStopPoint = message.xPos;
         theTrain.yFromStopPoint = message.yPos;
         theTrain.zFromStopPoint = message.zPos;
         theTrain.xStationStop = message.sXPos;
         theTrain.yStationStop = message.sYPos;
         theTrain.zStationStop = message.sZPos;
      }

      return null;
   }
}
