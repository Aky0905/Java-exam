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