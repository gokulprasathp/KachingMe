package com.wifin.kachingme.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wifin.kaching.me.ui.R;
import com.wifin.kachingme.applications.KachingMeApplication;
import com.wifin.kachingme.chat_home.SliderTesting;
import com.wifin.kachingme.database.DatabaseHelper;
import com.wifin.kachingme.database.Dbhelper;
import com.wifin.kachingme.pojo.CartDetailsDto;
import com.wifin.kachingme.pojo.LoginGetSet;
import com.wifin.kachingme.pojo.RestUserDetailsDto;
import com.wifin.kachingme.registration_and_login.Signin;
import com.wifin.kachingme.services.ContactLastSync;
import com.wifin.kachingme.services.TempConnectionService;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.vcardtemp.VCardManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommonMethods {
	Context context;
	ProgressDialog progressdialog;
//	SharedPreferences preference;
    DatabaseHelper dbAdapter;
    byte[] imageByte = null;
    SharedPreferences preference;
    Dbhelper dbHelper;

    public CommonMethods(Context con){
		this.context = con;
        dbAdapter = KachingMeApplication.getDatabaseAdapter();
        dbHelper = new Dbhelper(context);
        preference = this.context.getSharedPreferences(KachingMeApplication.getPereference_label(),
                Activity.MODE_PRIVATE);

    }
	public boolean isJSONValid(String test) {
		try {
			try {
				new JSONObject(test);
			} catch (JSONException ex) {
				// edited, to include @Arthur's comment
				// e.g. in case JSONArray is valid as well...
				try {
					new JSONArray(test);
				} catch (JSONException ex1) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
    public void stopAsyncTask(final AsyncTask asyncTask, final ProgressDialog progressDialog) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Constant.printMsg("commonmethod timeout error......" + asyncTask.getStatus() + "....." +
                        AsyncTask.Status.RUNNING);
                if (asyncTask != null) {
                    if (asyncTask.getStatus() == AsyncTask.Status.PENDING) {
                        Constant.printMsg("commonmethod timeout error....CANCEL STATE...");
                        asyncTask.cancel(true);
                        Constant.printMsg("commonmethod timeout error....CHECK CANCEL STATE..." +
                                asyncTask.getStatus() + "...." + asyncTask.isCancelled());
                        if (progressDialog != null && progressDialog.isShowing() && asyncTask.isCancelled()){
                            progressDialog.cancel();
                            Toast.makeText(context, "Network Error.!Please try again later!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Constant.printMsg("commonmethod timeout error NOTHING to CANCEL...");
                    }
                }
            }
        }, 22000);
    }

	public  void okDialogBox(Context contex, String msg) {

		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setMessage(msg);
		builder1.setCancelable(true);
		builder1.setPositiveButton(
				"OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
//
//			builder1.setNegativeButton(
//					"No",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int id) {
//							dialog.cancel();
//						}
//					});
		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

//	private boolean result;
//	public boolean yes_no_dialog(Context contex) {
//		AlertDialog.Builder builder = new AlertDialog.Builder(contex);
//		builder.setTitle("Erase hard drive")
//				.setMessage("Are you sure?")
//				.setIcon(android.R.drawable.ic_dialog_alert)
//				.setPositiveButton("Yes",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//												int which) {
//								// Yes button clicked, do something
//								result = true;
//							}
//						})
//				.setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						result = false;
//					}
//				}) // Do nothing on no
//				.show();
//		return result;
//	}

	public void Toast_call(Context contex, String message) {
		Toast.makeText(contex, message, Toast.LENGTH_SHORT).show();
	}
    public void autoTimeEnable() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, android.R.style.Theme_Material_Light_Dialog_Alert);
        // alertDialogBuilder.setTitle("Your Title");
        alertDialogBuilder.setMessage("Please Enable Automatic Date and Time")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        context.startActivity(new Intent(
                                android.provider.Settings.ACTION_DATE_SETTINGS));
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
	public void showAlertDialog(Context context, String message, Boolean status) {
//		AlertDialog.Builder  alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).;
//
//		// Setting Dialog Title
//		alertDialog.setTitle("Kaching.Me");
//
//		// Setting Dialog Message
//		alertDialog.setMessage(message);
//
//		if (status != null)
//			// Setting alert dialog icon
//			// alertDialog.setIcon((status) ? R.drawable.success :
//			// R.drawable.fail);
//
//			// Setting OK Button
//			alertDialog.setButton(
//					context.getResources().getString(R.string.ok),
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//
//						}
//					});
//
//		// Showing Alert Message
//		alertDialog.show();
//
		AlertDialog.Builder alertDialogBuilder  = new AlertDialog.Builder(new ContextThemeWrapper
				(context, android.R.style.Theme_Holo_Light_Dialog));
		alertDialogBuilder
				//.setTitle(context.getResources().getString(R.string.app_name))
				.setMessage(Html.fromHtml("<font color=#232323>"+message+"</font>"))
				.setCancelable(false)
				.setPositiveButton(Html.fromHtml("<font color=#db0606>"+"OK"+"</font>"),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public static void getToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}


	/**
	 * Utility function for decoding an image resource. The decoded bitmap will
	 * be optimized for further scaling to the requested destination dimensions
	 * and scaling logic.
	 *
	 * @param res
	 *            The resources object containing the image data
	 * @param resId
	 *            The resource id of the image data
	 * @param dstWidth
	 *            Width of destination area
	 * @param dstHeight
	 *            Height of destination area
	 * @param scalingLogic
	 *            Logic to use to avoid image stretching
	 * @return Decoded bitmap
	 */
	public static Bitmap decodeResource(Resources res, int resId, int dstWidth,
										int dstHeight, ScalingLogic scalingLogic) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateSampleSize(options.outWidth,
				options.outHeight, dstWidth, dstHeight, scalingLogic);
		Bitmap unscaledBitmap = BitmapFactory.decodeResource(res, resId,
				options);

		return unscaledBitmap;
	}

	public static Bitmap decodeFile(String path, int dstWidth, int dstHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false;
		options.inSampleSize = calculateSampleSize(options.outWidth,
				options.outHeight, dstWidth, dstHeight, ScalingLogic.FIT);
		Bitmap unscaledBitmap = BitmapFactory.decodeFile(path, options);

		return unscaledBitmap;
	}


	public static Bitmap createScaledBitmap(Bitmap unscaledBitmap,
											int dstWidth, int dstHeight) {
		Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight,
				ScalingLogic.FIT);
		Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(),
				unscaledBitmap.getHeight(), dstWidth, dstHeight,
				ScalingLogic.FIT);
		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(),
				dstRect.height(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(
				Paint.FILTER_BITMAP_FLAG));

		return scaledBitmap;
	}

	/**
	 * ScalingLogic defines how scaling should be carried out if source and
	 * destination image has different aspect ratio.
	 *
	 * CROP: Scales the image the minimum amount while making sure that at least
	 * one of the two dimensions fit inside the requested destination area.
	 * Parts of the source image will be cropped to realize this.
	 *
	 * FIT: Scales the image the minimum amount while making sure both
	 * dimensions fit inside the requested destination area. The resulting
	 * destination dimensions might be adjusted to a smaller size than
	 * requested.
	 */
	public enum ScalingLogic {
		CROP, FIT
	}

	/**
	 * Calculate optimal down-sampling factor given the dimensions of a source
	 * image, the dimensions of a destination area and a scaling logic.
	 *
	 * @param srcWidth
	 *            Width of source image
	 * @param srcHeight
	 *            Height of source image
	 * @param dstWidth
	 *            Width of destination area
	 * @param dstHeight
	 *            Height of destination area
	 * @param scalingLogic
	 *            Logic to use to avoid image stretching
	 * @return Optimal down scaling sample size for decoding
	 */
	public static int calculateSampleSize(int srcWidth, int srcHeight,
										  int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcWidth / dstWidth;
			} else {
				return srcHeight / dstHeight;
			}
		} else {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return srcHeight / dstHeight;
			} else {
				return srcWidth / dstWidth;
			}
		}
	}

	/**
	 * Calculates source rectangle for scaling bitmap
	 *
	 * @param srcWidth
	 *            Width of source image
	 * @param srcHeight
	 *            Height of source image
	 * @param dstWidth
	 *            Width of destination area
	 * @param dstHeight
	 *            Height of destination area
	 * @param scalingLogic
	 *            Logic to use to avoid image stretching
	 * @return Optimal source rectangle
	 */
	public static Rect calculateSrcRect(int srcWidth, int srcHeight,
										int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.CROP) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				final int srcRectWidth = (int) (srcHeight * dstAspect);
				final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
				return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth,
						srcHeight);
			} else {
				final int srcRectHeight = (int) (srcWidth / dstAspect);
				final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
				return new Rect(0, scrRectTop, srcWidth, scrRectTop
						+ srcRectHeight);
			}
		} else {
			return new Rect(0, 0, srcWidth, srcHeight);
		}
	}

	/**
	 * Calculates destination rectangle for scaling bitmap
	 *
	 * @param srcWidth
	 *            Width of source image
	 * @param srcHeight
	 *            Height of source image
	 * @param dstWidth
	 *            Width of destination area
	 * @param dstHeight
	 *            Height of destination area
	 * @param scalingLogic
	 *            Logic to use to avoid image stretching
	 * @return Optimal destination rectangle
	 */
	public static Rect calculateDstRect(int srcWidth, int srcHeight,
										int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
		if (scalingLogic == ScalingLogic.FIT) {
			final float srcAspect = (float) srcWidth / (float) srcHeight;
			final float dstAspect = (float) dstWidth / (float) dstHeight;

			if (srcAspect > dstAspect) {
				return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
			} else {
				return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
			}
		} else {
			return new Rect(0, 0, dstWidth, dstHeight);
		}
	}

	public static String humanReadableByteCount(long bytes, boolean si) {
		Constant.printMsg("filee scale " + bytes + "   " + si);
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

    /**
     * added by siva for contact sync annd get status from php
     */
    public void syncDataForStatus(final String strPrimary, final byte[] photo_ts, final String strSecondary) {
        Constant.printMsg("Service get called...primary......." + strPrimary);
        Constant.printMsg("Service get called...secondary......." + strSecondary);
//        RequestParams request_params = new RequestParams();
//        request_params.put("Wquery", "'SELECT * FROM tbl_user WHERE jid in ("
//                + strPrimary + ")'");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(600000);
        client.get(KachingMeConfig.USER_GETDATA_PHP+
                        "?Wcolumn=jid &Wvalue=" + strPrimary + KachingMeApplication.getHost() + "&Wtable=tbl_user ",
                null,
                new AsyncHttpResponseHandler(Looper.getMainLooper()) {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        // TODO Auto-generated method stub
                        Constant.printMsg("Service Failure Response::"
                                + new String(arg2));
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        // TODO Auto-generated method stub
                        ObjectMapper mapper = new ObjectMapper();
                        Constant.printMsg("Service Success Response::"
                                + new String(arg2));
                        String status = null;
                        String response = new String(arg2);
                        if (response != null && response.length() > 0) {
                            if (!response.equals(0)) {
                                CommonMethods com = new CommonMethods(context);
                                if (com.isJSONValid(response.trim())) {
                                    Constant.printMsg("Service Success Response Valid.........::");
                                    JSONArray jarry;
                                    JSONObject jsonObject;
                                    try {
                                        jarry = new JSONArray(response);
                                        for (int i = 0; i<jarry.length(); i++) {
                                            jsonObject = new JSONObject(jarry.get(i).toString());
                                            status = jsonObject.getString("status");
                                            Constant.printMsg("User status.............." + status);
                                        }
//                                        if (strSecondary != null) {
//                                            dbAdapter.updateInsertedContacts(
//                                                    strSecondary + KachingMeApplication.getHost(),
//                                                    strSecondary, photo_ts, status);
//                                        } else {
//                                            dbAdapter.updateInsertedContacts(
//                                                    strPrimary + KachingMeApplication.getHost(),
//                                                    strPrimary, photo_ts, status);
//
//                                        }
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                } else {
                                    Constant.printMsg("Service Success Response InValid.........::");
//                                    if (strSecondary != null) {
//                                        dbAdapter.updateInsertedContacts(
//                                                strSecondary + KachingMeApplication.getHost(),
//                                                strSecondary, photo_ts, status);
//                                    } else {
//                                        dbAdapter.updateInsertedContacts(
//                                                strPrimary + KachingMeApplication.getHost(),
//                                                strPrimary, photo_ts, status);
//
//                                    }
                                }
                            } else {
//                                if (strSecondary != null) {
//                                    dbAdapter.updateInsertedContacts(
//                                            strSecondary + KachingMeApplication.getHost(),
//                                            strSecondary, photo_ts, status);
//                                } else {
//                                    dbAdapter.updateInsertedContacts(
//                                            strPrimary + KachingMeApplication.getHost(),
//                                            strPrimary, photo_ts, status);
//
//                                }
                            }
                        } else {
//                            if (strSecondary != null) {
//                                dbAdapter.updateInsertedContacts(
//                                        strSecondary + KachingMeApplication.getHost(),
//                                        strSecondary, photo_ts, status);
//                            } else {
//                                dbAdapter.updateInsertedContacts(
//                                        strPrimary + KachingMeApplication.getHost(),
//                                        strPrimary, photo_ts, status);
//
//                            }
                        }

//                        JSONArray jarry;
//                        ArrayList<App_Users> ar_list = new ArrayList<App_Users>();
//                        try {
//                            jarry = new JSONArray(new String(response));
////							ar_list = mapper.readValue(
////									jarry.toString(),
////									mapper.getTypeFactory()
////											.constructCollectionType(
////													List.class, App_Users.class));
//                        } catch (Exception e) {
//                            // TODO: handle exception
//                        }
//
//                        for (int i = 0; i < ar_list.size(); i++) {
//                            App_Users user = ar_list.get(i);
//                            ContactsGetSet contects = dbAdapter
//                                    .getContact(user.jid);
//                            if (contects != null) {
//                                contects.setIs_niftychat_user(1);
//                                contects.setStatus(user.status);
//                                contects.setPhoto_ts(user.avatar);
//                                contects.setNifty_name(user.name);
//                                contects.setNifty_email(user.email);
////								dbAdapter.setDeleteContact(contects.getJid());
////								dbAdapter.insertContacts(contects);
//
//                            }
//
//                        }
////						editor.putBoolean(Constant.CONTACT_GROUP_SYNC_STATUS,
////								true);
////						editor.commit();
////
////						Intent intn = new Intent("Add_New_Contact");
////						sendBroadcast(intn);
////
////						Intent intent1 = new Intent(Contact_server.this,
////								Vcard_Sync_Service.class);
////						startService(intent1);//siva
                    }
                });
    }

//    public void updateDataForKachingUser(final String strPrimary, final byte[] photo_ts, String status, final String strSecondary) {
//        Constant.printMsg("photo insert called..........."+photo_ts);
//        if (strSecondary != null) {
//            dbAdapter.updateInsertedContacts(
//                    strSecondary + KachingMeApplication.getHost(),
//                    strSecondary, photo_ts, status);
//        } else {
//            dbAdapter.updateInsertedContacts(
//                    strPrimary + KachingMeApplication.getHost(),
//                    strPrimary, photo_ts, status);
//
//        }
//    }

    public byte[] getImagefromUrl(final String url) {
//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
                byte[] imageBlob = null;
                Constant.printMsg("image url....." + url);
                try {
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(url);
                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    int imageLength = (int) (entity.getContentLength());
                    InputStream is = entity.getContent();
                    imageBlob = new byte[imageLength];
                    int bytesRead = 0;
                    while (bytesRead < imageLength) {
                        int n = is.read(imageBlob, bytesRead, imageLength - bytesRead);
                        if (n <= 0)
                            ; // do some error handling
                        bytesRead += n;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageByte=imageBlob;
//            }
//        });
//        thread.start();
        return imageByte;
    }

    public  class getImagefromUrlAsy extends AsyncTask<String, Void, byte[]> {
        String primaryNumber,secondaryNumber,status;

        protected byte[] doInBackground(String... urls) {
            byte[] imageBlob = new byte[0];
            primaryNumber=urls[0];
            status=urls[2];
            secondaryNumber=urls[3];
            Constant.printMsg("image url....." + urls[1]);
            try {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(urls[1]);
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                int imageLength = (int) (entity.getContentLength());
                InputStream is = entity.getContent();
                imageBlob = new byte[imageLength];
                int bytesRead = 0;
                while (bytesRead < imageLength) {
                    int n = is.read(imageBlob, bytesRead, imageLength - bytesRead);
                    if (n <= 0)
                        ; // do some error handling
                    bytesRead += n;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return imageBlob;
        }

        protected void onPostExecute(byte[] byteImage) {
            Constant.printMsg("result for conver imagview............" + byteImage);
//            if (secondaryNumber!= null) {
            dbAdapter.updateInsertedPhotoByte(
                    primaryNumber + KachingMeApplication.getHost(),byteImage);
            if (Constant.isFirebaseBitmap){
                Constant.isFirebaseBitmap=false;
                Intent userchat_broadcast = new Intent("update_profile");
                context.sendBroadcast(userchat_broadcast);
            }
//            } else {
//                dbAdapter.updateInsertedContacts(
//                        primaryNumber + KachingMeApplication.getHost(),
//                        primaryNumber, byteImage, status);
//            }
        }
    }

    /**Login Process Methods*/

    public void loginUserProcess() {
        // String status="Available";
        dbAdapter.insertLogin(preference.getString("MyPrimaryNumber", ""),
                preference.getString("pin", ""), "",
                "", "", null);
        Constant.printMsg("login GFSKJHLKJDSLN" + dbAdapter.getLogin().size());
        Constant.mIsLogin = true;
        new getLoginVcard().execute();

//        byte[] av = null;
//        String status = "", fname = null, email = null;
//        try {
//            Constant.printMsg("login GFSKJHLKJDSLN.......vc....");
//            VCard vc = VCardManager.getInstanceFor(
//                    TempConnectionService.connection).loadVCard();
//            // vc.load(connection);
//            Constant.printMsg("login GFSKJHLKJDSLN........"+vc);
//            Constant.printMsg("login email::" + vc.getEmailWork());
//            Constant.printMsg("login FirstName()::" + vc.getFirstName());
//            Constant.printMsg("login Status::" + vc.getField("SORT-STRING"));
//            status = vc.getField("SORT-STRING");
//            fname = vc.getFirstName();
//            email = vc.getEmailWork();
//            av = new AvatarManager().saveBitemap(vc.getAvatar());
//        } catch (Exception e) {
//            // ACRA.getErrorReporter().handleException(e);
//            e.printStackTrace();
//            Constant.printMsg("Exception....." + e);
//        }
        Constant.printMsg("login GFSKJHLKJDSLN.....end...");
    }

    public class getLoginVcard extends AsyncTask<String, String, VCard> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressdialog = ProgressDialog.show(context, context.getResources()
                            .getString(R.string.please_wait),
                    context.getResources().getString(R.string.loading), false);
            progressdialog.show();
        }

        protected VCard doInBackground(String... params) {
            // TODO Auto-generated method stub
            VCard vc = null;
            try {
                Constant.printMsg("login GFSKJHLKJDSLN.......vc....");
//                ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp", new VCardProvider());
                vc = VCardManager.getInstanceFor(
                        TempConnectionService.connection).loadVCard();
                // vc.load(connection);
                Constant.printMsg("login GFSKJHLKJDSLN........" + vc);
            } catch (Exception e) {
                // ACRA.getErrorReporter().handleException(e);
                e.printStackTrace();
                Constant.printMsg("login Exception....." + e);
            }
            Constant.printMsg("login GFSKJHLKJDSLN final result........." + vc);
            return vc;
        }

        protected void onPostExecute(VCard result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progressDialog.dismiss();
            byte[] av = null;
            String status = "", fname = null, email = null;
            if (result != null) {
                Constant.printMsg("login email::" + result.getEmailWork());
                Constant.printMsg("login FirstName()::" + result.getFirstName());
                Constant.printMsg("login Status::" + result.getField("SORT-STRING"));
                status = result.getField("SORT-STRING");
                fname = result.getFirstName();
                email = result.getEmailWork();
                av = new AvatarManager().saveBitemap(result.getAvatar());

                try {
                    Presence presence = new Presence(
                            Presence.Type.available);
                    presence.setStatus(status);
                    // Send the packet (assume we have a Connection
                    // instance called "con").
                    TempConnectionService.connection.sendStanza(presence);
                } catch (Exception e) {
                    // ACRA.getErrorReporter().handleException(e);
                    e.printStackTrace();
                }

                KachingMeApplication.setUserID(preference.getString("MyPrimaryNumber", ""));
                KachingMeApplication.setNifty_name(fname);
                KachingMeApplication.setNifty_email(email);
                KachingMeApplication.setAvatar(av);
                KachingMeApplication.setAcra();
                LoginGetSet loginDataUpdate = new LoginGetSet();
                loginDataUpdate.setNifty_email(email);
                loginDataUpdate.setNifty_name(fname);
                loginDataUpdate.setAvatar(av);
                loginDataUpdate.setStatus(status);
                loginDataUpdate.setUserName(preference.getString("MyPrimaryNumber", ""));
                dbAdapter.setUpdateLogin(loginDataUpdate);
                // dbAdapter.insertLogin(sp.getString("MyPrimaryNumber",
                // ""),
                // txt_password.getText().toString(), status,
                // fname, email, av);
                File dir = new File(Constant.local_database_dir);
                File[] files = dir.listFiles();
                File file = null;
                try {
                    Arrays.sort(
                            files,
                            LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
                    for (int i = 0; i < files.length; i++) {
                        file = files[i];
                        System.out.printf(
                                "File %s - %2$tm %2$te,%2$tY%n= ",
                                file.getName(), file.lastModified());
                        SimpleDateFormat date_format = new SimpleDateFormat(
                                "hh:mma ,dd/MM/yyyy");
                        Date dt = new Date(file.lastModified());
                        String date = date_format.format(dt);

                    }
                } catch (Exception e) {
                    // //ACRA.getErrorReporter().handleException(e);
                    // TODO: handle exception
                }
                //String mobile = mMobileno.getText().toString();
                String mobile = preference.getString("MyPrimaryNumber", "");
                new getCartDetails().execute(mobile);
                // if (Constant.freebieflag == true) {
                if (file != null) {
                    // new
                    // encry_decry(signin.this).decodeFile(file);
                    context.startService(new Intent(context, ContactLastSync.class));

//                                Intent intent = new Intent(Signin.this,
//                                        RestoreBackup.class);
//                                startActivity(intent);
//                                finish();
                    context.startActivity(new Intent(context, SliderTesting.class));
//                finish();
                    ((Activity) context).finish();
                } else {
                    /**in future need the dialog  but noe its not shown*/
//                                AlertDialog.Builder builder = new AlertDialog.Builder(
//                                        Signin.this);
//                                builder.setTitle(getResources().getString(R.string.restore_conversation_from_cloud))
//                                        .setMessage(getResources().getString(R.string.are_you_want_to_restore_conversation))
//                                        .setIcon(android.R.drawable.ic_dialog_alert)
//                                        .setPositiveButton(getResources().getString(R.string.yes),
//                                                new DialogInterface.OnClickListener() {
//
//                                                    @Override
//                                                    public void onClick(
//                                                            DialogInterface dialog,
//                                                            int which) {
//                                                        // TODO Auto-generated
//                                                        startService(new Intent(Signin.this, ContactLastSync.class));
//                                                        Intent intent = new Intent(Signin.this, RestoreBackupCloud.class);
//                                                        startActivity(intent);
//                                                        finish();
//                                                    }
//                                                })
//                                        .setNegativeButton(
//                                                getResources().getString(
//                                                        R.string.no),
//                                                new DialogInterface.OnClickListener() {
//
//                                                    @Override
//                                                    public void onClick(
//                                                            DialogInterface dialog,
//                                                            int which) {
//                                                        // TODO Auto-generated
//                                                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Signin.this);
//                                                        Editor editor = sp.edit();
//                                                        editor.putString("activity_name", "SliderTesting");
//                                                        editor.putBoolean("decline", false);
//                                                        editor.commit();
//
//                                                        SharedPreferences sps = Signin.this.getSharedPreferences(KachingMeApplication.getPereference_label(),
//                                                                Activity.MODE_PRIVATE);
//                                                        Editor editor1 = sps.edit();
//                                                        editor1.putString("activity_name", "SliderTesting");
//                                                        editor1.putBoolean("decline", false);
//                                                        editor1.commit();
//
//                                                        stopService(new Intent(Signin.this, TempConnectionService.class));
//                                                        startService(new Intent(Signin.this, TempConnectionService.class));
//                                                        startService(new Intent(Signin.this, ContactLastSync.class));
//                                                        startActivity(new Intent(Signin.this, SliderTesting.class));
//                                                        finish();
//
//                                                    }
//
//                                                }).show();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("activity_name", "SliderTesting");
                    editor.putBoolean("decline", false);
                    editor.commit();
                    SharedPreferences sps = context.getSharedPreferences(KachingMeApplication.getPereference_label(),
                            Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sps.edit();
                    editor1.putString("activity_name", "SliderTesting");
                    editor1.putBoolean("decline", false);
                    editor1.commit();
                    //context.stopService(new Intent(context, TempConnectionService.class));
                    //context.startService(new Intent(context, TempConnectionService.class));
                    Signin.isUserExist = false;
                    context.startService(new Intent(context, ContactLastSync.class));
                    context.startActivity(new Intent(context, SliderTesting.class));
                    ((Activity) context).finish();
                }
            } else {
                Toast.makeText(context,"Login Failed.!! Try Again!!",Toast.LENGTH_SHORT).show();
            }
            progressdialog.cancel();
        }
    }

    public class getCartDetails extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = null;
            HttpConfig ht = new HttpConfig();
            String mob = params[0];
            result = ht.httpget(KachingMeConfig.GET_CARTDETAILS + mob);
            Constant.printMsg("result dis get cart"
                    + KachingMeConfig.GET_CARTDETAILS + mob + "    " + result);
            return result;
        }

        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progressDialog.dismiss();
            if (result != null && result.length() > 0) {
                try {
                    JSONObject jar = new JSONObject(result);
                    RestUserDetailsDto cartlocalmail = new RestUserDetailsDto();
                    List<CartDetailsDto> cartlocal = new ArrayList<CartDetailsDto>();
                    cartlocalmail.setBux(jar.getLong("bux"));
                    Long bx = jar.getLong("bux");
                    Constant.bux = bx;
                    Constant.printMsg("buxxx:::" + jar.getLong("bux"));
                    cartlocalmail.setUserId(jar.getLong("userId"));
                    Long usid = jar.getLong("userId");
                    Constant.userId = usid;
                    cartlocalmail.setFreeBeeFlag(jar.getBoolean("freeBeeFlag"));
                    boolean flag = jar.getBoolean("freeBeeFlag");
                    Constant.freebieflag = flag;
                    JSONArray jar1 = jar.getJSONArray("cartDetailsDtos");
                    Constant.printMsg("jar:;" + jar + "   " + jar1.length()
                            + "  " + jar.length());
                    for (int j = 0; j < jar1.length(); j++) {
                        JSONObject jObject4 = jar1.getJSONObject(j);
                        CartDetailsDto m = new CartDetailsDto();
                        // m.setUserId(jObject4.getLong("userId"));
                        m.setCartDetailsId(jObject4.getLong("cartDetailsId"));
                        // m.setPhoneNumber(jObject4.getString("phoneNumber"));
                        m.setOfferId(jObject4.getLong("offerId"));
                        m.setType(jObject4.getInt("type"));
                        m.setDescription(jObject4.getString("description"));
                        m.setPhotoPath(jObject4.getString("photoPath"));
                        m.setBux(jObject4.getLong("bux"));
                        m.setQrCodePath(jObject4.getString("qrCodePath"));
                        // m.setIsDelete(jObject4.getInt("isDelete"));
                        m.setMerchantId(jObject4.getString("merchantId"));
                        m.setCompanyLogoPath(jObject4
                                .getString("companyLogoPath"));
                        m.setMerchantName(jObject4.getString("websiteName"));
                        m.setDiscountPrice(jObject4.getString("discountPrice"));
                        m.setOfferPrice(jObject4.getString("offerPrice"));
                        m.setValidity(jObject4.getString("valid_until"));
                        Constant.printMsg("list Dtyo::" + m.getPhotoPath());
                        cartlocal.add(m);
                        SharedPreferences.Editor e = preference.edit();
                        e.putString("ExpiryDate", jObject4.getString("valid_until"));
                        e.commit();
                        ContentValues cv = new ContentValues();
                        cv.put("phonenumber", jObject4.getString("phoneNumber"));
                        cv.put("deelid", jObject4.getLong("offerId"));
                        cv.put("type", jObject4.getInt("type"));
                        cv.put("desc", jObject4.getString("description"));
                        cv.put("photopath", jObject4.getString("photoPath"));
                        cv.put("bux", jObject4.getLong("bux"));
                        cv.put("qrpath", jObject4.getString("qrCodePath"));
                        cv.put("merchantid", jObject4.getString("merchantId"));
                        cv.put("merchantimagepath",
                                jObject4.getString("companyLogoPath"));
                        //cv.put("merchantname",
                        //       jObject4.getString("merchantName"));
                        cv.put("discount", jObject4.getString("discountPrice"));
                        cv.put("offer", jObject4.getString("offerPrice"));
                        cv.put("merchantname",
                                jObject4.getString("websiteName"));
                        cv.put("companyname", jObject4.getString("companyName"));
                        cv.put("item", jObject4.getString("description"));
                        cv.put("prodname", jObject4.getString("freebeeName"));
                        cv.put("validity", jObject4.getString("valid_until"));
                        insertDB(cv);
                    }
                    cartlocalmail.setCartDetailsDtos(cartlocal);
                    Constant.restlistmain.add(cartlocalmail);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (Constant.bux != null) {
                    ContentValues cv = new ContentValues();
                    cv.put("bux", Constant.bux);
                    cv.put("phonenumber", Constant.userId);
                    insertDBBux(cv);

                    SharedPreferences.Editor e = preference.edit();
                    e.putLong("buxvalue", Constant.bux);
                    e.putLong("uservalue", Constant.userId);
                    e.commit();

                    Constant.point = Long.parseLong("0");
                    SharedPreferences.Editor e1 = preference.edit();
                    e1.putLong("donationpoint", Constant.point);
                    e1.commit();
                }
            } else {
                // Toast.makeText(context, "Network Error!Please try again later!",
                //        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void insertDB(ContentValues v) {
        try {
            int a = (int) dbHelper.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_CART, null, v);
            Constant.printMsg("No of inserted rows in shop details :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in new shop details ::::::" + e.toString());
        } finally {
            dbHelper.close();
        }
    }

    public void insertDBBux(ContentValues v) {

        try {
            int a = (int) dbHelper.open().getDatabaseObj()
                    .insert(Dbhelper.TABLE_BUX, null, v);

            Constant.printMsg("No of inserted rows in shop details :::::::::" + a);
        } catch (SQLException e) {
            Constant.printMsg("Sql exception in new shop details ::::::"
                    + e.toString());
        } finally {
            dbHelper.close();
        }
    }
}
