package MyScore;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MyScore extends JFrame {
    DefaultTableModel model = new DefaultTableModel();
    JTable table1 = new JTable(model);
    int colCount = 8;
    int rowCount = 0;

    public MyScore() {
        setTitle("2021E7038 최호희 기말과제");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Top Field"));
        add(panel1, BorderLayout.NORTH);

        panel1.add(new JLabel("학번:"));
        JTextField txtSID = new JTextField(6);
        txtSID.setFont(new Font("고딕", Font.BOLD, 15));
        panel1.add(txtSID);

        panel1.add(new JLabel("이름:"));
        JTextField txtNAME = new JTextField(4);
        txtNAME.setFont(new Font("고딕", Font.BOLD, 15));
        panel1.add(txtNAME);

        panel1.add(new JLabel("전공 점수:"));
        JTextField txtSCORE1 = new JTextField(3);
        txtSCORE1.setFont(new Font("고딕", Font.BOLD, 15));
        panel1.add(txtSCORE1);

        panel1.add(new JLabel("교양 점수:"));
        JTextField txtSCORE2 = new JTextField(3);
        txtSCORE2.setFont(new Font("고딕", Font.BOLD, 15));
        panel1.add(txtSCORE2);

        JButton btnINSERT = new JButton("INSERT");
        btnINSERT.setFont(new Font("고딕", Font.BOLD, 13));
        btnINSERT.setBackground(new Color(179, 198, 255));
        panel1.add(btnINSERT);

        JButton btnDELETE = new JButton("DELETE");
        btnDELETE.setFont(new Font("고딕", Font.BOLD, 13));
        btnDELETE.setBackground(new Color(179, 179, 255));
        panel1.add(btnDELETE);

        JLabel lblMessage = new JLabel("메시지보내기:");
        lblMessage.setFont(new Font("고딕", Font.BOLD, 13));
        panel1.add(lblMessage);

        JButton btnMessage = new JButton("Message");
        btnMessage.setFont(new Font("고딕", Font.BOLD, 13));
        btnMessage.setBackground(new Color(198, 179, 255));
        panel1.add(btnMessage);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(10, 1, 5, 5));

        JButton btn1 = new JButton("행 추가");
        btn1.setFont(new Font("고딕", Font.BOLD, 15));
        panel2.add(btn1);

        JButton btn2 = new JButton("셀 정렬");
        btn2.setFont(new Font("고딕", Font.BOLD, 15));
        panel2.add(btn2);

        JButton btn3 = new JButton("메모장에 저장");
        btn3.setFont(new Font("고딕", Font.BOLD, 15));
        panel2.add(btn3);

        JButton btn4 = new JButton("파일에서 읽어오기");
        btn4.setFont(new Font("고딕", Font.BOLD, 15));
        panel2.add(btn4);

        JButton btn5 = new JButton("보고서 파일 생성");
        btn5.setFont(new Font("고딕", Font.BOLD, 15));
        panel2.add(btn5);

        JButton btn6 = new JButton("HTML로 저장하기");
        btn6.setFont(new Font("고딕", Font.BOLD, 15));
        panel2.add(btn6);

        JButton btn7 = new JButton("CSV로 저장하기");
        btn7.setFont(new Font("고딕", Font.BOLD, 15));
        panel2.add(btn7);

        JButton[] jobListButtons = { btn1, btn2, btn3, btn4, btn5, btn6, btn7 };
        for (JButton button : jobListButtons) {
            button.setBackground(new Color(179, 236, 255)); // 변경하고 싶은 색상으로 설정
        }

        add(panel2, BorderLayout.WEST);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Job List"));

        String header[] = { "학번", "이름", "전공", "교양", "합계", "평균", "등급", "석차" };

        for (int C = 0; C < header.length; C++) {
            model.addColumn(header[C]);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table1.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // "합계" 열
        table1.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // "평균" 열
        table1.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // "등급" 열
        table1.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // "석차" 열

        JTableHeader tableHeader = table1.getTableHeader();
        Font headerFont = new Font("고딕", Font.BOLD, 15);
        tableHeader.setFont(headerFont);
        tableHeader.setBackground(new Color(179, 236, 255));

        JScrollPane scrollPane = new JScrollPane(table1);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Score Table"));
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        btnINSERT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newSid = txtSID.getText();
                String newName = txtNAME.getText();

                // 학번과 이름이 둘 다 동일한 경우 중복으로 처리
                if (isDuplicate(newSid, newName)) {
                    JOptionPane.showMessageDialog(null, "이미 존재하는 학번 및 이름입니다.", "중복 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int R = table1.getRowCount();
                model.addRow(new Object[] {});

                R = table1.getRowCount() - 1;
                table1.setValueAt(newSid, R, 0);
                table1.setValueAt(newName, R, 1);

                // 이 부분을 수정하여 소수점으로 변환
                double score1 = Double.parseDouble(txtSCORE1.getText());
                double score2 = Double.parseDouble(txtSCORE2.getText());

                table1.setValueAt(score1, R, 2);
                table1.setValueAt(score2, R, 3);

                double sum = score1 + score2;
                table1.setValueAt(sum, R, 4);

                double average = sum / 2.0;
                table1.setValueAt(average, R, 5);

                String grade = calculateGrade(average);
                table1.setValueAt(grade, R, 6);

                updateTotalsAndAverages();
                updateRanks();
            }
        });

        btnDELETE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] rows = table1.getSelectedRows();
                for (int x = rows.length - 1; x >= 0; x--) {
                    if (rows.length > 0) {
                        model.removeRow(rows[x]);
                    }
                }
            }
        });

        btnMessage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Message를 보낼 학생 선택
                int selectedRow = table1.getSelectedRow();

                // 선택된 행이 없으면 메시지 전송 불가
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "메시지를 보낼 학생을 선택하세요.", "선택 오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 선택된 학생의 학번과 이름 가져오기
                String selectedSid = (String) table1.getValueAt(selectedRow, 0);
                String selectedName = (String) table1.getValueAt(selectedRow, 1);

                // 여기에 교수님께서 보낼 메시지를 입력하는 다이얼로그 또는 팝업 창을 추가하고,
                String professorMessage = JOptionPane.showInputDialog(null, "교수님의 메시지를 입력하세요.");

                System.out.println("메시지 전송: " + selectedName + "(" + selectedSid + "): " + professorMessage);

            }
        });

        btn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.addRow(new Object[] {});
                updateTotalsAndAverages();
            }
        });

        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
                rightRenderer.setHorizontalAlignment(JLabel.CENTER);
                for (int C = 0; C < table1.getColumnCount(); C++) {
                    table1.getColumnModel().getColumn(C).setCellRenderer(rightRenderer);
                }
            }
        });

        btn3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try (FileWriter myWriter = new FileWriter(
                        "C:\\Users\\Choihohee\\AYU_Assignment\\JavaProgramming_01\\finalAssignment\\R00_2021E7038.txt")) {
                    for (int R = 0; R < table1.getRowCount(); R++) {
                        for (int C = 0; C < table1.getColumnCount(); C++) {
                            myWriter.write(table1.getValueAt(R, C) + " ");
                        }
                        myWriter.write("\n");
                    }
                    myWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        btn4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File myObj = new File(
                        "C:\\Users\\Choihohee\\AYU_Assignment\\JavaProgramming_01\\finalAssignment\\Mydata.txt");
                String D[];
                int R = table1.getRowCount();

                try (Scanner myReader = new Scanner(myObj)) {
                    while (myReader.hasNextLine()) {
                        model.addRow(new Object[] {});
                        String data = myReader.nextLine();
                        D = data.split(" ");
                        table1.setValueAt(D[0], R, 0);
                        table1.setValueAt(D[1], R, 1);
                        table1.setValueAt(D[2], R, 2);
                        table1.setValueAt(D[3], R, 3);
                        table1.setValueAt(D[4], R, 4);
                        table1.setValueAt(D[5], R, 5);
                        table1.setValueAt(D[5], R, 6);
                        table1.setValueAt(D[5], R, 7);
                        R++;
                    }
                    myReader.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        btn5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 이름에 따라 오름차순으로 정렬
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
                sorter.setComparator(1, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareTo(s2);
                    }
                });
                table1.setRowSorter(sorter);

                // R01 파일 생성
                try (FileWriter myWriter = new FileWriter(
                        "C:\\Users\\Choihohee\\AYU_Assignment\\JavaProgramming_01\\finalAssignment\\R01_2021E7038.txt")) {
                    myWriter.write("\t성적 평가 보고서\n");
                    myWriter.write("\t------------ -------- ----  ----  ----  ---- ---- ----\n");
                    myWriter.write("\t학번          이름    전공   교양  합계  평균 등급 석차\n");
                    myWriter.write("\t------------ -------- ----  ----  ----  ---- ---- ----\n");

                    for (int R = 0; R < model.getRowCount(); R++) {
                        myWriter.write(String.format("%10s", model.getValueAt(R, 0)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(R, 1)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(R, 2)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(R, 3)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(R, 4)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(R, 5)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(R, 6)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(R, 7)) + " ");

                        myWriter.write("\n");
                    }

                    myWriter.write("\t------------ -------- ----  ----  ----  ---- ---- ----\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                // 원래 순서로 돌아가도록 정렬 취소
                table1.setRowSorter(null);

                // R02 파일 생성 (이름에 따라 정렬된 상태로)
                try (FileWriter myWriter = new FileWriter(
                        "C:\\Users\\Choihohee\\AYU_Assignment\\JavaProgramming_01\\finalAssignment\\R02_2021E7038.txt")) {
                    // 정렬된 순서로 파일에 저장
                    int[] selectedModelIndices = getSelectedModelIndices();

                    for (int row : selectedModelIndices) {
                        int modelRow = table1.convertRowIndexToModel(row); // Convert view row index to model row index

                        myWriter.write(String.format("%10s", model.getValueAt(modelRow, 0)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(modelRow, 1)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(modelRow, 2)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(modelRow, 3)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(modelRow, 4)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(modelRow, 5)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(modelRow, 6)) + " ");
                        myWriter.write(String.format("%3s", model.getValueAt(modelRow, 7)) + " ");

                        myWriter.write("\n");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        btn6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("HTML 파일", "html");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (!selectedFile.getName().toLowerCase().endsWith(".html")) {
                        selectedFile = new File(selectedFile.getAbsolutePath() + ".html");
                    }

                    try (PrintWriter writer = new PrintWriter(selectedFile)) {
                        StringBuilder htmlContent = new StringBuilder();
                        htmlContent.append("<html><body><table>");

                        htmlContent.append("<tr>");
                        for (int col = 0; col < table1.getColumnCount(); col++) {
                            htmlContent.append("<th>").append(table1.getColumnName(col)).append("</th>");
                        }
                        htmlContent.append("</tr>");

                        for (int row = 0; row < table1.getRowCount(); row++) {
                            htmlContent.append("<tr>");
                            for (int col = 0; col < table1.getColumnCount(); col++) {
                                htmlContent.append("<td>").append(table1.getValueAt(row, col)).append("</td>");
                            }
                            htmlContent.append("</tr>");
                        }

                        htmlContent.append("</table></body></html>");

                        writer.println(htmlContent.toString());
                        JOptionPane.showMessageDialog(null, "HTML 파일이 성공적으로 저장되었습니다.");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "HTML 파일 저장 중 오류가 발생했습니다.");
                    }
                }
            }
        });

        btn7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = model.getRowCount();
                int colCount = model.getColumnCount();
                Object[][] tableData = new Object[rowCount][colCount];
                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < colCount; col++) {
                        tableData[row][col] = model.getValueAt(row, col);
                    }
                }

                int[] sums = new int[colCount];
                for (int col = 2; col < colCount; col++) {
                    for (int row = 0; row < rowCount; row++) {
                        sums[col] += Integer.parseInt(tableData[row][col].toString());
                    }
                }

                double[] averages = new double[colCount];
                for (int col = 2; col < colCount; col++) {
                    averages[col] = sums[col] / (double) rowCount;
                }

                int[] ranks = new int[rowCount];
                for (int i = 0; i < rowCount; i++) {
                    int rank = 1;
                    for (int j = 0; j < rowCount; j++) {
                        if (i != j && Integer.parseInt(tableData[i][4].toString()) < Integer
                                .parseInt(tableData[j][4].toString())) {
                            rank++;
                        }
                    }
                    ranks[i] = rank;
                }

                StringBuilder csvContent = new StringBuilder();

                for (int col = 0; col < colCount; col++) {
                    csvContent.append(model.getColumnName(col)).append(",");
                }
                csvContent.append("합계,평균,등급,석차,과목별평균\n");

                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < colCount; col++) {
                        csvContent.append(tableData[row][col]).append(",");
                    }
                    csvContent.append(sums[4]).append(",");
                    csvContent.append(averages[4]).append(",");
                    csvContent.append(ranks[row]).append(",");
                    csvContent.append(sums[2]).append(",");
                    csvContent.append(averages[2]).append("\n");

                }

                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV 파일", "csv");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (!selectedFile.getName().toLowerCase().endsWith(".csv")) {
                        selectedFile = new File(selectedFile.getAbsolutePath() + ".csv");
                    }

                    try (PrintWriter writer = new PrintWriter(selectedFile)) {
                        writer.println(csvContent.toString());
                        JOptionPane.showMessageDialog(null, "CSV 파일이 성공적으로 저장되었습니다.");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "CSV 파일 저장 중 오류가 발생했습니다.");
                    }
                }
            }
        });

        JLabel lblSID = new JLabel("최호희(2021E7038)");
        lblSID.setFont(new Font("고딕", Font.BOLD, 20));
        add(lblSID, BorderLayout.SOUTH);
        setSize(900, 600);
    }

    private String calculateGrade(double average) {
        if (average >= 4.5) {
            return "A+";
        } else if (average >= 4.0) {
            return "A";
        } else if (average >= 3.5) {
            return "B+";
        } else if (average >= 3.25) {
            return "B";
        } else if (average >= 3.0) {
            return "C+";
        } else if (average >= 2.75) {
            return "C";
        } else if (average >= 2.5) {
            return "D";
        } else {
            return "F";
        }
    }

    private int calculateRank(int currentRow) {
        int rowCount = model.getRowCount();
        double currentAverage = (double) model.getValueAt(currentRow, 5);
        int rank = 1;

        for (int otherRow = 0; otherRow < rowCount; otherRow++) {
            if (otherRow != currentRow) {
                double compareAverage = (double) model.getValueAt(otherRow, 5);
                if (compareAverage > currentAverage) {
                    rank++;
                }
            }
        }
        return rank;
    }

    private void updateRanks() {
        int rowCount = model.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            int rank = calculateRank(row);
            model.setValueAt(rank, row, 7);
        }
    }

    private void updateTotalsAndAverages() {
        int rowCount = model.getRowCount();

        // 합계 및 평균 업데이트
        int colCount = model.getColumnCount();
        int[] sums = new int[colCount - 2];
        double[] averages = new double[colCount - 2];

        for (int row = 0; row < rowCount; row++) {
            for (int col = 2; col < colCount - 2; col++) {
                int score = Integer.parseInt(model.getValueAt(row, col).toString());
                sums[col - 2] += score;
            }
        }

        for (int col = 0; col < sums.length; col++) {
            averages[col] = sums[col] / (double) rowCount;
        }

        for (int col = 2; col < colCount - 2; col++) {
            model.setValueAt(sums[col - 2], rowCount, col);
            model.setValueAt(String.format("%.2f", averages[col - 2]), rowCount, col + 1);
        }

        updateRanks(); // 석차 업데이트
    }

    private int[] getSelectedModelIndices() {
        int[] selectedViewIndices = table1.getSelectedRows();
        int[] selectedModelIndices = new int[selectedViewIndices.length];

        for (int i = 0; i < selectedViewIndices.length; i++) {
            selectedModelIndices[i] = table1.convertRowIndexToModel(selectedViewIndices[i]);
        }

        return selectedModelIndices;
    }

    private boolean isDuplicate(String sid, String name) {
        for (int row = 0; row < table1.getRowCount(); row++) {
            String existingSid = (String) table1.getValueAt(row, 0);
            String existingName = (String) table1.getValueAt(row, 1);

            if (existingSid.equals(sid) || existingName.equals(name))
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        MyScore myScore = new MyScore();
        myScore.setVisible(true);
    }
}