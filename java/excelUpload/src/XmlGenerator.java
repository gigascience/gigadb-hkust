import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mozilla.javascript.ast.NewExpression;

import Log.Log;

/**
 * 
 */

/**
 * @author 王森洪
 * 
 * @date 2012-5-1
 */
public class XmlGenerator {
	Database database;

	XmlGenerator() throws Exception {
		database = new Database();
	}

	//
	void generateXml(String identifier) throws SQLException, IOException {
		String path = "metadataDir/" + identifier.replace("/", "_") + ".xml";
		Log xmlFile = new Log(path, false);
		String query = "select author_name from author where identifier='"
				+ identifier + "' order by rank;";
		ResultSet resultSet = database.stmt.executeQuery(query);
		ArrayList<String> creatorList = new ArrayList<String>();
		while (resultSet.next()) {
			String creator = resultSet.getString(1);
			creatorList.add(creator);
		}
		// title
		query = "select title from dataset where identifier='" + identifier
				+ "';";
		resultSet = database.stmt.executeQuery(query);
		String title = null;
		while (resultSet.next()) {
			title = resultSet.getString(1);
			title = title.replace("<", "&lt;");
			title = title.replace(">", "&gt;");
			if (title.endsWith("."))
				title = title.substring(0, title.length() - 1);
		}
		// publisher
		query = "select publisher from dataset where identifier='" + identifier
				+ "';";
		resultSet = database.stmt.executeQuery(query);
		String publisher = null;
		while (resultSet.next()) {
			publisher = resultSet.getString(1);
		}
		// publication_year
		query = "select extract(year from  publication_date) from dataset where identifier='"
				+ identifier + "';";
		resultSet = database.stmt.executeQuery(query);
		String publicationYear = null;
		while (resultSet.next()) {
			publicationYear = resultSet.getString(1);
		}
		// date
		query = "select modification_date from dataset where identifier='"
				+ identifier + "';";
		resultSet = database.stmt.executeQuery(query);
		String modification_date = null;
		while (resultSet.next()) {
			modification_date = resultSet.getString(1);
		}
		// language
		String language = "eng";
		ArrayList<String> subjects = new ArrayList<String>();
		// subject
		query = "select dataset_type from datasettype where identifier='"
				+ identifier + "';";
		resultSet = database.stmt.executeQuery(query);
		// String
		while (resultSet.next()) {
			subjects.add(resultSet.getString(1));
		}
		// manuscript+doi
		query = "select manuscript_doi from dataset_manuscript where identifier='"
				+ identifier + "';";
		resultSet = database.stmt.executeQuery(query);
		ArrayList<String> relatedDoiList = new ArrayList<String>();
		while (resultSet.next()) {
			String doi = resultSet.getString(1);
			relatedDoiList.add(doi);
		}
		// size
		query = "select sum(file_size) from file where identifier='"
				+ identifier + "';";
		resultSet = database.stmt.executeQuery(query);
		long size = 0;
		while (resultSet.next()) {
			size = resultSet.getLong(1);
		}
		// process size
		// 0: B, 1: KB, 2: MB 3: GB 4. TB
		int level = 0;
		String[] unit = { "B", "KB", "MB", "GB", "TB" };
		// long temp=size;
		while (size >= 512) {
			size = (size + 512) / 1024;
			level++;
		}
		String sizeString = size + " " + unit[level];
		// description
		query = "select description from dataset where identifier='"
				+ identifier + "';";
		resultSet = database.stmt.executeQuery(query);
		String description = null;
		while (resultSet.next()) {
			description = resultSet.getString(1);
			description = description.replace("<", "&lt;");
			description = description.replace(">", "&gt;");
		}
		// decription type
		String descriptionType = "Abstract";
		// write to file
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<resource xmlns=\"http://datacite.org/schema/kernel-2.2\" "
				+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xsi:schemaLocation=\"http://datacite.org/schema/kernel-2.2 "
				+ "http://schema.datacite.org/meta/kernel-2.2/metadata.xsd\">\n";

		xml += "\t<identifier identifierType=\"DOI\">@</identifier>\n".replace(
				"@", identifier);
		xml += "\t<creators>\n";
		for (String creator : creatorList) {
			xml += ("\t\t<creator>\n\t\t\t<creatorName>@</creatorName>\n\t\t</creator>\n")
					.replace("@", creator);
		}
		xml += "\t</creators>\n";
		// title
		xml += "\t<titles>\n\t\t<title>@</title>\n\t</titles>\n".replace("@",
				title);
		xml += "\t<publisher>@</publisher>\n".replace("@", publisher);
		xml += "\t<publicationYear>@</publicationYear>\n".replace("@",
				publicationYear);
		// date
		if (modification_date != null) {
			xml += "\t<date dateType=\"Updated\">@</date>\n".replace("@",
					modification_date);
		}	
		// subject
		if(subjects.size()>0){
			xml += "\t<subjects>\n";
			for(int i=0;i<subjects.size();i++){
				xml+="\t\t<subject>"+subjects.get(i)+"</subject>\n";
			}
			xml += "\t</subjects>\n";
		}
		//language		
		xml += "\t<language>@</language>\n".replace("@", language);
		// resourceType
		xml += "\t<resourceType resourceTypeGeneral=\"Dataset\">GigaDB Dataset</resourceType>\n";
		if (relatedDoiList.size() != 0) {
			xml += "\t<relatedIdentifiers>\n";
			for (String relatedidentifier : relatedDoiList) {
				xml += ("\t\t<relatedIdentifier relatedIdentifierType=\"DOI\" relatedType=\"Cites\">"
						+ "@</relatedIdentifier>\n").replace("@",
						relatedidentifier);
			}
			xml += "\t</relatedIdentifiers>\n";
		}
		// // relatedIdentifierTYpe
		// xml +=
		// "\t<relatedIdentifierType>@</relatedIdentifierType>\n".replace(
		// "@", "DOI");
		// xml += "\t<relationType>@</relationType>\n".replace("@", "Cites");
		// size
		xml += "\t<sizes>\n\t\t<size>@</size>\n\t</sizes>\n".replace("@",
				sizeString);
		xml += "\t<rights>http://creativecommons.org/publicdomain/zero/1.0/</rights>\n";
		xml += "\t<descriptions>\n\t\t<description descriptionType=\"Abstract\">@</description>\n\t</descriptions>\n"
				.replace("@", description);

		//
		xml += "</resource>\n";
		xmlFile.writeLine(xml);
	}

	public static void main(String[] args) throws Exception {
		// XmlGenerator xmlGenerator = new XmlGenerator();
		// xmlGenerator.generateXml("10.5524/100003",
		// "metadataDir/metadata.xml");
		if (args.length > 0) {
			XmlGenerator xmlGenerator = new XmlGenerator();
			System.out.println("get outside arguments!");
			System.out.println(args[0]);
			String identifier = args[0];
			xmlGenerator.generateXml(identifier);
			return;
		}
		XmlGenerator xmlGenerator = new XmlGenerator();
		xmlGenerator.generateXml("10.5524/100031");
	}
}
