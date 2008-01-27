/*
 * ScrabbleWindow.java
 *
 * Created on November 10, 2007, 2:02 PM
 */
package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import persistence.PersistenceWindow;
import persistence.SavedBoard;
import scrabblehelper.StaticFields;
import scrabbletools.Board;
import scrabbletools.BoardAnagramUtils;
import scrabbletools.LetterScores;
import scrabbletools.WordPlacement;

/**
 *
 * @author  Nick
 */
public class ScrabbleWindow extends javax.swing.JFrame {

    
    PersistenceWindow persistenceWindow = new PersistenceWindow(this);

    /** Creates new form ScrabbleWindow */
    public ScrabbleWindow() {
        super("Scrabble Solver");
        initComponents();

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getWidth()) / 2;
        int y = (d.height - getHeight()) / 2;
        setLocation(x, y);

        setResizable(false);
        rackLetterField.setDocument(new PlainDocument() {

            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                String upCase = str.toUpperCase();
                if (this.getLength() >= 7) {
                    return;
                }
                if (Character.isLetter(upCase.charAt(0)) || str.charAt(0) == LetterScores.UNUSED_BLANK) {
                    super.insertString(offs, upCase, a);
                }
            }
        });

        wordPlacementList1.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                Object o = wordPlacementList1.getSelectedValue();
                if (o instanceof WordPlacement) {
                    WordPlacement wp = (WordPlacement) o;
                    scrabbleBoard.putWordPlacement(wp, true);
                }
            }
        });
        
        wordPlacementList1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int index = wordPlacementList1.locationToIndex(e.getPoint());
                    WordPlacement wp = wordPlacementList1.getModel().getElementAt(index);
                    scrabbleBoard.putWordPlacement(wp, false);
                    wordPlacementList1.getModel().clear();
                }
            }
        });
        
        scrabbleBoard.addScrabbleBoardListener(new ScrabbleBoardListener() {

            public void boardChanged() {
                wordPlacementList1.getModel().clear();
            }

            public void tileSelected(VisibleTile vt) {
                wordPlacementList1.clearSelection();
            }
            
            
        });
    }

    public SavedBoard getBoard() {
        SavedBoard result = new SavedBoard(scrabbleBoard.getCharArray());
        result.setRack(rackLetterField.getText());
        return result;
    }

    public void applyBoard(SavedBoard sb) {
        scrabbleBoard.setBoard(sb);
        rackLetterField.setText(sb.getRack());
    }

    public void solve() {
        wordPlacementList1.clearSelection();
        scrabbleBoard.clearTemporaryWordPlacement();
        
        BoardAnagramUtils utils = new BoardAnagramUtils(new Board());
        
        utils.setDictionary(StaticFields.getDictionary());
        utils.getBoard().setLetters(scrabbleBoard.getCharArray());
        utils.setLetters(rackLetterField.getText().toUpperCase().toCharArray());
        
        long startTime = System.currentTimeMillis();
        List<WordPlacement> words = utils.findAllBoardPossibilities();
        long time = System.currentTimeMillis() - startTime;

        resultDisplay.append("Total number of unique word placements:  " + words.size() + "\n");
        resultDisplay.append("Time to process (in milliseconds):  " + time);

        numberOfWordsLabel.setText(Integer.toString(words.size()));
        timeToProcessLabel.setText(Long.toString(time));

        displayWords(words);
    }

    public void displayWords(List<WordPlacement> words) {
        Collections.sort(words);
        resultDisplay.setText("");

        wordPlacementList1.getModel().clear();

        for (WordPlacement wp : words) {
            resultDisplay.append(wp.toString() + "\n\n");
        }

        wordPlacementList1.getModel().addAll(words);

        filter();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        resultDisplay = new javax.swing.JTextArea();
        scrabbleBoard = new gui.ScrabbleBoardPanel();
        rackLetterField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        anagramButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        loadSaveButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        wordPlacementList1 = new gui.WordPlacementList();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        timeToProcessLabel = new javax.swing.JLabel();
        numberOfWordsLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        maxResultsField = new javax.swing.JTextField();
        regExField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        resultDisplay.setColumns(20);
        resultDisplay.setEditable(false);
        resultDisplay.setLineWrap(true);
        resultDisplay.setRows(5);
        resultDisplay.setText("Instructions:\n\nFill in the board, enter your letters in the rack above, then press Solve Board!\n\n\n\nExtra Instructions:\n\nHold down shift while filling in the board to indicate played blanks.  They will show up as lowercase letters.\n\nUse the arrow keys to toggle between typing horizontally and vertically.\n\n\n\n\nEmail suggestions or comments to nwilkie@bigfoot.com\n");
        resultDisplay.setWrapStyleWord(true);
        jScrollPane1.setViewportView(resultDisplay);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        rackLetterField.setFont(new java.awt.Font("Tahoma", 0, 42));
        rackLetterField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        rackLetterField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rackLetterFieldActionPerformed(evt);
            }
        });
        rackLetterField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rackLetterFieldKeyReleased(evt);
            }
        });

        jButton1.setText("Solve Board!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        anagramButton.setText("Anagram");
        anagramButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anagramButtonActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("enter as zero (0) in rack.");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("To play blank tile,");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("This will significantly");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("extend calculation time.");

        loadSaveButton.setText("Load/Save");
        loadSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSaveButtonActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(wordPlacementList1);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setText("Time to process:");

        jLabel5.setText("Number of words:");

        timeToProcessLabel.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numberOfWordsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(timeToProcessLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeToProcessLabel)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(numberOfWordsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filters"));

        jLabel6.setText("Max Results:");

        maxResultsField.setText("30");
        maxResultsField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxResultsFieldActionPerformed(evt);
            }
        });

        regExField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regExFieldActionPerformed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Regular Expression:");

        jButton2.setText("Filter");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                        .addComponent(maxResultsField, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(regExField, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(maxResultsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(regExField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrabbleBoard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(anagramButton, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(rackLetterField, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(loadSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel1)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4))
                            .addComponent(rackLetterField, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(anagramButton)
                            .addComponent(jButton1)
                            .addComponent(loadSaveButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(scrabbleBoard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        solve();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void anagramButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anagramButtonActionPerformed
        new AnagramWindow().setVisible(true);        
}//GEN-LAST:event_anagramButtonActionPerformed

    private void rackLetterFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rackLetterFieldActionPerformed
        solve();
    }//GEN-LAST:event_rackLetterFieldActionPerformed

    private void rackLetterFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rackLetterFieldKeyReleased
           
    }//GEN-LAST:event_rackLetterFieldKeyReleased

    private void loadSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadSaveButtonActionPerformed
        persistenceWindow.setVisible(true);
        persistenceWindow.requestFocus();
    }//GEN-LAST:event_loadSaveButtonActionPerformed

    private void maxResultsFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxResultsFieldActionPerformed
        try {
            wordPlacementList1.getModel().setMaxNumber(Integer.parseInt(maxResultsField.getText()));
        } catch (NumberFormatException numberFormatException) {
        }

}//GEN-LAST:event_maxResultsFieldActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        filter();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void regExFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regExFieldActionPerformed
        filter();
    }//GEN-LAST:event_regExFieldActionPerformed

    public void filter() {
        try {
            wordPlacementList1.getModel().setMaxNumber(Integer.parseInt(maxResultsField.getText()));
        } catch (NumberFormatException numberFormatException) {
        }

        final String regEx = regExField.getText();
        
        wordPlacementList1.getModel().removeFilters();
        wordPlacementList1.getModel().addFilter(new WordPlacementFilter() {
            public boolean isValid(WordPlacement wp) {
                return Pattern.compile(regEx, Pattern.CASE_INSENSITIVE).matcher(
                        String.valueOf(wp.getWords().get(0).word)).find();
            }
        });
        
        wordPlacementList1.getModel().applyFilters();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anagramButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton loadSaveButton;
    private javax.swing.JTextField maxResultsField;
    private javax.swing.JLabel numberOfWordsLabel;
    private javax.swing.JTextField rackLetterField;
    private javax.swing.JTextField regExField;
    private javax.swing.JTextArea resultDisplay;
    private gui.ScrabbleBoardPanel scrabbleBoard;
    private javax.swing.JLabel timeToProcessLabel;
    private gui.WordPlacementList wordPlacementList1;
    // End of variables declaration//GEN-END:variables
}
