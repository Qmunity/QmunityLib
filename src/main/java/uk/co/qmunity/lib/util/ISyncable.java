package uk.co.qmunity.lib.util;

import uk.co.qmunity.lib.network.MCByteBuf;

public interface ISyncable {

    public void writeUpdateData(MCByteBuf buf);

    public void readUpdateData(MCByteBuf buf);

    public void sendUpdatePacket();

}
