public class BalancedTree {


    protected Node root;


    public BalancedTree() {
        this.root = new Node(null);

    }

    public void insert(Key newKey, Value newValue) {
        Node y = this.root;
        Node  newnode = new leaf(newKey.createCopy(), newValue.createCopy());

        if (y.leftNode == null) {//if this is the root
            setChildren(y, newnode, y.middleNode, null);
            y.size+=1;
            return;
        } else if (y.middleNode == null) {//the root have only one child
            if(newKey.compareTo(y.leftNode.key)>0){
                setChildren(y, y.leftNode, newnode, y.rightNode);
            }
            else
                setChildren(y, newnode, y.leftNode, y.rightNode);
            y.size=2;
            return;
        }
        while (y.leftNode!= null) {//find the right place for the new node
            y = getNode(newKey, y);
        }
        Node x = y.parent;
        Node v = insertAndSplit(x, newnode);

        while (x != this.root) {
            x = x.parent;

            if (v != null) {

                v = insertAndSplit(x, v);
            } else {
                updateKey(x);
                x.size = x.leftNode.size + x.middleNode.size;
                x.sumOfValues = x.leftNode.sumOfValues.createCopy();
                x.sumOfValues.addValue(x.middleNode.sumOfValues.createCopy());
                if (x.rightNode != null) {
                    x.size += x.rightNode.size;
                    x.sumOfValues = x.leftNode.sumOfValues.createCopy();
                    x.sumOfValues.addValue(x.middleNode.sumOfValues.createCopy());
                    x.sumOfValues.addValue(x.rightNode.sumOfValues.createCopy());
                }
            }
        }


        if (v != null) {// if we arived until here-we didnt find place to the new node in the tree and we need new root
            Node newroot = new Node(null);
            setChildren(newroot, x, v, null);
            this.root = newroot;
        }
    }


    private Node getNode(Key newKey, Node y) {//same ac class
        if (newKey.compareTo(y.leftNode.key) < 0) {
            return y.leftNode;
        } else if (newKey.compareTo(y.middleNode.key) < 0) {
            return y.middleNode;
        } else {
            if (y.rightNode == null) {
                return y.middleNode;
            } else {
                return y.rightNode;
            }

        }
    }


    public Node insertAndSplit(Node x, Node z) {//same as class
        Node l = x.leftNode;
        Node m = x.middleNode;
        Node r = x.rightNode;

        if (r == null) {
            if (z.key.compareTo(l.key) < 0) {
                setChildren(x, z, l, m);
            } else if (z.key.compareTo(m.key) < 0) {
                setChildren(x, l, z, m);
            } else {
                setChildren(x, l, m, z);
            }

            return null;
        }

        Node y = new Node(null);

        if (z.key.compareTo(l.key) < 0) {
            setChildren(x, z, l, null);
            setChildren(y, m, r, null);
        } else if (z.key.compareTo(m.key) < 0) {
            setChildren(x, l, z, null);
            setChildren(y, m, r, null);
        } else if (z.key.compareTo(r.key) < 0) {
            setChildren(x, l, m, null);
            setChildren(y, z, r, null);
        } else {
            setChildren(x, l, m, null);
            setChildren(y, r, z, null);
        }
        return y;
    }

    public void setChildren(Node x, Node l, Node m, Node r) {
        x.leftNode = l;
        l.parent = x;
        x.sumOfValues = x.leftNode.sumOfValues.createCopy();
        x.size = l.size;

        if (m != null) {
            m.parent = x;
            x.middleNode = m;
            x.size = l.size + m.size;
            x.sumOfValues.addValue(m.sumOfValues.createCopy());

        }
        if (m == null)
            x.middleNode = null;
        if (r == null)
            x.rightNode = null;

        if (r != null) {
            x.rightNode = r;
            r.parent = x;
            x.size += r.size;
            x.sumOfValues.addValue(x.rightNode.sumOfValues.createCopy());


        }
        updateKey(x);
    }

    public void updateKey(Node x) {
        x.key = x.leftNode.key;
        if (x.middleNode != null)
            x.key = x.middleNode.key;
        if (x.rightNode != null) {
            x.key = x.rightNode.key;
        }
    }

    public void delete(Key key) {
        Node p = searchintree(this.root, key);
        if (p == null) {
            return;
        } else {
            deletewithroot(p, key);
        }
    }

    public void deletewithroot(Node x, Key key) {
        Node ROOT = this.root;
        if (x == ROOT) {
            this.root = new Node(null);
            return;
        }
        if (x.parent == this.root) {
            Node l = null;
            Node r = null;
            if (x == this.root.leftNode) {
                if (this.root.middleNode == null) {
                    this.root = new Node(null);
                    return;
                } else {
                    l = ROOT.middleNode;
                    r = ROOT.rightNode;
                }
            } else if (x == this.root.middleNode) {
                l = ROOT.leftNode;
                r = ROOT.rightNode;
            } else {
                l = ROOT.leftNode;
                r = ROOT.middleNode;
            }
            setChildren(ROOT, l, r, null);
            return;
        }
        Node y = x.parent;
        if (x == y.leftNode) {
            setChildren(y, y.middleNode, y.rightNode, null);
        } else if (x == y.middleNode) {
            setChildren(y, y.leftNode, y.rightNode, null);
        } else {
            setChildren(y, y.leftNode, y.middleNode, null);
        }

        while (y != null) {
            if (y.middleNode == null) {
                if (y != this.root)
                    y = Borrow_Or_Merge(y);
                else {
                    this.root = y.leftNode;
                    y.leftNode.parent = null;
                    return;
                }
            } else {
                updateKey(y);
                y.size=y.leftNode.size+y.middleNode.size;
                y.sumOfValues = y.leftNode.sumOfValues.createCopy();
                y.sumOfValues.addValue(y.middleNode.sumOfValues.createCopy());
                if (y.rightNode != null) {
                    y.sumOfValues.addValue(y.rightNode.sumOfValues.createCopy());
                    y.size+=y.rightNode.size;
                }
                y = y.parent;
            }
        }
    }

    public Node Borrow_Or_Merge(Node y) {//like class
        if (y == null || y.parent == null) {
            return null;
        }
        Node z = y.parent;
        if (y == z.leftNode) {
            Node x = z.middleNode;
            if (x.rightNode != null) {
                setChildren(y, y.leftNode, x.leftNode, null);
                setChildren(x, x.middleNode, x.rightNode, null);

            } else {
                setChildren(x, y.leftNode, x.leftNode, x.middleNode);

                setChildren(z, x, z.rightNode, null);
            }
            return z;


        }
        if (y == z.middleNode) {
            Node x = z.leftNode;
            if (x.rightNode != null) {
                setChildren(y, x.rightNode, y.leftNode, null);
                setChildren(x, x.leftNode, x.middleNode, null);

            } else {
                setChildren(x, x.leftNode, x.middleNode, y.leftNode);
                setChildren(z, x, z.rightNode, null);

            }
            return z;
        }
        Node x = z.middleNode;
        if (x.rightNode != null) {
            setChildren(y, x.rightNode, y.leftNode, null);
            setChildren(x, x.leftNode, x.middleNode, null);

        } else {
            setChildren(x, x.leftNode, x.middleNode, y.leftNode);
            setChildren(z, z.leftNode, x, null);

        }
        return z;
    }

    public Value search(Key key) {
        Node x = searchintree(this.root, key);
        if (x == null) {
            return null;
        }
        return x.sumOfValues.createCopy();
    }

    public Node searchintree(Node Root, Key key) {//like class
        if (Root.key == null) {
            return null;
        }
        if (Root.leftNode == null) {
            if (Root.key.compareTo(key) == 0) {
                return Root;
            } else {
                return null;
            }
        }
        if (Root.key.compareTo(key) == 0 && Root.leftNode.key.compareTo(key) == 0) {
            return Root.leftNode;
        }
        if (this.root.middleNode == null)
            return null;
        if (Root.leftNode.key.compareTo(key) > 0 || Root.leftNode.key.compareTo(key) == 0) {
            return searchintree(Root.leftNode, key);

        } else if (Root.middleNode.key.compareTo(key) > 0 || Root.middleNode.key.compareTo(key) == 0) {
            return searchintree(Root.middleNode, key);

        } else {
            if (Root.rightNode != null)
                return searchintree(Root.rightNode, key);
            else
            return searchintree(Root.middleNode, key);
        }
    }

    public int rank(Key key) {
        Node x = searchintree(this.root, key);
        if (x == null) {
            return 0;
        }

        int rank = 1;
        Node root = this.root;
        while (!(root instanceof leaf)) {//we will find the right place and until we find it, we will sum the number of leaf in each node
            if (key.compareTo(root.leftNode.key) == 0 ||key.compareTo(root.leftNode.key) == -1 )
                root = root.leftNode;
            else if (key.compareTo(root.middleNode.key) == 0 ||key.compareTo(root.middleNode.key) == -1 ) {
                rank += root.leftNode.size;
                root = root.middleNode;
            } else {
                 if(root.rightNode!=null) {
                     rank += root.leftNode.size;
                     rank += root.middleNode.size;
                     root = root.rightNode;

                 }
            }
        }

        return rank;


    }

    public Key select(int i) {
        Node x = selectwithroot(this.root, i);
        if (x == null) {
            return null;
        }
        return x.key.createCopy();
    }

    public Node selectwithroot(Node x, int i) {//like class
        if (i <= 0) {
            return null;
        }
        if (x == null) {
            return null;
        }
        if (this.root.size < i) {
            return null;
        }


        if (x.middleNode == null) {
            return x;
        }
        if (x instanceof leaf) {
            return x;
        }


        int s_left = x.leftNode.size;
        int s_left_middle = x.middleNode.size + x.leftNode.size;
        if (i <= s_left) {
            return selectwithroot(x.leftNode, i);
        } else if (i <= s_left_middle) {
            return selectwithroot(x.middleNode, i - s_left);

        } else {
            return selectwithroot(x.rightNode, i - s_left_middle);
        }

    }

    public Value sumValuesInInterval(Key key1, Key key2) {

        Node theSmall = null;
        Node theBig = null;
        int x;
        int y;
        if (key1.compareTo(key2) > 0) {
            return null;
        }

        if (this.root.key == null) {
            return null;
        }

        Node ll = searchintree(this.root, key1);
        if (ll == null) {//if this node dont excist , we will insert him to find the next node ,the next node is the succsuer
            insert(key1, this.root.sumOfValues.createCopy());
            x = rank(key1);
            if (x == 2 && this.root.size == 2) {
                theSmall=this.root.leftNode;
            }
            else
            theSmall = selectwithroot(this.root, x+1);
            delete(key1);
            if (theSmall.leftNode != null)
                theSmall = theSmall.leftNode;


        } else {
            x = rank(key1);
            theSmall = ll;
        }
        Node pl = searchintree(this.root, key2);//if this node dont excist , we will insert him to find the next node ,the next node is the predeccor
        if (pl == null) {
            insert(key2, this.root.sumOfValues.createCopy());
            y = rank(key2);
            if (y == 2 && this.root.size == 2) {
               theBig=this.root.leftNode;
            }
            else
                theBig = selectwithroot(this.root, y-1);
            delete(key2);
            if (theBig.leftNode != null)
                theBig = theBig.leftNode;
        } else {
            theBig = pl;
            y = rank(key2);
        }
        if(x==y){
            if (ll == null && pl == null) {//if its the same number ant its not excict-no one will fit
                return null;
        }}

        if (theBig == theSmall) {
            if (ll == null && pl == null) {
                if (key1.compareTo(theSmall.key) < 0 && key2.compareTo(theBig.key) > 0) {
                    return theBig.sumOfValues.createCopy();
                } else {
                    return null;
                }

            } else
                return theBig.sumOfValues.createCopy();
        }
        Value val = theSmall.sumOfValues.createCopy();
        val.addValue(theBig.sumOfValues.createCopy());
        Node theSmallFather = theSmall.parent;
        Node theBigFather = theBig.parent;

        while (theSmall.parent != theBig.parent) {//we will sum all the values until we will get the father
            if (theSmall == theSmallFather.leftNode) {
                if (theSmallFather.middleNode != null)
                val.addValue(theSmallFather.middleNode.sumOfValues.createCopy());
                if (theSmallFather.rightNode != null)
                    val.addValue(theSmallFather.rightNode.sumOfValues.createCopy());
            }
           if(theSmall == theSmallFather.middleNode){
                if (theSmallFather.rightNode != null)
                    val.addValue(theSmallFather.rightNode.sumOfValues.createCopy());}


            theSmall = theSmall.parent;
            theSmallFather=theSmall.parent;

             if (theBig == theBigFather.middleNode) {
                val.addValue(theBigFather.leftNode.sumOfValues.createCopy());

            }if(theBigFather.rightNode!=null&&theBig==theBigFather.rightNode) {
                val.addValue(theBigFather.leftNode.sumOfValues.createCopy());
                val.addValue(theBigFather.middleNode.sumOfValues.createCopy());
            }
            theBig = theBig.parent;
             theBigFather=theBig.parent;
        }
        Node parentOfBoth = theBig.parent;
        if(theBigFather.rightNode!=null) {
            if (theSmall == parentOfBoth.leftNode) {
                if (theBig == parentOfBoth.rightNode) {
                    val.addValue(parentOfBoth.middleNode.sumOfValues.createCopy());
                }
            }
        }
        return val;

    }
    }


