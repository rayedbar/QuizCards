/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quizcardbuilder;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author moham
 */
public class QuizCardPlayer {

    private JTextArea display;
    private ArrayList<QuizCard> cardList;
    private QuizCard currentCard;
    private int currentCardIndex;
    private JFrame frame;
    private JButton loadButton;
    private boolean isShowAnswer;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new QuizCardPlayer().go();
    }
    
    private void go() {
        frame = new JFrame("Quiz Card Player");
        JPanel panel = new JPanel();

        Font bigFont = new Font("Serif", Font.PLAIN, 24);

        display = new JTextArea(10, 20);
        display.setLineWrap(true);
        display.setWrapStyleWord(true);
        display.setFont(bigFont);
        display.setEditable(false);

        JScrollPane qScroller = new JScrollPane(display);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        loadButton = new JButton("Show Question");
        loadButton.addActionListener(new NextCardListener());

        JMenuBar jMenuBar = new JMenuBar();
        JMenu jFileMenu = new JMenu("File");
        JMenuItem jLoadMenuItem = new JMenuItem("Load Card Set");
        jLoadMenuItem.addActionListener(new LoadCardListener());

        jFileMenu.add(jLoadMenuItem);
        jMenuBar.add(jFileMenu);

        panel.add(qScroller);
        panel.add(loadButton);

        frame.setJMenuBar(jMenuBar);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 500);
        frame.setVisible(true);
    }

    private class LoadCardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileOpener = new JFileChooser();
            fileOpener.showOpenDialog(frame);
            loadFile(fileOpener.getSelectedFile());
        }

        private void loadFile(File selectedFile) {
            String line = null;
            cardList = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                while ((line = reader.readLine()) != null) {
                    makeCard(line);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(QuizCardPlayer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(QuizCardPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
            showNextCard();
        }

        private void makeCard(String line) {
            String [] tokens = line.split("/");
            QuizCard card = new QuizCard(tokens[0], tokens[1]);
            cardList.add(card);
            System.out.println("Created a card");
        }
    }
    
    private void showNextCard() {
        currentCard = cardList.get(currentCardIndex);
        currentCardIndex++;
        display.setText(currentCard.getQuestion());
        loadButton.setText("Show Answer");
        isShowAnswer = true;
    }

    private class NextCardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isShowAnswer){
                display.setText(currentCard.getAnswer());
                loadButton.setText("Next Card");
                isShowAnswer = false;
            } else {
                if (currentCardIndex < cardList.size()){
                    showNextCard();
                } else {
                    display.setText("No more cards to display");
                    loadButton.setEnabled(false);
                }
            }
        }
    }

}
