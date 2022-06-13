package com.example.algoritmia_paraulogic;


import java.util.Arrays;
import java.util.Iterator;

/**
 * Clase para contener un conjunto de elementos sin ordenar.
 * @author Álvaro Pimentel Lorente
 * @param <E> Tipo de los elementos del conjunto.
 */
public class UnsortedArraySet<E> {

    private E[] array;
    private int n;

    /**
     * Se genra un array de objetos de tipo E y tamaño inicial maximo.
     *
     * @param max tamaño inicial máximo del array.
     */
    public UnsortedArraySet(int max) {
        this.array = (E[]) new Object[max];
        this.n = 0;
    }

    /**
     * Búsqueda lineal para ver si el elemento está en el conjunto.
     * O(n): búsqueda lineal.
     *
     * @param elem elemento a buscar.
     * @return boolean true si el elemento está en el conjunto, false en caso contrario.
     */
    public boolean contains(E elem) {
        for (int i = 0; i < n && !isEmpty(); i++) {
            if (this.array[i] != null && this.array[i].equals(elem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inserta un elemento en el conjunto.
     * O(n): busqueda lineal porque antes de añadir el elemento se busca si ya existe.
     *
     * @param elem elemento a añadir.
     * @return boolean true si se añade el elemento, false en caso contrario.
     */
    public boolean add(E elem) {
        if (n < this.array.length) {
            int i = 0;
            while (i < this.array.length) {
                if (!contains(elem) && this.array[i] == null) {
                    this.array[i] = elem;
                    n++;
                    return true;
                }
                i++;
            }
        }
        return false;
    }

    /**
     * Elimina un elemento del conjunto.
     * O(n): busqueda lineal porque antes de eliminar el elemento se busca si existe.
     *
     * @param elem elemento a eliminar.
     * @return boolean true si se elimina el elemento, false en caso contrario.
     */
    public boolean remove(E elem) {
        for (int i = 0; i < n && !isEmpty(); i++) {
            if (this.array[i] != null && this.array[i].equals(elem)) {
                this.array[i] = null;
                n--;
                return true;
            }
        }
        return false;
    }

    /**
     * Se comprueba si el conjunto está vacio
     * O(1)
     *
     * @return boolean true si el conjunto está vacio, false en caso contrario.
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Iterador del conjunto.
     * @return IteratorUnsortedArraySet
     */
    public Iterator iterator() {
        return new IteratorUnsortedArraySet();
    }

    /**
     * Método toString para imprimir el conjunto.
     *
     * @return String
     */
    public String toString() {
        return "UnsortedArraySet{" + "array=" + Arrays.deepToString(array) + ", n=" + n + '}';
    }

    private class IteratorUnsortedArraySet implements Iterator {

        private int idxIterator;

        private IteratorUnsortedArraySet() {
            idxIterator = 0;
        }

        @Override
        public boolean hasNext() {
            return idxIterator < n;
        }

        @Override
        public Object next() {
            idxIterator++;
            return array[idxIterator - 1];
        }
    }

}