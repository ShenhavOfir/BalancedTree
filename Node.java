

public class Node {

        protected Key key;
        protected Value value;
        protected Node leftNode;
        protected Node middleNode;
        protected Node parent;
        protected Node rightNode;
        protected int size;
        protected Value sumOfValues;


        Node(){
        }

        Node(Node p){
            this.leftNode=null;
            this.middleNode=null;
            this.rightNode =null;
            this.key=null;
            this.value=null;
            this.size=0;
            this.parent=null;
            this.sumOfValues=null;

        }

        Node(Key newKey, Value newValue){
            this.key= newKey.createCopy();
            this.value=newValue.createCopy();
            this.size=0;
            this.leftNode=null;
            this.middleNode=null;
            this.rightNode =null;
            this.sumOfValues=newValue.createCopy();

        }

    }

