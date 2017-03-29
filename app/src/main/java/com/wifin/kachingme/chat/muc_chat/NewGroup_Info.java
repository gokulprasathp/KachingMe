package com.wifin.kachingme.chat.muc_chat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.util.AlertManager;
import com.wifin.kachingme.util.Constant;
import com.wifin.kachingme.util.Log;
import com.wifin.kachingme.util.Utils;
import com.wifin.kachingme.util.cropimage.CropImage;

public class NewGroup_Info extends Fragment {

	Spinner spn_group_type;
	Button btn_next, btn_add_options;
	/* LinearLayout ll_options; */
	public static EditText txt_subject, txt_questions;
	int i = 0;
	public static int group_type = 0;
	public static ArrayList<String> ar_list_option = new ArrayList<String>();
	public static String group_question;
	// public static String group_subject;
	ListView list_option;
	static String TAG = NewGroup_Info.class.getSimpleName();
	ArrayAdapter<String> adapter;
	String[] option_ar;
	ImageView profile_pic;
	private static final int RESULT_CODE_GALLERY = 1, RESULT_CODE_CAMERA = 2,
			REQUEST_CODE_CROP_IMAGE = 3;
	public static byte[] img_byte = null;
	private Uri fileUri;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.newgroup_gropinfo, container, false);

		spn_group_type = (Spinner) v.findViewById(R.id.spn_grp_type);
		btn_next = (Button) v.findViewById(R.id.btn_next);
		btn_add_options = (Button) v.findViewById(R.id.btn_add_options);
		/* ll_options=(LinearLayout)v.findViewById(R.id.ll_options); */
		list_option = (ListView) v.findViewById(R.id.list_option);
		txt_subject = (EditText) v.findViewById(R.id.txt_grp_name);
		txt_questions = (EditText) v.findViewById(R.id.txt_question);
		profile_pic = (ImageView) v.findViewById(R.id.img_profile_pic);
		List<String> list = new ArrayList<String>();
		list.add("Selecciona el tipo de grupo");
		list.add("Grupo simple");

        try {
            Constant.typeFace(getActivity(),btn_next);
            Constant.typeFace(getActivity(),btn_add_options);
            Constant.typeFace(getActivity(),txt_subject);
            Constant.typeFace(getActivity(),txt_questions);

        } catch (Exception e) {
            e.printStackTrace();
        }

		/*
		 * list.add("Broadcast group"); list.add("Yes/No group");
		 * list.add("Like/DisLike group");
		 * list.add("Single answer option group");
		 * list.add("Multi answers option group");
		 */
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.simple_spinner_dropdown, list);
		dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
		spn_group_type.setVisibility(View.GONE);
		txt_questions.setVisibility(View.GONE);

		spn_group_type.setAdapter(dataAdapter);

		spn_group_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				group_type = position;
				group_type--;
				if (group_type == 0) {
					txt_questions.setVisibility(View.GONE);
				} else if (group_type >= 4) {
					txt_questions.setVisibility(View.VISIBLE);
					btn_add_options.setVisibility(View.VISIBLE);
				} else {
					txt_questions.setVisibility(View.VISIBLE);
					btn_add_options.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

        txt_subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 20)
                {
                    Toast.makeText(getActivity(), "Subject Not more than 20 Characters", Toast.LENGTH_SHORT).show();
                    txt_subject.setError("Enter 20 Characters only");
                }
            }
        });

		/*
		 * txt_subject.setOnKeyListener(new OnKeyListener() {
		 * 
		 * @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
		 * if(keyCode == KeyEvent.KEYCODE_BACK ) {
		 * getActivity().onBackPressed(); } else {
		 * Log.d(TAG,"Subject typed..."+txt_subject.getText().toString());
		 * group_subject=txt_subject.getText().toString();
		 * 
		 * } return true; } });
		 */

		profile_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectImage();
				// TODO Auto-generated method stub
				/*
				 * Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				 * photoPickerIntent.setType("image/*"); try{
				 * startActivityForResult(photoPickerIntent, 1); }
				 * catch(ActivityNotFoundException e) { AlertDialog.Builder b =
				 * new AlertDialog.Builder(getActivity());
				 * b.setMessage(getResources
				 * ().getString(R.string.it_seems_like_no_gallery))
				 * .setCancelable(false) ;
				 * b.setNegativeButton(getResources().getString(R.string.Ok),new
				 * DialogInterface.OnClickListener() { public void
				 * onClick(DialogInterface dialog, int id) { dialog.cancel(); }
				 * }); AlertDialog alert = b.create(); alert.show(); }
				 */

			}
		});

		txt_questions.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					getActivity().onBackPressed();
				} else {
					Log.d(TAG, "Question typed..."
							+ txt_questions.getText().toString());
					group_question = txt_questions.getText().toString();
				}
				return true;
			}
		});
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Log.d("TAG",123);
				// Constant.printMsg(123);

				if (txt_subject.getText().toString().trim().length() == 0) {
					new AlertManager().showAlertDialog(
							getActivity(),
							getResources().getString(
									R.string.please_enter_room_subject), true);
				} else if (group_type == -1) {
					new AlertManager().showAlertDialog(
							getActivity(),
							getResources().getString(
									R.string.please_select_group_type), true);
				} else if (txt_questions.getText().toString().trim().length() == 0
						&& group_type > 0) {
					new AlertManager()
							.showAlertDialog(
									getActivity(),
									getResources()
											.getString(
													R.string.please_enter_group_question),
									true);
				}

				else if (group_type >= 4
						&& (ar_list_option.size() == 0 || ar_list_option.size() == 1)) {
					new AlertManager()
							.showAlertDialog(
									getActivity(),
									getResources()
											.getString(
													R.string.please_give_more_than_two_options),
									true);
				} else {
					Log.d(TAG, "Subject::" + txt_subject.getText());
					Log.d(TAG, "Question::" + group_question);
					NewGroup_FragmentActivity.mViewPager.setCurrentItem(1);
				}
			}
		});

		btn_add_options.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setStatus();
				for (String string : ar_list_option) {
					Log.d("NewGroup_Info", "Options::" + string);
				}

			}
		});

		btn_next.setVisibility(View.GONE);
		setHasOptionsMenu(true);
		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		MenuItem item1 = menu.add(0, 1, 0, getActivity().getResources()
				.getString(R.string.next));
		MenuCompat.setShowAsAction(item1, MenuItem.SHOW_AS_ACTION_IF_ROOM);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Log.d("TAG",123);
		// Constant.printMsg(123);
		if (item.getItemId() == 1) {
			if (txt_subject.getText().toString().trim().length() == 0) {
				new AlertManager().showAlertDialog(
						getActivity(),
						getResources().getString(
								R.string.please_enter_room_subject), true);
			} else if (group_type == -1) {
				new AlertManager().showAlertDialog(
						getActivity(),
						getResources().getString(
								R.string.please_select_group_type), true);
			} else if (txt_questions.getText().toString().trim().length() == 0
					&& group_type > 0) {
				new AlertManager().showAlertDialog(
						getActivity(),
						getResources().getString(
								R.string.please_enter_group_question), true);
			}

			else if (group_type >= 4
					&& (ar_list_option.size() == 0 || ar_list_option.size() == 1)) {
				new AlertManager().showAlertDialog(
						getActivity(),
						getResources().getString(
								R.string.please_give_more_than_two_options),
						true);
			} else {
				Log.d(TAG, "Subject::" + txt_subject.getText());
				Log.d(TAG, "Question::" + group_question);
				NewGroup_FragmentActivity.mViewPager.setCurrentItem(1);
			}

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		registerForContextMenu(list_option);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int position;
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		position = (int) info.id;
		switch (item.getItemId()) {
		case R.id.delete:

			ar_list_option.remove(position);
			option_ar = new String[ar_list_option.size()];
			ar_list_option.toArray(option_ar);
			adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, option_ar);
			list_option.setAdapter(adapter);
			adapter.notifyDataSetChanged();

			break;
		case R.id.edit:
			setEdit_option(position);
			break;
		}

		return super.onContextItemSelected(item);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		MenuInflater m = getActivity().getMenuInflater();
		m.inflate(R.menu.menu_context_options, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void setEdit_option(final int index) {

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Edit option");

		final EditText input = new EditText(getActivity());

		input.setText(txt_subject.getText().toString());
		input.setSelectAllOnFocus(true);
		input.setText(ar_list_option.get(index));
		alert.setView(input);

		alert.setPositiveButton(getResources().getString(R.string.Ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();

						ar_list_option.set(index, value);
						option_ar = new String[ar_list_option.size()];
						ar_list_option.toArray(option_ar);
						adapter = new ArrayAdapter<String>(getActivity(),
								android.R.layout.simple_list_item_1, option_ar);
						list_option.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				});

		alert.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	public void setStatus() {

		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

		alert.setTitle("Options");

		final EditText input = new EditText(getActivity());

		input.setSelectAllOnFocus(true);
		alert.setView(input);

		alert.setPositiveButton(getResources().getString(R.string.Ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String value = input.getText().toString();

						if (value != "") {

							/*
							 * LayoutInflater layoutInflater = (LayoutInflater)
							 * getActivity
							 * ().getBaseContext().getSystemService(Context
							 * .LAYOUT_INFLATER_SERVICE); final View addView =
							 * layoutInflater
							 * .inflate(R.layout.add_options_inflater, null);
							 * final TextView textOut =
							 * (TextView)addView.findViewById(R.id.textView1);
							 * textOut.setText(value); ImageButton buttonRemove
							 * =
							 * (ImageButton)addView.findViewById(R.id.button1);
							 * buttonRemove.setOnClickListener(new
							 * OnClickListener(){
							 * 
							 * @Override public void onClick(View v) {
							 * 
							 * ((LinearLayout)addView.getParent()).removeView(
							 * addView); String
							 * str=textOut.getText().toString();
							 * ar_list_option.remove(str); }});
							 */
							/* ll_options.addView(addView); */

							ar_list_option.add(value);
							option_ar = new String[ar_list_option.size()];
							ar_list_option.toArray(option_ar);

							adapter = new ArrayAdapter<String>(getActivity(),
									android.R.layout.simple_list_item_1,
									option_ar);
							list_option.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						}
					}
				});

		alert.setNegativeButton(getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onActivityResult requestCode " + requestCode
				+ " resultCode " + resultCode);
		switch (requestCode) {
		/*
		 * case 1: if(resultCode == getActivity().RESULT_OK) { Uri selectedImage
		 * = data.getData(); String[] filePathColumn =
		 * {MediaStore.Images.Media.DATA}; Cursor cursor =
		 * getActivity().getContentResolver().query(selectedImage,
		 * filePathColumn, null, null, null); cursor.moveToFirst();
		 * 
		 * int columnIndex = cursor.getColumnIndex(filePathColumn[0]); String
		 * filePath = cursor.getString(columnIndex);
		 * //Constant.printMsg("filepath is : "+filePath); cursor.close();
		 * 
		 * Intent intent = new Intent(getActivity(), CropImage.class);
		 * intent.setType("image/*"); intent.putExtra(CropImage.IMAGE_PATH,
		 * filePath); intent.putExtra("outputX", 256);
		 * intent.putExtra("outputY", 256); intent.putExtra("aspectX", 1);
		 * intent.putExtra("aspectY", 1); intent.putExtra("scale", true);
		 * intent.putExtra("return-data", true); startActivityForResult(intent,
		 * REQUEST_CODE_CROP_IMAGE);
		 * 
		 * } break; case REQUEST_CODE_CROP_IMAGE:
		 * if(resultCode==getActivity().RESULT_OK) {
		 * 
		 * final Bundle extras = data.getExtras();
		 * 
		 * if (extras != null) { Bitmap bitmap = extras.getParcelable("data");
		 * 
		 * ByteArrayOutputStream out = new ByteArrayOutputStream();
		 * bitmap.compress(Bitmap.CompressFormat.JPEG, 100,out);
		 * img_byte=out.toByteArray(); profile_pic.setImageBitmap(bitmap); //
		 * new MyAsync_save().execute(); }
		 * 
		 * 
		 * } break;
		 */

		case RESULT_CODE_GALLERY:
			if (resultCode == getActivity().RESULT_OK) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getActivity().getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				final String filePath = cursor.getString(columnIndex);

				cursor.close();
				File file = new File(filePath);

				Intent intent = new Intent(getActivity(), CropImage.class);
				intent.setType("image/*");
				intent.putExtra(CropImage.IMAGE_PATH, filePath);

				intent.putExtra("outputX", 256);
				intent.putExtra("outputY", 256);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				this.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);

				// uploadFile(filePath, true);

			}
			break;
		case RESULT_CODE_CAMERA:
			if (resultCode == getActivity().RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				/*
				 * Toast.makeText(this, "Image saved to:\n" + fileUri.getPath(),
				 * Toast.LENGTH_LONG).show();
				 */
				File file = new File(fileUri.getPath());

				/* upload_profile_pic(fileUri.getPath()); */
				Intent intent = new Intent(getActivity(), CropImage.class);
				intent.setType("image/*");
				intent.putExtra(CropImage.IMAGE_PATH, fileUri.getPath());

				intent.putExtra("outputX", 256);
				intent.putExtra("outputY", 256);
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("scale", true);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
				// uploadFile(fileUri.getPath(), true);

			}
			break;
		case REQUEST_CODE_CROP_IMAGE:
			if (resultCode == getActivity().RESULT_OK) {

				final Bundle extras = data.getExtras();

				if (CropImage.croppedImage != null) {
                    try {
                        Bitmap bitmap = CropImage.croppedImage;
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        Drawable drawable = new BitmapDrawable(bitmap);
                        img_byte = out.toByteArray();
                        profile_pic.setImageDrawable(drawable);

                        CropImage.croppedImage = null;
                    } catch (Exception e) {

                    }

					/*
					 * img_profile.setImageBitmap(bitmap); new
					 * MyAsync_save().execute();
					 */
				}

			}
			break;

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void selectImage() {

		final CharSequence[] options = { "Gallery", "Camera" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Group icon");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Camera")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					fileUri = Utils.getOutputMediaFileUri(1);
					// create a file to save the image
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set
																		// the
																		// image
																		// file
																		// name
					// start the image capture Intent
					startActivityForResult(intent, RESULT_CODE_CAMERA);
					// image_picker(1);
				} else if (options[item].equals("Gallery")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, RESULT_CODE_GALLERY);
					// image_picker(2);

				}
				/*
				 * else if (options[item].equals("Cancel")) { dialog.dismiss();
				 * }
				 */
			}
		});
		builder.show();
	}

	public void onBackPressed() {

		Intent i = new Intent(getActivity(), SliderTesting.class);
		startActivity(i);

	}

}
