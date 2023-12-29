import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PuzzleGame extends JFrame implements ActionListener {
    JButton b1, b2;
    JButton[][] pan = new JButton[5][5];
    int[][] panCount = new int[5][5];
    int brow, bcol;

    public PuzzleGame() {
        setTitle("내가 만든 퍼즐 게임");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 버튼 및 패널 초기화
        initButtonsAndPanel();
        // 난수 생성 및 화면 표시
        startGame();

        setSize(350, 400);
        setVisible(true);

        b1.addActionListener(this);
    }

    private void initButtonsAndPanel() {
        b1 = new JButton("시작");
        b2 = new JButton("종료");
        JPanel p = new JPanel();
        p.add(b1);
        p.add(b2);
        add("South", p);

        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(5, 5, 5, 5));
        add("Center", p2);

        for (int i = 0, k = 1; i < 5; i++) {
            for (int j = 0; j < 5; j++, k++) {
                pan[i][j] = new JButton(String.valueOf(k));
                pan[i][j].setFont(new Font("굴림체", Font.BOLD, 50));
                pan[i][j].addActionListener(this);
                p2.add(pan[i][j]);
            }
        }
    }

    private void startGame() {
        getRand();
        display();
    }

    public void getRand() {
        int su = 0;
        boolean bCheck;
        int[] com = new int[25];

        for (int i = 0; i < 25; i++) {
            bCheck = true;
            while (bCheck) {
                su = (int) (Math.random() * 25);
                bCheck = false;
                for (int j = 0; j < i; j++) {
                    if (su == com[j]) {
                        bCheck = true;
                        break;
                    }
                }
            }
            com[i] = su;
            panCount[i / 5][i % 5] = su;
            if (su == 24) {
                brow = i / 5;
                bcol = i % 5;
            }
        }
    }

    public void display() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == brow && j == bcol) {
                    pan[i][j].setText("");
                    pan[i][j].setEnabled(false);
                } else {
                    pan[i][j].setText(String.valueOf(panCount[i][j] + 1));
                    pan[i][j].setEnabled(true);
                }
            }
        }
    }

    public boolean isEnd() {
        int k = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (panCount[i][j] != k) {
                    return false;
                }
                k++;
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            startGame();
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (e.getSource() == pan[i][j]) {
                    panCount[brow][bcol] = panCount[i][j];
                    panCount[i][j] = 24;
                    brow = i;
                    bcol = j;
                    display();
                    if (isEnd()) {
                        JOptionPane.showMessageDialog(this, "Game Over!!");
                        System.exit(0);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new PuzzleGame();
    }
}
