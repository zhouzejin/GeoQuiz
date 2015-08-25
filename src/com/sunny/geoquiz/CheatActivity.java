package com.sunny.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {
	
	public static final String EXTRA_ANSWER_IS_TRUE = 
			"com.sunny.geoquiz.answer_is_true";
	public static final String EXTRA_ANSWER_SHOWN = 
			"com.sunny.geoquiz.answer_shown";
	
	private boolean mAnswerIsTrue;
	
	private TextView mAnswerTextView;
	private Button mShowAnswerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);
		
		// Answer will not be shown until the user presses the button
		setAnswerShownResult(false);
		
		// 取出从前一个Activity传递的数据
		mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
		
		mAnswerTextView = (TextView) findViewById(R.id.answer_textview);
		
		mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
		mShowAnswerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mAnswerIsTrue) {
					mAnswerTextView.setText(R.string.true_button);
				} else {
					mAnswerTextView.setText(R.string.false_button);
				}
				
				setAnswerShownResult(true); // 用户按下了显示答案的按钮，告诉父Activity该用户作弊
			}
		});
	}
	
	private void setAnswerShownResult(boolean isAnswerShown) {
		Intent data = new Intent();
		data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
		setResult(RESULT_OK, data); // 设置返回父Activity的数据
	}

}
