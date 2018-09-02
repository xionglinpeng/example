package jcf;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MyLinkedHashMap<K,V> extends MyHashMap<K,V> implements Map<K,V>{





    static class Entry<K,V> extends MyHashMap.Node<K,V> {
        Entry<K,V> before, after;
        public Entry(int hash, K key, V value, MyHashMap.Node<K, V> next) {
            super(hash, key, value, next);
        }
    }


    transient MyLinkedHashMap.Entry<K,V> head;

    transient MyLinkedHashMap.Entry<K,V> tail;

    final boolean accessOrder;

    void afterNodeAccess(MyHashMap.Node<K,V> e){
        MyLinkedHashMap.Entry<K,V> last;
        if (!accessOrder && (last = tail) != e) {
            MyLinkedHashMap.Entry<K, V> p = (MyLinkedHashMap.Entry<K, V>) e,
                    b = p.before, a = p.after;
            p.after = null;
            if (b == null) {
                head = a;
            } else {
                b.after = a;
            }
            if (a != null) {
                a.before = b;
            } else {
                last = b;
            }
            if (last == null) {
                head = p;
            } else {
                p.before = last;
                last.after = p;
            }
            tail = p;
        }
    }


    public MyLinkedHashMap(int initialCapacity, float loadFactor){
        super(initialCapacity,loadFactor);
        accessOrder = false;
    }

    public MyLinkedHashMap(int initialCapacity){
        super(initialCapacity);
        accessOrder = false;
    }

    public MyLinkedHashMap() {
        super();
        this.accessOrder = false;
    }

    public MyLinkedHashMap(Map<? extends K, ? extends V> m){
        super();
        this.accessOrder = false;
//        putMapEntries(m, false);
    }

    public MyLinkedHashMap(int initialCapacity, float loadFactor, boolean accessOrder){
        super(initialCapacity, loadFactor);
        this.accessOrder = accessOrder;
    }


    private void linkNodeLast(MyLinkedHashMap.Entry<K,V> p) {
        MyLinkedHashMap.Entry<K,V> last = tail;
        tail = p;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last .after = p;
        }
    }

    @Override
    Node<K, V> newNode(int hash, K key, V value, Node<K, V> e) {
        MyLinkedHashMap.Entry<K,V> p =
                new MyLinkedHashMap.Entry<K, V>(hash, key, value, e);
        linkNodeLast(p);
        return p;
    }
}
