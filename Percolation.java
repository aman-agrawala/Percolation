//import edu.princeton.cs.algs4.StdRandom;
//import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] grid;
    private WeightedQuickUnionUF connect;
    private int connectLength;
    private int n;
    
    public Percolation(int n)
    {
        this.n = n;
        if( n  <= 0)
        {
            throw new IllegalArgumentException();
        }
        grid = new int[n][n]; // create a grid filled with 0. 0 is blocked, 1 is open, 2 is full. 
        
        connect = new WeightedQuickUnionUF((n*n)+2); // create UF array with 0 element as a "virtual" top and the last element as a "virtual" bottom
        for(int i = 1; i < n+1; i++) // unite virtual top with top 5 elements and virtual bottom with bottom 5 elements
        {
            connect.union(i,0); // connects the virtual top with first row          
            connect.union(i+(n-1)*n,(n*n)+1); // connects the virtual bottom with bottom row
        }
        connectLength = n*n+2;
    }
    
    public void open(int row, int col)
    {
        if(grid[row-1][col-1] == 0) // check if site is not already open
        {
            int[] adjacent = new int[4]; // we store the location of adjacents in this variable.
            // 1 stands for an adjacent block on top the specified location, 2 for a block to the
            // right, 3 for a block to the bottom, 4 for a block to the left and 0 for not open block.
            
            int n = grid.length;
            int connectLoc; // First we convert the r,c pair to a location in connect
            if(col%n==0)
            {
                connectLoc = col+(row-1)*5;
            }
            else
            {
                int c = col%5;
                connectLoc = c + (row-1)*5;
            }
            
            if(connect.connected(0,connectLoc)) // if the opened value is connected to the virtual top then fill it in.
            {
                grid[row-1][col-1] = 2;
            }
            else
            {
                grid[row-1][col-1] = 1;
            }
            
            for(int i =0; i<4; i++) // iterate through the four possible adjacent spots
            {
                try
                {
                    switch(i)
                    {
                        case 0: 
                            if(grid[row-2][col-1] >= 1) // top location
                        {
                            connect.union(connectLoc,connectLoc-n);
                            adjacent[0] = 1;
                            break;
                        }
                        case 1:
                            if(grid[row-1][col] >= 1) // right side
                        {
                            connect.union(connectLoc,connectLoc+1);
                            adjacent[1] = 2;
                            break;
                        }
                        case 2:
                            if(grid[row][col-1] >= 1) // bottom side
                        {
                            connect.union(connectLoc, connectLoc+n);
                            adjacent[2] = 3;
                            break;
                        }
                        case 3:
                            if(grid[row-1][col-2] >= 1) // left side
                        {
                            connect.union(connectLoc,connectLoc-1);
                            adjacent[3] = 4;
                            break;
                        }
                    }
                }
                catch(Exception e)
                {
                    
                }
            }
            
            for(int a = 0; a < 4; a++) // Now we try to find out if adjacents are full or need to be filled
            {
                switch(adjacent[a])
                {
                    case 1:
                        if((grid[row-2][col-1] == 2 && connect.connected(0,connectLoc-n) || grid[row-1][col-1] == 2))
                    {
                        grid[row-2][col-1] = 2;
                        grid[row-1][col-1] = 2;
                    }
                        break;
                    case 2:
                        if((grid[row-1][col] == 2 && connect.connected(0,connectLoc+1) || grid[row-1][col-1] == 2))
                    {
                        grid[row-1][col] = 2;
                        grid[row-1][col-1] = 2;
                    }
                        break;
                    case 3:
                        if((grid[row][col-1] == 2 && connect.connected(0,connectLoc+n) || grid[row-1][col-1] == 2))
                    {
                        grid[row][col-1] = 2;
                        grid[row-1][col-1] = 2;
                    }
                        break;
                    case 4:
                        if((grid[row-1][col-2] == 2 && connect.connected(0,connectLoc-1) || grid[row-1][col-1] == 2))
                    {
                        grid[row-1][col-2] = 2;
                        grid[row-1][col-1] = 2;
                    }
                        break;
                }
            }    
        }
        
        
//        if(grid[row-1][col-1] == 0) // check if site is not already open
//        {
//            grid[row-1][col-1] == 1 // site is now open
//            if(!(row == 1 || row == n && col == 1 || col == n) ) // not a corner
//            {
//                if(row == 1) // site isn't a corner but on top row
//                {
//                    connect.union(connectLoc,0); // connect to virtual top
//                    grid[row-1][col-1] = 2; // site is now full
//                    if(grid[row-1][col-2] == 2) // if the site to the left is full then connect
//                    {
//                        connect.union(connectLoc,connectLoc-1);
//                    }
//                    else if(grid[row][col-1] == 2) // if site to the bottom is full then connect
//                    {
//                        connect.union(connectLoc,connectLoc+n);
//                    }
//                    else if(grid[row-1][col] == 2) // if site to right is full then connect
//                    {
//                        connect.union(connectLoc,connectLoc+1);
//                    }
//                }
//                
//                else if(row == 5) // site isn't a corner but on bottom row
//                {
//                    connect.union(connectLoc,(n*n)+1); // connect to virtual bottom
//                    if(grid[row-1][col-2] == 2) // site on left is full
//                    {
//                        connect.union(connectLoc,connectLoc-1);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                    else if(grid[row-2][col-1] == 2) // site on top is full
//                    {
//                        connect.union(connectLoc, connectLoc-n);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                    else if(grid[row-1][col]) // site on right is full
//                    {
//                        connect.union(connectLoc,connectLoc+1);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                }
//                
//                else if(col == 1) // leftmost column of grid
//                {
//                    if(grid[row-2][col-1] == 2) // top site is full
//                    {
//                        connect.union(connectLoc,connectLoc-n);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                    else if(grid[row-1][col] == 2) // site on right is full
//                    {
//                        connect.union(connectLoc,connectLoc+1);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                    else if(grid[row][col-1] == 2) // bottom site is full
//                    {
//                        connect.union(connectLoc, connectLoc+n);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                }
//                
//                else if(col == 5) // rightmost column of grid
//                {
//                    if(grid[row-2][col-1] == 2) // top site is full
//                    {
//                        connect.union(connectLoc, connectLoc-n);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                    else if(grid[row-1][col-2] == 2) // left site is full
//                    {
//                        connect.union(connectLoc,connectLoc-1);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                    else if(grid[row][col-1] == 2) // bottom site is full
//                    {
//                        connect.union(connectLoc,connectLoc+n);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                }
//                
//                else // if its not an edge row or column it must be in the middle somewhere
//                {
//                    if(grid[row-2][col-1] == 2) // top site is full
//                    {
//                        connect.union(connectLoc,connectLoc-n);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                    else if(grid[row-1][col] == 2) // site on right is full
//                    {
//                        connect.union(connectLoc, connectLoc+1);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                    else if(grid[row][col-1] == 2) // site on the bottom is full
//                    {
//                        connect.union(connectLoc,connectLoc+n);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                    else if(grid[row-1][col-2] == 2) // site on the left is full
//                    {
//                        connect.union(connectLoc, connectLoc-1);
//                        grid[row-1][col-1] = 2; // site is now full
//                    }
//                }
//            }
//            
//            else // if the site is a corner
//            {
//                
//            }
//            
//        }
    }
    
    public boolean isOpen(int row, int col)
    {
        if(grid[row-1][col-1] >= 1)
        {
            return true;
        }
        return false;
    }
    
    public boolean isFull(int row, int col)
    {
        int connectLoc = 0;
        if(col%n==0)
        {
            connectLoc = col+(row-1)*5;
        }
        else
        {
            int c = col%5;
            connectLoc = c + (row-1)*5;
        }
        
        if(grid[row-1][col-1] == 2 || connect.connected(0,connectLoc))
        {
            grid[row-1][col-1] = 2;
            return true;
        }
        return false;
    }
    
    public boolean percolates()
    {
        
        if(connect.connected(0,connectLength-1))
        {
            return true;
        }
        return false;
    }
    
    public static void main(String[] args)
    {
        Percolation test = new Percolation(5);
//        test.open(1,5);
//        test.open(2,5);
//        test.open(3,5);
//        test.open(4,5);
//        test.open(5,5);
        
        test.open(2,5);
        test.open(3,5);
        test.open(1,5);
        test.open(5,5);
        test.open(4,5);
        System.out.println(test.connect.connected(6,0));
//        System.out.println(test.grid[0][0]);
//        System.out.println(test.grid[1][0]);
//        System.out.println(test.connect.connected(1,2));
//        System.out.println(test.isOpen(1,1));
//        System.out.println(test.isFull(1,1));
        System.out.println(test.percolates());
        System.out.println(test.isFull(4,1));
    }
}