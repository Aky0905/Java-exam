import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Timetable 클래스는 시간표를 수정하고 관리하는 GUI 애플리케이션입니다.
 * 사용자는 수업을 추가, 삭제하고, 파일에 저장하거나 불러올 수 있습니다.
 * 
 * @author Kim Jae Yeong
 * 
 * @created 2024-12-26
 * @lastModified 2024-12-26
 */
public class timetable {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton removeButton;
    private JButton saveButton;
    private JButton loadButton;

    // 수업 정보를 저장하는 리스트
    private List<String[]> classList = new ArrayList<>();

    /**
     * Timetable 생성자.
     * GUI 구성 요소를 초기화하고, 기본 시간표 데이터를 설정합니다.
     */
    public timetable() {
        frame = new JFrame("시간표 수정하기");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        String[] columnNames = {"시간", "월요일", "화요일", "수요일", "목요일", "금요일"};
        String[][] initialData = {
            {"09:00 - 10:00", "", "", "", "", ""},
            {"10:00 - 11:00", "", "", "", "", ""},
            {"11:00 - 12:00", "", "", "", "", ""},
            {"12:00 - 13:00", "", "", "", "", ""},
            {"13:00 - 14:00", "", "", "", "", ""},
            {"14:00 - 15:00", "", "", "", "", ""},
            {"15:00 - 16:00", "", "", "", "", ""},
            {"16:00 - 17:00", "", "", "", "", ""},
            {"17:00 - 18:00", "", "", "", "", ""}
        };

        tableModel = new DefaultTableModel(initialData, columnNames);
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (column == 0) {
                    c.setBackground(new Color(200, 230, 201)); // 연한 초록색
                } else if (getValueAt(row, column) != null && !getValueAt(row, column).toString().isEmpty()) {
                    c.setBackground(new Color(255, 224, 178)); // 연한 주황색
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };

        // 셀 가운데 정렬
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
                return c;
            }
        });

        table.setFillsViewportHeight(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(102, 204, 255));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        removeButton = new JButton("수업 삭제");
        saveButton = new JButton("저장");
        loadButton = new JButton("불러오기");

        removeButton.setBackground(new Color(244, 67, 54));
        removeButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setForeground(Color.WHITE);
        loadButton.setBackground(new Color(33, 150, 243));
        loadButton.setForeground(Color.WHITE);

        buttonPanel.add(removeButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeClass();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTimetable();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTimetable();
            }
        });

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * 선택된 행의 수업을 삭제합니다.
     * 삭제할 수업이 선택되지 않은 경우 오류 메시지를 표시합니다.
     */
    private void removeClass() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            for (int i = 1; i < table.getColumnCount(); i++) {
                tableModel.setValueAt("", selectedRow, i);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "삭제할 수업을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 현재 시간표를 파일에 저장합니다.
     * 저장 중 오류가 발생할 경우 오류 메시지를 표시합니다.
     */
    private void saveTimetable() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("timetable.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) {
                        writer.write(","); // 구분자로 쉼표 사용
                    }
                }
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "시간표가 저장되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 파일에서 시간표 데이터를 불러옵니다.
     * 불러오는 중 오류가 발생할 경우 오류 메시지를 표시합니다.
     */
    private void loadTimetable() {
        try (BufferedReader reader = new BufferedReader(new FileReader("timetable.txt"))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < tableModel.getRowCount()) {
                String[] values = line.split(",");
                for (int col = 0; col < values.length && col < tableModel.getColumnCount(); col++) {
                    tableModel.setValueAt(values[col], row, col);
                }
                row++;
            }
            JOptionPane.showMessageDialog(frame, "시간표가 불러와졌습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "불러오는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 주어진 시간에 해당하는 행의 인덱스를 찾습니다.
     *
     * @param time 찾고자 하는 시간 문자열
     * @return 해당 시간의 행 인덱스, 찾지 못한 경우 -1 반환
     */
    private int findRowIndex(String time) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(time)) {
                return i;
            }
        }
        return -1; // 찾지 못한 경우
    }

    /**
     * 주어진 요일 문자열에 해당하는 인덱스를 반환합니다.
     *
     * @param day 찾고자 하는 요일 문자열
     * @return 해당 요일의 인덱스, 잘못된 경우 -1 반환
     */
    private int getDayIndex(String day) {
        switch (day) {
            case "월요일": return 1;
            case "화요일": return 2;
            case "수요일": return 3;
            case "목요일": return 4;
            case "금요일": return 5;
            default: return -1; // 잘못된 요일
        }
    }

    /**
     * 애플리케이션의 메인 메서드입니다.
     * Swing GUI를 생성합니다.
     *
     * @param args 명령줄 인수
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(timetable::new);
    }
}
