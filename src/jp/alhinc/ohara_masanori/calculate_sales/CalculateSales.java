package jp.alhinc.ohara_masanori.calculate_sales;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CalculateSales {
	public static void main(String[] args) {

		//HashMap branchMapを作成
		HashMap<String,String>branchNameMap = new HashMap<String,String>();
		HashMap<String,Long>branchSaleMap = new HashMap<String,Long>();
		BufferedReader br = null;

		if(args.length != 1){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		try{
			//branch.lst作成
			File dir = new File(args[0],"branch.lst");
			FileReader fr = new FileReader(dir);
			br = new BufferedReader(fr);
			String branchNamedata ;
			while((branchNamedata = br.readLine()) != null) {

				//データの分割
				String[] items = branchNamedata.split(",",-1);
				Long money =0l;
				if(!items[0].matches("\\d{3}") || (items.length !=2)){
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;

				}
				branchNameMap.put(items[0],items[1]);
				branchSaleMap.put(items[0],money);
			}

		} catch (FileNotFoundException e) {
			System.out.println("支店定義ファイルが存在しません");
			return;

		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;

		} finally {

			try {
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		//commodity.list作成
		HashMap<String,String>commodityNameMap = new HashMap<String,String>();
		HashMap<String,Long>commoditySaleMap = new HashMap<String,Long>();

		try{

			File dir = new File(args[0],"commodity.lst");
			br = new BufferedReader(new FileReader(dir));

			String commodityNameData ;
			Long money =0l;
			while((commodityNameData = br.readLine()) != null) {

				String[] items = commodityNameData.split(",",-1);

				if(!items[0].matches("^[a-zA-Z0-9]{8}$") ||(items.length !=2)){
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}

				commodityNameMap.put(items[0],items[1]);
				commoditySaleMap.put(items[0],money);
			}

		}catch (FileNotFoundException e){
			System.out.println("商品定義ファイルが存在しません");
			return;

		}catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;

		}finally{
			try{
				if(br != null) {
					br.close();
				}
			}catch (IOException e){
				e.printStackTrace();
				return;
			}
		}

		//売上げファイル
		File dir = new File(args[0]);
		File[] files =dir.listFiles();
		ArrayList<File> rcdFiles = new ArrayList<>();
		for(int i = 0; i < files.length ; i++ ){
			if(files[i].getName().matches("\\d{8}.rcd$$") && (files[i].isFile())){
				rcdFiles.add(files[i]);
			}
		}
		Collections.sort(rcdFiles);

		for(int i = 1; i <rcdFiles.size() ; i++ ){
			String str1 = new String(rcdFiles.get(i).getName());
			String str2 = str1.substring(0,8);
			int rcdi =Integer.parseInt(str2);
			String str3 = new String(rcdFiles.get(i - 1).getName());
			String str4 = str3.substring(0,8);
			int rcdi2 =Integer.parseInt(str4);
			if(rcdi != rcdi2 +1){
				System.out.println("売上ファイル名が連番になっていません");
				return;
			}
		}

		for(int i = 0; i < rcdFiles.size() ; i++ ){
			try{

				FileReader fr = new FileReader(rcdFiles.get(i));
				br = new BufferedReader(fr);
				String strshop;
				ArrayList<String> shops = new ArrayList<String>();

				//rcdFile読み込み
				while((strshop = br.readLine()) != null){
					shops.add(strshop);
				}
				if(shops.size() !=3){
					System.out.println(rcdFiles.get(i).getName() + "のフォーマットが不正です");
					return;
				}
				if(!branchSaleMap.containsKey(shops.get(0))){

					System.out.println(rcdFiles.get(i).getName() + "の支店コードが不正です");
					return;
				}
				if(!commoditySaleMap.containsKey(shops.get(1))){

					System.out.println(rcdFiles.get(i).getName() + "の商品コードが不正です");
					return;
				}

				//合計金額（branchSaleMap）
				branchSaleMap.get(shops.get(0));
				if(!shops.get(2).matches("\\d+$")){
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
				long amount = Long.parseLong(shops.get(2));
				long shopsale = branchSaleMap.get(shops.get(0));
				long pulsamount = (amount + shopsale);
				if( pulsamount > 9999999999l ){

					System.out.println("合計金額が10桁を超えました");
					return;
				}

				branchSaleMap.put(shops.get(0),pulsamount);

				//合計金額（commoditySaleMap）
				commoditySaleMap.get(shops.get(1));
				long amount2 = commoditySaleMap.get(shops.get(1));
				 pulsamount = (amount + amount2);
				if (pulsamount > 9999999999l){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				commoditySaleMap.get(shops.get(1));
				commoditySaleMap.put(shops.get(1),pulsamount);

			}catch (FileNotFoundException e){
					System.out.println("予期せぬエラーが発生しました");
					return;

			}catch (IOException e) {
					System.out.println(e);
			}finally{
				try{
					if(br != null) {
						br.close();
					}
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}

		List<Map.Entry<String,Long>> branchSaleList =
		new ArrayList<Map.Entry<String,Long>>(branchSaleMap.entrySet());
		Collections.sort(branchSaleList, new Comparator<Map.Entry<String,Long>>() {
			@Override
			public int compare(Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		BufferedWriter bw = null;
		try{
			File file = new File(args[0], "branch.out");
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			for (Entry<String,Long>s :branchSaleList){
				bw.write(s.getKey() + "," + branchNameMap.get(s.getKey()) + "," + s.getValue());
				bw.newLine();
			}
		} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
		}finally{
			try {
				if(bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}

		List<Map.Entry<String,Long>> commodityList =
		new ArrayList<Map.Entry<String,Long>>(commoditySaleMap.entrySet());
		Collections.sort(commodityList, new Comparator<Map.Entry<String,Long>>() {
			@Override
			public int compare(Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		try{
			File file = new File(args[0], "commodity.out");
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			for (Entry<String,Long>s :commodityList){
				bw.write(s.getKey() + "," + commodityNameMap.get(s.getKey()) + "," + s.getValue());
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました");
			return;
		} finally{
			try {
				if(bw != null) {
					bw.close();
				}
			} catch (IOException e) {

				System.out.println("予期せぬエラーが発生しました");
				return;
			}
		}
	}
}

