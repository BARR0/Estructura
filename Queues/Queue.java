package Queues;

public interface Queue<E>{
    public boolean isEmpty();
    public E dequeue();
    public void enqueue(E e);
    public E front();
    public E rear();
}