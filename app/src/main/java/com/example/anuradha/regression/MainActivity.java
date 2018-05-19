package com.example.anuradha.regression;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.anuradha.regression.models.Regressor;
import com.example.anuradha.regression.models.TensorFlowRegressor;

import com.example.anuradha.regression.recipe_model.Recommend;
import com.example.anuradha.regression.recipe_model.Recommendation;
import com.example.anuradha.regression.recipe_model.TensorflowRecommend;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<Regressor> mClassifiers = new ArrayList<>();
    private List<Recommend> mRecommeders = new ArrayList<>();

    private static final int INPUT_SIZE = 11;
    private TextView mytext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.my_button);
        mytext = (TextView) findViewById(R.id.my_text_view);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadModel();
                clk();
            }
        });
    }

    //creates a model object in memory using the saved tensorflow protobuf model file
    //which contains all the learned weights
    private void loadModel() {
        //The Runnable interface is another way in which you can implement multi-threading other than extending the
        // //Thread class due to the fact that Java allows you to extend only one class. Runnable is just an interface,
        // //which provides the method run.
        // //Threads are implementations and use Runnable to call the method run().
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //add 2 classifiers to our classifier arraylist
                    //the tensorflow classifier and the keras classifier
                    mRecommeders.add(
                            TensorflowRecommend.create(getAssets(), "Keras",
                                    "opt_recipe.pb", "label.txt"
                                    , "embedding_1_input", "embedding_2_input",
                                    "merge_1/ExpandDims"));
                } catch (final Exception e) {
                    //if they aren't found, throw an error!
                    throw new RuntimeException("Error initializing classifiers!", e);
                }
            }
        }).start();
    }

    private void clk(){
//        float input[] = new float[11];
//        input[0] = 3;
//        input[1] = 104809;
//        input[2] = 3;
//        input[3] = 0;
//        input[4] = 231;
//        input[5] = 20480;
//        input[6] = 5392;
//        input[7] = 5392;
//        input[8] = 5393;
//        input[9] = 5393;
//        input[10] = 232;
//
//        //init an empty string to fill with the classification output
//        String text = "";
//        //for each classifier in our array
//        for (Regressor classifier : mClassifiers) {
//            //perform classification on the image
//            float res = classifier.recognize(input);
//            text += res;
//
//            mytext.setText(text);
//        }

        String text = "";
        int id = 0;
        int[] recipeid = new int[] {10,20,35, 49, 50};

        for (Recommend recommend : mRecommeders) {
            for (int i = 0; i < recipeid.length; i++) {
                final Recommendation rec = recommend.recognize(id, recipeid[i]);
                if (rec.getLabel() == null) {
                    text += recommend.name() + ": ?\n";
                } else {
                    //else output its name
                    text += String.format("%s: %s, %f\n", recommend.name(), rec.getLabel(),
                            rec.getConf());
                }
            }
        }

        mytext.setText(text);
    }

}
