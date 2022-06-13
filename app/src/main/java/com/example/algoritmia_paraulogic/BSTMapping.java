package com.example.algoritmia_paraulogic;

import java.util.Iterator;
import java.util.Stack;

/**
 * @author Álvaro Pimentel Lorente
 */

public class BSTMapping<K extends Comparable, V> {

    public Iterator iterator() {
        return new IteratorBSTSetMapping();
    }

    private class IteratorBSTSetMapping implements Iterator {
        private Stack<Node> iterator;

        public IteratorBSTSetMapping() {

            Node p;
            iterator = new Stack();
            if (root != null) {
                p = root;
                while (p.left != null) {
                    iterator.push(p);
                    p = p.left;
                }
                iterator.push(p);
            }

        }

        @Override
        public Object next() {
            Node p = iterator.pop();
            Pair pair = new Pair(p.key, p.valor);
            if (p.right != null) {
                p = p.right;
                while (p.left != null) {
                    iterator.push(p);
                    p = p.left;
                }
                iterator.push(p);
            }
            return pair;
        }

        @Override
        public boolean hasNext() {
            return !iterator.isEmpty();

        }
    }

    protected class Pair {

        protected V valor;
        protected K key;

        protected Pair(K key, V valor) {
            this.valor = valor;
            this.key = key;
        }

        public V getValor() {
            return valor;
        }

        public K getKey() {
            return key;
        }

        @Override
        public String toString() {
            return valor + " ("+ key +")";
        }
    }

    private class Node {

        private V valor;
        private K key;
        private Node left, right;

        public Node(K key, V valor, Node left, Node right) {
            this.valor = valor;
            this.key = key;
            this.left = left;
            this.right = right;
        }

        public void setValor(V valor) {
            this.valor = valor;
        }
    }

    private class Cerca {

        boolean trobat;

        public Cerca(boolean trobat) {
            this.trobat = trobat;
        }

    }

    private Node root;

    public BSTMapping() {
        root = null;
    }

    boolean isEmpty() {
        return root == null;
    }

    public boolean put(K key, V valor) {
        Cerca cerca = new Cerca(false);
        this.root = put(key, valor, root, cerca);
        return !cerca.trobat;
    }

    private Node put(K key, V valor, Node current, Cerca cerca) {
        if (current == null) {
            return new Node(key, valor, null, null);
        } else {
            if (current.key.equals(key)) {
                cerca.trobat = true;
                current.setValor(valor);
                return current;
            }
            if (key.compareTo(current.key) < 0) {
                current.left = put(key, valor, current.left, cerca);
            } else {
                current.right = put(key, valor, current.right, cerca);
            }
            return current;
        }
    }


    public V get(K key) {
        return get(key, root);
    }

    private V get(K key, Node current) {
        if (current == null) {
            return null;
        } else {
            if (current.key.equals(key)) {
                return current.valor;
            }

            if (key.compareTo(current.key) < 0) {
                return get(key, current.left);
            } else {
                return get(key, current.right);
            }
        }
    }


}


