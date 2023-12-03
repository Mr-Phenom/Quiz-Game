package com.company.quizgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;

public class Quiz_page extends AppCompatActivity {
    TextView time,correct,wrong;
    TextView questions,a,b,c,d;
    Button next,finish;

    FirebaseDatabase database  = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child("Questions");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference databaseReference = database.getReference();

    String quizQuestion;
    String quizAnswerA;
    String quizAnswerB;
    String quizAnswerC;
    String quizAnswerD;
    String quizCorrectAnswer;
    int questionCount;
    int questionNumber =1;
    String userAnswer;
    int userCorrect =0;
    int userIncorrect =0;

    CountDownTimer countDownTimer;
    private static  long TOTAL_TIME = 30000;
    boolean timerCountinue;
    long timeLeft = TOTAL_TIME;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);
        startTimer();

        time = findViewById(R.id.textViewTime);
        correct = findViewById(R.id.textViewCorrect);
        wrong = findViewById(R.id.textViewWrong);
        questions = findViewById(R.id.textViewQuestion);
        a = findViewById(R.id.textViewA);
        b = findViewById(R.id.textViewB);
        c = findViewById(R.id.textViewC);
        d = findViewById(R.id.textViewD);
        next = findViewById(R.id.buttonNext);
        finish = findViewById(R.id.buttonFinish);

        game();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game();

            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendScore();

                Intent i = new Intent(Quiz_page.this,Score_Page.class);
                startActivity(i);
                finish();
                
            }
        });

        a.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                a.setClickable(false);
                b.setClickable(false);
                c.setClickable(false);
                d.setClickable(false);

                userAnswer ="a";

                if(userAnswer.equals(quizCorrectAnswer))
                {
                    a.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    correct.setText(String.valueOf(userCorrect));
                }
                else
                {
                    a.setBackgroundColor(Color.RED);
                    userIncorrect++;
                    wrong.setText(String.valueOf(userIncorrect));
                    findAnswer();
                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                a.setClickable(false);
                b.setClickable(false);
                c.setClickable(false);
                d.setClickable(false);


                userAnswer ="b";
                if(userAnswer.equals(quizCorrectAnswer))
                {
                    b.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    correct.setText(String.valueOf(userCorrect));
                }
                else
                {
                    b.setBackgroundColor(Color.RED);
                    userIncorrect++;
                    wrong.setText(String.valueOf(userIncorrect));
                    findAnswer();
                }
            }
        });
        c.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                a.setClickable(false);
                b.setClickable(false);
                c.setClickable(false);
                d.setClickable(false);


                userAnswer ="c";

                if(userAnswer.equals(quizCorrectAnswer))
                {
                    c.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    correct.setText(String.valueOf(userCorrect));
                }
                else
                {
                    c.setBackgroundColor(Color.RED);
                    userIncorrect++;
                    wrong.setText(String.valueOf(userIncorrect));
                    findAnswer();
                }
            }
        });
        d.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                a.setClickable(false);
                b.setClickable(false);
                c.setClickable(false);
                d.setClickable(false);


                userAnswer ="d";
                if(userAnswer.equals(quizCorrectAnswer))
                {
                    d.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                    correct.setText(String.valueOf(userCorrect));
                }
                else
                {
                    d.setBackgroundColor(Color.RED);
                    userIncorrect++;
                    wrong.setText(String.valueOf(userIncorrect));
                    findAnswer();
                }
            }
        });

    }
    public void game()
    {


        a.setBackgroundColor(Color.WHITE);
        b.setBackgroundColor(Color.WHITE);
        c.setBackgroundColor(Color.WHITE);
        d.setBackgroundColor(Color.WHITE);

        a.setClickable(true);
        b.setClickable(true);
        c.setClickable(true);
        d.setClickable(true);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                questionCount = (int)dataSnapshot.getChildrenCount();

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                quizQuestion = dataSnapshot.child(String.valueOf(questionNumber)).child("q").getValue().toString();
                quizCorrectAnswer = dataSnapshot.child(String.valueOf(questionNumber)).child("ans").getValue().toString();
                quizAnswerA = dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
                quizAnswerB = dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
                quizAnswerC = dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
                quizAnswerD = dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();

                questions.setText(quizQuestion);
                a.setText(quizAnswerA);
                b.setText(quizAnswerB);
                c.setText(quizAnswerC);
                d.setText(quizAnswerD);

               if(questionNumber<questionCount)
               {
                   questionNumber++;
               }
               else
               {
                   Toast.makeText(Quiz_page.this, "All done!", Toast.LENGTH_SHORT).show();
                   next.setClickable(false);
                   //pauseTimer();
               }
               

            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(Quiz_page.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void findAnswer()
    {
        if(quizCorrectAnswer.equals("a"))
        {
            a.setBackgroundColor(Color.GREEN);
        }
        if(quizCorrectAnswer.equals("b"))
        {
            b.setBackgroundColor(Color.GREEN);
        }
        if(quizCorrectAnswer.equals("c"))
        {
            c.setBackgroundColor(Color.GREEN);
        }
        if(quizCorrectAnswer.equals("d"))
        {
            d.setBackgroundColor(Color.GREEN);
        }
    }
    public void startTimer()
    {
        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {

                timerCountinue =false;
                pauseTimer();
                questions.setText("Sorry times up");

                sendScore();
            }
        }.start();
        timerCountinue = true;
    }

    public void resetTimer()
    {
        timeLeft = TOTAL_TIME;
        updateCountDownText();

    }

    public  void updateCountDownText()
    {
        int seconds = (int)(timeLeft/1000)%60;
        time.setText(""+seconds);
    }

    public void pauseTimer()
    {
        countDownTimer.cancel();
        timerCountinue =false;
    }

    public void sendScore()
    {

        String userUId = user.getUid();
        databaseReference.child("scores").child(userUId).child("correct").setValue(userCorrect)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(Quiz_page.this, "Score sent successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
        databaseReference.child("scores").child(userUId).child("wrong").setValue(userIncorrect);


    }
}