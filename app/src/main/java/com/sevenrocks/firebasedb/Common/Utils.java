package com.sevenrocks.firebasedb.Common;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.sevenrocks.firebasedb.BeanClasses.LotBean;
import com.sevenrocks.firebasedb.DataBase.DB;
import com.sevenrocks.firebasedb.R;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;


public class Utils {

	// Copy to sdcard for debug use
	public static void copyDatabase(Context c) {

		Log.v("CopyDataBase",""+c.getDatabasePath("sevenrocks.sqlite"));
		Log.v("CopyDataBase",""+c.getPackageName());
		File Db = new File(String.valueOf(c.getDatabasePath("sevenrocks.sqlite")));
		Date d = new Date();

		File file = new File(Environment.getExternalStorageDirectory()+"/sevenrocks.sqlite");
		file.setWritable(true);

		try {
			copyFile(new FileInputStream(Db), new FileOutputStream(file));
		} catch (IOException e) {
			Log.v("CopyDataBase",""+e);
		}
	}

	public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
		FileChannel fromChannel = null;
		FileChannel toChannel = null;
		try {
			fromChannel = fromFile.getChannel();
			toChannel = toFile.getChannel();
			fromChannel.transferTo(0, fromChannel.size(), toChannel);
		} finally {
			try {
				if (fromChannel != null) {
					fromChannel.close();
				}
			} finally {
				if (toChannel != null) {
					toChannel.close();
				}
			}
		}
	}

	public static  String ReadFile(){
		String line = null;
		File file = new File(Environment.getExternalStorageDirectory() +
				File.separator + "test.txt");
		try {
			FileInputStream fileInputStream = new FileInputStream (file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();

			while ( (line = bufferedReader.readLine()) != null )
			{
				stringBuilder.append(line + System.getProperty("line.separator"));
			}
			fileInputStream.close();
			line = stringBuilder.toString();

			bufferedReader.close();
		}
		catch(FileNotFoundException ex) {
			Log.d("IOException1", ex.getMessage());
			return  "";
		}
		catch(IOException ex) {
			Log.d("IOException", ex.getMessage());
			return  "";
		}
		return line;
	}

	public static void intertValueInDB(DB db)throws Exception{

		 /*  try {
            Utils.intertValueInDB(new DB(this));
            Utils.copyDatabase(this);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

		db.truncate("lot");
		db.truncate("bundle");
		db.truncate("piece");
		String lotNo="";
		String bundleNo="";
		String pieceNo="";
		JSONObject jsonObject=new JSONObject(ReadFile());
		JSONObject MainObjcet=jsonObject.getJSONObject("BundleNumbering");


		/*Iterator<?> keys = MainObjcet.keys();
		while( keys.hasNext() ) {

			 lotNo = (String)keys.next();
			Log.v("interValueINDB","lotNo "+lotNo);

		}*/

			Iterator<String> keys = MainObjcet.keys();
			while (keys.hasNext()) {
				lotNo=keys.next();

				try{
					Log.v("interValueINDB","XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXlotNo "+lotNo);
					JSONObject lotJson=MainObjcet.getJSONObject(lotNo);
					Iterator<String> lotkeys = lotJson.keys();
					while(lotkeys.hasNext()){
						bundleNo=lotkeys.next();
						try{
							Log.v("interValueINDB","XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXbundleNo "+bundleNo);

							JSONObject bundleJson=lotJson.getJSONObject(bundleNo);
							HashMap<String,String> map=new HashMap();

							if(lotJson.has("lotCount"))
							map.put("lotCount",lotJson.getString("lotCount"));

							if(lotJson.has("scanStatus"))
							map.put("scanStatus",lotJson.getString("scanStatus"));

							if(lotJson.has("styleCode"))
							map.put("styleCode",lotJson.getString("styleCode"));

							map.put("lotNo",lotNo);
							map.put("bundleNo",bundleNo);
							db.insert("lot",map);

							Iterator<String> bundlekeys = bundleJson.keys();

							while (bundlekeys.hasNext()){
								pieceNo=bundlekeys.next();
								Log.v("interValueINDB","pieceNo "+pieceNo);
								if(isInteger(pieceNo)){
								HashMap<String,String> map1=new HashMap();
								map1.put("peiceNo",pieceNo);

								if(bundleJson.has("destCount"))
								map1.put("destCount",bundleJson.getString("destCount"));

								if(bundleJson.has("folding"))
								map1.put("folding",bundleJson.getString("folding"));

								if(bundleJson.has("fromLocation"))
								map1.put("fromLocation",bundleJson.getString("fromLocation"));

								if(bundleJson.has("scanStatus"))
								map1.put("scanStatus",bundleJson.getString("scanStatus"));

								if(bundleJson.has("pieceCount"))
								map1.put("pieceCount",bundleJson.getString("pieceCount"));

								if(bundleJson.has("purpose"))
								map1.put("purpose",bundleJson.getString("purpose"));

								if(bundleJson.has("tailor"))
								map1.put("tailor",bundleJson.getString("tailor"));

								if(bundleJson.has("sourceCount"))
								map1.put("sourceCount",bundleJson.getString("sourceCount"));

								if(bundleJson.has("toLocation"))
								map1.put("toLocation",bundleJson.getString("toLocation"));

								if(bundleJson.has("size"))
								map1.put("sizes",bundleJson.getString("size"));

								if(bundleJson.has("overlock"))
								map1.put("overlock",bundleJson.getString("overlock"));

								map1.put("lotNo",lotNo);
								map1.put("bundleNo",bundleNo);
								db.insert("bundle",map1);

										JSONObject pieceJson=bundleJson.getJSONObject(pieceNo);
										HashMap<String,String> map2=new HashMap();

										if (pieceJson.has("status"))
											map2.put("status",pieceJson.getString("status"));

										if (pieceJson.has("shoulder"))
											map2.put("shoulder",pieceJson.getString("shoulder"));

										if (pieceJson.has("scanStatus"))
											map2.put("scanStatus",pieceJson.getString("scanStatus"));

										if (pieceJson.has("scanDate")){
											String newDate=pieceJson.getString("scanDate").replace("\"","").replace("\\/","-");
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                                Date date = sdf.parse(newDate);
                                                long millis = date.getTime();
                                                map2.put("scanDate",millis+"");

										}

										if (pieceJson.has("packDate"))
											map2.put("packDate",pieceJson.getString("packDate"));

										if (pieceJson.has("length"))
											map2.put("lengths",pieceJson.getString("length"));

										if (pieceJson.has("chest"))
											map2.put("chest",pieceJson.getString("chest"));

										map2.put("lotNo",lotNo);
										map2.put("bundleNo",bundleNo);
										map2.put("peiceNo",pieceNo);

										db.insert("piece",map2);
									}


							}
						}catch (Exception er){
							Log.v("interValueINDB","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"+lotNo+" "+bundleNo+" "+er);
						}
					}
				}catch (Exception e){
					Log.v("interValueINDB","EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"+lotNo+" "+bundleNo+" "+e);
				}



			}



	}

	public static void insertintoIndexTable(DB db){
		db.truncate("p_index");
		String query="select chest,peiceNo,scanDate from piece where scanStatus='\"Scanned\"'";
		Cursor cur=db.findCursor(query);
		if(cur!=null && cur.moveToNext()){
			for(int i=0;i<cur.getCount();i++){
				String chest=cur.getString(cur.getColumnIndex("chest"));
				String peiceNo=cur.getString(cur.getColumnIndex("peiceNo"));
				String scanDate=cur.getString(cur.getColumnIndex("scanDate"));
				String overlock=getIdByColumn("bundle","overlock","peiceNo",peiceNo,db);
				HashMap<String ,String > map=new HashMap();
				map.put("chest",chest);
				map.put("pieceNo",peiceNo);
				map.put("scanDate",scanDate);
				map.put("overlock",overlock);
				db.insert("p_index",map);
				cur.moveToNext();
			}
			cur.close();
		}
	}

	public static String getIdByColumn(String table_name , String column_name, String condition,String condition_name ,	DB db ) {

		try
		{
			Cursor cur = null;
			String item_query = " SELECT "+ column_name +" FROM " + table_name + " WHERE "+condition+" = "+'"'+condition_name+'"' ;

			cur = db.findCursor(item_query);
			if (cur != null && cur.moveToNext()) {
				return cur.getString(cur.getColumnIndex(column_name));
			}
			cur.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return "";
	}

	public static boolean isInteger(String input) {
		try
		{
			Integer.parseInt( input );
			return true;
		}
		catch( Exception e)
		{
			return false;
		}
	}

	public static void navigate(Activity from, Class to,boolean isBack) {
		Intent intent = new Intent( from, to );
		from.startActivity(intent);
		if(!isBack) from.overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
		else from.overridePendingTransition(R.anim.move_right_in_activity, R.anim.move_left_out_activity);
		from.finish();

	}

	public static ArrayList<LotBean> getLotData(DB db) {

		ArrayList<LotBean> lotList = new ArrayList();

		Cursor cur = null;

		String item_query = " select * from lot group by lotNo order by lotNo ASC";


		cur = db.findCursor(item_query);


		if (cur != null && cur.moveToNext()) {

			for (int i = 0; i < cur.getCount(); i++) {

				LotBean lotBean=new LotBean();

				lotBean.setLotno(cur.getString(cur.getColumnIndex(DB.Table.lot.lotNo.toString())));
				lotBean.setLotCount(cur.getString(cur.getColumnIndex(DB.Table.lot.lotCount.toString())));
				lotBean.setBundleNo(cur.getString(cur.getColumnIndex(DB.Table.lot.bundleNo.toString())));
				lotBean.setScanStatus(cur.getString(cur.getColumnIndex(DB.Table.lot.scanStatus.toString())));
				lotBean.setStyleCode(cur.getString(cur.getColumnIndex(DB.Table.lot.styleCode.toString())));

				lotList.add(lotBean);
				cur.moveToNext();

			}


		}

		cur.close();

		return lotList;

	}

	public static  String isNullValue(String value){
		try{
			Log.v("",value);
			return value;
		}catch (Exception e){
			return  "NA";
		}
	}

	public static int getNoOfValue(String beginDate,String endDate,String chestValue,String overlockValue,Preferences pref,DB db){
		String query="select * from p_index where CAST(scanDate AS integer) between CAST('"+beginDate+"' AS integer) and CAST('"+endDate+"' AS integer) and chest='"+chestValue+"' and overlock='"+overlockValue+"'";
		return db.findCursor(query).getCount();

	}

	public static int getTotalNo(String beginDate,String endDate,String overlockValue ,Preferences pref,DB db){
		String query="select * from p_index where CAST(scanDate AS integer) between CAST('"+beginDate+"' AS integer) and CAST('"+endDate+"' AS integer) and overlock='"+overlockValue+"'";
		return db.findCursor(query).getCount();

	}

	public static double roundTwoDecimals(double d)
	{
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}


}
