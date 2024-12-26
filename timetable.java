public class Timetable {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton removeButton;
    private JButton saveButton;
    private JButton loadButton;

    private List<String[]> classList = new ArrayList<>();

    public Timetable() {
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
                    c.setBackground(new Color(200, 230, 201)); 
                } else if (getValueAt(row, column) != null && !getValueAt(row, column).toString().isEmpty()) {
                    c.setBackground(new Color(255, 224, 178));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };

        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER); 
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
    private void saveTimetable() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("timetable.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.write(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) {
                        writer.write(","); 
                    }
                }
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "시간표가 저장되었습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
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
    private int findRowIndex(String time) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(time)) {
                return i;
            }
        }
        return -1; 
    }

    private int getDayIndex(String day) {
        switch (day) {
            case "월요일": return 1;
            case "화요일": return 2;
            case "수요일": return 3;
            case "목요일": return 4;
            case "금요일": return 5;
            default: return -1; 
        }
    }