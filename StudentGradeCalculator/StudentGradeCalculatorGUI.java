import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentGradeCalculatorGUI extends JFrame {
    private JTextField[] subjectFields;
    private JLabel resultLabel;
    private String[] subjects = {"English", "Math", "Science", "Social", "Tamil"};

    public StudentGradeCalculatorGUI() {
        // Set up the main frame
        setTitle("Student Grade Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title
        JLabel titleLabel = new JLabel("Student Grade Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create input panel for subjects
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        subjectFields = new JTextField[5];

        for (int i = 0; i < 5; i++) {
            inputPanel.add(new JLabel(subjects[i] + ":"));
            subjectFields[i] = new JTextField();
            inputPanel.add(subjectFields[i]);
        }
        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create calculate button
        JButton calculateButton = new JButton("Calculate Grades");
        calculateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateGrades();
            }
        });
        mainPanel.add(calculateButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Create result area
        resultLabel = new JLabel("");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(resultLabel);

        // Add main panel to frame
        add(mainPanel);
    }

    private void calculateGrades() {
        try {
            int totalMarks = 0;
            
            // Validate and sum up marks
            for (JTextField field : subjectFields) {
                int marks = Integer.parseInt(field.getText());
                if (marks < 0 || marks > 100) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter valid marks between 0 and 100",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                totalMarks += marks;
            }

            // Calculate average percentage
            double averagePercentage = (double) totalMarks / 5;
            
            // Determine grade
            String grade = calculateGrade(averagePercentage);

            // Display results
            String result = String.format("<html><div style='text-align: center;'>" +
                "<br>====== Results ======<br>" +
                "Total Marks: %d/500<br>" +
                "Average Percentage: %.2f%%<br>" +
                "Grade: %s<br>" +
                "===================</div></html>",
                totalMarks, averagePercentage, grade);
                
            resultLabel.setText(result);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numeric marks",
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String calculateGrade(double averagePercentage) {
        if (averagePercentage >= 90) return "A+";
        if (averagePercentage >= 80) return "A";
        if (averagePercentage >= 70) return "B";
        if (averagePercentage >= 60) return "C";
        if (averagePercentage >= 50) return "D";
        return "F";
    }

    public static void main(String[] args) {
        // Run GUI in Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StudentGradeCalculatorGUI().setVisible(true);
            }
        });
    }
}