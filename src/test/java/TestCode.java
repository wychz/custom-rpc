import org.junit.Test;

public class TestCode {


    @Test
    public void test() {
        char[][] grid = {{'1','1'}};
        System.out.println(numIslands(grid));
    }

    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        UF uf = new UF(grid);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    grid[i][j] = '2';
                    if (i - 1 >= 0 && grid[i - 1][j] == '1') {
                        uf.union(i * cols + j, (i - 1) * cols + j);
                    }
                    if (i + 1 < rows && grid[i + 1][j] == '1') {
                        uf.union(i * cols + j, (i + 1) * cols + j);
                    }
                    if (j - 1 >= 0 && grid[i][j - 1] == '1') {
                        uf.union(i * cols + j, i * cols + j - 1);
                    }
                    if (j + 1 < cols && grid[i][j + 1] == '1') {
                        uf.union(i * cols + j, i + cols + j + 1);
                    }
                }
            }
        }
        return uf.getCount();
    }

    class UF {
        private int count;
        private int[] parent;
        private int[] size;

        public UF(char[][] grid) {
            count = 0;
            int rows = grid.length;
            int cols = grid[0].length;
            parent = new int[rows * cols];
            size = new int[rows * cols];
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    if(grid[i][j] == '1') {
                        parent[i * cols + j] = i * cols + j;
                        count++;
                    }
                    size[i * cols + j] = 0;
                }
            }
        }

        public int find(int x) {
            int root = x;
            while(root != parent[root]) {
                root = parent[root];
            }
            while(x != root) {
                int temp = parent[x];
                parent[x] = root;
                x = temp;
            }
            return root;
        }

        public void union(int x, int y) {
            int rootP = find(x);
            int rootQ = find(y);
            if(rootP != rootQ) {
                if(size[rootP] < size[rootQ]) {
                    parent[rootP] = rootQ;
                    size[rootQ] += size[rootP];
                } else {
                    parent[rootQ] = rootP;
                    size[rootP] += size[rootQ];
                }
                count--;
            } else {
                return;
            }
        }

        public boolean connected(int x, int y) {
            return find(x) == find(y);
        }

        public int getCount() {
            return count;
        }
    }
}
