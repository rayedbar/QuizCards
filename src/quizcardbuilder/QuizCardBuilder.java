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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Rayed Bin Wahed
 */
public class QuizCardBuilder {

    private final JFrame frame;
    private final JTextArea question;
    private final JTextArea answer;

    private final ArrayList<QuizCard> cardList;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        QuizCardBuilder builder = new QuizCardBuilder();
        builder.go();
    }

    public QuizCardBuilder() {
        cardList = new ArrayList<>();
        frame = new JFrame("Quiz Card Builder");
        question = new JTextArea(6, 20);
        answer = new JTextArea(6, 20);
    }

    private void go() {
        JPanel mainPanel = new JPanel();

        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        question.setFont(bigFont);
        question.setLineWrap(true);
        question.setWrapStyleWord(true);

        JScrollPane qScrollPane = new JScrollPane(question);
        qScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        qScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        answer.setFont(bigFont);
        answer.setLineWrap(true);
        answer.setWrapStyleWord(true);

        JScrollPane aScrollPane = new JScrollPane(answer);
        aScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        aScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JLabel qLabel = new JLabel("Question");
        JLabel aLabel = new JLabel("Answer");

        JButton nextButton = new JButton("Next Card");
        nextButton.addActionListener(new NextCardListener());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new NewMenuListener());
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new SaveMenuListener());

        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        mainPanel.add(qLabel);
        mainPanel.add(qScrollPane);
        mainPanel.add(aLabel);
        mainPanel.add(aScrollPane);
        mainPanel.add(nextButton);

        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setVisible(true);
    }

    private class NextCardListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String q = question.getText();
            String a = answer.getText();

            QuizCard card = new QuizCard(q, a);
            cardList.add(card);

            clearCard();
        }
    }

    private class NewMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            cardList.clear();
            clearCard();
        }
    }

    private class SaveMenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            QuizCard card = new QuizCard(question.getText(), answer.getText());
            cardList.add(card);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showSaveDialog(frame);
            saveFile(fileChooser.getSelectedFile());
        }

        private void saveFile(File selectedFile) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                for (QuizCard card : cardList) {
                    writer.write(card.getQuestion() + "/");
                    writer.write(card.getAnswer() + "\r\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(QuizCardBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void clearCard() {
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }
}
