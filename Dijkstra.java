import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.*;


public class Dijkstra {	
   ArrayList<Vertex> vertices = new ArrayList<Vertex>();
   boolean directed; 
   int nvertices;
   int nedges; 
   int numComp;
   int[][] matrix;
   Node[] heap;
   int [][] spt;
   int cap = 0;
   int size = 0;

   public Dijkstra(){
    } 

    public void MyHeap(int capacity) {
        heap = new Node[capacity + 1];
        cap = capacity;
    }

    public boolean insert(Node newArg) {
        if (size == cap) {
            return false;
        }
        size ++;
        heap[size] = newArg;
        System.out.println(Arrays.toString(heap));
        driftUp(size);
        return true;
    }

    public boolean buildHeap(Node[] heapList) {
        if (size > cap) {
            return false;
        }
        for (int i = 1; i < heapList.length; i++) {
            heap[i] = heapList[i];
        }
        //size = heapList.length - 1;
        for (int i = (size/2); i >= 1; i --) {
            driftDown(i);
        }
        return true;
    }

    public void driftUp(int index) {
        int hole = size;
        Node newArg = heap[index];
        while ((hole > 1) && newArg.disToStart < heap[hole/2].disToStart) {
            heap[hole] = heap[hole/2];
            hole = hole/2;
        }
        heap[hole] = newArg;
    }

    
    public void driftDown(int index) {
        Node tmp = heap[index];
        while ((index * 2) <= getHeapSize()) {
            int child = index * 2;
            if ((child != getHeapSize()) && (heap[child + 1].disToStart < heap[child].disToStart)) {
                child ++;
            }
            if (heap[child].disToStart < tmp.disToStart) {
                heap[index] = heap[child];
                index = child;
            }
            else {
                break;
            }
        }
        heap[index] = tmp;
    }
    

    public Node findMin() {
        if (getHeapSize() == 0) {
            return null;
        }
        return heap[1];
        
    }

    public Node deleteMin() {
        if (size == 0) {
            return null;
        }
        Node min = heap[1];
        heap[1] = heap[getHeapSize()];
        heap[getHeapSize()] = null;
        size --;
        driftDown(1);
        return min;
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if (size == cap) {
            return true;
        }
        return false;
    }

    public int getHeapCap() {
        return cap;
    }

    public int getHeapSize() {
        return size;
    }
    /*
    public int[] heapSortDecreasing(int[] toSort) {

        MyHeap heap = new MyHeap(toSort.length);
        heap.buildHeap(toSort);
        int[] sorted = new int[heap.getHeapSize()];
        int size = heap.getHeapSize() - 1;
        for (int i = 0; i <= size; i++) {
            sorted[size - i] = heap.deleteMin();
        }
        return sorted;
    }
    */
    

    public static int[][] findShortPaths (String filename) {
        // Open file, if no file exists return empty list
        Dijkstra dijke = new Dijkstra();
   		try {
   			dijke.readfile_graph(filename);
   		}
   		catch (FileNotFoundException ex) {
   			System.out.println("No file Found");
   			return new int[0][0];
        }
        dijke.MyHeap(dijke.nvertices + 1);
        dijke.size = dijke.nvertices;
        Node[] heaplist = dijke.createHeapList();
        dijke.buildHeap(heaplist);
        
        dijke.spt = new int[dijke.nvertices][3];
        int count = 0;
        while (dijke.size > 0) {
            Node min = dijke.deleteMin();
            min.key.discovered = true; 
            
            for (Vertex v: min.key.edges) {
                if (!v.discovered) {
                    dijke.updateVert(v, min, dijke.heap);
                }
            }
            dijke.spt[count][0] = min.key.key;
            dijke.spt[count][1] = min.disToStart;
            dijke.spt[count][2] = min.from.key;
            count ++;
            dijke.buildHeap(dijke.heap);
           
        }
        Arrays.sort(dijke.spt, new java.util.Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return Integer.compare(a[0], b[0]);
            }
        });
        return dijke.spt;
    }

    public void updateVert(Vertex vert, Node min, Node[] heap) {
        boolean updated = false;
        int i = 1;
        while (!updated) {
            if (heap[i].key.key == vert.key) {
                if (heap[i].disToStart == Integer.MAX_VALUE) {
                    heap[i].disToStart = matrix[vert.key][min.key.key] + min.disToStart;
                    heap[i].from = min.key;
                }
                else if (heap[i].disToStart > matrix[vert.key][min.key.key] + min.disToStart) {
                    heap[i].disToStart = matrix[vert.key][min.key.key] + min.disToStart;
                    heap[i].from = min.key;
                }
                updated = true;
            }
            else {
                i ++;
            }
        }
    }

    public Node[] createHeapList() {
        Node[] list1 = new Node[nvertices + 1];
        list1[0] = null;
        for (int i = 1; i < nvertices + 1; i ++) {
            if (vertices.get(i - 1).key == 1) {
                list1[1] = new Node(vertices.get(i - 1), 0, vertices.get(i - 1));
            }
            else {
                list1[i] = new Node(vertices.get(i - 1), Integer.MAX_VALUE, null);
            }   
        }
        return list1;
    }

    public void printGrid() { 
        for(int i = 0; i < nvertices + 1; i++)
        {
            for(int j = 0; j < nvertices + 1; j++)
            {
                System.out.printf("%5d ", matrix[i][j]);
            }
            System.out.println();
        }
    } 
	
   void readfile_graph(String filename) throws FileNotFoundException {
      int x,y,weight;
        //read the input
      FileInputStream in = new FileInputStream(new File(filename));
      Scanner sc = new Scanner(in);
      int temp = sc.nextInt(); // if 1 directed; 0 undirected
      directed = (temp == 1);
    	nvertices = sc.nextInt();
      for (int i=0; i<=nvertices-1; i++){
         Vertex tempv = new Vertex(i+1);   // kludge to store proper key starting at 1
         vertices.add(tempv);
      }

        nedges = sc.nextInt();   // m is the number of edges in the file
        matrix = new int[nvertices + 1][nvertices + 1];
      int nedgesFile = nedges;
		for (int i=1; i<=nedgesFile ;i++)	{
			// System.out.println(i + " compare " + (i<=nedges) + " nedges " + nedges);
         x=sc.nextInt();
            y=sc.nextInt();
                weight= sc.nextInt();
         //  System.out.println("x  " + x + "  y:  " + y  + " i " + i);
			insert_edge(x,y, weight, directed);
		}  
		   // order edges to make it easier to see what is going on
		for(int i=0;i<=nvertices-1;i++)	{
			Collections.sort(vertices.get(i).edges);
        }
    }
    
	public int findUnvisited() {
      for (int i = 0; i < nvertices; i++) {

         if (((Vertex)vertices.get(i)).discovered == false) {
           return i;
         }
      }
      
      return -1;
   }

   
   static void process_vertex_early(Vertex v)	{
		timer++;
		v.entry_time = timer;
		//     System.out.printf("entered vertex %d at time %d\n",v.key, v.entry_time);
	}
	
	static void process_vertex_late(Vertex v)	{
		timer++;
		v.exit_time = timer;
		//     System.out.printf("exit vertex %d at time %d\n",v.key , v.exit_time);
	}

	static void process_edge(Vertex x,Vertex y) 	{
		int c = edge_classification(x,y);
		if (c == BACK) System.out.printf("back edge (%d,%d)\n",x.key,y.key);
		else if (c == TREE) System.out.printf("tree edge (%d,%d)\n",x.key,y.key);
		else if (c == FORWARD) System.out.printf("forward edge (%d,%d)\n",x.key,y.key);
		else if (c == CROSS) System.out.printf("cross edge (%d,%d)\n",x.key,y.key);
		else System.out.printf("edge (%d,%d)\n not in valid class=%d",x.key,y.key,c);	
	}
	
	static void initialize_search(Dijkstra g)	{
		for(Vertex v : g.vertices)		{
			v.processed = v.discovered = false;
			v.parent = null;
		}
	}
	
	static final int TREE = 1, BACK = 2, FORWARD = 3, CROSS = 4;
	static int timer = 0;
	
	static int edge_classification(Vertex x, Vertex y)	{
		if (y.parent == x) return(TREE);
		if (y.discovered && !y.processed) return(BACK);
		if (y.processed && (y.entry_time > x.entry_time)) return(FORWARD);
		if (y.processed && (y.entry_time < x.entry_time)) return(CROSS);
		System.out.printf("Warning: self loop (%d,%d)\n",x,y);
		return -1;
	}
   
	void insert_edge(int x, int y, int weight, boolean directed) 	{
		Vertex one = vertices.get(x-1);
      Vertex two = vertices.get(y-1);
      one.edges.add(two);      
		vertices.get(x-1).degree++;
		if(!directed) {
            insert_edge(y,x, weight, true);
            matrix[y][x] = weight;
            matrix[x][y] = weight;
        }
		else {
			((Vertex) vertices.get(y-1)).in_degree ++;
            nedges++;
        }
	}
   void remove_edge(Vertex x, Vertex y)  {
		if(x.degree<0)
			System.out.println("Warning: no edge --" + x + ", " + y);
		x.edges.remove(y);
		x.degree--;
		y.in_degree --;
	} 

	void print_graph()	{
		for(Vertex v : vertices)	{
			System.out.println("vertex: "  + v.key + "   in: " + v.in_degree);
			for(Vertex w : v.edges)
				System.out.print("  adjacency list: " + w.key);
			System.out.println();
		}
    }
    
    class Node implements Comparable<Node> {
        Vertex key;
        int disToStart;
        Vertex from;

        public Node(Vertex cur, int dis, Vertex prev) {
            key = cur;
            disToStart = dis;
            from = prev;
        }

        public int compareTo(Node other){
         if (this.disToStart > other.disToStart){
            return 1;
         }         else if (this.disToStart < other.disToStart) {
            return -1;
         }
         else 
            return 0;
         }

         public int getKey() {
             return key.key;
         }
         public int getDis() {
             return disToStart;
         }

         public int getPrevKey() {
             return from.key;
         }

         public String toString() {
             return "(" + getKey() + "(" + getDis() + "," + ")";
         }
    }

   class Vertex implements Comparable<Vertex> {
      int key;
      int degree;
      int component;
      int in_degree;
      int color = -1;    // use mod numColors, -1 means not colored
      boolean discovered = false;
      boolean processed = false;
      int entry_time = 0;
      int exit_time = 0;
      Vertex parent = null;
      int toSource = -1;
      ArrayList<Vertex> edges = new ArrayList<Vertex>();
   
      public Vertex(int tkey){ 
         key = tkey;
      }
      public int compareTo(Vertex other){
         if (this.key > other.key){
            return 1;
         }         else if (this.key < other.key) {
            return -1;
         }
         else 
            return 0;
         }
      }

	Vertex unProcessedV(){	   
      for(Vertex v:  vertices)  {
         if (! v.processed ) return v;
      }
   return null;	
   }
}