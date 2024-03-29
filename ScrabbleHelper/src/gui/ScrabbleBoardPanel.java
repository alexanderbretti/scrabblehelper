/*
 * ScrabbleBoardPanel.java
 *
 * Created on November 10, 2007, 2:02 PM
 */
package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import persistence.SavedBoard;
import scrabbletools.BoardLayout;
import scrabbletools.LetterScores;
import scrabbletools.TileLine;
import scrabbletools.WordPlacement;

/**
 *
 * @author  Nick
 */
public class ScrabbleBoardPanel extends javax.swing.JPanel 
        implements ListSelectionListener, ListDataListener {

    public static final int ROWS = 15;
    public static final int COLS = 15;
    VisibleTile[][] tiles;
    private boolean moveAcross = true;
    
    public List<ScrabbleBoardListener> scrabbleBoardListeners = new ArrayList<ScrabbleBoardListener>();

    public boolean isMoveAcross() {
        return moveAcross;
    }

    public void setMoveAcross(boolean moveAcross) {
        this.moveAcross = moveAcross;
    }

    public enum Direction {

        LEFT, RIGHT, UP, DOWN
    }

    /** Creates new form ScrabbleBoardPanel */
    public ScrabbleBoardPanel() {
        initComponents();
        GridLayout gl = new GridLayout();
        gl.setColumns(ROWS);
        gl.setRows(COLS);
        gl.setVgap(0);
        gl.setHgap(0);
        setLayout(gl);
        initializeTiles();
    }
    
    public void putWordPlacement(WordPlacement wp, boolean temporary) {
        clearTemporaryWordPlacement();
        TileLine tl = wp.getLine();
        int startRow = tl.startRow;
        int startCol = tl.startCol;
        
        int currentRow = startRow;
        int currentCol = startCol;
        
        for (int i = 0; i < tl.length; i++) {
            if (tl.isAcross) {
                currentCol = startCol + i;
            } else {
                currentRow = startRow + i;
            }
            
            VisibleTile vt = tiles[currentRow][currentCol];
            if (!Character.isLetter(wp.getOccupied()[i])) {
                vt.setTemporaryDisplay(temporary);
                vt.setLetter(wp.getPlacedLetters()[i]);
            }
        }
        
        if (!temporary) {
            fireScrabbleBoardChange();
        }
    }
    
    public void clearTemporaryWordPlacement() {
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                VisibleTile vt = tiles[row][col];
                if (vt.isTemporaryDisplay()) {
                    vt.setLetter(LetterScores.EMPTY_SQUARE);
                    vt.setTemporaryDisplay(false);
                }
            }
        }
    }
    
    public void letterEditStarted(VisibleTile vt) {
        clearTemporaryWordPlacement();
        fireScrabbleBoardSelection(vt);
    }
    
    public void letterEditStopped(VisibleTile vt) {
        
    }

    public void paint(Graphics g) {
        super.paint(g);
        int rowMult = getHeight() / ROWS;
        int colMult = getWidth() / COLS;
        g.setColor(Color.BLACK);
        for (int row = 0; row < (ROWS + 1); row++) {
            int currentY = row * rowMult;
            if (row == ROWS) {
                currentY--;
            }
            g.drawLine(0, currentY, getWidth(), currentY);
        }
        for (int col = 0; col <= COLS; col++) {
            int currentX = col * colMult;
            if (currentX == getWidth()) {
                currentX--;
            }
            g.drawLine(currentX, 0, currentX, getHeight());
        }
    }
    

    public void setBoard(SavedBoard sb) {
        char[][] boardLetters = sb.getBoard();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                tiles[row][col].setLetter(boardLetters[row][col]);
            }
        }
        repaint();
    }

    public void initializeTiles() {
        VisibleTile[][] newTiles = new VisibleTile[((GridLayout) getLayout()).getRows()][];
        for (int row = 0; row < ((GridLayout) getLayout()).getRows(); row++) {
            newTiles[row] = new VisibleTile[((GridLayout) getLayout()).getColumns()];
            for (int col = 0; col < ((GridLayout) getLayout()).getColumns(); col++) {
                newTiles[row][col] = new VisibleTile(this,
                        BoardLayout.charBoardValues[row][col],
                        row, col);
                add(newTiles[row][col]);
                newTiles[row][col].letter = LetterScores.EMPTY_SQUARE;
            }
        }
        this.tiles = newTiles;
    }

    public void letterDeleted(VisibleTile tile) {
        fireScrabbleBoardChange();
        if (isMoveAcross()) {
            moveFocus(tile, Direction.LEFT);
        } else {
            moveFocus(tile, Direction.UP);
        }
    }
    
    public void moveBackward(VisibleTile tile) {
        if (isMoveAcross()) {
            moveFocus(tile, Direction.LEFT);
        } else {
            moveFocus(tile, Direction.UP);
        }
    }
    
    public void moveForward(VisibleTile tile) {
        if (isMoveAcross()) {
            moveFocus(tile, Direction.RIGHT);
        } else {
            moveFocus(tile, Direction.DOWN);
        }
    }

    public void letterEnterred(VisibleTile tile) {
        fireScrabbleBoardChange();
        if (isMoveAcross()) {
            moveFocus(tile, Direction.RIGHT);
        } else {
            moveFocus(tile, Direction.DOWN);
        }
    }

    public void moveFocus(VisibleTile tile, Direction d) {
        int row = tile.row;
        int col = tile.col;

        if (d == Direction.DOWN) {
            if (!moveAcross) {
                if (row < (ROWS - 1)) {
                    row++;
                } else {
                    tile.stopEditing();
                    requestFocus();
                }
            } else {
                setMoveAcross(false);
                tile.repaint();
                return;
            }
        } else if (d == Direction.UP) {
            if (!moveAcross) {
                if (row > 0) {
                    row--;
                } else {
                    tile.stopEditing();
                    requestFocus();
                }
            } else {
                setMoveAcross(false);
                tile.repaint();
                return;
            }
        } else if (d == Direction.LEFT) {
            if (moveAcross) {
                if (col > 0) {
                    col--;
                } else {
                    tile.stopEditing();
                    requestFocus();
                }
            } else {
                setMoveAcross(true);
                tile.repaint();
                return;
            }
        } else if (d == Direction.RIGHT) {
            if (moveAcross) {
                if (col < (COLS - 1)) {
                    col++;
                } else {
                    tile.stopEditing();
                    requestFocus();
                }
            } else {
                setMoveAcross(true);
                tile.repaint();
                return;
            }
        }
        tile.stopEditing();
        tiles[row][col].edit();
    }
    
    public int getEdgeTileType(VisibleTile vt) {
        Point p = vt.getLocationOnBoard();
        if (p.x == COLS - 1) {
            return 1;
        } else if (p.y == ROWS - 1) {
            return -1;
        }
        return 0;
    }
    
    public void toggleMoveDirection(VisibleTile vt) {
        setMoveAcross(!isMoveAcross());
        vt.repaint();
    }

    public char[][] getCharArray() {
        char[][] result = new char[tiles.length][tiles[0].length];
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[0].length; col++) {
                VisibleTile vt = tiles[row][col];
                if (!vt.isTemporaryDisplay()) {
                    result[row][col] = vt.getLetter();
                } else {
                    result[row][col] = LetterScores.EMPTY_SQUARE;
                }
            }
        }
        return result;
    }
    
    public VisibleTile getFocusedVisibleTile() {
        for (VisibleTile[] vtArray : tiles) {
            for (VisibleTile vt : vtArray) {
                if (vt.isFocusOwner()) {
                    return vt;
                }
            }
        }
        return null;
    }
    
    public void addScrabbleBoardListener(ScrabbleBoardListener sbl) {
        scrabbleBoardListeners.add(sbl);
    }
    
    public void clearScrabbleBoardListeners() {
        scrabbleBoardListeners.clear();
    }
    
    public void fireScrabbleBoardChange() {
        for (ScrabbleBoardListener sbl : scrabbleBoardListeners) {
            sbl.boardChanged();
        }
    }
    
   public void fireScrabbleBoardSelection(VisibleTile vt) {
        for (ScrabbleBoardListener sbl : scrabbleBoardListeners) {
            sbl.tileSelected(vt);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(450, 450));
        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() instanceof WordPlacementList) {
            Object o  = ((WordPlacementList)e.getSource()).getSelectedValue();
            if (o instanceof WordPlacement) {
                putWordPlacement((WordPlacement)o, true);
            } else {
                clearTemporaryWordPlacement();
            }
        }
    }
    
    public MouseListener getMouseListenerForWordPlacementList() {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getSource() instanceof WordPlacementList) {
                        Object o = ((WordPlacementList)e.getSource()).getSelectedValue();
                        if (o instanceof WordPlacement) {
                            putWordPlacement((WordPlacement)o, false);
                        }
                    }
                }
            }
        };
    }

    public void intervalAdded(ListDataEvent e) {
    }

    public void intervalRemoved(ListDataEvent e) {
    }

    public void contentsChanged(ListDataEvent e) {
        clearTemporaryWordPlacement();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
