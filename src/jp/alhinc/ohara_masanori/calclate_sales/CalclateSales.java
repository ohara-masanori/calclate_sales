package jp.alhinc.ohara_masanori.calclate_sales;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CalclateSales {
	public static void main(String[] args) {

		//branch.listのデータを分割する。
		HashMap<String,String>branchNameMap = new HashMap<String,String>();
		HashMap<String,Long>branchSaleMap = new HashMap<String,Long>();
		try{

			File dir = new File(args[0],"branch.lst");
			FileReader fr = new FileReader(dir);     //Fire（引数）からFileReaderをオブジェクトを作成。
			BufferedReader br = new BufferedReader(fr);      //FileReader（引数）BRオブジェクトを作成。

			String branchNamedata ;

			while((branchNamedata = br.readLine()) != null) {

				//データの分割
				String[] items = branchNamedata.split(",",-1);
				Long money =0l;

				items[0].matches("\\d{3}");
				if(!items[0].matches("\\d{3}") &&(items.length !=2)){
					System.out.println("支店定義ファイルのフォーマットが不正です。");

					 return;
				}

				branchNameMap.put(items[0],items[1]);
				branchSaleMap.put(items[0],money);
			}
			br.close();		//finalyに変換する。

		}catch (FileNotFoundException e){     //ファイル自体があるのか確認。
			System.out.println("支店定義ファイルが存在しません。");

		}catch (IOException e) {       //キーボード故障確認
			System.out.println("予期せぬエラーが発生しました。");
		}

		//commondity.lstのデータを分割する。
		HashMap<String,String>commondityNameMap = new HashMap<String,String>();
		HashMap<String,Long>commonditySaleMap = new HashMap<String,Long>();

		try{

			File dir = new File(args[0],"commondity.lst");
			FileReader fr = new FileReader(dir);     //Fire（引数）からFileReaderをオブジェクトを作成。
			BufferedReader br = new BufferedReader(fr);      //FileReader（引数）BRオブジェクトを作成。

			String commondityNameData ;
			Long money =0l;
			while((commondityNameData = br.readLine()) != null) {

				String[] items = commondityNameData.split(",",-1);
				System.out.println(items[0].matches("^[a-zA-Z0-9]+$"));
				
				if(!items[0].matches("^[a-zA-Z0-9]+$") &&(items.length !=2)){
					System.out.println("商品定義ファイルのフォーマットが不正です。");
					
					return;
				}
				commondityNameMap.put(items[0],items[1]);
				commonditySaleMap.put(items[0],money);
			}
			br.close();

		}catch (FileNotFoundException e){
			System.out.println("商品定義ファイルが存在しません。");

		}catch (IOException e) {
			System.out.println("予期せぬエラーが発生しました。");
		}finally{

		}

			//ディレクトリの中の.rcdファイルを選別する。
		File dir = new File(args[0]);

		File[] files =dir.listFiles();
		ArrayList<File> rcdFiles = new ArrayList<>();
		for(int i = 0; i < files.length ; i++ ){
			if(files[i].getName().matches("\\d{8}.rcd")){
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
		 //売上げをbranchSaleMapに代入
		for(int i = 0; i < rcdFiles.size() ; i++ ){
			try{

				FileReader fr = new FileReader(rcdFiles.get(i));     //Fire（引数）からFileReaderをオブジェクトを作成。
				BufferedReader br = new BufferedReader(fr);      //FileReader（引数）BRオブジェクトを作成。

				String strshop;
				ArrayList<String> shops = new ArrayList<String>();

				while((strshop = br.readLine()) != null){

					shops.add(strshop);
				}

				if(branchSaleMap.containsKey(shops.get(0))){
					branchSaleMap.get((0));
				}else{
					System.out.println(rcdFiles.get(i).getName() + "の支店コードが不正です");
					return;
				}

				if(commonditySaleMap.containsKey(shops.get(1))){
					commonditySaleMap.get((0));
				}else{
					System.out.println(rcdFiles.get(i).getName() + "の商品コードが不正です");
					return;
				}

				if(shops.size() !=3){
					System.out.println(rcdFiles.get(i).getName() + "のフォーマットが不正です");
					return;
				}

				branchSaleMap.get(shops.get(0));
				long amount = Long.parseLong(shops.get(2));
				long shopsale = branchSaleMap.get(shops.get(0));
				long pulsamount = (amount + shopsale);
				if( pulsamount >1000000000){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				branchSaleMap.put(shops.get(0),pulsamount);

				commonditySaleMap.get(shops.get(1));
				commonditySaleMap.get(shops.get(1));
				commonditySaleMap.put(shops.get(1),amount);

				br.close();

			}catch (FileNotFoundException e){     //ファイル自体があるのか確認。
					System.out.println("ファイルが存在しません。");

			}catch (IOException e) {       //キーボード故障確認
					System.out.println(e);
			}
		}

		//Branch.outに作成。
		List<Map.Entry<String,Long>> branchSaleList =
		new ArrayList<Map.Entry<String,Long>>(branchSaleMap.entrySet());
		Collections.sort(branchSaleList, new Comparator<Map.Entry<String,Long>>() {
			@Override
			public int compare(Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});
		for (Entry<String,Long>s :branchSaleList) {
			System.out.println("支店コード : " + s.getKey() );
			System.out.println("支店名 :" + branchNameMap.get(s.getKey()));
			System.out.println("合計金額 : " + s.getValue());
		}

		//commodity.outの作成
		List<Map.Entry<String,Long>> commondityList =
		new ArrayList<Map.Entry<String,Long>>(commonditySaleMap.entrySet());
		Collections.sort(commondityList, new Comparator<Map.Entry<String,Long>>() {
			@Override
			public int compare(Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		for (Entry<String,Long>s :commondityList) {
			System.out.println("商品コード : " + s.getKey() );
			System.out.println("商品名 :" + commondityNameMap.get(s.getKey()));
			System.out.println("合計金額 : " + s.getValue());
		}

	}
}

