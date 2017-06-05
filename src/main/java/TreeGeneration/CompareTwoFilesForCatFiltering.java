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

	
	public static void compareHashsetAndFile(final String str_FileName,final HashSet<String> hset_Cats) 
	{
		Date start = new Date();
		try {
				BufferedReader br_mainFile = new BufferedReader(new FileReader(str_FileName));
				File log= new File(str_FileName+"_CatFiltered"+Global.levelOfTheTree);
	
				if (log.exists()) 
				{
					log.delete();
				}
				log.createNewFile();

				BufferedWriter bufferedWriter = null;
				FileWriter fileWriter;

				fileWriter = new FileWriter(log, false);
				bufferedWriter = new BufferedWriter(fileWriter);

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
					}
				}
				for(String line:hsetResult) 
				{
					bufferedWriter.write(line);
					bufferedWriter.newLine();
				}
	
				System.out.println("size"+hsetResult.size());
				System.out.println("count"+count);
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



