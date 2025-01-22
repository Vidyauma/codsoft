import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
}

class ATMInterface {
    private BankAccount account;
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel balanceLabel;

    public ATMInterface() {
        account = new BankAccount(10000); // Initial balance of 10,000
        createMainFrame();
    }

    private void createMainFrame() {
        frame = new JFrame("ATM Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        mainPanel = new JPanel(new CardLayout());
        addIntroPage();
        addHomePage();
        addDepositPage();
        addWithdrawPage();
        addBalancePage();

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void addIntroPage() {
        JPanel introPanel = new JPanel(new BorderLayout(10, 10));
        introPanel.setBackground(new Color(240, 240, 255)); // Pastel background color
        introPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel introLabel = new JLabel("Welcome to the ATM Interface", JLabel.CENTER);
        introLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Reduced font size
        introLabel.setForeground(Color.BLUE);

        JButton continueButton = new JButton("Continue");
        continueButton.setBackground(new Color(173, 216, 230)); // Light blue

        introPanel.add(introLabel, BorderLayout.CENTER);
        introPanel.add(continueButton, BorderLayout.SOUTH);

        continueButton.addActionListener(e -> switchToPage("HomePage"));

        mainPanel.add(introPanel, "IntroPage");
        switchToPage("IntroPage");
    }

    private void addHomePage() {
        JPanel homePanel = new JPanel(new GridLayout(5, 1, 10, 10));
        homePanel.setBackground(new Color(240, 240, 255)); // Pastel background color
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton depositButton = new JButton("Deposit");
        depositButton.setBackground(new Color(255, 228, 181)); // Light orange
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBackground(new Color(255, 192, 203)); // Light pink
        JButton checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.setBackground(new Color(152, 251, 152)); // Light green
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(240, 128, 128)); // Light coral

        homePanel.add(depositButton);
        homePanel.add(withdrawButton);
        homePanel.add(checkBalanceButton);
        homePanel.add(exitButton);

        depositButton.addActionListener(e -> switchToPage("DepositPage"));
        withdrawButton.addActionListener(e -> switchToPage("WithdrawPage"));
        checkBalanceButton.addActionListener(e -> {
            updateBalanceLabel();
            switchToPage("BalancePage");
        });
        exitButton.addActionListener(e -> System.exit(0));

        mainPanel.add(homePanel, "HomePage");
    }

    private void addDepositPage() {
        JPanel depositPanel = new JPanel(new BorderLayout(10, 10));
        depositPanel.setBackground(new Color(240, 240, 255));
        depositPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Enter the amount to deposit:");
        JTextField amountField = new JTextField();
        JButton depositButton = new JButton("Deposit");
        depositButton.setBackground(new Color(224, 255, 255)); // Light cyan
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 239, 213)); // Light pastel yellow

        depositPanel.add(label, BorderLayout.NORTH);
        depositPanel.add(amountField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 255));
        buttonPanel.add(depositButton);
        buttonPanel.add(backButton);
        depositPanel.add(buttonPanel, BorderLayout.SOUTH);

        depositButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                account.deposit(amount);
                JOptionPane.showMessageDialog(frame, "Deposited Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> switchToPage("HomePage"));

        mainPanel.add(depositPanel, "DepositPage");
    }

    private void addWithdrawPage() {
        JPanel withdrawPanel = new JPanel(new BorderLayout(10, 10));
        withdrawPanel.setBackground(new Color(240, 240, 255));
        withdrawPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Enter the amount to withdraw:");
        JTextField amountField = new JTextField();
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBackground(new Color(240, 230, 140)); // Khaki
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 222, 173)); // Navajo white

        withdrawPanel.add(label, BorderLayout.NORTH);
        withdrawPanel.add(amountField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 255));
        buttonPanel.add(withdrawButton);
        buttonPanel.add(backButton);
        withdrawPanel.add(buttonPanel, BorderLayout.SOUTH);

        withdrawButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (account.withdraw(amount)) {
                    JOptionPane.showMessageDialog(frame, "Withdrawal Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient Balance!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid Amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> switchToPage("HomePage"));

        mainPanel.add(withdrawPanel, "WithdrawPage");
    }

    private void addBalancePage() {
        JPanel balancePanel = new JPanel(new BorderLayout(10, 10));
        balancePanel.setBackground(new Color(240, 240, 255));
        balancePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        balanceLabel = new JLabel("", JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(255, 218, 185)); // Light peach

        balancePanel.add(balanceLabel, BorderLayout.CENTER);
        balancePanel.add(backButton, BorderLayout.SOUTH);

        backButton.addActionListener(e -> switchToPage("HomePage"));

        mainPanel.add(balancePanel, "BalancePage");
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("Current Balance: Rs. " + account.getBalance());
    }

    private void switchToPage(String pageName) {
        CardLayout layout = (CardLayout) mainPanel.getLayout();
        layout.show(mainPanel, pageName);
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATMInterface::new);
    }
}
