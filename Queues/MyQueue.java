package Queues;

import java.util.NoSuchElementException;

/**
 * MyQueue
 */
public class MyQueue<E> implements Queue<E> {
    private Node front,
                 rear;

    public MyQueue () {
        this.front = null;
        this.rear = null;
    }
    public boolean isEmpty(){
        return this.front == null;
    }
    public E dequeue(){
        if(this.isEmpty()) throw new NoSuchElementException();
        E tmp = this.front.elem;
        if((this.front = this.front.next) == null) this.rear = null;
        return tmp;
    }
    public void enqueue(E e){
        Node tmp = new Node();
        tmp.elem = e;
        if(this.rear != null)
            this.rear.next = tmp;
        else
            this.front = tmp;
        this.rear = tmp;
    }
    public E front(){
        if(this.isEmpty()) throw new NoSuchElementException();
        return this.front.elem;
    }
    public E rear(){
        if(this.isEmpty()) throw new NoSuchElementException();
        return this.rear.elem;
    }
    private class Node{
        public E elem;
        public Node next;
    }
}