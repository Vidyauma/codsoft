import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

// Course class to store course information
class Course {
    String code;
    String title;
    String description;
    int capacity;
    String schedule;
    int enrolledStudents;

    public Course(String code, String title, String description, int capacity, String schedule) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.enrolledStudents = 0;
    }

    public boolean hasAvailableSlots() {
        return enrolledStudents < capacity;
    }
}

// Student class to store student information
class Student {
    String id;
    String name;
    ArrayList<Course> registeredCourses;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }
}

// Main GUI class
public class CourseRegistrationSystem extends JFrame {
    private ArrayList<Course> courses;
    private ArrayList<Student> students;
    private Student currentStudent;
    private DefaultTableModel availableCoursesModel;
    private DefaultTableModel registeredCoursesModel;
    private JTable availableCoursesTable;
    private JTable registeredCoursesTable;

    public CourseRegistrationSystem() {
        // Initialize data
        courses = new ArrayList<>();
        students = new ArrayList<>();
        initializeSampleData();

        // Set up the main frame
        setTitle("Course Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add student login panel
        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, BorderLayout.NORTH);

        // Create tables panel
        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Available courses table
        JPanel availableCoursesPanel = new JPanel(new BorderLayout());
        availableCoursesPanel.setBorder(BorderFactory.createTitledBorder("Available Courses"));
        String[] availableColumns = {"Code", "Title", "Description", "Available Slots", "Schedule"};
        availableCoursesModel = new DefaultTableModel(availableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableCoursesTable = new JTable(availableCoursesModel);
        availableCoursesPanel.add(new JScrollPane(availableCoursesTable), BorderLayout.CENTER);

        // Register button
        JButton registerButton = new JButton("Register Selected Course");
        registerButton.addActionListener(e -> registerCourse());
        availableCoursesPanel.add(registerButton, BorderLayout.SOUTH);

        // Registered courses table
        JPanel registeredCoursesPanel = new JPanel(new BorderLayout());
        registeredCoursesPanel.setBorder(BorderFactory.createTitledBorder("Registered Courses"));
        String[] registeredColumns = {"Code", "Title", "Schedule"};
        registeredCoursesModel = new DefaultTableModel(registeredColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        registeredCoursesTable = new JTable(registeredCoursesModel);
        registeredCoursesPanel.add(new JScrollPane(registeredCoursesTable), BorderLayout.CENTER);

        // Drop button
        JButton dropButton = new JButton("Drop Selected Course");
        dropButton.addActionListener(e -> dropCourse());
        registeredCoursesPanel.add(dropButton, BorderLayout.SOUTH);

        tablesPanel.add(availableCoursesPanel);
        tablesPanel.add(registeredCoursesPanel);
        mainPanel.add(tablesPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loginPanel.add(new JLabel("Student ID:"));
        JTextField studentIdField = new JTextField(10);
        loginPanel.add(studentIdField);
        loginPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(15);
        loginPanel.add(nameField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String id = studentIdField.getText();
            String name = nameField.getText();
            if (!id.isEmpty() && !name.isEmpty()) {
                loginStudent(id, name);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter both ID and Name");
            }
        });
        loginPanel.add(loginButton);

        return loginPanel;
    }

    private void initializeSampleData() {
        // Add sample courses
        courses.add(new Course("CS101", "Introduction to Programming", "Basic programming concepts", 30, "Mon/Wed 9:00-10:30"));
        courses.add(new Course("CS102", "Data Structures", "Fundamental data structures", 25, "Tue/Thu 11:00-12:30"));
        courses.add(new Course("CS103", "Database Systems", "Database management concepts", 35, "Mon/Wed 2:00-3:30"));
        courses.add(new Course("CS104", "Web Development", "Web programming basics", 40, "Tue/Thu 3:30-5:00"));
        courses.add(new Course("CS105", "AI Fundamentals", "Introduction to AI concepts", 20, "Fri 1:00-4:00"));
    }

    private void loginStudent(String id, String name) {
        currentStudent = students.stream()
                .filter(s -> s.id.equals(id))
                .findFirst()
                .orElseGet(() -> {
                    Student newStudent = new Student(id, name);
                    students.add(newStudent);
                    return newStudent;
                });
        updateTables();
        JOptionPane.showMessageDialog(this, "Welcome, " + name + "!");
    }

    private void updateTables() {
        // Update available courses table
        availableCoursesModel.setRowCount(0);
        for (Course course : courses) {
            if (!currentStudent.registeredCourses.contains(course)) {
                availableCoursesModel.addRow(new Object[]{
                    course.code,
                    course.title,
                    course.description,
                    course.capacity - course.enrolledStudents,
                    course.schedule
                });
            }
        }

        // Update registered courses table
        registeredCoursesModel.setRowCount(0);
        for (Course course : currentStudent.registeredCourses) {
            registeredCoursesModel.addRow(new Object[]{
                course.code,
                course.title,
                course.schedule
            });
        }
    }

    private void registerCourse() {
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "Please login first!");
            return;
        }

        int selectedRow = availableCoursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to register!");
            return;
        }

        String courseCode = (String) availableCoursesModel.getValueAt(selectedRow, 0);
        Course selectedCourse = courses.stream()
                .filter(c -> c.code.equals(courseCode))
                .findFirst()
                .orElse(null);

        if (selectedCourse != null && selectedCourse.hasAvailableSlots()) {
            selectedCourse.enrolledStudents++;
            currentStudent.registeredCourses.add(selectedCourse);
            updateTables();
            JOptionPane.showMessageDialog(this, "Successfully registered for " + selectedCourse.title);
        } else {
            JOptionPane.showMessageDialog(this, "Course is full!");
        }
    }

    private void dropCourse() {
        if (currentStudent == null) {
            JOptionPane.showMessageDialog(this, "Please login first!");
            return;
        }

        int selectedRow = registeredCoursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to drop!");
            return;
        }

        String courseCode = (String) registeredCoursesModel.getValueAt(selectedRow, 0);
        Course selectedCourse = currentStudent.registeredCourses.stream()
                .filter(c -> c.code.equals(courseCode))
                .findFirst()
                .orElse(null);

        if (selectedCourse != null) {
            selectedCourse.enrolledStudents--;
            currentStudent.registeredCourses.remove(selectedCourse);
            updateTables();
            JOptionPane.showMessageDialog(this, "Successfully dropped " + selectedCourse.title);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CourseRegistrationSystem().setVisible(true);
        });
    }
}