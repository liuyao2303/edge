import com.liuyao.framework.lang.RpcException;
import com.liuyao.framework.rpc.client.channel.NettyChannelClient;
import com.liuyao.framework.rpc.client.proxy.ServiceProxy;
import com.liuyao.framework.service.api.Animinal;

public class Test {

    @org.junit.Test
    public void test() throws RpcException {

        NettyChannelClient channelClient = new NettyChannelClient("127.0.0.1", 8089);
        ServiceProxy serviceProxy = new ServiceProxy(channelClient, Animinal.class);

        Animinal animinal = serviceProxy.getProxyInstance();
        int r = animinal.earn(123);
        System.out.println(r);
        r = animinal.earn(3232);
        System.out.println(r);
        animinal.fee("hello huanhuan");
    }
}
