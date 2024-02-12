import java.awt.*;

public class BagliListe {
    Node head;
    Node tail;

    public BagliListe() {
        this.head = null;
        this.tail = null;
    }

    public void basaEkle(int x,int y){
        Node temp = new Node(x,y);
        if(head == null){
            head = tail = temp;
        }
        else{
            temp.next = head;
            head.prev = temp;
            head = temp;
        }
    }

    public void sonaEkle(int x,int y){
        Node temp = new Node(x,y);
        if(head == null){
            head = tail = temp;
        }
        else{
            tail.next = temp;
            temp.prev = tail;
            tail = temp;
        }
    }

    public void bastanSil(){
        head.next.prev = null;
        head=head.next;
    }

    public void sondanSil(){
        tail = tail.prev;
        tail.next = null;
    }

    public void aradanSil(Node node){
        Node iter = head;
        while(iter != null){
            if(iter == node){
                node.prev.next = node.next;
                node.next.prev = node.prev;
                break;
            }
            iter = iter.next;
        }
    }

    public void yaz(){
        Node iter = head;
        while(iter != null){
            System.out.println(iter.x);
            iter = iter.next;
        }
    }
}
