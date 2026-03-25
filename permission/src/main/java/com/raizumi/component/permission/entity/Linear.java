package com.raizumi.component.permission.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Provisional customize
 * @param <K>
 * @param <V>
 */
public class Linear <K , V> {
    private final Comparator<K> comparator;

    @SuppressWarnings("unchecked")
    public Linear(){
        arr = (K[])  new Object[8];

        comparator = new Comparator<K>() {};
    }

    @SuppressWarnings("unchecked")
    public Linear(Comparator<K> comparator) {
        arr = (K[])  new Object[8];

        this.comparator = comparator;
    }

    private int end = 0;

    private K[] arr;

    private Map<K, V> scrutinies = new HashMap<>();

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Map<K, V> getScrutinies() {
        return scrutinies;
    }

    public void setScrutinies(Map<K, V> scrutinies) {
        this.scrutinies = scrutinies;
    }


    public void put(K k, V v) {
        V t1 = scrutinies.get(k);
        if (t1 == null) {
            scrutinies.put(k, v);

            arr[end] = k;
            for (int i = end; i > 0; i--) {
                Winner<K> winner = comparator.won(arr[i], arr[i - 1]);
                arr[i] = winner.getWinner();
                arr[i - 1] = winner.getLosser();
            }

            end++;
            if(end == arr.length){
                arr = Arrays.copyOf(arr, end * 2);
            }
        }
    }


    public V get(){
        if (end == 0) {
            return null;
        }

        K k = arr[0];

        return scrutinies.get(k);
    }

    public V remove() {
        if (end == 0) {
            return null;
        }

        K k = arr[0];
        V v = scrutinies.get(k);
        scrutinies.remove(k);

        for(int i = 0; i < end; i++) {
            if (i == end - 1){
                end = i;
            }else{
                arr[i] = arr[i+1];
            }
        }

        return v;
    }

    public interface Comparator<K>{
        default Winner<K> won(K k1, K k2){

            if(k1 == k2){
                return new Winner<>(k1, k2);
            }

            return new Winner<>(
                    k1.hashCode() > k2.hashCode() ? k1 : k2,
                    k1.hashCode() > k2.hashCode() ? k2 : k1
            );
        }
    }

    public static class Winner <K> {

        public Winner() {
            winner = null;
            losser = null;
        }

        public Winner(K winner, K losser) {
            this.winner = winner;
            this.losser = losser;
        }


        private K winner;

        private K losser;


        public K getWinner() {
            return winner;
        }

        public void setWinner(K winner) {
            this.winner = winner;
        }

        public K getLosser() {
            return losser;
        }

        public void setLosser(K losser) {
            this.losser = losser;
        }
    }
}
