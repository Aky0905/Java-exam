public class Timetable {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton removeButton;
    private JButton saveButton;
    private JButton loadButton;

    private List<String[]> classList = new ArrayList<>();