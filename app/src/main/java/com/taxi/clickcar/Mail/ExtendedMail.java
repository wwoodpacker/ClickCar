package com.taxi.clickcar.Mail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taxi.clickcar.R;

public class ExtendedMail  {

	int SELECTION = 3;
	
	Context mainContext;
	
	String title;
	String text;
	String from;
	String where;
	String attach;
	
    public ExtendedMail(Context context,String _text,String _title){
		title=_title;
		text=_text;
		mainContext=context;
	}

     public void Run() {

		 attach = "";
		 sender_mail_async async_sending = new sender_mail_async();
		 async_sending.execute();
	 }

        


    
    private class sender_mail_async extends AsyncTask<Object, String, Boolean> {
    	ProgressDialog WaitingDialog;

		@Override
		protected void onPreExecute() {
			WaitingDialog = ProgressDialog.show(mainContext, mainContext.getString(R.string.send), mainContext.getString(R.string.send_mail), true);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			WaitingDialog.dismiss();
			Toast.makeText(mainContext, mainContext.getString(R.string.success_mail), Toast.LENGTH_LONG).show();
			//((Activity)mainContext).finish();
		}

		@Override
		protected Boolean doInBackground(Object... params) {

			try {

				
				from = "woodpacker370@gmail.com";
				where = "woodpacker370@gmail.com";
				
                MailSenderClass sender = new MailSenderClass("woodpacker370@gmail.com", "t380974576950");
                
                sender.sendMail(title, text, from, where, attach);
			} catch (Exception e) {
				Toast.makeText(mainContext, "Error send!", Toast.LENGTH_SHORT).show();
			}
			
			return false;
		}
	}
}