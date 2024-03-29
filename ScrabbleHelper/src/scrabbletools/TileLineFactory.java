/*
 * TileLineFactory.java
 *
 * Created on September 13, 2007, 5:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package scrabbletools;

import java.util.ArrayList;
import java.util.Arrays;
import scrabbletools.BoardAnagramUtils.Square;

/**
 *
 * @author Nick
 */
public class TileLineFactory {
    /** Creates a new instance of TileLineFactory */

    public TileLineFactory() {
    }

    public ArrayList<TileLine> generateTileLines(Square[][] processedBoard, char[] rack1) {
        ArrayList<TileLine> result = new ArrayList<TileLine>(100);
        char[] sortedRack = rack1.clone();

        Arrays.sort(sortedRack);

        // scan across
        for (int row = 0; row < processedBoard.length; row++) {
            boolean rowConnected = false;
            check:
            for (int col = 0; col < processedBoard[0].length; col++) {  // scan row for any worthwhile connected spots
                if (processedBoard[row][col].isConnectedToRestOfBoard()) {
                    rowConnected = true;
                    break check;
                }
            }
            if (!rowConnected) {
                continue;
            }
            for (int startCol = 0; startCol < (processedBoard[0].length - 1); startCol++) {
                if (processedBoard[row][startCol].isOccupied()) {
                    continue;
                }
                int playedTiles = 1;
                boolean isConnected = false;
                endColSearch:
                for (int endCol = startCol; endCol < processedBoard[row].length &&
                        playedTiles <= 7; endCol++) {
                    Square s = processedBoard[row][endCol];
                    if (s.isOccupied()) {
                        isConnected = true;
                        continue endColSearch;
                    }
                    playedTiles++;
                    if (s.hasAdjascent()) {
                        isConnected = true;
                        if (!s.horizontalAdjascent && s.verticalPossibilities.length == 0) {
                            break endColSearch;
                        } else if (Arrays.binarySearch(sortedRack, LetterScores.UNUSED_BLANK) >= 0) {
                        } else if (!s.horizontalAdjascent) {
                            boolean valid = false;
                            for (int poss = 0; poss < s.verticalPossibilities.length; poss++) {
                                if (Arrays.binarySearch(sortedRack, s.verticalPossibilities[poss]) >= 0) {
                                    valid = true;
                                    continue;
                                }
                            }
                            if (!valid) {
                                break endColSearch;
                            }
                        }
                    }
                    if (isConnected) {
                        result.add(new TileLine(row, startCol, endCol - startCol + 1, true));
                    }
                }
            }
        }


        //scan vertically
        for (int col = 0; col < processedBoard[0].length; col++) {
            boolean colConnected = false;
            check:
            for (int row = 0; row < processedBoard.length; row++) {  // scan row for any worthwhile connected spots
                if (processedBoard[row][col].isConnectedToRestOfBoard()) {
                    colConnected = true;
                    break check;
                }
            }

            if (!colConnected) { //if it isn't worthwhile looking, don't
                continue;
            }

            for (int startRow = 0; startRow < (processedBoard.length - 1); startRow++) {
                if (processedBoard[startRow][col].isOccupied()) {
                    continue;
                }
                int playedTiles = 1;
                boolean isConnected = false;
                endRowSearch:
                for (int endRow = startRow; endRow < processedBoard.length && playedTiles <= 7; endRow++) {
                    Square s = processedBoard[endRow][col];
                    if (s.isOccupied()) {
                        isConnected = true;
                        continue;
                    }
                    playedTiles++;
                    if (s.hasAdjascent()) {
                        isConnected = true;
                        if (!s.verticalAdjascent && s.horizontalPossibilities.length == 0) {
                            break endRowSearch;
                        } else if (Arrays.binarySearch(sortedRack, LetterScores.UNUSED_BLANK) >= 0) {
                        } else if (!s.verticalAdjascent) {
                            boolean valid = false;
                            for (int poss = 0; poss < s.horizontalPossibilities.length; poss++) {
                                if (Arrays.binarySearch(sortedRack, s.horizontalPossibilities[poss]) >= 0) {
                                    valid = true;
                                    continue;
                                }
                            }
                            if (!valid) {
                                break endRowSearch;
                            }
                        }
                    }
                    if (isConnected) {
                        result.add(new TileLine(startRow, col, endRow - startRow + 1, false));
                    }
                }
            }
        }

        return result;
    }
}
