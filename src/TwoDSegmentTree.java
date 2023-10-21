/**
 * Implementation of a 2D segment tree data structure that helps efficiently answer
 * queries about specific values within a sub-region (submatrix) of a 2D grid.
 * Specifically, it can answer whether there's a clue of a certain number (below 64)
 * within a queried rectangular section of the grid.
 */
public class TwoDSegmentTree {
    // The dimensions of the grid.
    int n, m;

    // The 2D segment tree representation using a 2D array.
    long t[][];

    /**
     * Constructs an empty 2D segment tree for given sizes.
     * 
     * @param n The number of rows in the grid.
     * @param m The number of columns in the grid.
     */
    TwoDSegmentTree(int n, int m) {
        this.n = n;
        this.m = m;
        t = new long[n << 1][m << 1];
        for (int i = 0; i < n << 1; i++) {
            for (int j = 0; j < m << 1; j++) {
                t[i][j] = 0;
            }
        }
    }

    /**
     * Updates the value at a specific position in the grid.
     *
     * @param x     Row index of the position.
     * @param y     Column index of the position.
     * @param value The value to insert/remove in the grid.
     */
    public void change(int x, int y, int value) {
        // get the indices of the 1x1 submatrix
        x += n;
        y += m;

        // Bitwise XOR to update the value at the specified position
        t[x][y] ^= (1 << value);

        // Propagate the changes upwards to update the segment tree
        for (int tx = x; tx > 0; tx >>= 1) {
            for (int ty = y; ty > 0; ty >>= 1) {
                if (tx > 1)
                    t[tx >> 1][ty] = t[tx][ty] | t[tx ^ 1][ty];
                if (ty > 1)
                    t[tx][ty >> 1] = t[tx][ty] | t[tx][ty ^ 1];
            }
        }
    }

    /**
     * Performs a query on the segment tree to check for the presence of a given value
     * within a specific submatrix defined by its top-left and bottom-right corners.
     *
     * @param x1    Row index of the top-left corner.
     * @param y1    Column index of the top-left corner.
     * @param x2    Row index of the bottom-right corner.
     * @param y2    Column index of the bottom-right corner.
     * @param value The value to check for within the submatrix.
     * @return True if the value is found within the submatrix, False otherwise.
     */
    public Boolean query(int x1, int y1, int x2, int y2, int value) {
        x1 += n;
        x2 += n;
        y1 += m;
        y2 += m;

        Boolean flag = false;
        long set = 1 << value; // Bitmask for the query value

        for (int lx = x1, rx = x2; lx <= rx; lx = (lx + 1) >> 1, rx = (rx - 1) >> 1) {
            for (int ly = y1, ry = y2; ly <= ry; ly = (ly + 1) >> 1, ry = (ry - 1) >> 1) {
                // Check if current segments contain the 'value'
                if ((lx & 1) != 0 && (ly & 1) != 0)
                    flag = flag || ((t[lx][ly] & set) != 0);
                if ((lx & 1) != 0 && (ry & 1) == 0)
                    flag = flag || ((t[lx][ry] & set) != 0);
                if ((rx & 1) == 0 && (ly & 1) != 0)
                    flag = flag || ((t[rx][ly] & set) != 0);
                if ((rx & 1) == 0 && (ry & 1) == 0)
                    flag = flag || ((t[rx][ry] & set) != 0);
                if (flag)
                    return true; // Early exit if 'value' is found
            }
        }
        return false;
    }
}
