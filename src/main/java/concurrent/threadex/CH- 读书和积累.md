## 0.异步 vs 同步 

> 总结就是：
>
> - 阻塞：啥也不干，干等
> - 非阻塞：做其他的事情，但是需要间隔地回来查看
>
>
> - 同步：主动查询
> - 异步：他人通知

##### 阻塞和非阻塞

- 阻塞：需要进程等待IO
- 非阻塞：进程无需等待（但是需要主动间隔地来查询IO事件，或者被回调通知后再来查询IO事件）

##### 同步和异步

- 同步：**<u>进程</u>主动<u>等待或查询</u>IO事件**。进程可能是主动阻塞等待，或者非阻塞的形式，间隔地主动查询。一定是进程主动发起的查询或等待。
- 异步：**由<u>内核</u>主动告知<u>进程</u>，是否有IO事件**。进程可能是在别的地方阻塞，啥也不干地等待内核告知，也有可能在做别的事情同时等待着内核的告知。

> JAVA使用的selector，底层是基于I/O多路复用模型，个人认为不好直接说是同步还是异步。
>
> selector<u>主动调用并等待数据</u>的过程，是同步的
>
> 而<u>socket返回数据，导致selector被回调</u>，这个过程又是异步的






> [Java NIO - Nyima's Blog (gitee.io)](https://nyimac.gitee.io/2020/11/30/Java NIO/)

## 1.BIO的流 vs NIO的通道

**IO**

传统IO在传输数据时，根据输入输出的不同需要分别建立不同的链接，而且传输的数据是以流的形式在链接上进行传输的

就像自来水要通过水管将自来水厂和家连接起来一样

[![img](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037948.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20201109084453.png)



**NIO**

NIO在传输数据时，会在输入输出端之间建立**通道**，然后将数据放入到**缓冲区**中。缓冲区通过通道来传输数据

这里通道就像是铁路，能够连通两个地点。缓冲区就像是火车，能够真正地进行数据的传输



[![img](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037949.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20201109085054.png)

 

## 2.通道详解

###### 1、简介

Channel由java.nio.channels 包定义的。Channel 表示**IO 源与目标打开的连接**。Channel 类似于传统的“流”。只不过**Channel 本身不能直接访问数据，Channel 只能与Buffer 进行交互**

###### 2、图解

应用程序进行读写操作调用函数时，**底层调用的操作系统提供给用户的读写API**，调用这些API时会生成对应的指令，CPU则会执行这些指令。在计算机刚出现的那段时间，**所有读写请求的指令都有CPU去执行**，过多的读写请求会导致CPU无法去执行其他命令，从而CPU的利用率降低

[![img](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037950.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20201109153039.png)

后来，**DMA**(Direct Memory Access，直接存储器访问)出现了。当IO请求传到计算机底层时，**DMA会向CPU请求，让DMA去处理这些IO操作**，从而可以让CPU去执行其他指令。DMA处理IO操作时，会请求获取总线的使用权。**当IO请求过多时，会导致大量总线用于处理IO请求，从而降低效率**

[![img](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037951.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20201109153439.png)

于是便有了**Channel(通道)**，Channel相当于一个**专门用于IO操作的独立处理器**，它具有独立处理IO请求的能力，当有IO请求时，它会自行处理这些IO请求

[![img](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037952.png)](https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20201109154113.png)





## 3.linux select poll epoll 模型

> java NIO就是多路复用的思想，具体在实现上，可以采用select/epoll模型
>
> 
>
> java NIO的selector仅仅是api层面，底层可以使用linux的select、poll、epoll都有可能。确切的来说，java的应该是基于reactor模型的多路复用IO。

#### ①Select——基于轮询

select处理多个socket连接，需要：

- 发起select系统调用

- 将应用空间中的socket的fd**全部**复制到内核空间下（形成fds集合）

- 当所有socket无事件发生时，select会阻塞睡眠

- 一旦一个socket有事件，select会被唤醒，但是select并不知道是哪个socket_fd唤醒的

- 内核得知有fd发生事件，就会把**全部**fd返回到用户空间（全部返回，包括无事件发生的fd）

  ![image-20230205004115889](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037953.png)

- 在用户空间开始轮询所有的socket_fd，检查事件

- 最后select将真正发生事件的socket_fd告知应用程序（对应java nio的异步阻塞selector.select()）

  > 底层机制是：
  >
  > ①linux的select会在监控的每个socket的睡眠队列中，安插一个眼线，然后直接睡眠
  >
  > ②一旦某个socket发生事件，就会唤醒睡眠队列中的所有等待者，包括select安插的眼线
  >
  > ③眼线会唤醒后，就会主动去唤醒睡着的select
  >
  > ④但是select被唤醒后，不知道是哪个socket上的眼线唤醒的，就会去轮询socket_fd
  >
  > ![image-20220717005625970](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037954.png)

###### 缺陷

- 有socket_fd从用户空间到内核空间的拷贝过程，占用空间时间
- 可以拷贝到内核空间的socket_fd数目被限制（每个 select 能拷贝的 fds 集合大小只有1024）
- 每次select被唤醒，需要遍历所有的socket_fd



#### ②poll模型——基于轮询

<u>解决了select的拷贝数目限制</u>！

但是拷贝过程、轮询过程仍然存在，仍然存在性能问题。





#### ③epoll——event poll事件驱动

回顾select本身存在的缺陷大致如下：

- 首先复制一遍当前所有的socket fd集合到内核空间

- 发生事件后，返回全部 socket fd 集合回到用户空间

- 用户再次遍历 fd，找出有事件发生的 fd （不确定）

在 epoll 模型中，这些问题得到了很好地解决。



首先 epoll 使用 3 个函数，进行 socket_fd 的监听：

- epoll_create：创建 epoll 对象

  ![image-20230205012205648](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037955.png)

- epoll_ctl ： 根据epoll_id去添加，修改，删除各个文件描述符

  ![image-20230205012244064](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037956.png)

- epoll_wait ： 等待外界的io数据请求，有些类似于select()操作

  ![image-20230205012321760](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037957.png)






在细节实现上，<u>socket唤醒睡眠队列时，回调函数中有个额外的操作，就是把当前socket放到就绪事件链表中。——避免了epoll轮询所有的socket！</u>

![image-20220717021337552](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037958.png)

所以综上可知，epoll解决了select（poll）的两大难题：

- 无需大量的socket_fd复制（每次epoll时只需要增删fd红黑树）
- 内核、用户应用无需轮询（socket唤醒时，会直接放到就绪list中返回）

> epoll也会在socket睡眠队列中安插眼线 ，但是区别在于，socket唤醒眼线时候的回调，会把当前socket事件加入就绪list中，便于epoll直接查询，而无需轮询树上socket







## 4.数据传输过程

#### 普通过程

- 用户进程在读取数据（用户从IO设备获取数据）之前，需要操作系统从物理设备上读取，之后再拷贝到用户缓冲区中。

- 用户只能读取用户缓冲区中的数据。

- 用户缓冲区可以是用户在代码中定义的数组、申请的内存

- 每次或者写，都牵涉到两次拷贝

  ```ruby
  			  1                   2
  用户缓冲区  <=======> 内核缓冲区 <=======> 物理设备
  ```

  

![image-20220718195526189](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037959.png)

#### 使用直接内存

那就只需要1次拷贝，因为直接内存会申请在操作系统内存区域。

```ruby
			 1
内核缓冲区 <=======> 物理设备
```





## 5.零拷贝的概念

在传统的数据传输过程中，需要将数据从设备读取到内核缓冲区，再从内核缓冲区读取到应用缓冲区（用户缓冲区、用户空间）。

##### 零拷贝技术

则是将数据读取到内核缓冲区之后，**无需拷贝到应用缓冲区**，直接发送到目标地点！

##### 零拷贝的应用

java的transferTo这个api（用来做文件内容转移）就是零拷贝







## 6.handler和childHandler的区别

- 方法来源不同：
  - ServerBootstrap#handler来源于父类AbstractBootstrap
  - childHandler是ServerBootstrap自身的方法
- 配置要求不同：
  - ServerBootstrap#handler可以不配置
  - #childHandler必须配置，否则抛出异常IllegalStateException: childHandler not set。
- 使用方式不同：
  - ServerBootstrap#handler 对应的的Channel是io.netty.channel.socket.ServerSocketChannel的实例（即AbstractBootstrap#channel 配置的Channel Class）
  - ServerBootstrap#childHandler对应的Channel是io.netty.channel.socket.SocketChannel实例。换而言之，ServerBootstrap#handler设置的ChannelHandler是被添加到ServerChannel的ChannelPipeline中，而ServerBootstrap#childHandler设置的ChannelHandler是被添加到Channel的ChannelPipeline中
- message不同：
  - ServerBootstrap#handler的msg是SocketChannel
  - ServerBootstrap#childHandler的msg是客户端传来的实打实的数据
- 可以配置使用不同的线程。parentGroup是为ServerSocketChannel配置的线程，child是为SocketChannel配置的线程。
- **调用时机不同**：
  - ServerBootstrap#handler添加的handler实例链，只在客户端连接**创建时调用**（ServerChannel负责创建子Channel，对应点3），在创建完成之后，不会再调用
  - ServerBootstrap#<u>childHandler添加的handler实例链，对于Channel的所有事件都会被调用触发</u>







## 7.滑动窗口

> [大厂面试必备：TCP滑动窗口是干什么的？TCP的可靠性体现在哪里？_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1xY411u782?spm_id_from=333.337.search-card.all.click&vd_source=45e12cdcd5475d3cca69758be583e0db)
>
> [TCP滑动窗口_晨曦艾米的博客-CSDN博客_tcp滑动窗口](https://blog.csdn.net/ZBraveHeart/article/details/123691305)

#### 前提知识——数据包

TCP三次握手的时候，会互相通知对方，本端允许传输的单个数据包的最大长度（也称为MSS），握手之后，双方默认按<u>MSS较小的那端</u>作为通信双方的数据包大小限制。在通信过程中，数据连续发送的过程时每个包最大也就不能超过MSS

> 通信过程中：
>
> - 当数据包超出该长度，应该自觉拆成多个MSS包依次连续发送
> - 当数据包小于该长度，可以适当粘包发送（提升效率）



#### 为什么需要滑动窗口？

- TCP是可靠性协议，每个包发送后，都需要得到对端的ACK确认报文

- 由于每个包都需要对方逐步确认，那么严重影响数据通信效率

- 那么我们是不是就得考虑，是否允许每次发送多个包？然后再去接收对方的所有ACK报文？

  > 其实真正的实践中，没有、而且也没必要确认所有的ACK报文，我们直接响应序号最大的报文，这样就代表这个序号之前的报文都已接收！
  >
  > 比如发送了3000、4000、5000、6000、7000这5个数据包，接收端可能仅仅回复了3个响应：ACK=3001、ACK=5001、ACK=7001。

- 实际上，滑动窗口就是对网络包流水线互传的一种限制



#### 滑动窗口初识

如果每次发一个数据包，都等对端ACK，这样数据传输过去的效率是很低的。为了解决这一问题，TCP创造了滑动窗口这个概念。

**滑动窗口允许：可以一次发送n个包，然后再慢慢等对方的ACK到来**。这<u>n个包可以连续发送</u>，不需要挨个等ACK再发下一个包。

现在我们知道了，滑动窗口本身是一个数量上的限制！





#### 滑动窗口

##### 1窗口组成

下面以A发给B为例：

![image-20220723062437987](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037960.png)

A发送窗口包括：

- 已经发送但未确认的
- 剩下可以一并发送的报文（就算发过去，接收端也是能接收的）

B接收窗口包括：

- 整个允许接收的窗口（里面有确认的+没确认的部分）



##### 2窗口移动

当A的报文发给B后：

①B确认之后，B的窗口立刻往前挪

②A收到B的确认后，A的窗口立刻往前挪，又可以发送新的包了！



##### 3数据包确认

B回复ACK的时候，会带上一个数据包数目。假设A发送了7、8、9、10、11。B回复了:

- ACK=12外加数目等于5：说明了B收到了12之前的5个包
- ACK=12外加数目等于2：说明B收到的是10、11这两个包，789暂时还没收到
- ACK=10外加数目等于3：说明B收到的789这3个包

总结就是：

![image-20220723070450812](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037961.png)







#### 细节

- 由于
- 如果接收方再次收到已经ACK确认的报文，那么还是会回复ACK。因为再次接收到这个报文，说明自己对该报文的ACK丢失了，发送方重发了！







> #### 补充 —— GBN协议 演化到 SR协议
>
> ##### GBN
>
> GBN协议规定：接收方，如果收到乱序到达的数据包，则直接丢弃！
>
> > 比如期待5、6、7三个包，但是5、6还没到，7却先到了！那么GBN协议下，接收方又不能直接回复7，因为一回复7，则表示7之前的都收到了！所以只能把数据包7直接丢掉！
> >
> > 因为可以想象到，如果发567，而5在网络传输中直接loss了，而先到到的7又被接收方直接丢了，那么发送端等会需要全部重传！**从最早丢的包开始，后面全部重传！**
> >
> > 需要注意的是，单纯的GBN协议，无需接收方维护滑动窗口！因为接收方只管按序确认！
> >
> > 这就是Go-Back-N协议。
>
> #### SR
>
> SR协议对此进行优化，规定：接收方，如果接收到乱序数据包，则先确认并暂存！但是SR同时引入了新的问题：由于可以直接乱序确认序号较大的数据包，那么就**需要对每个数据包进行挨个单独确认，不能像GBN那样一次性确认**了！
>
> > 此时，在SR协议下，**发送方就应该为每个数据包设置单独的超时时间，哪个超时没确认，重传哪个包**（Selective Repeat，选择性重传）。不过也避免了像BGN那样回退重传N个包了。
> >
> > 和GBN不同，SR协议下，接收方也需要维护一个滑动窗口了！有点现代TCP的意思了。
>
> 值得庆幸的是，现代TCP是GBN和SR的混合体！具体实现方式是：每次确认报文，会包含一个确认的数据包数量:
>
> - 如果数据包数量为1，显然是乱序确认的
> - 如果数据包数量为n，





## 8.netty服务端优雅写法

#### 1.main线程注意事项

```java

public class DiscardServer {       
    try {                       
        f = b.bind(port).sync();
        // Wait until the server socket is closed.
        // In this example, this does not happen, but you can do that to gracefully
        // shut down your server.
        f.channel().closeFuture().sync();
    } finally {
        // 资源优雅释放
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        f.channel().close();
    }
}   
```

注意，一定要调用`f.channel().closeFuture().sync();`，<u>否则main函数不会阻塞，并直接执行到finally块中</u>，导致线程被关闭，服务也被关闭！



#### 2.常用服务端写法

![image-20220723235901967](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037962.png)

```java
public class StudyServer {
    static final Logger log = LoggerFactory.getLogger(StudyServer.class);
    void start() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 连接建立时会执行该方法
                            log.debug("connected {}", ctx.channel());
                            super.channelActive(ctx);
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            // 连接断开时会执行该方法
                            log.debug("disconnect {}", ctx.channel());
                            super.channelInactive(ctx);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(8080);
            log.debug("{} binding...", channelFuture.channel());
            channelFuture.sync();
            log.debug("{} bound...", channelFuture.channel());
            // 关闭channel
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            log.debug("stopped");
        }
    }

    public static void main(String[] args) {
        new StudyServer().start();
    }
}
```



#### 3.设置netty发送和接收的缓冲区

使用serverBootstrap的`childOption()`来设置缓冲区

```java
// 假设定长16
int fixedLen = 16; 
new ServerBootstrap()
    .group(boss,worker)
    .channel(NioServerSocketChannel.class)
    // 在childOption中传入定长长度
    .childOption(
    	ChannelOption.RCVBUF_ALLOCATOR,
    	new AdaptiveRecvByteBufAllocator(fixedLen,fixedLen,fixedLen)
)
```





## 9.ByteBuf和String互转

String 转ByteBuf

```JAVA
String msg = "Test Message";
byte[] bytes = msg.getBytes(CharsetUtil.UTF_8);
ByteBuf buf = Unpooled.wrappedBuffer(bytes);
```

ByteBuf转String

```java
// 方式1
String TestMsg = buf.toString(CharsetUtil.UTF_8);
// 方式2
new String(ByteBufUtil.getBytes(buf), StandardCharsets.UTF_8)
```

> buf转string可能报错：`java.lang.UnsupportedOperationException: direct buffer`
>
> 使用方式2来解决





## 10.根据消息msg类型，来决定是否执行handler

如果我们的消息是类型A，我们就执行该handler，否则就不执行。应该怎么实现？

##### 之前的做法

需要在handler中添加类型判断语句，根据不同的分支，决定不同的处理逻辑

```java
ch.pipeline().addLast(
    new ChannelInboundHandlerAdapter() {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            // netty 的 httpCodec 会把http请求解析成两个部分
            if (msg instanceof A){
                // handling A
            }
            else if (msg instanceof B) {
                // handling B
            }
            else {
                // default handling
            }
            super.channelRead(ctx, msg);
        }
    }
);
```

##### netty中更优雅的写法

使用`SimpleChannelInboundHandler<类型x>`，这样该handler只会处理x类型的msg数据。

```java
// 该处理器只会处理 A 类型的msg
ch.pipeline().addLast(new SimpleChannelInboundHandler<A>() {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, A msg) {
        // handling A
        log.info("收到 A 数据: {}",msg);
    }
});
// 该处理器只会处理 B 类型的msg
ch.pipeline().addLast(new SimpleChannelInboundHandler<B>() {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, B msg) {
        // handling B
        log.info("收到 B 数据: {}",msg);
    }
});
```





## 11.Reactor模式

#### 1.概述

netty使用的就是reactor模式，什么是reactor模式？简而言之就是：**<font color="red">查询IO事件 + 分给handler处理</font>**，这种模型。



#### 2.Reactor模式基本构成

具体来说，在Reactor模式中有Reactor和Handler两个重要的组件：

- Reactor：负责查询IO事件，当检测到一个IO事件时将其发送给相应的Handler处理器去处理。

  > 这里的IO事件就是NIO中选择器查询出来的通道IO事件。

- Handler：即 acceptor 处理器。与IO事件（或者选择键）绑定，负责IO事件的处理，完成真正的<u>连接建立</u>、<u>通道的读取</u>、<u>处理业务逻辑</u>、<u>负责将结果写回通道</u>等。



#### 3.reactor的发展

##### ①单reactor单线程设计

![单reactor单线程设计](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037963.png)

> 单线程reactor模式下，每个handler都是一个对象，绑定（没错，就是attach操作）在selectionKey上。当selector把sk查出来之后，会调用attachment方法，取出handler对象，然后执行handler对象的run方法（run方法就是业务处理方法）

##### ②单reactor多线程设计

![单reactor多线程设计](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037964.png)

##### ③多reactor多线程设计（主流）

![image-20220725211447700](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037965.png)





单 reactor 每次轮询 accept、read、write 等事件，虽然快，但可能存在这么一种情况：

```ruby
某个 channel 发生了 read 事件，而客户端迟迟不发送数据、或者发送了海量数据，导致主reactor 会一直在该 channel 上阻塞，怠慢了其他的channel！
```

这就是为什么引入了主从 reactor，每个 channel 一个 reactor













## 12.handler的共享(@Sharable)

#### 1.概述

正常情况下，我们常常选择在一个channel中添加handler，每个handler还都是new出来的。但是这些handler，有的时候是不能共用的，有的时候可以共用的。

- 不能共用：比如各种解码器，会保存前后状态的，不同channel之间累积数据会冲突！
- 可以共用：比如loggingHandler，无状态，纯打印的

> 多个channel中使用，就意味着会被不同的EventLoop（线程）来调用！





#### 2.共享handler

netty在能够共享的handler的类定义上，加上了`@Sharable`注解，来表明这个handler对象可以在多个channel中共享使用。比如：

![image-20220726094000191](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037966.png)

> 一般解码器类这种，都不能是@Sharable的。应该在 channel 的 init 地方，每次都 new 一个新对象。而不能共用一个对象



我们可以在一定能共享的handler上加上`@sharable`，（比如ByteToMessageCodec的子类就不能加，该类中强制检查了子类不能有`@sharable`注解）















## 13.空闲检测和心跳

##### （1）空闲检测

如果一段时间内，<u>①没有从channel读取到数据</u>，或者<u>②没有往channel中写数据</u>，则会触发对应的**空闲事件**！（说明连接建立之后，你既没有发数据的想法，对面也没给你发数据）

![image-20220729095600708](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037967.png)

如果长时间不响应，直接关闭连接，这种策略只适合某些场景，大部分场景下，尤其是长链接的情况下，还是应该维持一个心跳。

> 注意一般双方约定，写空闲时间，要比读空闲时间短。否则你还没来得及写，对面就认为没读到，就关闭连接了。



##### （2）心跳机制

心跳包是应用层框架来编码的，而不是通信框架提供的功能。

![image-20220729114107183](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037968.png)



#### 补充

sofa-bolt框架，也提供了心跳机制。因为RPC模型下，基本上都是长链接，高并发场景下，三次握手会成为很大的开销。所以一般会维护长链接，并且保证连接一直存活。

①HeartbeatHandler定义

![image-20220729180606629](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037969.png)

②使用HeartbeatHandler

![image-20220729180203738](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037970.png)

③心跳处理机制

![image-20220729180157346](https://study-notes-picture.oss-cn-hangzhou.aliyuncs.com/202302081037971.png)









## 14.handler 使用经验

> [netty入门之inbound &outbound - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/334326729)
>
> [netty中Pipeline的ChannelHandler执行顺序案例详解 - 田志良 - 博客园](https://link.zhihu.com/?target=https%3A//www.cnblogs.com/tianzhiliang/p/11739372.html)

1、InboundHandler是通过fire事件决定是否要执行下一个InboundHandler，如果哪个InboundHandler没有调用fire事件，那么往后的Pipeline就断掉了。(补充，实现ChannelInboundHandlerAdapter类的子类的相应方法中也可以通过在函数结尾处调用super.channelRead等方法，简介调用fire事件)。

2、InboundHandler是按照Pipleline的加载顺序，顺序执行。

3、OutboundHandler是按照Pipeline的加载顺序，逆序执行。

4、有效的InboundHandler是指通过fire事件能触达到的最后一个InboundHander。

5、如果想让所有的OutboundHandler都能被执行到，那么必须把OutboundHandler放在最后一个有效的InboundHandler之前。

6、推荐的做法是通过addFirst加载所有OutboundHandler，再通过addLast加载所有InboundHandler。

7、OutboundHandler是通过write方法实现Pipeline的串联的。

8、如果OutboundHandler在Pipeline的处理链上，其中一个OutboundHandler没有调用write方法，最终消息将不会发送出去。

9、ctx.writeAndFlush是从当前ChannelHandler开始，逆序向前执行OutboundHandler。

10、ctx.writeAndFlush所在ChannelHandler后面的OutboundHandler将不会被执行。

11、ctx.channel().writeAndFlush 是从最后一个OutboundHandler开始，依次逆序向前执行其他OutboundHandler，即使最后一个ChannelHandler是OutboundHandler，在InboundHandler之前，也会执行该OutbondHandler。

12、千万不要在OutboundHandler的write方法里执行ctx.channel().writeAndFlush，否则就死循环了。







## 15.rpc 源码看 netty 水位线功能

> [深入理解netty---从偶现宕机看netty流量控制_ITPUB博客](http://blog.itpub.net/69912579/viewspace-2794140/)
>
> [ Netty笔记(优雅退出,流量控制,流量整形,内存池,读写队列积压,内存泄漏)_pathosis9的博客-CSDN博客](https://blog.csdn.net/u014270781/article/details/105840248)（流量控制一节）

#### 源码

在 bolt 通信框架的源码（sofa-rpc的http2rpc也有）中，开启 ServerBootstrap 的时候会配置 netty 的水位线：

```java
serverBootstrap.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024 * 1024, 8 * 1024 * 1024));
```

然后在通信层会检测连接是否健康：

```java
public Object invokeSync(...) {
    // ...
    final Connection conn = getConnectionAndInitInvokeContext(url, invokeContext);
    this.connectionManager.check(conn);
    
     // ...
    return this.invokeSync(conn, request, invokeContext, timeoutMillis);
}
```

具体的checklist 里面，就有水位的判断，重点在于` channel.isWritable()` 方法：

```java
if (!connection.getChannel().isWritable()) {
    // No remove. Most of the time it is unwritable temporarily.
    throw new RemotingException("Check connection failed for address: "
                                + connection.getUrl() + ", maybe write overflow!"); // 写溢出？
}
```

> 为什么超过水位就 isWritable 返回 false：
>
> netty源码（ChannelOutBoundBuffer 中）中，当ChannelOutboundBuffer的容量超过高水位设定阈值后，isWritable()返回false，设置channel不可写（setUnwritable），并且触发fireChannelWritabilityChanged()。



#### 水位线的用法

1. 通过 bootstrap 设置好水位线
2. 在发送数据时，判断数据是否超过水位线
3. 超过水位线（数据量过大），就应该手工设置处理步骤：`抛异常` or `记录当前断点，等待发出一些数据后，继续发`





#### 需要水位线的原因

一般Netty数据处理流程如下：将读取的数据交由业务线程处理，处理完成再发送出去（整个过程是异步的），Netty为了提高网络的吞吐量，在业务层与socket之间增加了一个ChannelOutboundBuffer。

在调用channel.write的时候，所有写出的数据其实并没有写到socket，而是<u>先写到ChannelOutboundBuffer</u>。当调用channel.flush的时候才真正的向socket写出。因为这中间有一个buffer，就存在速率匹配了，而且这个buffer还是无界的（链表），也就是你如果没有控制channel.write的速度，会有大量的数据在这个buffer里堆积，如果又碰到socket写不出数据的时候（isActive此时判断无效）或者写得慢的情况。

很有可能的结果就是资源耗尽，而且如果ChannelOutboundBuffer存放的是DirectByteBuffer，这会让问题更加难排查。





## 16.为什么netty 服务启动后，自动关闭了？

##### 现象

很多时候启动写好的 netty 服务，然后执行完 boostrap 的创建，和后续的bind 等操作之后，就自动结束了！无论调用什么 sync 方法都没用！

##### 原因

核心：

- 主从 reactor 线程是否非 `daemon`（可以独立存活的） ——检查 boss 和 worker 的threadFactory 看看，生产 thread 是否设置 非daemon

  > 因为 daemon 情况下，主线程执行结束，主从 reactor 自动就会被回收了，因为是 daemon 的，所以 netty 服务也关闭了

- 如果 reactor 都是非 daemon 的，就检查一下，主从 reactor 线程是不是在启动成功之前，主线程就结束了？

  > 即便 reactor 都是可以独立存活的，但是如果 reactor 还处于启动初始化情况下，还没完全创建出来，主线程就结束了，那么就会终止 reactor 的创建。
  >
  > 例如：netty 服务（主从 reactor ）是放在一个 son 线程中启动的，而 son 线程又是线程池中的线程，且是daemon的，此时可能会存在主线程结束，导致 son 这个 daemon 线程结束，进而导致 reactor 还没来得及拉起来

##### 建议方案

**reactor 都是设置非daemon**，通过服务端方法提供 shutdown 来进行 reactor 的gracefully 关闭！

> 实际上，如果reactor 是 daemon的，即便是手工调用 `channelFuture.awaitUninterruptibly()`等待方法，也无济于事，这样只会等 netty 服务启动完毕再关。。。
