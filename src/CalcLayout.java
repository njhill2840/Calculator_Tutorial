import javax.print.attribute.standard.MediaSize;
import javax.swing.*; //importing Swing so that BoxLayout can be used
import java.awt.*; //import awt to set fonts, styles, sizes of calculator
import java.awt.event.*; //import event package for action listeners, etc.

public class CalcLayout  extends JFrame{

    //create the various specific buttons needed to create a calculator
    JButton btnAdd, btnSubtract, btnDivide, btnMultiply, btnClear, btnDelete, btnEquals, btnDot;
    //create an array for the digit buttons
    JButton numBtn[];
    //initialize where the results will display
    JTextField output;
    //create variables to hold the values being entered and their operation
    String previous, current, operator;

    //create constructor for the class
    public CalcLayout() {
        //name the calculator--title that will appear in the GUI
        super("Basic Calculator");
        //create panel to hold all components
        JPanel mainPanel = new JPanel();

        current = "";
        previous = "";

        //create subpanels inside the main panel
        JPanel row1 = new JPanel();
        JPanel row2 = new JPanel();
        JPanel row3 = new JPanel();
        JPanel row4 = new JPanel();
        JPanel row5 = new JPanel();

        //initialize buttons
        output = new JTextField(16);
        btnSubtract = new JButton("-");
        btnAdd = new JButton("+");
        btnDivide = new JButton("÷");
        btnMultiply = new JButton("x");
        btnDot = new JButton(".");
        btnEquals = new JButton("=");
        btnClear = new JButton("AC");
        btnDelete = new JButton("C");

        //instantiate action listeners, listen for click on calc button
        NumberBtnHandler numBtnHandler = new NumberBtnHandler();
        OtherBtnHandler otherBtnHandler = new OtherBtnHandler();
        OperatorBtnHandler opBtnHandler = new OperatorBtnHandler();


        //initialize array of number buttons
        numBtn = new JButton[11];
        numBtn[10] = btnDot;
        //for loop to set the values as well as the style of each button
        for (int count = 0; count<numBtn.length-1; count++) {
            numBtn[count] = new JButton(String.valueOf(count));
            numBtn[count].setFont(new Font("Monospaced", Font.BOLD, 22));
            //listen for action (click)
            numBtn[count].addActionListener(numBtnHandler);
        }

        //setting style for all other buttons
        btnDot.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnEquals.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnAdd.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnSubtract.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnDivide.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnMultiply.setFont(new Font("Monospaced", Font.BOLD, 22));
        btnClear.setFont(new Font("Monospaced", Font.BOLD, 24));
        btnDelete.setFont(new Font("Monospaced", Font.BOLD, 24));

        //set style and default output for the output display
        output.setMaximumSize(new Dimension(290, 40)); //setting size by pixels
        output.setFont(new Font("Monospaced", Font.BOLD, 30));
        output.setDisabledTextColor(new Color(0, 0, 0));
        output.setMargin(new Insets(0, 5, 0, 0));
        output.setText("0");

        //add action listeners to non-operation, non-numeric  buttons
        btnDot.addActionListener(numBtnHandler);
        btnDelete.addActionListener(otherBtnHandler);
        btnClear.addActionListener(otherBtnHandler);
        btnEquals.addActionListener(otherBtnHandler);

        //add action listeners to operation buttons
        btnMultiply.addActionListener(opBtnHandler);
        btnAdd.addActionListener(opBtnHandler);
        btnSubtract.addActionListener(opBtnHandler);
        btnDivide.addActionListener(opBtnHandler);
        output.setText("0");

        //creating the layout of each row of buttons
        //LINE_AXIS allows for the orientation of the device to set the order
        row1.setLayout(new BoxLayout (row1, BoxLayout.LINE_AXIS));
        row2.setLayout(new BoxLayout (row2, BoxLayout.LINE_AXIS));
        row3.setLayout(new BoxLayout (row3, BoxLayout.LINE_AXIS));
        row4.setLayout(new BoxLayout (row4, BoxLayout.LINE_AXIS));
        row5.setLayout(new BoxLayout (row5, BoxLayout.LINE_AXIS));

        //top row (under display)
        //force clear and delete buttons whole way to the right
        row1.add(Box.createHorizontalGlue());
        row1.add(btnClear);
        row1.add(btnDelete);

        //second row down
        row2.add(numBtn[7]);
        row2.add(numBtn[8]);
        row2.add(numBtn[9]);
        row2.add(btnDivide);

        //third row down
        row3.add(numBtn[4]);
        row3.add(numBtn[5]);
        row3.add(numBtn[6]);
        row3.add(btnMultiply);

        //fourth row down
        row4.add(numBtn[1]);
        row4.add(numBtn[2]);
        row4.add(numBtn[3]);
        row4.add(btnSubtract);

        //fifth row down
        row5.add(numBtn[0]);
        row5.add(btnDot);
        row5.add(btnEquals);
        row5.add(btnAdd);

        //add rows to the main panel so we can see all buttons on GUI
        //PAGE_AXIS allows for computer layout control like LINE_AXIS
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(output);
        //add space between display and buttons by pixels--width, height
        mainPanel.add(Box.createRigidArea(new Dimension (0, 5)));
        mainPanel.add(row1);
        mainPanel.add(row2);
        mainPanel.add(row3);
        mainPanel.add(row4);
        mainPanel.add(row5);

        //add mainPanel to the JFrame
        this.add(mainPanel);
        //set action when user clicks close button
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //set true so it is visible
        this.setVisible(true);
        //set size of Frame in pixels--width, height
        this.setSize( 300,260);

        //


    }

    //start building functions
    public void delete() {
        if (current.length() > 0) {
            current = current.substring(0, current.length() - 1);
        }
    }

    public void clear() {
        current = "";
        previous = "";
        operator = null;
    }

    public void updateOutput() {
        output.setText(current);
    }

    public void appendToOutput(String num) {
        //Prevents adding more than one dot on the output
        if (num.equals(".") && current.contains(".")) {
            return;
        }
        current += num;
    }

    public void selectOperator(String newOperator) {
        //check if no number entered
        if (current.isEmpty()) {
            operator = newOperator;
            return;
        }

        //calculate previous equation if user did not press equals
        if (!previous.isEmpty()) {
            calculate();
        }

        operator = newOperator;
        previous = current;
        current = "";
    }

    public void calculate() {
        //check if either input is empty
        if (previous.length() < 1 || current.length() < 1) {
            return;
        }

        //set default double and convert from string to double
        double result = 0.0;
        double num1 = Double.parseDouble(previous);
        double num2 = Double.parseDouble(current);

        //provide computation for assigned string of selected operation
        switch (operator) {
            case "x":
                result = num1 * num2;
                break;
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "÷":
                result = num1 / num2;
                break;
            default:
                break;
        }

        //convert current value to string from double; reset operator and previous
        //in prep of next input
        current = String.valueOf(result);
        operator = "equals";
        previous = "";
        processOutputNumber();
    }

    //check if result ends with ".0" and drop decimal if so
    public void processOutputNumber() {
        if (current.length() > 0) {
            String integerPart = current.split("\\.")[0];
            String decimalPart = current.split("\\.")[1];
            if (decimalPart.equals("0")) {
                current = integerPart;
            }
        }
    }

    //create main method to call class when run
    public static void main(String[] args){
    new CalcLayout();
    }

    //creates process for event: number or dot button clicked
    private class NumberBtnHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton selectedBtn = (JButton) e.getSource();
            for (JButton btn : numBtn) {
                if (selectedBtn == btn) {
                    if (operator=="equals") {
                        current = "";
                        operator = null;
                    }
                    appendToOutput(btn.getText());
                    updateOutput();
                }

            }

        }
    }

    //creates process for event: math operator button is clicked
    private class OperatorBtnHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton selectedBtn = (JButton) e.getSource();
            if (selectedBtn == btnMultiply) {
                selectOperator(btnMultiply.getText());
            } else if (selectedBtn == btnAdd) {
                selectOperator(btnAdd.getText());
            } else if (selectedBtn == btnSubtract) {
                selectOperator(btnSubtract.getText());
            } else if (selectedBtn == btnDivide) {
                selectOperator(btnDivide.getText());
            }
            updateOutput();
        }
    }

    //creates process for event: delete, clear, or equals is clicked
    private class OtherBtnHandler implements ActionListener {

        @Override
        public void actionPerformed (ActionEvent e ) {
            JButton selectedBtn = (JButton) e.getSource();
            if (selectedBtn == btnDelete) {
                delete();
            } else if (selectedBtn == btnClear) {
                clear();
            } else if (selectedBtn == btnEquals) {
                calculate();
            }
            updateOutput();
        }
    }
}


