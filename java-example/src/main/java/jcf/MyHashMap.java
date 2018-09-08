package jcf;

import org.omg.CORBA.INITIALIZE;
import org.omg.CORBA.TRANSACTION_MODE;

import java.io.Serializable;
import java.util.*;

public class MyHashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>,Cloneable, Serializable {

    /**
     * 初始化桶大小，因为底层是数组，所以这是数组的默认大小
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; //aka 16

    /**
     * 桶最大值。
     */
    static final int MAXIMUM_CAPACITY = 1 << 30; //aka 1073741824

    /**
     * 默认的负载因子（0.75）
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75F;

    /**
     * 用于判断是否需要将链表转换为红黑树的阈值
     */
    static final int TREEIFY_THRESHOLD = 1 << 3; //aka 8




    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        K key;
        V value;
        Node<K,V> next;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public int hashCode() {
            //FIXME
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            //FIXME
            return super.equals(obj);
        }
    }


    /**
     * table 真正存放数据的数组。
     */
    transient Node<K,V>[] table;

    /**
     * Map 存放数量的大小。
     */
    transient int size;

    /**
     * 桶大小，可在初始化时显式指定。
     * 调整大小的下一个大小值(容量*负载因子)。
     */
    int threshold;

    /**
     * 负载因子，可在初始化时显式指定。
     */
    final float loadFactor;

    /**
     * Returns a power of two size for the given target capacity.
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity,DEFAULT_LOAD_FACTOR);
    }


    public MyHashMap(){
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }



    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }


    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key),key)) == null ? null : e.value;
    }


    final Node<K,V> getNode(int hash,Object key) {
        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
        //首先将key hash之后取得所定位的桶
        //如果桶为空，直接返回null
        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
            //判断桶的第一个位置，的key是否为查询的key，是就直接返回value
            if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                //如果第一个不匹配，则判断它的下一个是红黑树还是链表
                if (first instanceof TreeNode)
                    //红黑树就按照数的方式查找返回值
                    return null;
                //不然就按照链表的方式遍历匹配返回值
                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                }while ((e = e.next) != null);
            }
        }
        return null;
    }




    public V put(K key,V value) {
        return putVal(hash(key), key, value,false,true);
    }

    /**
     * 实现了Map.put和相关方法
     *
     * @param hash hash for key
     * @param key the key
     * @param value the value to put
     * @param onlyIfAbsent if true, don't change existing value
     * @param evict if false, the table is in creation mode.
     * @return previous value, or null if none
     */
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        //判断当前桶是否为空，空的就需要初始化
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        // 更具当前key的hashCode定位到具体的桶中的位置，并判断是否为空，
        // 为空表明没有hash冲突，就直接在当前位置创建一个新桶即可。
        // (n - 1) & hash 是计算位置的算法
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash,key,value,null);
        else {
            Node<K,V> e; K k;
            //如果当前桶有值（hash冲突），那么就要比较当前桶中的key，key的hashCode与写入的key是否相等
            //相等就赋值给e
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            //如果当前桶为红黑数，那么就要按照红黑树的方式写入数据
            else if (p instanceof TreeNode)
                e = p;
            else {//如果是个链表
                //就需要将当前key，value封装成一个新节点，写入到当前桶的后面（形成链表）
                for (int binCount = 0 ;; ++binCount) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash,key,value,null);
                        //判断当前链表的大小是否大于预设的阈值，大于就要转换为红黑树
                        if (binCount >= TREEIFY_THRESHOLD - 1)
                            treeifyBin(tab,hash);
                        break;
                    }
                    //如果在遍历过程中找到key相同时直接退出遍历
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            //如果e != null，就相当于存在相同的key，那就需要将值覆盖
            if (e != null) {
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                //空实现，有子类实现
                afterNodeAccess(e);
                return oldValue;
            }
        }
        //最后判断是否需要进行扩容
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }

    //回调允许LinkedHashMap后操作
    void afterNodeAccess(Node<K,V> p) {}
    void afterNodeInsertion(boolean evict) {}


    final Node<K,V>[] resize(){
        return null;
    }


    final void treeifyBin(Node<K,V>[] tab, int hash){}


    Node<K,V> newNode(int hash,K key, V value, Node<K,V> next) {
        return new Node<>(hash,key,value,next);
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return null;
    }

    static class Entry<K,V> extends MyHashMap.Node<K,V> {
        MyHashMap.Entry<K,V> before, after;
        Entry(int hash, K key, V value, MyHashMap.Node<K,V> next) {
            super(hash, key, value, next);
        }
    }


    static final class TreeNode<K,V> extends Entry<K,V> {
        MyHashMap.TreeNode<K,V> parent;  // red-black tree links
        MyHashMap.TreeNode<K,V> left;
        MyHashMap.TreeNode<K,V> right;
        MyHashMap.TreeNode<K,V> prev;    // needed to unlink next upon deletion
        boolean red;
        TreeNode(int hash, K key, V val, MyHashMap.Node<K,V> next) {
            super(hash, key, val, next);
        }
    }
}
