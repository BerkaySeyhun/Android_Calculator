package com.example.calc1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;


public class CalculatorActivity extends AppCompatActivity {

    //EditText to display input
    private EditText inputDisplay;
    //StringBuilder to store the input text
    private StringBuilder inputText;
    //Counter to keep track of the calculation numbers
    // len for Length of the inputText
    private int count, len;
    //Index of the last occurrence of current operation in the inputText
    private int plusIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // Initialize UI components
        inputDisplay = findViewById(R.id.inputDisplay);
        inputText = new StringBuilder();

        //Initialize variables
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        plusIndex = -1;
        count = 0;
        len = 0;

        // add click Listeners to buttons dynamically
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);

            if (child instanceof Button) {
                Button button = (Button) child;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleButtonClick(button.getText().toString());
                    }
                });
            }
        }
    }

    private void handleButtonClick(String buttonText) {

        if (count < 4 || !control(buttonText)) {
            try {
                switch (buttonText) {
                    case "%":
                    case "1/x":
                    case "x²":
                    case "√x":
                        inputText = calculateResultQ(buttonText);
                        len = inputText.length();
                        break;
                    case "+":
                    case "-":
                    case "×":
                    case "÷":
                        if(len==0 && (buttonText.equals("×")||buttonText.equals("÷"))){
                            showToast("first need a number");
                        }else {
                            inputText.append(buttonText);
                            len++;
                            count++;
                        }
                        break;
                    case "CE":
                        deleteLast();
                        break;
                    case "C":
                        clearAll();
                        break;
                    case "DEL":
                        deleteLastNumber();
                        break;
                    case "=":
                        inputText = calculateResult();
                        break;
                    case "+/-":
                        changeSign();
                        break;
                    default:
                        inputText.append(buttonText);
                        len++;
                        break;
                }
                updateInputDisplay();
            } catch (Exception e) {
                showToast("Invalid Operation");
                e.printStackTrace();
            }
        } else{
            showToast("Invalid Operation Number");
        }
    }
    // A Method for delete last number ( after a operation sign, not delete a operationSign
    private void deleteLastNumber() {
        if (len > 0) {
            String last = inputText.charAt(len - 1) + "";
            if (control(last)) {
                count--;
            }
            inputText.deleteCharAt(len - 1);
            len--;
        }
    }
    // A Method for delete last index
    private void deleteLast() {
        if(len>0) {
        int temp = len;
        for (int i = temp; 0 <= i; i--) {
            String myString = inputText.charAt(i - 1) + "";
            if (!control(myString)) {
                inputText.deleteCharAt(i - 1);
                len--;
                if (len == 0) {
                    break;
                }
            } else {
                break;
            }
        }}

    }
    //A Method for delete all Input
    private void clearAll() {
        inputText.delete(0, len);
        count = 0;
        len = inputText.length();
    }
    // A method for take the last number from inputText
    private StringBuilder takeLastNumber() {
        StringBuilder inputTextR = new StringBuilder("");
        int sabit = inputText.length();
        for (int i = sabit; 0 <= i; i--) {
            String myString = inputText.charAt(i - 1) + "";
            if (!control(myString)) {
                inputTextR.append(myString);
                inputText.deleteCharAt(i - 1);
                len--;
                if (len == 0) {
                    break;
                }
            } else {
                break;
            }
        }
        return inputTextR;

    }
    //A Method for change the current number sign
    private void changeSign() {
        StringBuilder newText = takeLastNumber();
        if (len > 0) {
            String temp1 = inputText.charAt(len - 1) + "";
            if (temp1.equals("×") || temp1.equals("÷")) {
                count++;
                inputText.append("-");
            } else if (temp1.equals("-")) {
                inputText.deleteCharAt(len - 1);
                inputText.append("+");
            } else {
                inputText.deleteCharAt(len - 1);
                inputText.append("-");

            }
        } else {
            newText.append("-");
        }
        newText.reverse();
        inputText.append(newText);
        len = inputText.length();

    }
    //A Method to check if a button text is a CalculationOperation
    private boolean control(String buttonText) {
        String[] keywords = {"%", "1/x", "x²", "√x", "+", "×", "÷", "-"};
        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].equals(buttonText)) {
                return true;
            }
        }
        return false;

    }
    // A Method to display a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    //A Method to update the input display in the EditText
    private void updateInputDisplay() {

        inputDisplay.setText(inputText.toString());
    }
    // A Method for calculate "%","1/x","x²","√x" operations
    private StringBuilder calculateResultQ(String buttonText) {

        String text = takeLastNumber().reverse().toString();
        double result;
        int intNumber;
        String stringResult;

        if(len==1) {
            try {
                result = Double.parseDouble(inputText.append(text).toString());
                inputText.delete(0, inputText.length());
            } catch (NumberFormatException e) {
                showToast("Invalid Input");
                return inputText;
            }
        } else {
            try {
                result = Double.parseDouble(text);
            } catch (NumberFormatException e) {
                showToast("Invalid Input");
                return inputText;
            }
        }
            if (!text.isEmpty()) {
                switch (buttonText) {
                    case "%":
                        result /= 100;
                        break;
                    case "1/x":
                        if (result == 0) {
                            showToast("Cannot divide by zero");
                            return inputText.append("NaN");
                        }
                        result = 1 / result;
                        break;
                    case "x²":
                        result *= result;
                        break;
                    case "√x":
                        if (result < 0) {
                            showToast("Cannot calculate square root of a negative number");
                            return inputText.append("NaN");
                        }
                        result = Math.sqrt(result);
                        break;
                    default:
                        break;
                }

        }
        stringResult = Double.toString(result);
        if (result % 1 == 0 && result<2147483647) {
            intNumber = (int) result;
            stringResult = Integer.toString(intNumber);
        }
        inputText.append(stringResult);

        return inputText;
    }
    // A Method for that directs inputText according to the order of operations
    private StringBuilder calculateResult() {

        plusIndex = inputText.lastIndexOf("×");
        while (plusIndex != -1) {
            inputText = connectText(plusIndex, "×");
            len = inputText.length();
            plusIndex = inputText.lastIndexOf("×");
        }

        plusIndex = inputText.lastIndexOf("÷");
        while (plusIndex != -1) {
            inputText = connectText(plusIndex, "÷");
            len = inputText.length();
            plusIndex = inputText.lastIndexOf("÷");
        }

        plusIndex = inputText.lastIndexOf("+");
        while (plusIndex != -1) {
            inputText = connectText(plusIndex, "+");
            len = inputText.length();
            plusIndex = inputText.lastIndexOf("+");
        }

        plusIndex = inputText.lastIndexOf("-");
        while (plusIndex != -1 && plusIndex != 0) {
            inputText = connectText(plusIndex, "-");
            len = inputText.length();
            plusIndex = inputText.lastIndexOf("-");
        }


        return inputText;
    }

    // A Method for make calculations "-","+","/","x"
    private StringBuilder connectText(int plusIndex, String index) {

        StringBuilder inputText1 = new StringBuilder();
        StringBuilder inputText2 = new StringBuilder();

        String yeniString = "";
        double result1;
        double result2;
        double result = 0;

        inputText.deleteCharAt(plusIndex);
        count--;
        len--;

        int sabit = plusIndex;

        // extract numbers on the right and left of plusIndex
        for (int i = sabit; i < len; i = i + 0) {// right of plusIndex
            String cal1 = inputText.charAt(i) + "";

            if (cal1.equals("-") && (inputText1.length() == 0)) {
                inputText1.append(cal1);
                inputText.deleteCharAt(i);
                len--;
                count--;
                if (i == len) {
                    break;
                }
            } else if (!control(cal1)) {
                inputText1.append(cal1);
                inputText.deleteCharAt(i);
                len--;
                if (i == len) {
                    break;
                }
            } else {
                break;
            }
        }
        if (sabit > 0) {//left side of plusIndex
            for (int k = sabit - 1; k >= 0; k--) {
                String cal2 = inputText.charAt(k) + "";
                int temp = inputText2.indexOf("-");

                if (cal2.equals("-") && temp == -1) {
                    inputText2.append(cal2);
                    inputText.deleteCharAt(k);
                    plusIndex--;
                    count--;
                    len--;
                    break;
                } else if (!control(cal2) && temp == -1) {
                    inputText2.append(cal2);
                    inputText.deleteCharAt(k);
                    len--;
                    plusIndex--;
                    if (k == 0) {
                        break;
                    }
                } else {
                    break;
                }
            }
        } else {
            inputText2.append("0");
        }

        // convert string to double for calculation
        result1 = Double.parseDouble(inputText1.toString());
        result2 = Double.parseDouble(inputText2.reverse().toString());

        switch (index) {
            case "+":
                result = (result1 + result2);
                break;
            case "-":
                result = (result2 - result1);
                break;
            case "÷":
                if(result1!=0) {
                    result = (result2 / result1);
                    break;
                }else{
                    showToast("Cannot Divide By Zero");
                    clearAll();
                    return inputText.append("Cannot Divide By Zero");
                }
            case "×":
                result = (result1 * result2);
                break;
            default:
                break;
        }

        if (result < 0) {
            count++;
        }

        int intNumber;
        String stringResult = Double.toString(result);

        if (result % 1 == 0 && result<2147483647) {// check double or integer for result
            intNumber = (int) result;
            stringResult = Integer.toString(intNumber);
        }

        // append inputText
        if (inputText.length() != 0) {
            if (plusIndex != 0) {
                String cal3 = inputText.charAt(plusIndex - 1) + "";
                if (!control(cal3) && result >= 0) {
                    inputText.append("+");
                    count++;
                    len++;
                    plusIndex++;
                }
            }
            if (plusIndex != len) {
                yeniString = inputText.substring(plusIndex);
                inputText.delete(plusIndex, len);
            }
            inputText.append(stringResult).append(yeniString);
        } else {
            inputText.append(stringResult);
        }

        return inputText;
    }
}
