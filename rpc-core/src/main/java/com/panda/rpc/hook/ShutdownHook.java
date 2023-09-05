package com.panda.rpc.hook;

import com.panda.rpc.factory.ThreadPoolFactory;
import com.panda.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author [PANDA] 1843047930@qq.com
 * @date [2021-03-14 13:45]
 * @description
 */
public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    /**
     *单例模式创建钩子，保证全局只有这一个钩子
     */
    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook(){
        return shutdownHook;
    }

    //注销服务的钩子
    public void addClearAllHook() {
        logger.info("服务端关闭前将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 注销所有服务
            NacosUtil.clearRegistry();
            // 关闭所有线程池
            ThreadPoolFactory.shutDownAll();
        }));
    }
}
/**
 * 钩子函数注意事项：
 * 1.钩子函数应该尽量避免进行复杂或耗时的操作。因为钩子函数是在程序即将关闭时执行的，如果钩子函数中有耗时的操作，可能会导致程序关闭过程变慢或阻塞。
 * 2.钩子函数应该尽量避免依赖其他资源或服务。因为在程序关闭时，其他资源或服务可能已经关闭或不可用，如果钩子函数依赖这些资源或服务，可能会导致异常或错误。
 * 3.钩子函数应该尽量保持简洁和高效。由于钩子函数是在程序即将关闭时执行的，为了保证程序能够正常关闭，钩子函数应该尽量快速地完成任务，避免出现阻塞或异常。
 * 4.钩子函数中应该避免使用System.exit()方法。在钩子函数中调用System.exit()方法会导致程序立即退出，可能会导致其他资源无法正常释放或关闭。
 * 5.钩子函数在多线程环境下可能会有一些问题。如果程序中存在多个钩子函数，它们是并发执行的，可能会导致线程安全问题。在编写钩子函数时，应该考虑线程安全性，并尽量避免对共享资源进行竞争操作。
 * */
