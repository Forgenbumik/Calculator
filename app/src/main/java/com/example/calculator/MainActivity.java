package com.example.calculator;

import static java.lang.Character.isDigit;
import static java.lang.Double.isNaN;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView TextResult; //вывод результата, строка выражения

    String expression; // строка всего выражения

    double firstop; //первый элемент выражения

    double secop; //второй элемент выражения

    Character operation = '\0'; //символ операции: (+, -, *, /)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // поле ввода выражения и вывода результата
        TextResult = findViewById(R.id.TextResult);
        // обработчик события для мгновенного изменения строки выражения
        TextResult.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                expression = TextResult.getText().toString();
            }
        });
        //привязка обработчиков события нажатия на кнопку клавиатуры
        findViewById(R.id.Button1).setOnClickListener((view)->onNumberClick("1"));
        findViewById(R.id.Button2).setOnClickListener((view)->onNumberClick("2"));
        findViewById(R.id.Button3).setOnClickListener((view)->onNumberClick("3"));
        findViewById(R.id.Button4).setOnClickListener((view)->onNumberClick("4"));
        findViewById(R.id.Button5).setOnClickListener((view)->onNumberClick("5"));
        findViewById(R.id.Button6).setOnClickListener((view)->onNumberClick("6"));
        findViewById(R.id.Button7).setOnClickListener((view)->onNumberClick("7"));
        findViewById(R.id.Button8).setOnClickListener((view)->onNumberClick("8"));
        findViewById(R.id.Button9).setOnClickListener((view)->onNumberClick("9"));
        findViewById(R.id.Button0).setOnClickListener((view)->onNumberClick("0"));
        findViewById(R.id.Button00).setOnClickListener((view)->onNumberClick("00"));
        findViewById(R.id.ButtonPlus).setOnClickListener((view)->onOperationClick('+'));
        findViewById(R.id.ButtonMinus).setOnClickListener((view)->onOperationClick('-'));
        findViewById(R.id.ButtonMultiply).setOnClickListener((view)->onOperationClick('×'));
        findViewById(R.id.ButtonDivide).setOnClickListener((view)->onOperationClick('÷'));
        findViewById(R.id.ButtonPoint).setOnClickListener((view)->ButtonPointClick());
        findViewById(R.id.ButtonPercents).setOnClickListener((view)->ButtonPercentsClick());
        findViewById(R.id.ButtonResult).setOnClickListener((view)->onResultClick());
        findViewById(R.id.ButtonReset).setOnClickListener((view)-> ButtonResetClick());
    }

    public void ButtonPointClick() {
        if (expression.charAt(expression.length() - 1) != operation &
        !expression.isEmpty() & expression.charAt(expression.length() - 1) != ',') {
            TextResult.append(".");
        }
    }

    public void ButtonResetClick() {
        firstop = 0;
        secop = 0;
        operation = '\0';
        TextResult.setText("");
    }

    public void onResultClick() {
        secop = takeSecondOperand();
        TextResult.setText(String.valueOf(Result(firstop,operation,secop)));
        operation = '\0';
    }

    @SuppressLint("SetTextI18n")
    public void ButtonPercentsClick() {
        if (operation != '\0') { //если есть символ операции, то выделять второй операнд и вместо него ставить secop/100
            secop = takeSecondOperand()/100; //вычисление процента
            int operationIndex = expression.indexOf(operation); //индекс символа операции
            String finalExpression = expression.substring(0,operationIndex+1)+ secop; //замена второго аргумента
            TextResult.setText(finalExpression); // замена строки выражения
        }
        else { //если нет, то вместо строки вставить строка/100
            firstop = Double.parseDouble(expression)/100; //запись первого аргумента
            TextResult.setText(Double.toString(firstop)); // замена аргумента в строке
        }
    }

    public void onNumberClick(String number) {
        TextResult.append(number);
    }

    public void onOperationClick(char operation) {
        if (!expression.isEmpty() & //если выражение не пустое
                isDigit(expression.charAt(expression.length() - 1))) { //если последний символ - цифра
            if (this.operation == '\0') {
                firstop = Double.parseDouble(expression);
                this.operation = operation;
                TextResult.append(String.valueOf(operation));
            }
            else {
                this.operation = operation;
                secop = takeSecondOperand();
                firstop = Result(firstop, this.operation, secop);
                if (firstop % 1 == 0) {
                    TextResult.setText((int)firstop+"");
                }
                else {
                    TextResult.setText(String.valueOf(firstop));
                }
                this.operation = operation;
                TextResult.append(String.valueOf(operation));
            }
        }
    }

    public double Result(double firstoperand, char operation, double secoperand) {
        switch (operation) {
            case '+':
                return firstoperand+secoperand;
            case '-':
                return firstoperand-secoperand;
            case '×':
                return firstoperand*secoperand;
            case '÷':
                return firstoperand/secoperand;
        }
        return 0;
    }

    public double takeSecondOperand() {
        //возвращает срез строки с символа операции и до конца
        return Double.parseDouble(expression.substring(expression.indexOf(operation)+1));
    }
}