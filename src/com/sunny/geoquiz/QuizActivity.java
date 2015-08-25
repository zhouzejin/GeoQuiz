package com.sunny.geoquiz;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
	
	private static final String KEY_INDEX = "index";
	
	private Button mTrueButton;
	private Button mFalseButton;
	private ImageButton mNextImageButton;
	private ImageButton mPrevImageButton;
	private TextView mQuestionTextView;
	private Button mCheatButton;
	
	private TrueFalse[] mQuestionBank = new TrueFalse[] {
		new TrueFalse(R.string.question_oceans, true), 
		new TrueFalse(R.string.question_mideast, false), 
		new TrueFalse(R.string.question_africa, false), 
		new TrueFalse(R.string.question_americas, true), 
		new TrueFalse(R.string.question_asia, true), 
	};
	
	private int mCurrentIndex = 0;
	private boolean mIsCheater = false;

	@TargetApi(11) // 使用注解向Android Lint声明版本信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		// API11及以上才支持ActionBar，这里需要限定条件
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setSubtitle("Bodies of Water");
		}
		
		// 取出销毁前保存的数据来初始化界面的数据
		if (savedInstanceState != null) {
			mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
		}
		
		initView();
		updateQuestion();
	}

	private void initView() {
		mQuestionTextView = (TextView) findViewById(R.id.question_textview);
		mQuestionTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				updateQuestion();
			}
		});
		
		mTrueButton = (Button) findViewById(R.id.true_button);
		mTrueButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkAnswer(true);
			}
		});
		
		mFalseButton = (Button) findViewById(R.id.false_button);
		mFalseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkAnswer(false);
			}
		});
		
		mNextImageButton = (ImageButton) findViewById(R.id.next_button);
		mNextImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
				mIsCheater = false; // 进行下一题，清除上一题查看答案的状态
				updateQuestion();
			}
		});
		
		mPrevImageButton = (ImageButton) findViewById(R.id.prev_button);
		mPrevImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) 
						% mQuestionBank.length;
				updateQuestion();
			}
		});
		
		mCheatButton = (Button) findViewById(R.id.cheat_button);
		mCheatButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
				boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
				intent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
				// startActivity(intent);
				startActivityForResult(intent, 0);
			}
		});
	}
	
	/**
	 * 更新显示问题的TextView
	 */
	private void updateQuestion() {
		int question = mQuestionBank[mCurrentIndex].getQuestion();
		mQuestionTextView.setText(question);
	}
	
	/**
	 * 检查用户选择的答案
	 * @param userPressedTrue
	 */
	private void checkAnswer(boolean userPressedTrue) {
		boolean ansserIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
		int messageResId = 0;
		
		if (mIsCheater) {
			if (ansserIsTrue == userPressedTrue) {
				messageResId = R.string.judgment_toast;
			} else {
				messageResId = R.string.incorrect_judgement_toast;
			}
		} else {
			if (ansserIsTrue == userPressedTrue) {
				messageResId = R.string.correct_toast;
			} else {
				messageResId = R.string.incorrect_toast;
			}
		}
		
		Toast.makeText(this, messageResId, Toast.LENGTH_LONG)
			.show();
	}
	
	/**
	 * 在销毁Activity前保存数据，可以在横屏时加载保存的数据，从而使横屏后的界面上的数据与竖屏保持一致
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putInt(KEY_INDEX, mCurrentIndex);
	}
	
	/**
	 * 处理子Activity返回的结果
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
