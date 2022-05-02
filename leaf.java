public class leaf extends Node {



    leaf(Node p){

        this.leftNode=null;
        this.middleNode=null;
        this.rightNode =null;
        this.key=null;
        this.value=null;
        this.size=0;
        this.parent=p;
        this.sumOfValues=null;
    }

    leaf(Key newKey, Value newValue){
        this.key= newKey.createCopy();
        if (newValue!=null)
        this.value=newValue.createCopy();
        this.leftNode=null;
        this.middleNode=null;
        this.rightNode =null;
        this.size=1;
        this.sumOfValues=newValue.createCopy();

    }
}
