package com.example.structure.proxy.chapt0;

import java.util.List;

public interface IWork {

    void startWork();

    String obtainWork();

    void saveWork(String work);

/*  void s(List<String> a);
    void s(List<Integer> b);

    初始标记（CMS initial mark）Stop The World
    并发标记（CMS concurrent mark）
    重新标记（CMS remark）Stop The World
    并发清除（CMS concurrent sweep）

    CMS收集器

    - CMS收集器对CPU资源非常敏感，CMS收集器并发阶段默认启动的回收线程数是(CPU数量+3)/4，虽然不会导致用户线程停顿，但是会
        因为占用了一部分线程资源，而导致应用程序变慢，随着CPU数量越少，对用户线程影响越大。
    - 因为CMS收集器是并发清理，所以在清理阶段会产生浮动垃圾，因此需要为这些浮动垃圾预留一定的空间，
      但是如果预留空间不够，就会产生Concurrent Mode failure，进而启用Serial Old收集器，从而导致更长的停顿时间
        JDK 1.5 68%
        JDK 1.6 92%
        -XX:CMSInitiatingOccupancyFraction
        设置CMS收集器在老年代内存达到多少百分比时触发垃圾收集
    - 因为CMS收集是使用并发-清除算法，所以会产生空间碎片
        -XX:+UseCMSCompactAtFullCollection
        默认是开启的
        用于在CMS收集器顶不住要进行FullGC时开启内存碎片的合并整理（内存整理过程是无法并发的）
        -XX:CMSFullGCsBeforeCompaction
        设置执行多少次不压缩的Full GC后，跟着来一次待压缩的（默认值为0，表示每次进入Full GC时都进行碎片整理）


    */

}
