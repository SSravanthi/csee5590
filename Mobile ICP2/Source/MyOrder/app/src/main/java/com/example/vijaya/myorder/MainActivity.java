package com.example.vijaya.myorder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_TAG = "MainActivity";
    final int PIZZA_PRICE = 10;
    final int SPINACH_PANNER_PRICE = 2;
    final int CHEESE_PRICE = 1;
    final int BBQCHICKEN_PRICE = 2;
    final int PEPPERONI_PRICE = 2;
    int quantity = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */

    public void submitOrder(View view) {

        // get user input
        EditText userInputNameView = (EditText) findViewById(R.id.user_input);
        String userInputName = userInputNameView.getText().toString();

        TextView quantity_text_view = (TextView) findViewById(R.id.quantity_text_view);
        quantity = Integer.parseInt(quantity_text_view.getText().toString());

        // check if Spinach Panner is selected
        CheckBox spinachPanner = (CheckBox) findViewById(R.id.spinach_panner_checked);
        boolean hasSpinachPanner = spinachPanner.isChecked();

        // check if cheese is selected
        CheckBox cheese = (CheckBox) findViewById(R.id.cheese_checked);
        boolean hasCheese = cheese.isChecked();

        // check if BBQ Chicken is selected
        CheckBox bbqChicken = (CheckBox) findViewById(R.id.bbqchicken_checked);
        boolean hasBbqChicken = bbqChicken.isChecked();

        // check if Pepporini is selected
        CheckBox pepperoni = (CheckBox) findViewById(R.id.pepperoni_checked);
        boolean hasPepperoni = pepperoni.isChecked();

        // calculate and store the total price
        float totalPrice = calculatePrice(hasSpinachPanner, hasCheese, hasBbqChicken, hasPepperoni);

        // create and store the order summary
        String orderSummaryMessage = createOrderSummary(userInputName, hasSpinachPanner, hasCheese, hasBbqChicken, hasPepperoni, totalPrice);

        Intent redirect = new Intent(MainActivity.this, OrderDelivery.class);
        redirect.putExtra("EXTRA_MESSAGE", orderSummaryMessage);
        MainActivity.this.startActivity(redirect);
        // Write the relevant code for making the buttons work(i.e implement the implicit and explicit intents

    }

    public void sendEmail(View view) {
        // Write the relevant code for triggering email

        // Hint to accomplish the task

        EditText userInputNameView = (EditText) findViewById(R.id.user_input);
        String userInputName = userInputNameView.getText().toString();

        TextView quantity_text_view = (TextView) findViewById(R.id.quantity_text_view);
        quantity = Integer.parseInt(quantity_text_view.getText().toString());

        // check if Spinach Panner is selected
        CheckBox spinachPanner = (CheckBox) findViewById(R.id.spinach_panner_checked);
        boolean hasSpinachPanner = spinachPanner.isChecked();

        // check if cheese is selected
        CheckBox cheese = (CheckBox) findViewById(R.id.cheese_checked);
        boolean hasCheese = cheese.isChecked();

        // check if BBQ Chicken is selected
        CheckBox bbqChicken = (CheckBox) findViewById(R.id.bbqchicken_checked);
        boolean hasBbqChicken = bbqChicken.isChecked();

        // check if Pepporini is selected
        CheckBox pepperoni = (CheckBox) findViewById(R.id.pepperoni_checked);
        boolean hasPepperoni = pepperoni.isChecked();

        // calculate and store the total price
        float totalPrice = calculatePrice(hasSpinachPanner, hasCheese, hasBbqChicken, hasPepperoni);

        // create and store the order summary
        String orderSummaryMessage = createOrderSummary(userInputName, hasSpinachPanner, hasCheese, hasBbqChicken, hasPepperoni, totalPrice);


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        String TO[] = {"nagendraraju.konduru@gmail.com"};
        String CC[] =    {"sravanthisomalaraju@gmail.com"};
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC,CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pizza Delivery Order Details");
        emailIntent.putExtra(Intent.EXTRA_TEXT, orderSummaryMessage);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }



        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        if (intent.resolveActivity(getPackageManager()) !=null){
            startActivity(intent);
        }*/
    }

    private String boolToString(boolean bool) {
        return bool ? (getString(R.string.yes)) : (getString(R.string.no));
    }

    private String createOrderSummary(String userInputName, boolean hasSpinachPanner, boolean hasCheese, boolean hasBbqChicken, boolean hasPepperoni, float price) {
        String orderSummaryMessage = "ORDER DETAILS"+ "\n\n" +getString(R.string.order_summary_name, userInputName) + "\n\n" +
                getString(R.string.order_summary_spinach_panner, boolToString(hasSpinachPanner)) + "\n" +
                getString(R.string.order_summary_cheese, boolToString(hasCheese)) + "\n" +
                getString(R.string.order_summary_bbqChicken, boolToString(hasBbqChicken)) + "\n" +
                getString(R.string.order_summary_pepperoni, boolToString(hasPepperoni)) + "\n\n\n" +
                getString(R.string.order_summary_quantity,quantity) + "\n" +
                getString(R.string.order_summary_total_price, price) + "\n\n" +
                getString(R.string.thank_you);
        return orderSummaryMessage;
    }

    /**
     * Method to calculate the total price
     *
     * @return total Price
     */
    private float calculatePrice(boolean hasSpinachPanner, boolean hasCheese, boolean hasBbqChicken, boolean hasPepperoni) {
        int basePrice = PIZZA_PRICE;
        if (hasSpinachPanner) {
            basePrice += SPINACH_PANNER_PRICE;
        }
        if (hasCheese) {
            basePrice += CHEESE_PRICE;
        }
        if (hasBbqChicken) {
            basePrice += BBQCHICKEN_PRICE;
        }
        if (hasPepperoni) {
            basePrice += PEPPERONI_PRICE;
        }
        return quantity * basePrice;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method increments the quantity of coffee cups by one
     *
     * @param view on passes the view that we are working with to the method
     */

    public void increment(View view) {
        if (quantity < 100) {
            quantity = quantity + 1;
            display(quantity);
        } else {
            Log.i("MainActivity", "Please select less than one hundred cups of coffee");
            Context context = getApplicationContext();
            String lowerLimitToast = getString(R.string.too_much_coffee);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, lowerLimitToast, duration);
            toast.show();
            return;
        }
    }

    /**
     * This method decrements the quantity of coffee cups by one
     *
     * @param view passes on the view that we are working with to the method
     */
    public void decrement(View view) {
        if (quantity > 1) {
            quantity = quantity - 1;
            display(quantity);
        } else {
            Log.i("MainActivity", "Please select atleast one cup of coffee");
            Context context = getApplicationContext();
            String upperLimitToast = getString(R.string.too_little_coffee);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, upperLimitToast, duration);
            toast.show();
            return;
        }
    }
}