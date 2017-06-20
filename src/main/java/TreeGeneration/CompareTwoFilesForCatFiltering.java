package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

public class CompareTwoFilesForCatFiltering {

	/**
	 * @param args
	 * @throws IOException 
	 */

	
	public static void compareTwoHashsetAndFile(final String str_FileName,final HashSet<String> hset_Cats,final HashSet<String> setEntities) 
	{
		Date start = new Date();
		try {
				BufferedReader br_mainFile = new BufferedReader(new FileReader(str_FileName));
				File log= new File(str_FileName+"_CatEntFiltered_"+Global.levelOfTheTree);
	
				if (log.exists()) 
				{
					log.delete();
				}
				log.createNewFile();

				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(log, false));

				String lineMain;
				HashSet<String> hsetResult = new HashSet<>();
				while ((lineMain = br_mainFile.readLine()) != null) 
				{
			 
					
					final String category = lineMain.split(" ")[1].toLowerCase().replace(">", "").replace("category:", "");
					final String entity = lineMain.split(" ")[0].toLowerCase().replace(">", "").replace("category:", "");
					if (hset_Cats.contains(category)&&setEntities.contains(entity))
					{
						hsetResult.add(lineMain);
					}
					else
					{
						System.out.println(lineMain);
					}
					
				}
				for(String line:hsetResult) 
				{
					bufferedWriter.write(line);
					bufferedWriter.newLine();
				}
	
				System.out.println("size"+hsetResult.size());
				//System.out.println("count"+count);
				
				br_mainFile.close();       
				bufferedWriter.close();
			}
				 
	
	catch (IOException e) {
		e.printStackTrace();
	}
	Date end = new Date();
	System.out.println ("MainLoop: "+(end.getTime() - start.getTime())/1000 + " seconds");

}	
	
	
	public static void compareHashsetAndFile(final String str_FileName,final HashSet<String> hset_Cats) 
	{
		System.out.println("start comparing, categories size "+ hset_Cats.size());
		Date start = new Date();
		try {
				BufferedReader br_mainFile = new BufferedReader(new FileReader(str_FileName));
				File log= new File(str_FileName+"_CatFiltered_"+Global.levelOfTheTree);
	
				if (log.exists()) 
				{
					log.delete();
				}
				log.createNewFile();

				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(log, false));

				String lineMain;
				String lineCategory;
				int count = 0;
				HashSet<String> hsetResult = new HashSet<>();
				while ((lineMain = br_mainFile.readLine()) != null) 
				{
			 
					final String replace = lineMain.split(" ")[1].toLowerCase().replace(">", "").replace("category:", "");
					if (hset_Cats.contains(replace))
					{
						hsetResult.add(lineMain);
						//System.out.println(lineMain);
					}
					else
					{
						//System.out.println(lineMain);
					}
					
				}
				for(String line:hsetResult) 
				{
					bufferedWriter.write(line);
					bufferedWriter.newLine();
				}
	
				System.out.println("size"+hsetResult.size());
				//System.out.println("count"+count);
				count=0;
				
				br_mainFile.close();       
				bufferedWriter.close();
			}
				 
	
	catch (IOException e) {
		e.printStackTrace();
	}
	Date end = new Date();
	System.out.println ("MainLoop: "+(end.getTime() - start.getTime())/1000 + " seconds");

}	

}



