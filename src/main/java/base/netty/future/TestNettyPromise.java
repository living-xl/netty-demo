package base.netty.future;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup loopGroup = new NioEventLoopGroup();
        DefaultPromise<Integer> promise = new DefaultPromise<Integer>(loopGroup.next());
        new Thread(()->{
            log.debug("开始计算");
            try {
//                int a = 1/0;
                Thread.sleep(1000);
                promise.setSuccess(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }
        }).start();
        log.debug("等待结果");
        log.debug("结果-{}",promise.get());

    }
}
