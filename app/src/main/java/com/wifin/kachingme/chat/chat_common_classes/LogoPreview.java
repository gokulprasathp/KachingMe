package com.wifin.kachingme.chat.chat_common_classes;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat.single_chat.ChatTest;
import com.wifin.kachingme.emojicons.emojicon.EmojiconEditText;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.adaptors.SmileysAdapter;
import com.wifin.kachingme.util.Emoji;
import com.wifin.kachingme.emojicons.emojicon.EmojiconGridFragment;
import com.wifin.kachingme.emojicons.emojicon.LogoEmojiconsFragment;
import com.wifin.kachingme.emojicons.emojicon.emoji.Emojicon;



public class LogoPreview extends FragmentActivity implements AnimationListener,
		EmojiconGridFragment.OnEmojiconClickedListener,
		LogoEmojiconsFragment.OnEmojiconBackspaceClickedListener {
	private static final int NO_OF_EMOTICONS = 54;
	ImageView mMovingImg;
	TextView mMovingTxt;
	Animation animFadein;
	EmojiconEditText mEditText;
	TextView mViewText;
	AbsoluteLayout mMainLayout;
	Button mPreviewBtn, mEmojiBtn, mEditBtn, mSendBtn;
	int mEditTextId = 0, mViewTextId = 500, mEditTextEdit = 0;
	int mTestId = 500;
	LinearLayout emoticonsCover;
	int mXDistance = 0;
	int mYDistance = 0;
	int mRawXDistance = 0;
	int mRawYDistance = 0;
	boolean mAnimation = false;
	boolean mAnimationImg = false;
	int width = 0;
	int height = 0;
	private int keyboardHeight;
	private PopupWindow popupWindow;
	private View popUpView;
	private boolean isKeyBoardVisible;
	private Bitmap[] emoticons;
	boolean mEditBtnClicked = false;
	boolean mDynamicEdit = false;
	LinearLayout mBtnLayout, emoji_frag;
	Bitmap bp;
	GridView gridview_emo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		initialize();
		screenArrange();

		Bundle extras = getIntent().getExtras();
		byte[] byteArray = extras.getByteArray("Bitmap");

		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0,
				byteArray.length);

		mMovingImg.setImageBitmap(bmp);

		mMovingImg.setDrawingCacheEnabled(true);

		animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.move);

		animFadein.setAnimationListener(this);

		mSendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mEditTextEdit > 1) {
					mEditText = (EmojiconEditText) findViewById(mEditTextEdit);

				}
				if (mAnimation == false) {

					if (mEditBtn.getText().toString().equalsIgnoreCase("Ok")) {
						Toast.makeText(getApplicationContext(),
								"Please click ok button", Toast.LENGTH_SHORT)
								.show();
					} else {
						viewToBitmap(mMainLayout);
						BitmapDrawable bd = new BitmapDrawable(
								viewToBitmap(mMainLayout));
						mMovingImg.setImageDrawable(bd);

						if (mEditTextId >= 1) {
							for (int i = 1; i <= mEditTextId; i++) {
								mEditText = (EmojiconEditText) findViewById(i);
								mViewText = (TextView) findViewById(i + 500);
								System.out
										.println("ID of mEditText:::>>>>" + i);

								mEditText.setVisibility(View.GONE);
								mViewText.setVisibility(View.GONE);

							}
						}

						Drawable myDrawable = mMovingImg.getDrawable();

						Bitmap bmp = ((BitmapDrawable) myDrawable).getBitmap();

						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bmp.compress(Bitmap.CompressFormat.JPEG, 75, stream);
						byte[] byteArray = stream.toByteArray();

						Constant.logo = true;
						Constant.logobit = byteArray;
						Intent in = new Intent(LogoPreview.this, ChatTest.class);
						startActivity(in);
						finish();

					}
				} else {
					for (int i = 1; i <= mEditTextId; i++) {
						mEditText = (EmojiconEditText) findViewById(i);
						mViewText = (TextView) findViewById(i + 500);
						// smEditText.clearAnimation();
						if (mEditText.getText().toString().length() != 0) {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.VISIBLE);
						} else {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.GONE);
						}
					}
					// mEditText.setVisibility(View.GONE);
					mMovingImg.clearAnimation();
					mMainLayout.clearAnimation();

					mAnimation = false;
					mMainLayout.setEnabled(true);
				}
			}
		});

		mEmojiBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mEditTextEdit > 1) {
					mEditText = (EmojiconEditText) findViewById(mEditTextEdit);

				}
				if (mAnimation == false) {

					if (mEditBtn.getText().toString().equalsIgnoreCase("Ok")) {

						if (emoji_frag.getVisibility() == View.VISIBLE) {
							emoji_frag.setVisibility(View.GONE);
							// btn_emo.setImageResource(R.drawable.emoji_btn_normal);
							// ((InputMethodManager)
							// getSystemService(Context.INPUT_METHOD_SERVICE))
							// .toggleSoftInput(
							// InputMethodManager.SHOW_FORCED,
							// InputMethodManager.HIDE_IMPLICIT_ONLY);
						} else {
							for (int i = 1; i <= mEditTextId; i++) {
								mEditText = (EmojiconEditText) findViewById(i);
								mViewText = (TextView) findViewById(i + 500);
								emoji_frag.setVisibility(View.VISIBLE);
								// btn_emo.setImageResource(R.drawable.ic_action_hardware_keyboard);
								((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
										.hideSoftInputFromWindow(
												mEditText.getWindowToken(), 0);

								// mBtnLayout.setX((int) (width * 1.92 / 100));
								// mBtnLayout.setY(height * 85 / 100);
								//
								mMovingImg.setX(width * 30 / 100);
								mMovingImg.setY(height * 0 / 100);
								// AbsoluteLayout.LayoutParams layoutparams1 =
								// ((AbsoluteLayout.LayoutParams) mMainLayout
								// .getLayoutParams());
								// Constant.printMsg("height:::>>>>" + height);
								// Constant.printMsg("width:::>>>>" + width);
								// // layoutparams1.x = (int) (width * 0.92 /
								// 100);
								// // layoutparams1.y = height * 75 / 100;
								// layoutparams1.height = height * 70 / 100;
								// layoutparams1.width = width * 80 / 100;

								// mMainLayout.setLayoutParams(layoutparams1);
								// mMainLayout.setX(width * 20 / 100);
								// mMainLayout.setY(height * 0 / 100);
								mBtnLayout.setVisibility(View.GONE);
								// layoutparams.x = (int) (width * 0.92 / 100);
								// layoutparams.y = height * 75 / 100;
							}
							// btn_emo.set
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"Please click Edit button", Toast.LENGTH_SHORT)
								.show();

					}
				} else {
					for (int i = 1; i <= mEditTextId; i++) {
						mEditText = (EmojiconEditText) findViewById(i);
						mViewText = (TextView) findViewById(i + 500);
						// smEditText.clearAnimation();
						if (mEditText.getText().toString().length() != 0) {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.VISIBLE);
						} else {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.GONE);
						}
					}
					// mEditText.setVisibility(View.GONE);
					mMovingImg.clearAnimation();
					mMainLayout.clearAnimation();
					mAnimation = false;
					mMainLayout.setEnabled(true);
				}
			}
		});

		mEditBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mEditTextEdit > 1) {
					mEditText = (EmojiconEditText) findViewById(mEditTextEdit);

				}
				if (mAnimation == false) {

					if (mEditBtnClicked == false) {
						mEditBtn.setText("OK");
						mEditBtnClicked = true;
						mDynamicEdit = true;
						Constant.printMsg("entered:::::if::::");
						for (int i = 1; i <= mEditTextId; i++) {
							mEditText = (EmojiconEditText) findViewById(i);
							mViewText = (TextView) findViewById(i + 500);
							mEditText.setEnabled(true);
							Constant.printMsg("ID of mEditText:::>>>>" + i);
							mEditText.setVisibility(View.VISIBLE);
							mViewText.setVisibility(View.GONE);
							// mViewText.setVisibility(View.VISIBLE);
						}
					} else {
						Constant.printMsg("entered:::::if::::dsdf");
						mEditBtn.setText("Edit");
						mEditBtnClicked = false;

						if (emoji_frag.getVisibility() == View.VISIBLE) {
							emoji_frag.setVisibility(View.GONE);
							// btn_emo.setImageResource(R.drawable.emoji_btn_normal);
							((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
									.toggleSoftInput(
											InputMethodManager.SHOW_FORCED,
											InputMethodManager.HIDE_IMPLICIT_ONLY);
						}
						for (int i = 1; i <= mEditTextId; i++) {
							mEditText = (EmojiconEditText) findViewById(i);
							mViewText = (TextView) findViewById(i + 500);
							Constant.printMsg("ID of mEditText:::>>>>" + i);
							Constant.printMsg("texttt:::>>>>"
									+ mEditText.getText().toString());
							if (mEditText.getText().toString().length() != 0) {

								mViewText.setText(mEditText.getText()
										.toString().trim());
								mEditText.setVisibility(View.GONE);
								mEditText.setEnabled(false);
								mViewText.setVisibility(View.VISIBLE);

							} else {
								mViewText.setVisibility(View.GONE);
								mEditText.setVisibility(View.GONE);
							}

							// mEditText.setVisibility(View.GONE);
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						}

					}
				} else {
					for (int i = 1; i <= mEditTextId; i++) {
						mEditText = (EmojiconEditText) findViewById(i);
						mViewText = (TextView) findViewById(i + 500);
						// smEditText.clearAnimation();
						if (mEditText.getText().toString().length() != 0) {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.VISIBLE);
						} else {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.GONE);
						}
					}
					// mEditText.setVisibility(View.GONE);
					mMovingImg.clearAnimation();
					mMainLayout.clearAnimation();

					mAnimation = false;
					mMainLayout.setEnabled(true);
				}
			}
		});

		mPreviewBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mAnimation == false) {
					if (mEditBtn.getText().toString().equalsIgnoreCase("Ok")) {

						Toast.makeText(getApplicationContext(),
								"Please click Ok button", Toast.LENGTH_SHORT)
								.show();
					} else {

						for (int i = 1; i <= mEditTextId; i++) {
							mEditText = (EmojiconEditText) findViewById(i);
							mViewText = (TextView) findViewById(i + 500);
							Constant.printMsg("ID of mEditText:::>>>>" + i);
							if (mEditText.getText().toString().length() != 0) {
								mViewText.setText(mEditText.getText()
										.toString().trim());
								mViewText.setVisibility(View.VISIBLE);
								mViewText.startAnimation(animFadein);

							} else {
								mEditText.setVisibility(View.GONE);
								mViewText.setVisibility(View.GONE);

							}
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.GONE);
							mMainLayout.setEnabled(false);
							// mEditText.setEnabled(false);
						}

						mAnimation = true;

						mMovingImg.startAnimation(animFadein);
						mMainLayout.startAnimation(animFadein);

					}
				} else {

					for (int i = 1; i <= mEditTextId; i++) {
						mEditText = (EmojiconEditText) findViewById(i);
						mViewText = (TextView) findViewById(i + 500);
						// smEditText.clearAnimation();
						if (mEditText.getText().toString().length() != 0) {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.VISIBLE);
						} else {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.GONE);
						}
					}
					// mEditText.setVisibility(View.GONE);
					mMainLayout.clearAnimation();
					mMovingImg.clearAnimation();
					mAnimation = false;
					mMainLayout.setEnabled(true);

				}
				// if (mEditTextId == 0 && mAnimation == false) {
				// mMovingImg.startAnimation(animFadein);
				// mAnimation= true;
				// }
			}
		});

		mMainLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				if (mAnimation == false) {

					mXDistance = (int) event.getX();
					mYDistance = (int) event.getY();
					mRawXDistance = (int) v.getRight();
					mRawYDistance = (int) event.getRawY();
					Constant.printMsg("entered:::::touch::::");
					String text = "You click at x = " + mXDistance
							+ " and y = " + mYDistance + "rawX  "
							+ mRawXDistance + "rawy  " + mRawYDistance
							+ "top:::::" + v.getTop() + "bottom:::"
							+ v.getBottom() + "test:::" + event.getRawX()
							+ "test1:::" + event.getRawY() + "test2:::"
							+ mMainLayout.getScrollX() + "test3:::::"
							+ mMainLayout.getScrollY() + "test4::::"
							+ mMainLayout.getScaleX() + "test5:::::"
							+ mMainLayout.getScaleY();
					Constant.printMsg("texttt:::::::::" + text);
					if (mEditBtnClicked == true && mDynamicEdit == true) {
						Constant.printMsg("entered:::touch::if::::");
						addEditText();
						mDynamicEdit = false;

					}
				} else {
					for (int i = 1; i <= mEditTextId; i++) {
						mEditText = (EmojiconEditText) findViewById(i);
						mViewText = (TextView) findViewById(i + 500);
						// smEditText.clearAnimation();
						if (mEditText.getText().toString().length() != 0) {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.VISIBLE);
						} else {
							mViewText.clearAnimation();
							mEditText.setVisibility(View.GONE);
							mViewText.setVisibility(View.GONE);
						}
					}
					// mEditText.setVisibility(View.GONE);
					mMovingImg.clearAnimation();
					mMainLayout.clearAnimation();
					mAnimation = false;
					mMainLayout.setEnabled(true);
				}
				return false;
			}
		});

		gridview_emo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String value = gridview_emo.getAdapter().getItem(position)
						.toString();
				value = mEditText.getText() + value;

				// edt_msg.setText(new Emoji(Chat.this).getSmiledText(value));
			}
		});

		// readEmoticons();
		// enablePopUpView();
		gridview_emo.setAdapter(new SmileysAdapter(new Emoji(this)
				.getArrayList(), LogoPreview.this, new Emoji(this)
				.getEmoticons()));

		// doBindService();
	}

	private void changeKeyboardHeight(int height) {

		if (height > 100) {
			keyboardHeight = height;
			AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, keyboardHeight, 12, 12);
			emoticonsCover.setLayoutParams(params);
		}

	}

	public Bitmap viewToBitmap(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;
	}

	private void screenArrange() {
		// TODO Auto-generated method stub
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		height = metrics.heightPixels;
		width = metrics.widthPixels;

		// LayoutParams params = mMainLayout.generateLayoutParams();
		// params.x = remoteObject.x;
		// params.y = remoteObject.y;
		// mLayout.addView(button, params);

		// AbsoluteLayout.LayoutParams layoutparams1 =
		// ((AbsoluteLayout.LayoutParams) mMainLayout
		// .getLayoutParams());
		// Constant.printMsg("height:::>>>>" + height);
		// Constant.printMsg("width:::>>>>" + width);
		// layoutparams1.x = (int) (width * 0.92 / 100);
		// layoutparams1.y = height * 75 / 100;
		// layoutparams1.height = height * 90 / 100;
		// layoutparams1.width = width * 80 / 100;
		//
		// mMainLayout.setLayoutParams(layoutparams1);

		AbsoluteLayout.LayoutParams layoutparams = ((AbsoluteLayout.LayoutParams) mBtnLayout
				.getLayoutParams());
		Constant.printMsg("height:::>>>>" + height);
		Constant.printMsg("width:::>>>>" + width);
		layoutparams.x = (int) (width * 0.92 / 100);
		layoutparams.y = height * 75 / 100;
		layoutparams.height = height * 10 / 100;
		layoutparams.width = width;

		mBtnLayout.setLayoutParams(layoutparams);

		LinearLayout.LayoutParams params1 = ((LinearLayout.LayoutParams) mEditBtn
				.getLayoutParams());
		params1.height = height * 9 / 100;
		params1.width = width * 23 / 100;
		params1.leftMargin = (int) (width * 1.6 / 100);
		params1.topMargin = height * 2 / 100;
		mEditBtn.setLayoutParams(params1);

		LinearLayout.LayoutParams params2 = ((LinearLayout.LayoutParams) mPreviewBtn
				.getLayoutParams());
		params2.height = height * 9 / 100;
		params2.width = width * 23 / 100;
		params2.leftMargin = (int) (width * 1.6 / 100);
		params2.topMargin = height * 2 / 100;
		mPreviewBtn.setLayoutParams(params2);

		LinearLayout.LayoutParams params3 = ((LinearLayout.LayoutParams) mEmojiBtn
				.getLayoutParams());
		params3.height = height * 9 / 100;
		params3.width = width * 23 / 100;
		params3.leftMargin = (int) (width * 1.6 / 100);
		params3.topMargin = height * 2 / 100;
		mEmojiBtn.setLayoutParams(params3);

		LinearLayout.LayoutParams params4 = ((LinearLayout.LayoutParams) mSendBtn
				.getLayoutParams());
		params4.height = height * 9 / 100;
		params4.width = width * 23 / 100;
		params4.leftMargin = (int) (width * 1.6 / 100);
		params4.topMargin = height * 2 / 100;
		mSendBtn.setLayoutParams(params4);

		AbsoluteLayout.LayoutParams params = ((AbsoluteLayout.LayoutParams) mMovingImg
				.getLayoutParams());
		Constant.printMsg("height:::>>>>" + height);
		Constant.printMsg("width:::>>>>" + width);
		// params.x = width * 22 / 100;
		// params.y = height * 10 / 100;
		params.x = width * 10 / 100;
		params.y = height * 5 / 100;
		params.height = height * 70 / 100;
		params.width = width * 90 / 100;

		// mMovingImg.setLayoutParams(params);
		// mMainLayout.setX(width * 10 / 100);
		// mMainLayout.setY(width * 90 / 100);
		if (width >= 600) {
			mEmojiBtn.setTextSize(16);
			mSendBtn.setTextSize(16);
			mPreviewBtn.setTextSize(16);
			mEditBtn.setTextSize(16);

		} else if (width > 501 && width < 600) {
			mEmojiBtn.setTextSize(15);
			mSendBtn.setTextSize(15);
			mPreviewBtn.setTextSize(15);
			mEditBtn.setTextSize(15);

		} else if (width > 260 && width < 500) {
			mEmojiBtn.setTextSize(14);
			mSendBtn.setTextSize(14);
			mPreviewBtn.setTextSize(14);
			mEditBtn.setTextSize(14);

		} else if (width <= 260) {
			mEmojiBtn.setTextSize(13);
			mSendBtn.setTextSize(13);
			mPreviewBtn.setTextSize(13);
			mEditBtn.setTextSize(13);

		}
	}

	private void readEmoticons() {

		emoticons = new Bitmap[NO_OF_EMOTICONS];
		for (short i = 0; i < NO_OF_EMOTICONS; i++) {
			emoticons[i] = getImage((i + 1) + ".png");
		}

	}

	protected void addEditText() {
		// TODO Auto-generated method stub
		if (mAnimation == false) {

			Constant.printMsg("Entered into addedittext:::>>>>");
			mEditTextId++;
			mViewTextId++;

			Constant.printMsg("mEditText:::>>>>" + mEditTextId);

			mEditText = new EmojiconEditText(this);
			mEditText.setTextColor(Color.BLACK);
			mEditText.setHint("TypeHere");
			mEditText.setId(mEditTextId);

			mEditText.setBackgroundColor(Color.WHITE);
			mEditText.setX(mXDistance);
			mEditText.setY(mYDistance);
			mEditText.setTextSize(16);
			mEditText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
					| InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
			// InputType.TYPE_CLASS_TEXT
			mEditText.setBottom(0);
			mEditText.setTop(0);
			mEditText.setRight(0);
			mEditText.setLeft(0);

			mViewText = new TextView(this);
			mViewText.setTextColor(Color.WHITE);
			mViewText.setHint("test");
			mViewText.setId(mViewTextId);
			mViewText.setBottom(-5);
			mViewText.setTop(-7);
			mViewText.setRight(0);
			mViewText.setLeft(-12);

			mViewText.setX(mXDistance);
			mViewText.setY(mYDistance);
			mViewText.setTextSize(16);

			mMainLayout.addView(mEditText);
			mMainLayout.addView(mViewText);
			mViewText.setVisibility(View.GONE);

			mEditText.requestFocus();

			if (width >= 600) {
				mEditText.setTextSize(16);
				mViewText.setTextSize(16);

			} else if (width > 501 && width < 600) {

				mEditText.setTextSize(15);
				mViewText.setTextSize(15);

			} else if (width > 260 && width < 500) {

				mEditText.setTextSize(14);
				mViewText.setTextSize(14);

			} else if (width <= 260) {

				mEditText.setTextSize(13);
				mViewText.setTextSize(13);

			}
		}

		mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

				if (hasFocus) {
					int editId = v.getId();
					mEditTextEdit = editId;

				}

			}

		});

	}

	private Bitmap getImage(String path) {
		AssetManager mngr = getAssets();
		InputStream in = null;
		try {
			in = mngr.open("emoticons/" + path);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Bitmap temp = BitmapFactory.decodeStream(in, null, null);
		Constant.printMsg("path of emoji:::>>>>" + in);
		return temp;
	}

	private void initialize() {
		// TODO Auto-generated method stub
		mMainLayout = (AbsoluteLayout) findViewById(R.id.moving_layout);
		mMovingImg = (ImageView) findViewById(R.id.move_image);
		mEditBtn = (Button) findViewById(R.id.okay_button);
		mSendBtn = (Button) findViewById(R.id.send_button);
		mPreviewBtn = (Button) findViewById(R.id.preview_button);
		mEmojiBtn = (Button) findViewById(R.id.emoji_button);
		emoticonsCover = (LinearLayout) findViewById(R.id.footer_for_emoticons);
		mBtnLayout = (LinearLayout) findViewById(R.id.btn_layout);
		emoji_frag = (LinearLayout) findViewById(R.id.emoji_frag);
		gridview_emo = (GridView) findViewById(R.id.gridview_emo);

	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEmojiconBackspaceClicked(View v) {
		// TODO Auto-generated method stub
		Constant.printMsg("idd::::;" + mEditTextEdit);
		// for (int i = 1; i <= mEditTextId; i++) {
		if (mEditTextEdit > 1) {
			mEditText = (EmojiconEditText) findViewById(mEditTextEdit);
			LogoEmojiconsFragment.backspace(mEditText);

		} else {
			LogoEmojiconsFragment.backspace(mEditText);

		}

		// mViewText = (TextView) findViewById(i + 500);
		// }
	}

	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		// TODO Auto-generated method stub
		// for (int i = 1; i <= mEditTextId; i++) {
		if (mEditTextEdit > 1) {
			mEditText = (EmojiconEditText) findViewById(mEditTextEdit);
			// mViewText = (TextView) findViewById(i + 500);
			LogoEmojiconsFragment.input(mEditText, emojicon);
		} else {
			LogoEmojiconsFragment.input(mEditText, emojicon);

		}
		// }

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		Constant.printMsg("called back press:::::::::::");
		if (mBtnLayout.getVisibility() == View.GONE) {
			mBtnLayout.setVisibility(View.VISIBLE);
			mMovingImg.setX(width * 10 / 100);
			mMovingImg.setY(height * 5 / 100);
			// AbsoluteLayout.LayoutParams layoutparams1 =
			// ((AbsoluteLayout.LayoutParams) mMainLayout
			// .getLayoutParams());
			// Constant.printMsg("height:::>>>>" + height);
			// Constant.printMsg("width:::>>>>" + width);
			// // layoutparams1.x = (int) (width * 0.92 / 100);
			// // layoutparams1.y = height * 75 / 100;
			// layoutparams1.height = height;
			// layoutparams1.width = width * 80 / 100;
			//
			// mMainLayout.setLayoutParams(layoutparams1);
			if (emoji_frag.getVisibility() == View.VISIBLE) {
				emoji_frag.setVisibility(View.GONE);
				// btn_emo.setImageResource(R.drawable.emoji_btn_normal);
				// ((InputMethodManager)
				// getSystemService(Context.INPUT_METHOD_SERVICE))
				// .toggleSoftInput(InputMethodManager.SHOW_FORCED,
				// InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		} else {
			finish();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}
