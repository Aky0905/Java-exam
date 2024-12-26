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
