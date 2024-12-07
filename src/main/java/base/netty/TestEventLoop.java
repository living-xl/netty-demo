package base.netty;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        log.info("next -{}",group.next());
        log.info("next -{}",group.next());
        log.info("next -{}",group.next());
        log.info("next -{}",group.next());

//        group.next().execute(()->{
//            try{
//                Thread.sleep(1000);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            log.debug("ok");
//        });

        group.scheduleAtFixedRate(()->{
            try{
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
            log.debug("ok");
        },0,5,TimeUnit.SECONDS);
        log.debug("main end");
    }
}
