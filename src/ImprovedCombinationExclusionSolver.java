import java.io.IOException;
import java.util.Vector;


/**
 * Implements an improved combination exclusion solver,
 * quasi identical to the combination exclusion solver, with a segment tree
 * to improve path finding.
 * This solver extends the base functionality provided by BaseSolver.
 */
public class ImprovedCombinationExclusionSolver extends BaseSolver {
    private int cluePosition, nbChanged = 0;
    private Cell[][] chains;
    private Boolean[][] combination;
    private Boolean[] validClue;
    private Vector<Cell> numberedCells;
    private TwoDSegmentTree st;

    ImprovedCombinationExclusionSolver(String fileName) throws IOException {
        super(fileName);
        st = new TwoDSegmentTree(n, m);
        numberedCells = new Vector<Cell>();

        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (configuration[i][j].value > 0) {
                    numberedCells.add(configuration[i][j]);
                    st.change(i, j, configuration[i][j].value - 1);
                }

        chains = new Cell[numberedCells.size()][];
        combination = new Boolean[numberedCells.size()][];
        validClue = new Boolean[numberedCells.size()];

        for (int k = 0; k < numberedCells.size(); k++) {
            validClue[k] = false;
            Cell currCell = numberedCells.get(k);
            chains[k] = new Cell[currCell.value];
            combination[k] = new Boolean[currCell.value];
            chains[k][0] = currCell;
            currCell.position = k;
        }
    }

    private Boolean generateFirst(Cell currCell, int idx) {

        Cell origineCell = chains[cluePosition][0];

        if (idx == origineCell.value - 1) {
            if (currCell.state == Cell.State.Colored)
                return true;

            if (currCell.value == origineCell.value) {
                currCell.state = Cell.State.MaybeColored;
                gui.changeRed(currCell.i, currCell.j);

                chains[cluePosition][idx] = currCell;
                combination[cluePosition][idx] = true;
                currCell.state = Cell.State.NotColored;
                gui.changeGrey(currCell.i, currCell.j);
                return true;
            } else
                return false;
        }
        if (chains[cluePosition][idx + 1] != null && chains[cluePosition][idx + 1].state == Cell.State.Colored
                && chains[cluePosition][idx + 1].b1 == origineCell.position) {

            if (Math.abs(chains[cluePosition][idx + 1].i - currCell.i)
                    + Math.abs(chains[cluePosition][idx + 1].j - currCell.j) == 1) {
                chains[cluePosition][idx] = currCell;
                combination[cluePosition][idx] = true;
                return generateFirst(chains[cluePosition][idx + 1], idx + 1);
            } else
                return false;
        }
        if (currCell.state != Cell.State.Colored) {
            currCell.state = Cell.State.MaybeColored;
            gui.changeRed(currCell.i, currCell.j);
        }
        
        for (int k = 0; k < 4; k++) {
            int ni = currCell.i + mvt[k][0], nj = currCell.j + mvt[k][1];
            if (ni > -1 && ni < n && nj > -1 && nj < m && configuration[ni][nj].state == Cell.State.NotColored
                    && (idx + 1 == origineCell.value - 1 || configuration[ni][nj].value == 0)
                    && st.query(Math.max(0, ni - origineCell.value + idx + 1), 
                            Math.max(0, nj - origineCell.value + idx + 1),
                            Math.min(n - 1, ni + origineCell.value - idx - 1),
                            Math.min(m - 1, nj + origineCell.value - idx - 1), origineCell.value - 1)) {
                if (generateFirst(configuration[ni][nj], idx + 1)) {

                    if (currCell.state != Cell.State.Colored) {
                        currCell.state = Cell.State.NotColored;
                        gui.changeGrey(currCell.i, currCell.j);
                    }
                    chains[cluePosition][idx] = currCell;
                    combination[cluePosition][idx] = true;
                    return true;
                }
            }
        }
        if (currCell.state != Cell.State.Colored) {
            currCell.state = Cell.State.NotColored;
            gui.changeGrey(currCell.i, currCell.j);
        }
        return false;
    }

    private Boolean generateAll(Cell currCell, int idx) {
        Cell origineCell = chains[cluePosition][0];

        if (idx == origineCell.value - 1) {
            if (currCell.state == Cell.State.Colored)
                return true;

            if (currCell.value == origineCell.value) {
                currCell.state = Cell.State.MaybeColored;
                gui.changeRed(currCell.i, currCell.j);

                if (chains[cluePosition][idx] != currCell) {
                    combination[cluePosition][idx] = false;
                }
                currCell.state = Cell.State.NotColored;
                gui.changeGrey(currCell.i, currCell.j);
                return true;
            } else
                return false;
        }
        if (chains[cluePosition][idx + 1].state == Cell.State.Colored
                && chains[cluePosition][idx + 1].b1 == origineCell.position) {

            if (Math.abs(chains[cluePosition][idx + 1].i - currCell.i)
                    + Math.abs(chains[cluePosition][idx + 1].j - currCell.j) == 1) {
                if (generateAll(chains[cluePosition][idx + 1], idx + 1)) {
                    if (chains[cluePosition][idx] != currCell)
                        combination[cluePosition][idx] = false;
                    return true;
                } else
                    return false;
            } else
                return false;
        }
        Boolean flag = false;
        if (currCell.state != Cell.State.Colored) {
            currCell.state = Cell.State.MaybeColored;
            gui.changeRed(currCell.i, currCell.j);
        }

        for (int k = 0; k < 4; k++) {
            int ni = currCell.i + mvt[k][0], nj = currCell.j + mvt[k][1];
            if (ni > -1 && ni < n && nj > -1 && nj < m && configuration[ni][nj].state == Cell.State.NotColored
                    && (idx + 1 == origineCell.value - 1 || configuration[ni][nj].value == 0)
                    && st.query(Math.max(0, ni - origineCell.value + idx + 1),
                            Math.max(0, nj - origineCell.value + idx + 1),
                            Math.min(n - 1, ni + origineCell.value - idx - 1),
                            Math.min(m - 1, nj + origineCell.value - idx - 1), origineCell.value - 1)) {
                if (generateAll(configuration[ni][nj], idx + 1)) {
                    if (chains[cluePosition][idx] != currCell) {
                        combination[cluePosition][idx] = false;
                    }
                    flag = true;
                }
            }
        }
        
        if (currCell.state != Cell.State.Colored) {
            currCell.state = Cell.State.NotColored;
            gui.changeGrey(currCell.i, currCell.j);
        }
        return flag;
    }

    private void combineExclude() {
        if (chains[cluePosition][0].value == 1) {
            chains[cluePosition][0].state = Cell.State.Colored;
            validClue[cluePosition] = true;
            nbChanged++;
            gui.changeGreen(chains[cluePosition][0].i, chains[cluePosition][0].j);
            return;
        }
        st.change(chains[cluePosition][0].i, chains[cluePosition][0].j,
                chains[cluePosition][0].value - 1);
        generateFirst(chains[cluePosition][0], 0);
        generateAll(chains[cluePosition][0], 0);
        st.change(chains[cluePosition][0].i, chains[cluePosition][0].j,
                chains[cluePosition][0].value - 1); 

        int l = chains[cluePosition][0].value;

        if (combination[cluePosition][l - 1]) {
            Boolean flag = true; 
            for (int k = 0; k < l; k++) {
                if (combination[cluePosition][k] && chains[cluePosition][k].state != Cell.State.Colored) {
                    chains[cluePosition][k].state = Cell.State.Colored;
                    nbChanged++;
                    chains[cluePosition][k].b1 = chains[cluePosition][0].position;
                    gui.changeGreen(chains[cluePosition][k].i, chains[cluePosition][k].j);
                } else if (!combination[cluePosition][k])
                    flag = false;
            }
            validClue[chains[cluePosition][l - 1].position] = true;
            st.change(chains[cluePosition][0].i, chains[cluePosition][0].j,
                    chains[cluePosition][0].value - 1);
            
            if (flag) {
                validClue[cluePosition] = true;
                st.change(chains[cluePosition][l - 1].i, chains[cluePosition][l - 1].j,
                        chains[cluePosition][l - 1].value - 1);
            }
        }
    }

    private Boolean backtracking(Cell currCell, int idx) {

        if (validClue[cluePosition]) {
            if (++cluePosition == numberedCells.size()) {
                return true;
            }
            if (backtracking(chains[cluePosition][0], 0))
                return true;
            cluePosition--;
            return false;
        }
        Cell origineCell = chains[cluePosition][0];

        if (idx == origineCell.value - 1) {
            if (currCell.value == origineCell.value) {
                if (currCell.state != Cell.State.Colored) {
                    currCell.state = Cell.State.MaybeColored;
                    gui.changeRed(currCell.i, currCell.j);
                    validClue[currCell.position] = true;
                    st.change(currCell.i, currCell.j, currCell.value - 1);
                }
                if (++cluePosition == numberedCells.size() || backtracking(chains[cluePosition][0], 0)) {
                    if (currCell.state != Cell.State.Colored) {
                        currCell.state = Cell.State.Colored;
                        gui.changeGreen(currCell.i, currCell.j);
                    }
                    return true;
                } else {
                    if (currCell.state != Cell.State.Colored) {
                        currCell.state = Cell.State.NotColored;
                        gui.changeGrey(currCell.i, currCell.j);
                        validClue[currCell.position] = false;
                        st.change(currCell.i, currCell.j, currCell.value - 1);
                    }
                    cluePosition--;
                    return false;
                }
            } else
                return false;
        }

        if (chains[cluePosition][idx + 1].state == Cell.State.Colored
                && chains[cluePosition][idx + 1].b1 == origineCell.position) {
            if (Math.abs(chains[cluePosition][idx + 1].i - currCell.i)
                    + Math.abs(chains[cluePosition][idx + 1].j - currCell.j) == 1) {
                if (currCell.state != Cell.State.Colored) {
                    currCell.state = Cell.State.MaybeColored;
                    gui.changeRed(currCell.i, currCell.j);
                }
                if (backtracking(chains[cluePosition][idx + 1], idx + 1)) {
                    if (currCell.state != Cell.State.Colored) {
                        currCell.state = Cell.State.Colored;
                        gui.changeGreen(currCell.i, currCell.j);
                    }
                    return true;
                }
                if (currCell.state != Cell.State.Colored) {
                    currCell.state = Cell.State.NotColored;
                    gui.changeGrey(currCell.i, currCell.j);
                }
                return false;
            } else
                return false;
        }

        if (currCell.state != Cell.State.Colored) {
            currCell.state = Cell.State.MaybeColored;
            gui.changeRed(currCell.i, currCell.j);
        }

        for (int k = 0; k < 4; k++) {
            int ni = currCell.i + mvt[k][0], nj = currCell.j + mvt[k][1];
            if (ni > -1 && ni < n && nj > -1 && nj < m && configuration[ni][nj].state == Cell.State.NotColored
                    && (idx + 1 == origineCell.value - 1 || configuration[ni][nj].value == 0)
                    && st.query(Math.max(0, ni - origineCell.value + idx + 1),
                            Math.max(0, nj - origineCell.value + idx + 1),
                            Math.min(n - 1, ni + origineCell.value - idx - 1),
                            Math.min(m - 1, nj + origineCell.value - idx - 1), origineCell.value - 1)) {
                if (backtracking(configuration[ni][nj], idx + 1)) {
                    if (currCell.state != Cell.State.Colored) {
                        currCell.state = Cell.State.Colored;
                        gui.changeGreen(currCell.i, currCell.j);
                    }
                    return true;
                }
            }
        }

        if (currCell.state != Cell.State.Colored) {
            currCell.state = Cell.State.NotColored;
            gui.changeGrey(currCell.i, currCell.j);
        }
        return false;
    }

    @Override
    void solve() {
        if (numberedCells.size() == 0)
            return;
        int nbChangedOld;
        do {
            nbChangedOld = nbChanged;
            for (Cell curCell : numberedCells) {
                if (!validClue[curCell.position]) {
                    cluePosition = curCell.position;
                    combineExclude();
                }
            }
        } while (nbChangedOld != nbChanged);
        cluePosition = 0;

        backtracking(chains[cluePosition][0], 0);
    }
}
