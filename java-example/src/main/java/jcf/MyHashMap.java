package jcf;

import org.omg.CORBA.TRANSACTION_MODE;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class MyHashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>,Cloneable, Serializable {

    /**
     * 初始化桶大小，因为底层是数组，所以这是数组的默认大小
     */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; //aka 16

    /**
     * 桶最大值。
     */
    static final int MAXINUM_CPAPCITY = 1 << 30; //aka 1073741824

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
     */
    int threshold;

    /**
     * 负载因子，可在初始化时显式指定。
     */
    final float loadFactor;


    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }


    public V put(K key,V value) {
        return putVal(hash(key),key,value);
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
    final V putVal(int hash, K key, V value) {
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



        }








    }


    final Node<K,V>[] resize(){
        return null;
    }



    Node<K,V> newNode(int hash,K key, V value, Node<K,V> next) {
        return new Node<>(hash,key,value,next);
    }


    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
