# <div align = "center">OO第二单元总结</div>

## **一、同步块与锁**

### 1.1 HW5

​		由于我作业采用的架构是生产者-消费者模式，RequestInput类作为生产者线程，Elevator类作为消费者线程，RequestQueue作为二者的共享对象。所以同步块就是对共享队列进行操作的部分，使用`synchonized`关键字对RequestQueue对象进行保护即可，在创建生产者线程和消费者线程时，都会把同一个已经new好的共享队列对象存入其中，所以要对共享队列访问时，直接用synchonized对队列加锁即可，保护的是**RequestQueue**这个对象，这样当有多个线程要访问它时就会被阻塞了，保证了数据安全

​		RequestInput作为输入线程，从输入中读取请求，然后请求访问共享队列，在同步块中执行与共享队列相关的操作，添加完乘客后立即释放锁，尽量避免在同步块中执行其他操作，提高程序效率，其要对锁进行addPerson操作，具体行为如下图所示：

```java
synchronized (requestQueueHashMap) {
                requestQueueHashMap.addPerson(temp.getSrc(), temp);
                requestQueueHashMap.notifyAll();
            }
```

​		Elevator作为消费者线程，在第五次作业中有6个电梯类线程，在主线程中直接启动即可。它仅会在设置和更换主请求、接人、放人时访问请求队列，同步块中要对锁进行deletePerson操作，部分实现如下：

```java
public void passenIn(Person person) {
        synchronized (requestQueueHashMap) {
            requestQueueHashMap.delPerson(person);
        }
        curPersons.add(person);
        printPas(1, person);
    }
```

### 1.2 HW6

​		第六次作业多了电梯的增加和维护，增加的话就是在输入线程里增加即可，此时不需要上锁，直接设置完参数后start即可，没有什么细节

​		至于维护，也是在输入线程里完成，需要维护的电梯收到请求后，用synchonized关键字对请求队列上锁，放出乘客，此时对锁的操作是addPerson，类似于输入线程

### 1.3 HW7

​		第七次作业增加了Mx和Nx的限制和可达性的限制。这次增加的锁是对开门限制增加了synchogized锁。为了限制服务中和只接人电梯的数量，我新增了一个DoorLock类，里面有11把object**虚空锁**，负责每一层楼的线程安全，利用其中的静态方法即可实现，如图所示：

```java
public class DoorLock {
    private static ArrayList<Object> locks = new ArrayList<>();
    private static int [] serving = new int[12];
    private static int [] onlyRev = new int[12];

    public static ArrayList<Object> getLocks() {
        return locks;
    }

    public static int[] getServing() {
        return serving;
    }

    public static int[] getOnlyRev() {
        return onlyRev;
    }
}
```

​		在电梯需要开门的时候利用synchonized关键字上锁，根据该层楼只接人和服务中的电梯数量判断需要等待还是继续执行：

```java
synchronized (DoorLock.getLocks().get(this.curFloor)) {
                if (DoorLock.getServing()[curFloor] == 4) {
                    try {
                        DoorLock.getLocks().get(curFloor).wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if (this.onlyReceive()) {
                        while (DoorLock.getOnlyRev()[curFloor] == 2) {
                            try {
                                DoorLock.getLocks().get(curFloor).wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        DoorLock.getOnlyRev()[curFloor]++;
                    }
                    DoorLock.getServing()[curFloor]++;
                }
            }
```

## 二、调度器设计

​		我这单元所采用的策略是自由竞争，所以没有实现调度器，可能会导致耗电量很高；但是由于采取了主请求策略，电梯执行前会先设置主请求，主请求不为空才会去移动，而不是一个请求进入队列后所有电梯一起竞争，因此主请求会使自由竞争的耗电量减少一些，输入线程在把请求add到队列里后会notifyall唤醒电梯线程

## 三、调度策略

### HW5

​		输入线程读取输入请求，然后把它加入共享队列；电梯线程启动后，每次执行都要先判断是否能结束线程，即输入结束 && 共享队列为空 && 电梯内没有人 ；若不能退出则判断是否需要wait()，即输入没结束&&共享队列为空 && 电梯内没有人；最后启动策略类的look算法进行工作，具体调度如下：

- 首先判断该电梯是否存在主请求，若没有则设置主请求：首先找到电梯`运行方向`上的距离电梯最近的请求设置为主请求，若不存在则设置距离电梯最近的请求作为主请求；
- 直接前往主请求所在楼层，然后捎带上主请求同楼层与主请求同向的请求(副请求)；
- 前往主请求的目的楼层，每经过一层，就判断当前楼层是否有副请求到达目的地或者可以捎带副请求(同方向即可捎带)；
- 到达后，主请求和副请求出去后，若电梯内还有人，则随机选择一个乘客作为主请求；若没人了，则在当前楼层的请求中选择主请求：首先找到当前楼层中目的地方向与电梯前进方向相同的乘客设置为主请求；若不存在则找到目的地距离当前楼层最近的乘客作为主请求，然后进行当前楼层的捎带。

​		我的调度策略通过设置主请求来减少不必要的电量消耗，再通过尽可能多的捎带来减少运行时间的消耗，UML类图如下：

![](D:\Java文件\oo_hw\oo_8\5uml.png)

​		UML协作图如下：

![](D:\Java文件\oo_hw\oo_8\5.drawio.png)

### HW6

​		为了实现电梯的维护，相对于HW5我新增了一个Maintainer类，里面有存放电梯的容器和判断所有电梯内部都没有乘客的方法；先在主线程里new一个Maintainer类对象，然后放到输入线程里，每次维修电梯就在maintainer的容器中取出该电梯，将其维修标志设置为1，然后电梯收到维修标志时会进行乘客释放并维修；然后调度策略需要略微更改：所有电梯线程的结束需要增加一个判断，即所有电梯内部都不存在乘客，这时才可以退出，否则最后一部电梯维修时放出的乘客会没人运送，UML类图如下：

![](D:\Java文件\oo_hw\oo_8\6uml.png)

​		UML协作图如下：

![](D:\Java文件\oo_hw\oo_8\6.drawio.png)

​		从协作图中很容易看出，本次的迭代线程数量没有变化，仅需要实现简单的增加电梯线程的启动和维护，电梯收到维护请求时将内部乘客释放到RequestQueue里即可

### HW7

​		第7次作业要求实现电梯可达性与服务中、只接人电梯数量的限制。

​		首先为了实现**可达性**，我在Maintainer类里增加了三个access、path、val二维数组，采用Floyd算法实现各个楼层之间的最短路，增加freshAdd和freshMaintain方法来更新各个楼层之间路径的权重，Floyd方法用于更新各个楼层之间的途径楼层，即路径方案；每次有PersonRequest时利用maintainer中的SetRoute方法为乘客设置换乘方案，然后每次到达目的楼层后检查目的地容器中是否还有换乘楼层，若为空则已到达最终目的地；有电梯维护请求时，需要更新最短路径方案，有电梯增加请求时也需要更新(利用freshAdd和freshMaintain方法即可完成)，此时候乘表中的乘客的换乘方案也需要同步更新。

​		为了实现两种状态电梯数量的限制，我新增了一个DoorLock类，用于存放11个楼层的11把`虚空锁`，电梯要开门的时候需要给该楼层用synchonized关键字上锁，锁的对象就是容器中该楼层对应的object对象，实现电梯之间的互斥开门，所以这次迭代只是在Maintainer类里面新增了**四个方法**，新增了一个DoorLock类，其余没什么很大的改动，可扩展性还是比较好的，具体UML类图如下：

![](D:\Java文件\oo_hw\oo_8\7uml.png)

UML协作图如下：

![](D:\Java文件\oo_hw\oo_8\7.drawio.png)

​		这次迭代线程的数量也没有变化，仅仅是增加了在Maintainer对象内部Floyd算法的实现，与DoorLock锁的增加，可扩展性良好。

​		总的来说，对于这三次作业，我的设计中比较稳定的内容就是电梯的调度策略--Look算法，不管新增什么要求电梯都是利用主请求和捎带策略来完成；易变的内容是对于新增要求的处理，比如第六次作业中新增了Maintainer类用于实现电梯的维护，第七次作业中扩展了Maintainer类和新增了DoorLock类实现可达性和状态电梯数量限制

## 四、Debug

​		第二单元的bug是真的不好找，不仅废眼睛更废脑子

​		首先第五次作业中由于我事先规划好了整个程序的架构与各个类之间的职责，所以写完之后没有出现任何bug，强测的所有点也都过了

​		第六次作业写完之后自己进行了测试，编造了几类数据点，找到一个自由竞争架构下的bug，即相比于上一次作业，这次作业电梯线程的结束不能单单判断输入结束与请求队列为空，还要新增一个判断即其他所有电梯内都无乘客，因为最后一个运行的电梯可能会出现被维修的情况，如果此时其余所有电梯都结束线程了，被放下的乘客就没人运送了，所以注意一下这点就可以，最后强测点也都过了

​		第七次作业新增的要求是比较有意思、有挑战性的，自由竞争架构下需要利用图算法实现可达性。由于我新增了每一层楼的虚空锁来保证Mx和Nx的限制，所以由于考虑不周，出现了死锁的情况。具体是：电梯到达某一楼层后，会实现副请求的出入，此时我的代码逻辑是先对共享队列加锁，然后开门。很明显，这必然会出现死锁的情况。要开门的时候如果已经达到Nx或者Mx的限制，那么就会在开门这一层被阻塞，导致共享队列锁没有被释放，其他线程想要访问共享队列时无法访问，从而无法释放该层的虚空锁，进而导致死锁

​		这个bug其实我在公测截止前利用评论区大佬们的评测机发现了，但由于这周事情太多时间不够没有找到原因。强测中寄了一个死锁的点，为了修复这个bug，我是利用java里的**jconsole**来解决的，该工具可以找到线程中锁的占用，很容易看出死锁问题。

​		解决的办法就是将两个锁分开，避免嵌套上锁的情况。

```java
synchronized (requestQueueHashMap) {
                if (requestQueueHashMap.getRequestMap().containsKey(curFloor)) {
                    for (Person person : requestQueueHashMap.getRequestMap().get(curFloor)) {
                        if (takeAble(person)) {
                            flag = true;
                            break;   // 如果需要开门，置标志位，break释放锁，避免死锁，到下一个同步块去拿该楼层开门锁
                        }
                    }
                }
            }
            if (flag) {
                if (!this.doorFlag) {
                    printEle(1);  // 拿该楼层开门锁实现开门操作
                }
                attachedIn(); // 捎带乘客
                this.doorFlag = true;
            }
            attachedIn();
```

​		第七次作业还存在的bug就是**CTLE--轮询**，我的设计是若存在健全电梯，则让残疾电梯wait，防止残疾电梯不断执行却接不到人，导致轮询占用cpu；但是我犹豫没有考虑全，若不存在健全电梯了，此时由残疾电梯协作来实现调度，比如一个乘客从1楼到11楼需要先换乘到7楼，再到11楼，那么一个残疾电梯需要先给他送到7楼，再由另一个电梯给他送到11楼；第一个电梯运行的过程中，第二个电梯会不断在while里执行，却没有能够接的人，因为只有第一个电梯执行完它才有机会接人，所以此时它应该wait，否则就会一直while占用cpu导致轮询。所以为了修复此bug，若电梯执行完一个循环后没有可以接的人，则wait即可，若候乘表有新增请求会将电梯线程唤醒进行接人，这就解决了轮询的问题

## 五、心得体会

**(1) 线程安全**

​		线程安全的实现主要通过线程安全类的设计来保证。对共享数据进行包装，比如共享队列，使用synchonized关键字和wait、notifyall方法实现线程之间对共享对象的互斥访问，从而实现线程安全

**(2) 层次化设计**

​		这一单元的层次化主要体现在`迭代层次化`上。每一次迭代都增加一个类来满足新增的要求，不会影响其他部分类的功能，具有很强的迭代性和扩展性

​		感觉这一单元的三次作业难度并不是很高，第六次作业实现维护请求，新增一个Maintainer类即可完成；第七次作业实现可达性，利用数据结构课中学过的Floyd算法或者Dfs+剪枝都可以实现，再满足服务中和只接人的限制，通过加锁即可完成。但是唯一比较难的应该就是其中细节的考虑和实现了，稍有考虑不周就可能出现bug然后寄掉，所以一定要对自己的架构有一个宏观的认知，有充足的时间来做自动化测试，才能保证架构的安全
